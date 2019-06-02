package com.map.service.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.map.service.R;
import com.map.service.bean.FavBus;

import java.util.List;


public class FavBusAdapter extends BaseRecyclerViewAdapter<FavBus> {

    private OnDeleteClickLister mDeleteClickListener;

    public FavBusAdapter(Context context, List<FavBus> data) {
        super(context, data, R.layout.item_fav_bus);
    }

    @Override
    protected void onBindData(RecyclerViewHolder holder, FavBus bean, int position) {
        View view = holder.getView(R.id.fav_bus_delete);
        view.setTag(position);
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                    }
                }
            });
        }
        ((TextView) holder.getView(R.id.fav_bus_title)).setText(bean.getBus_name());
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}