package com.bubing.camera.models.ad;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.FrameLayout;

import com.bubing.camera.R;

/**
 * @ClassName: AdViewHolder
 * @Description: 广告viewolder
 * @Author: bubing
 * @Date: 2020-05-09 17:58
 */
public class AdViewHolder extends RecyclerView.ViewHolder {
    public FrameLayout adFrame;

    public AdViewHolder(View itemView) {
        super(itemView);
        adFrame = (FrameLayout) itemView.findViewById(R.id.ad_frame);
    }
}
