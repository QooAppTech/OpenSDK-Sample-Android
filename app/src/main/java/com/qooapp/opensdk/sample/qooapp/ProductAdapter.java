package com.qooapp.opensdk.sample.qooapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.qooapp.opensdk.sample.qooapp.model.Product;

import java.util.List;

/**
 */
public class ProductAdapter extends BaseAdapter{

    private Context mContext;
    private List<Product> mDataList;
    private ProductCallback mCallback;

    public final static int TYPE_BUY = 0;
    public final static int TYPE_DETAIL = 1;
    public ProductAdapter(Context context, List<Product> dataList, ProductCallback callback){
        this.mContext = context;
        this.mDataList = dataList;
        mCallback = callback;
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
            holder.btnBuy = view.findViewById(R.id.btn_buy);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final Product info = mDataList.get(position);

        holder.tvName.setText(info.getName()+" | "+info.getProduct_id());
        holder.tvPrice.setText(info.getPrice().get(0).amount + " " + info.getPrice().get(0).currency);
        holder.tvIndex.setText("" +(position+1));
        view.setOnClickListener(view1 -> mCallback.callback(TYPE_DETAIL, info.getProduct_id()));
        holder.btnBuy.setVisibility(View.VISIBLE);
        holder.btnBuy.setOnClickListener(view1 -> mCallback.callback(TYPE_BUY, info.getProduct_id()));
        return view;
    }

    public void clear(){
        mDataList.clear();
    }

    private class ViewHolder{
        public TextView tvName;
        public TextView tvIndex;
        public TextView tvPrice;
        public Button btnBuy;
    }

    public interface ProductCallback {
        void callback(int type, String productId);
    }

}
