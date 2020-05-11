package com.bubing.camera.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bubing.camera.R;
import com.bubing.camera.constant.Constants;
import com.bubing.camera.result.ResultStorage;
import com.bubing.camera.setting.Setting;
import com.bubing.camera.utils.media.DurationUtils;
import com.bubing.camera.widget.PressedImageView;

/**
 * @ClassName: PreviewPhotosFragmentAdapter
 * @Description: 预览所有选中图片集合的适配器
 * @Author: bubing
 * @Date: 2020-05-09 18:24
 */
public class PreviewPhotosFragmentAdapter extends RecyclerView.Adapter<PreviewPhotosFragmentAdapter.PreviewPhotoVH> {
    private LayoutInflater inflater;
    private OnClickListener listener;
    private int checkedPosition = -1;

    public PreviewPhotosFragmentAdapter(Context context, OnClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @Override
    public PreviewPhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PreviewPhotoVH(inflater.inflate(R.layout.item_preview_selected_photos_easy_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(PreviewPhotoVH holder, int position) {
        final int p = position;
        String path = ResultStorage.getPhotoPath(position);
        String type = ResultStorage.getPhotoType(position);
        Uri uri = ResultStorage.getPhotoUri(position);
        long duration = ResultStorage.getPhotoDuration(position);

        final boolean isGif = path.endsWith(Constants.Type.GIF) || type.endsWith(Constants.Type.GIF);
        if (Setting.showGif && isGif) {
            Setting.imageEngine.loadGifAsBitmap(holder.ivPhoto.getContext(), uri, holder.ivPhoto);
            holder.tvType.setText(R.string.gif_easy_photos);
            holder.tvType.setVisibility(View.VISIBLE);
        } else if (Setting.showVideo && type.contains(Constants.Type.VIDEO)) {
            Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), uri, holder.ivPhoto);
            holder.tvType.setText(DurationUtils.format(duration));
            holder.tvType.setVisibility(View.VISIBLE);
        } else {
            Setting.imageEngine.loadPhoto(holder.ivPhoto.getContext(), uri, holder.ivPhoto);
            holder.tvType.setVisibility(View.GONE);
        }

        if (checkedPosition == p) {
            holder.frame.setVisibility(View.VISIBLE);
        } else {
            holder.frame.setVisibility(View.GONE);
        }
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhotoClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ResultStorage.count();
    }

    public void setChecked(int position) {
        if (checkedPosition == position) {
            return;
        }
        checkedPosition = position;
        notifyDataSetChanged();
    }

    class PreviewPhotoVH extends RecyclerView.ViewHolder {
        PressedImageView ivPhoto;
        View frame;
        TextView tvType;

        public PreviewPhotoVH(View itemView) {
            super(itemView);
            ivPhoto = (PressedImageView) itemView.findViewById(R.id.iv_photo);
            frame = itemView.findViewById(R.id.v_selector);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

    public interface OnClickListener {
        void onPhotoClick(int position);
    }
}

