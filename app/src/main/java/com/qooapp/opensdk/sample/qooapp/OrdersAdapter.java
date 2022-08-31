package com.qooapp.opensdk.sample.qooapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qooapp.opensdk.sample.qooapp.model.OrderBean;

import java.util.List;

/**
 * An adapter to list
 * @email devel@qoo-app.com
 */
public class OrdersAdapter extends BaseAdapter{

    private Context mContext;
    private List<OrderBean> mDataList;
    public OrdersAdapter(Context context, List<OrderBean> dataList){
        this.mContext = context;
        this.mDataList = dataList;
    }
    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_product, null);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvIndex = view.findViewById(R.id.tv_index);
            holder.tvPrice = view.findViewById(R.id.tv_price);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final OrderBean info = mDataList.get(position);
        holder.tvName.setText(info.getProduct_id() + "(" + info.getPurchase_id() + ")");
        holder.tvPrice.setText(info.getAmount()+info.getCurrency());
        holder.tvIndex.setText((position+1)+"");
        return view;
    }

    public void clear(){
        mDataList.clear();
    }

    private class ViewHolder{
        public TextView tvName;
        public TextView tvIndex;
        public TextView tvPrice;
    }

}
