package com.general.bubing.framework;

import android.view.View;
import android.widget.Button;

import com.bubing.framework.SingleActivity;
import com.general.bubing.R;

public class StyleActivity extends SingleActivity {

    @Override
    public void onBuild() {
        super.onBuild();
        setContentView(R.layout.activity_style);

        final int styleId = this.getExtras().getInt("styleId");

        Button closeBttn = (Button) findViewById(R.id.closeBttn);
        closeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (styleId == 1) {
                    close();
                } else if (styleId == 2) {
                    close(ANIM_TO_LEFT);
                } else if (styleId == 3) {
                    close(ANIM_TO_BOTTOM);
                } else if (styleId == 4) {
                    close(ANIM_TO_TOP);
                }
            }
        });
    }

}
