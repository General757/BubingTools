package com.general.bubing;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bubing.camera.models.ResultPhoto;
import com.bubing.camera.models.album.entity.Photo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/**
 * @ClassName: CameraPhotoAdapter
 * @Description: 返回图片的列表适配器
 * @Author: bubing
 * @Date: 2020-05-09 21:26
 */
public class CameraPhotoAdapter extends RecyclerView.Adapter<CameraPhotoAdapter.MainVH> {
    private ArrayList<ResultPhoto> list;
    private LayoutInflater mInflater;
    private RequestManager mGlide;

    CameraPhotoAdapter(Context cxt, ArrayList<ResultPhoto> list) {
        this.list = list;
        mInflater = LayoutInflater.from(cxt);
        mGlide = Glide.with(cxt);
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainVH(mInflater.inflate(R.layout.item_camera_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(MainVH holder, int position) {
        ResultPhoto photo = list.get(position);
        mGlide.load(photo.getOriginalUri()).into(holder.ivPhoto);

        holder.tvMessage.setText(
                "[图片名称]： " + photo.getName() +
                        "\n[图片地址]：" + photo.getPath() +
                        "\n[图片类型]：" + photo.getType() +
                        "\n[宽]：" + photo.getWidth() +
                        "\n[高]：" + photo.getHeight() +
                        "\n[文件大小,单位bytes]：" + photo.getSize() +
                        "\n[文件时长,毫秒]：" + photo.getDuration() +
                        "\n[日期，时间戳，毫秒]：" + photo.getTime() +
                        "\n[是否选择原图]：" + photo.isSelectedOriginal() +
                        "\n[图片来源]：" + photo.getImageType() +
                        "\n[原始图片Uri]：" + photo.getOriginalUri() +
                        "\n[原始图片路径]：" + photo.getOriginalPath() +
                        "\n[压缩图片路径]：" + photo.getCompressPath() +
                        "\n[是否裁剪]：" + photo.isCropped() +
                        "\n[是否压缩]：" + photo.isCompressed()
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainVH extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvMessage;

        MainVH(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}

