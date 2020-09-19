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

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockHolder> {
    private List<Stock> stocks = new ArrayList<>();

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item, parent, false);

        return new StockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockHolder holder, int position) {
        Stock currentStock = stocks.get(position);

        holder.tvName.setText(currentStock.getName());
        holder.tvDescription.setText(currentStock.getDescription());
        holder.tvPrice.setText("₺ " + String.format("%.2f", currentStock.getPrice()));

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

    static class StockHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private CardView cvAdd;
        private TextView tvPrice;
        private TextView tvName;
        private TextView tvDescription;

        public StockHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            cvAdd = itemView.findViewById(R.id.cvAdd);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
