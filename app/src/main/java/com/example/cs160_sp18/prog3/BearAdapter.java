package com.example.cs160_sp18.prog3;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BearAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<Bear> mBears;
    private BearViewHolder.OnBearListener mOnBearListener;

    public BearAdapter(Context context, ArrayList<Bear> bears, BearViewHolder.OnBearListener onBearListener) {
        this.mContext = context;
        this.mBears = bears;
        this.mOnBearListener = onBearListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bears_cell_layout, parent, false);
        return new BearViewHolder(view, mOnBearListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bear bear = mBears.get(position);
        ((BearViewHolder) holder).bind(bear);
    }

    @Override
    public int getItemCount() {
        return mBears.size();
    }
}

class BearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mBearImageView;
    TextView mBearName;
    TextView mBearDistance;
    RelativeLayout parentLayout;
    OnBearListener onBearListener;

    public BearViewHolder(View itemView, OnBearListener onBearListener) {
        super(itemView);
        mBearImageView = (ImageView) itemView.findViewById(R.id.bear_image);
        mBearName = (TextView) itemView.findViewById(R.id.bear_name);
        mBearDistance = (TextView) itemView.findViewById(R.id.distance_text);
        parentLayout = itemView.findViewById(R.id.bear_layout);
        this.onBearListener = onBearListener;

        itemView.setOnClickListener(this);
    }

    void bind(Bear bear) {
        mBearImageView.setImageResource(bear.imgId);
        mBearName.setText(bear.bearName);
        mBearDistance.setText(bear.getDistance() + "m away");
    }

    @Override
    public void onClick(View view) {
        onBearListener.onBearClick(getAdapterPosition());
    }

    public interface OnBearListener {
        void onBearClick(int position);
    }
}
