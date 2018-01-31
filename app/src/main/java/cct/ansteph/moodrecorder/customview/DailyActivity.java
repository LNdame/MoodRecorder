package cct.ansteph.moodrecorder.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cct.ansteph.moodrecorder.R;

/**
 * Created by loicstephan on 2018/01/19.
 */

public class DailyActivity extends LinearLayout{

   private TextView txtActiName;
   private ImageView imgActiSymbol;

   private String activityName;
   private byte [] imageByte;

    public DailyActivity(Context context) {
        super(context);
        initViews(context);
    }

    public DailyActivity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DailyActivity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public void initViews(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.daily_activity, this);

        txtActiName = (TextView) findViewById(R.id.txtActiName);
        imgActiSymbol = (ImageView) findViewById(R.id.imgActiSym);


        if(!TextUtils.isEmpty(getActivityName()))
            txtActiName.setText(getActivityName());

        if(getImageByte() !=null){
        byte [] imgActi = getImageByte();
        Bitmap bmp  = BitmapFactory.decodeByteArray(imgActi, 0,imgActi.length);
        imgActiSymbol.setImageBitmap(bmp);
        }


    }
 /*
        byte [] imojiimg = entries.get(position).getEmoji().getImageByte();
        Bitmap bmp  = BitmapFactory.decodeByteArray(imojiimg, 0,imojiimg.length);

        holder.imgMood.setImageBitmap(bmp); */

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
        if(txtActiName!=null)
            txtActiName.setText(activityName);
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;

        if(imgActiSymbol!=null){
            byte [] imgActi = imageByte;
            Bitmap bmp  = BitmapFactory.decodeByteArray(imgActi, 0,imgActi.length);
            imgActiSymbol.setImageBitmap(bmp);
        }
    }
}
