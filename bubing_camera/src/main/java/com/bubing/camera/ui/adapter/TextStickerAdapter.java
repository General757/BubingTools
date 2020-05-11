package com.bubing.camera.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bubing.camera.R;
import com.bubing.camera.models.sticker.StickerModel;
import com.bubing.camera.models.sticker.entity.TextStickerData;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * @ClassName: TextStickerAdapter
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-05-09 17:04
 */
public class TextStickerAdapter extends RecyclerView.Adapter<TextStickerAdapter.TextViewHolder> {

    private List<TextStickerData> datas;
    private OnItemClickListener onItemClickListener;

    public TextStickerAdapter(Context cxt, OnItemClickListener listener) {
        super();
        this.onItemClickListener = listener;
        this.datas = new ArrayList<>();
        TextStickerData data = new TextStickerData(cxt.getString(R.string.text_sticker_hint_name), cxt.getString(R.string.text_sticker_hint));
        this.datas.add(0, data);
        TextStickerData d = new TextStickerData(cxt.getString(R.string.text_sticker_date), "-1");
        datas.add(d);
        datas.addAll(StickerModel.textDataList);
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_sticker, parent, false);
        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        final TextStickerData data = datas.get(position);

        holder.tvSticker.setText(data.stickerName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(data.stickerValue);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView tvSticker;

        public TextViewHolder(View itemView) {
            super(itemView);
            tvSticker = (TextView) itemView.findViewById(R.id.puzzle);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String stickerValue);
    }
}

