package cct.ansteph.moodrecorder.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.customview.DailyActivity;
import cct.ansteph.moodrecorder.model.Activity;
import cct.ansteph.moodrecorder.model.Entry;
import cct.ansteph.moodrecorder.utils.BitmapTransfomUtils;

/**
 * Created by loicstephan on 2018/01/24.
 */

public class EntryRecyclerAdapter extends RecyclerView.Adapter<EntryRecyclerAdapter.EntryViewHolder> {

    private ArrayList<Entry> entries;
    Context mContext;


    public EntryRecyclerAdapter(ArrayList<Entry> entries, Context mContext) {
        this.entries = entries;
        this.mContext = mContext;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);

        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {

        holder.txtEntryTime.setText(entries.get(position).getRecordTime());
        holder.txtEntryDate.setText(entries.get(position).getRecordDate());
        holder.txtEntryMood.setText(entries.get(position).getEmoji().getMoodName());

        // TODO: 2018/01/25  uncomment when the emoji object is added to the list

        byte [] imojiimg = entries.get(position).getEmoji().getImageByte();
        Bitmap bmp  = BitmapFactory.decodeByteArray(imojiimg, 0,imojiimg.length);

        holder.imgMood.setImageBitmap(bmp);

       for(Activity activity: entries.get(position).getActivityList())
       {
           DailyActivity dailyActivity = new DailyActivity(mContext);

           dailyActivity.setActivityName(activity.getActivityName());
           dailyActivity.setImageByte(activity.getImageByte());

           holder.lytDailyActi.addView(dailyActivity);
       }



        final View itemview = holder.itemView;


    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        public final ImageView imgMood;
        public final TextView txtEntryDate;
        public final TextView txtEntryTime;
        public final TextView txtEntryMood;
        public final LinearLayout lytDailyActi;
       // public final TextView txtPromoEnd;

        public EntryViewHolder(View view) {
            super(view);
            this.mView = view;
            this.imgMood =(ImageView) itemView.findViewById(R.id.imgMood);
            this.txtEntryDate = (TextView)itemView.findViewById(R.id.txtentry_date) ;

            this.txtEntryTime = (TextView)itemView.findViewById(R.id.txtentry_time) ;
            this.txtEntryMood = (TextView)itemView.findViewById(R.id.txtentry_mood) ;
             this.lytDailyActi = (LinearLayout) itemView.findViewById(R.id.lytDailyActivity) ;

        }
    }

}
