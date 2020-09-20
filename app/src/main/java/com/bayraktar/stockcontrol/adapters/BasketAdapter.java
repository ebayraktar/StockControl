package com.bayraktar.stockcontrol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.database.model.Stock;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketHolder> {
    private List<Stock> stocks = new ArrayList<>();
    IStockListener stockListener;

    public BasketAdapter(IStockListener stockListener) {
        this.stockListener = stockListener;
    }

    public void addCount(int position) {
        Stock currentStock = stocks.get(position);
        currentStock.setQuantity(currentStock.getQuantity() + 1);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public BasketAdapter.BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.basket_item, parent, false);
        return new BasketAdapter.BasketHolder(itemView, stockListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.BasketHolder holder, int position) {
        Stock currentStock = stocks.get(position);

        holder.tvName.setText(currentStock.getName());
        holder.tvDescription.setText(currentStock.getDescription());
        holder.tvPrice.setText("₺ " + String.format("%.2f", currentStock.getPrice()));

        holder.tvCount.setText(String.valueOf(currentStock.getQuantity()));

        Glide.with(holder.itemView)
                .load(currentStock.getImageUrl())
                .fitCenter()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    public void updateCount(int count) {

    }

    static class BasketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvName;
        private TextView tvDescription;
        private TextView tvPrice;

        private TextView tvCount;

        IStockListener stockListener;

        public BasketHolder(@NonNull View itemView, IStockListener stockListener) {
            super(itemView);
            this.stockListener = stockListener;

            ivImage = itemView.findViewById(R.id.ivImage);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            ImageView ivIncrease = itemView.findViewById(R.id.ivIncrease);
            tvCount = itemView.findViewById(R.id.tvCount);
            ImageView ivDecrease = itemView.findViewById(R.id.ivDecrease);

            ivImage.setOnClickListener(this);
            ivIncrease.setOnClickListener(this);
            ivDecrease.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivImage:
                    stockListener.onItemClick(ivImage, getAdapterPosition());
                    break;
                case R.id.ivIncrease:
                    stockListener.onIncreaseClick(v, getAdapterPosition());
                    break;
                case R.id.ivDecrease:
                    stockListener.onDecreaseClick(v, getAdapterPosition());
                    break;
            }
        }
    }

    public interface IStockListener {
        void onItemClick(View v, int position);

        void onIncreaseClick(View v, int position);

        void onDecreaseClick(View v, int position);
    }
}
