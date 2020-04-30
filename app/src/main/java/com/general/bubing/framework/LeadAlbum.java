package com.general.bubing.framework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bubing.framework.BaseApplication;
import com.bubing.framework.PageIntent;
import com.bubing.framework.SingleActivity;
import com.bubing.framework.widget.PageIndicator;
import com.bubing.framework.widget.UiPagerView;
import com.general.bubing.App;
import com.general.bubing.R;

/**
 * @ClassName: FrameworkActivity
 * @Description: java类作用描述
 * @Author: bubing
 * @Date: 2020-04-30 14:00
 */
public class LeadAlbum extends SingleActivity implements UiPagerView.PositionListener {
    class ImageAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public ImageAdapter(LayoutInflater inflater) {
            super();
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return resource.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_lead_album, null);
            }

            ImageView leadImage = (ImageView) convertView.findViewById(R.id.leadImage);
            leadImage.setImageResource(resource[position]);

            Button leadBttn = (Button) convertView.findViewById(R.id.leadBttn);
            leadBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMainPage();
                }
            });

            leadBttn.setVisibility(position == resource.length - 1 ? View.VISIBLE : View.GONE);

            return convertView;
        }
    }

    int[] resource = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    UiPagerView pagerview;

    PageIndicator indicator;

    @Override
    public void onBuild() {
        setContentView(R.layout.activity_lead_album);

        pagerview = (UiPagerView) findViewById(R.id.pagerview);
        LayoutInflater inflater = getLayoutInflater();
        pagerview.setAdapter(new ImageAdapter(inflater));
        pagerview.setPositionListener(this);

        indicator = (PageIndicator) findViewById(R.id.indicator);
        indicator.setPageCount(resource.length);
    }

    @Override
    public void onPositionChange(int position) {
        indicator.setPageIndex(position);
    }

    public void startMainPage() {
        PageIntent intent = new PageIntent(this, HomeGroupActivity.class);
        startPagement(intent);
        close();

        App app = (App) getApplication();
        app.setFirstStart( false);
    }
}
