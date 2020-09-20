package com.bayraktar.stockcontrol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.database.model.Stock;
import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockHolder> {
    private List<Stock> stocks = new ArrayList<>();
    IStockListener stockListener;

    public StockAdapter(IStockListener stockListener) {
        this.stockListener = stockListener;
    }

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item, parent, false);
        return new StockHolder(itemView, stockListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StockHolder holder, int position) {
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

        if (!currentStock.isFav()) {
            holder.cvFav.setVisibility(View.GONE);
        } else {
            holder.cvFav.setVisibility(View.VISIBLE);

        }
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

    static class StockHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvPrice;
        private TextView tvName;
        private TextView tvDescription;
        private FoldingCell folding_cell;

        private TextView tvCount;
        private CardView cvFav;

        IStockListener stockListener;

        public StockHolder(@NonNull View itemView, IStockListener stockListener) {
            super(itemView);
            this.stockListener = stockListener;

            ivImage = itemView.findViewById(R.id.ivImage);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            folding_cell = itemView.findViewById(R.id.folding_cell);

            ImageView ivIncrease = itemView.findViewById(R.id.ivIncrease);
            tvCount = itemView.findViewById(R.id.tvCount);
            ImageView ivDecrease = itemView.findViewById(R.id.ivDecrease);
            ImageView ivAdd = itemView.findViewById(R.id.ivAdd);
            cvFav = itemView.findViewById(R.id.cvFav);

            ivImage.setOnClickListener(this);
            ivIncrease.setOnClickListener(this);
            ivDecrease.setOnClickListener(this);
            ivAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivImage:
                    stockListener.onItemClick(ivImage, getAdapterPosition());
                    break;
                case R.id.ivIncrease:
                    stockListener.onIncreaseClick(folding_cell, getAdapterPosition());
                    break;
                case R.id.ivDecrease:
                    stockListener.onDecreaseClick(folding_cell, getAdapterPosition());
                    break;
                case R.id.ivAdd:
                    stockListener.onAddClick(folding_cell, getAdapterPosition());
                    break;
            }
        }
    }

    public interface IStockListener {
        void onItemClick(View v, int position);

        void onAddClick(View v, int position);

        void onIncreaseClick(View v, int position);

        void onDecreaseClick(View v, int position);
    }
}
