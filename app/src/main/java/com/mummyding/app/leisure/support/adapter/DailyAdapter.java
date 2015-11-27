package com.mummyding.app.leisure.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mummyding.app.leisure.R;
import com.mummyding.app.leisure.cache.cache.DailyCache;
import com.mummyding.app.leisure.cache.table.DailyTable;
import com.mummyding.app.leisure.model.daily.DailyBean;
import com.mummyding.app.leisure.support.Utils;
import com.mummyding.app.leisure.ui.support.WebViewLocalActivity;

import java.util.List;

/**
 * Created by mummyding on 15-11-21.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder>{

    private List<DailyBean> item;
    private Context mContext;
    private DailyCache cache ;
    public DailyAdapter(List<DailyBean> item, Context mContext) {
        this.item = item;
        this.mContext = mContext;
        cache = new DailyCache(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_daily, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DailyBean dailyBean = getItem(position);
        holder.title.setText(dailyBean.getTitle());
        holder.image.setImageURI(null);
        holder.image.setImageURI(Uri.parse(dailyBean.getImage()));
        holder.info.setText(dailyBean.getInfo());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewLocalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(mContext.getString(R.string.id_html_content), dailyBean.getDescription());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.collect_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dailyBean.setIs_collected(isChecked ? 1:0);
                cache.execSQL(DailyTable.updateCollectionFlag(dailyBean.getTitle(), isChecked ? 1 : 0));
                if(isChecked){
                    cache.addToCollection(dailyBean);
                }else{
                    cache.execSQL(DailyTable.deleteCollectionFlag(dailyBean.getTitle()));
                }
            }
        });
        holder.collect_cb.setChecked(dailyBean.getIs_collected());
    }

    private DailyBean getItem(int position){
        return item.get(position);
    }
    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private SimpleDraweeView image;
        private TextView info;
        private CheckBox collect_cb;
        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            title = (TextView) parentView.findViewById(R.id.title);
            image = (SimpleDraweeView) parentView.findViewById(R.id.image);
            info = (TextView) parentView.findViewById(R.id.info);
            collect_cb = (CheckBox) parentView.findViewById(R.id.collect_cb);
        }
    }
}