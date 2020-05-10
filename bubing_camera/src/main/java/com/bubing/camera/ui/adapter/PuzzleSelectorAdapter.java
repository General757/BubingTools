package com.bubing.camera.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bubing.camera.R;
import com.bubing.camera.constant.Constants;
import com.bubing.camera.models.album.entity.Photo;
import com.bubing.camera.setting.Setting;
import com.bubing.camera.utils.media.DurationUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: PuzzleSelectorAdapter
 * @Description: 拼图相册适配器
 * @Author: bubing
 * @Date: 2020-05-09 18:31
 */
public class PuzzleSelectorAdapter extends RecyclerView.Adapter {

    private ArrayList<Photo> dataList;
    private LayoutInflater mInflater;
    private OnClickListener listener;

    public PuzzleSelectorAdapter(Context cxt, ArrayList<Photo> dataList, OnClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
        this.mInflater = LayoutInflater.from(cxt);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(mInflater.inflate(R.layout.item_puzzle_selector_easy_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final int p = position;
        Photo photo = dataList.get(position);
        String path = photo.path;
        String type = photo.type;
        Uri uri = photo.uri;
        long duration = photo.duration;
        final boolean isGif = path.endsWith(Constants.Type.GIF) || type.endsWith(Constants.Type.GIF);
        if (Setting.showGif && isGif) {
            Setting.imageEngine.loadGifAsBitmap(((PhotoViewHolder) holder).ivPhoto.getContext(), uri, ((PhotoViewHolder) holder).ivPhoto);
            ((PhotoViewHolder) holder).tvType.setText(R.string.gif_easy_photos);
            ((PhotoViewHolder) holder).tvType.setVisibility(View.VISIBLE);
        } else if (Setting.showVideo && type.contains(Constants.Type.VIDEO)) {
            Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), uri, ((PhotoViewHolder) holder).ivPhoto);
            ((PhotoViewHolder) holder).tvType.setText(DurationUtils.format(duration));
            ((PhotoViewHolder) holder).tvType.setVisibility(View.VISIBLE);
        } else {
            Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), uri, ((PhotoViewHolder) holder).ivPhoto);
            ((PhotoViewHolder) holder).tvType.setVisibility(View.GONE);
        }

        ((PhotoViewHolder) holder).ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPhotoClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == dataList ? 0 : dataList.size();
    }

    public interface OnClickListener {
        void onPhotoClick(int position);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvType;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            this.ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            this.tvType = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }
}

