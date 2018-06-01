package com.king.yyh.wk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.king.yyh.wk.R;
import com.king.yyh.wk.activity.exexecutorpool.ThreadPoolExecutorActivity;
import com.king.yyh.wk.activity.select2.PicMainActivity2;
import com.king.yyh.wk.activity.select2.SelectPicActivity;
import com.king.yyh.wk.utils.WaterMaskUtil;
import com.king.yyh.wk.utils.WaterMaskView;


/**
 * 图片上带有文字
 */
public class PicAndTextMainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private ImageView ivWaterMark;
    private RadioGroup rgWaterMark;

    private WaterMaskView waterMaskView;
    private Bitmap sourBitmap;
    private Bitmap waterBitmap;
    private Bitmap watermarkBitmap;

    private final static int LEFT_TOP=0;
    private final static int RIGHT_TOP=1;
    private final static int RIGHT_BOTTOM=2;
    private final static int LEFT_BOTTOM=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picandtextmain);

        ivWaterMark= (ImageView) findViewById(R.id.wartermark_pic);
        rgWaterMark= (RadioGroup) findViewById(R.id.rg_wartermark);

        rgWaterMark.setOnCheckedChangeListener(this);

        sourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_03);

        waterMaskView = new WaterMaskView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        waterMaskView.setLayoutParams(params);
        waterMaskView.setCompanyText("XXXXXX公司");
        waterMaskView.setInfoText("这是相关信息1这是相关信息2这是相关信息3这是相关信息4这是相关信息5");

        //默认设置水印位置在左下
        saveWaterMask(LEFT_BOTTOM);
        findViewById(R.id.btn_threadpoolexecutor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PicAndTextMainActivity.this,ThreadPoolExecutorActivity.class));
            }
        });
    }

    /**
     * @param position 左上为0，顺时针算起
     */
    private void saveWaterMask(int position) {
        waterBitmap = WaterMaskUtil.convertViewToBitmap(waterMaskView);

        //根据原图处理要生成的水印的宽高
        float width = sourBitmap.getWidth();
        float height = sourBitmap.getHeight();
        float be = width / height;

        if ((float) 16 / 9 >= be && be >= (float) 4 / 3) {
            //在图片比例区间内16;9~4：3内，将生成的水印bitmap设置为原图宽高各自的1/5
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) height / 5);
        } else if (be > (float) 16 / 9) {
            //生成4：3的水印
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) width*3 / 20);
        } else if (be < (float) 4 / 3) {
            //生成4：3的水印
            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) height*4 / 15, (int) height / 5);
        }

        switch (position) {
            case LEFT_TOP:
                watermarkBitmap = WaterMaskUtil.createWaterMaskLeftTop(this, sourBitmap, waterBitmap, 0, 0);
                break;
            case RIGHT_TOP:
                watermarkBitmap = WaterMaskUtil.createWaterMaskRightTop(this, sourBitmap, waterBitmap, 0, 0);
                break;
            case RIGHT_BOTTOM:
                watermarkBitmap = WaterMaskUtil.createWaterMaskRightBottom(this, sourBitmap, waterBitmap, 0, 0);
                break;
            case LEFT_BOTTOM:
                watermarkBitmap = WaterMaskUtil.createWaterMaskLeftBottom(this, sourBitmap, waterBitmap, 0, 0);
                break;
        }
        ivWaterMark.setImageBitmap(watermarkBitmap);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
            case R.id.rb_wartermark_lefttop:
                startActivity(new Intent(PicAndTextMainActivity.this,PicMainActivity2.class));
                saveWaterMask(LEFT_TOP);
                break;
            case R.id.rb_wartermark_righttop:
                startActivity(new Intent(PicAndTextMainActivity.this,PictrueSelectMainActivity.class));
                saveWaterMask(RIGHT_TOP);
                break;
            case R.id.rb_wartermark_rightbottom:
                startActivity(new Intent(PicAndTextMainActivity.this,MainActivity.class));
                saveWaterMask(RIGHT_BOTTOM);
                break;
            case R.id.rb_wartermark_leftbottom:
                saveWaterMask(LEFT_BOTTOM);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sourBitmap != null) {
            sourBitmap.recycle();
            sourBitmap = null;
        }
        if (waterBitmap != null) {
            waterBitmap.recycle();
            waterBitmap = null;
        }
    }

}
