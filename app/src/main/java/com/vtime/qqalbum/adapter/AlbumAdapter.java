package com.vtime.qqalbum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vtime.qqalbum.R;
import com.vtime.qqalbum.bean.AlbumBean;

import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context mContext;
    private List<AlbumBean> mDatas;
    private LayoutInflater mInflater;


    public AlbumAdapter(Context mContext, List<AlbumBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<AlbumBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void refreshData(List<AlbumBean> mDatas) {
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_album, parent, false));
    }

    @Override
    public void onBindViewHolder(final AlbumAdapter.ViewHolder holder, final int position) {
        final AlbumBean item = mDatas.get(position);
        Glide.with(mContext).load(item.getUrl()).into(holder.item_iv);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public AlbumBean getItem(int position) {
        return mDatas.get(position);
    }

    public List<AlbumBean> getDatas() {
        return mDatas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_iv;

        public ViewHolder(View itemView) {
            super(itemView);
            item_iv = (ImageView) itemView.findViewById(R.id.item_iv);
        }
    }

}
