package com.qooapp.opensdk.sample.qooapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qooapp.opensdk.sample.qooapp.model.Product;

import java.util.List;

/**
 */
public class ProductAdapter extends BaseAdapter{

    private Context mContext;
    private List<Product> mDataList;
    public ProductAdapter(Context context, List<Product> dataList){
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
        final Product info = mDataList.get(position);

        holder.tvName.setText(info.getName());
        holder.tvPrice.setText(info.getPrice().get(0).amount + " " + info.getPrice().get(0).currency);
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
