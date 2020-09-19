package com.bayraktar.stockcontrol.ui.stock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.adapters.StockAdapter;
import com.bayraktar.stockcontrol.database.model.Stock;

import java.util.List;

public class StockFragment extends Fragment {

    private StockViewModel mViewModel;
    RecyclerView rvStocks;
    StockAdapter stockAdapter;

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stock, container, false);
        rvStocks = root.findViewById(R.id.rvStocks);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvStocks.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvStocks.setHasFixedSize(true);

        stockAdapter = new StockAdapter();
        rvStocks.setAdapter(stockAdapter);

        mViewModel = ViewModelProviders.of(this).get(StockViewModel.class);
        mViewModel.getAllStocks().observe(getViewLifecycleOwner(), new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                stockAdapter.setStocks(stocks);
            }
        });
    }

}