package com.bayraktar.stockcontrol.ui.stock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.adapters.StockAdapter;
import com.bayraktar.stockcontrol.database.model.Stock;
import com.bayraktar.stockcontrol.ui.basket.BasketActivity;
import com.bayraktar.stockcontrol.view.StockDetailActivity;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashMap;
import java.util.List;

public class StockFragment extends Fragment implements StockAdapter.IStockListener {

    private static int BASKET_REQUEST_CODE = 8;
    private static int STOCK_DETAIL_REQUEST_CODE = 9;

    RecyclerView rvStocks;
    StockAdapter stockAdapter;
    HashMap<Integer, Integer> basket;
    List<Stock> _stocks;

    private boolean basketClickable;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stock, container, false);

        rvStocks = root.findViewById(R.id.rvStocks);
        basket = new HashMap<>();
        setHasOptionsMenu(true);
        setMenuVisible(false);
        basketClickable = true;
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvStocks.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvStocks.setHasFixedSize(true);

        stockAdapter = new StockAdapter(this);
        rvStocks.setAdapter(stockAdapter);

        StockViewModel mViewModel = new ViewModelProvider(this).get(StockViewModel.class);
        mViewModel.getAllStocks().observe(getViewLifecycleOwner(), new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                _stocks = stocks;
                stockAdapter.setStocks(stocks);
            }
        });

        if (savedInstanceState != null) {
            Toast.makeText(getContext(), "SAVED INSTANCE DAHA SONRA UYGULANACAK", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater _inflater) {
        _inflater.inflate(R.menu.stock_toolbar, menu);
        MenuItem totalPriceMenu = menu.findItem(R.id.menu_total_price);
        float totalPrice = 0f;
        if (!basket.isEmpty()) {
            for (Integer key : basket.keySet()) {
                float stockPrice = findStockPriceById(key);
                int tempCount = basket.get(key);
                totalPrice += stockPrice * tempCount;
            }
        }
        totalPriceMenu.setTitle("₺ " + String.format("%.2f", totalPrice));

        if (getActivity() != null) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh, null);
            Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
            rotation.setRepeatCount(1);
            iv.startAnimation(rotation);
            menu.findItem(R.id.menu_basket).setActionView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startBasketActivity();
                }
            });
        }
        super.onCreateOptionsMenu(menu, _inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startBasketActivity();
        return super.onOptionsItemSelected(item);
    }

    void startBasketActivity() {
        Intent intent = new Intent(getActivity(), BasketActivity.class);
        intent.putExtra("hashMap", basket);
        startActivityForResult(intent, BASKET_REQUEST_CODE);
    }

    float findStockPriceById(int id) {
        if (_stocks != null && _stocks.size() > 0) {
            for (int i = 0; i < _stocks.size(); i++) {
                Stock stock = _stocks.get(i);
                if (stock.getId() == id) {
                    return stock.getPrice();
                }
            }
        }
        return 0;
    }

    void setMenuVisible(boolean visible) {
        if (getActivity() != null) {
            setHasOptionsMenu(visible);
            getActivity().invalidateOptionsMenu();
        }
    }

    int updateBasket(int position, boolean isAdd) {

        int count;
        Stock stock = _stocks.get(position);
        if (!basket.containsKey(stock.getId())) {
            count = 1;
        } else {
            count = basket.get(stock.getId());
            if (isAdd)
                count++;
            else
                count--;
        }
        stock.setQuantity(count);
        if (count == 0) {
            basket.remove(stock.getId());
        } else {
            basket.put(stock.getId(), count);
        }
        checkBasket();
        return count;
    }

    void checkBasket() {
        if (basket.isEmpty()) {
            setMenuVisible(false);
        } else {
            setMenuVisible(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == STOCK_DETAIL_REQUEST_CODE) {
            basketClickable = true;
            if (resultCode == Activity.RESULT_OK) {
                Log.d("TAG", "onActivityResult: " + requestCode);
                if (data != null) {
                    boolean fav = data.getBooleanExtra("fav", false);
                    int position = data.getIntExtra("position", -1);

                    Stock stock = _stocks.get(position);
                    if (stock.isFav() != fav) {
                        if (stock.getQuantity() > 0)
                            Toast.makeText(getContext(), "STOK MİKTARI İLE İLGİLENMEK GEREKİYOR", Toast.LENGTH_SHORT).show();
                        stock.setFav(fav);
                        stockAdapter.notifyItemChanged(position);
                    }
                }
            }
        } else if (requestCode == BASKET_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Toast.makeText(getContext(), "DEĞİŞİKLİKLER DAHA SONRA UYGULANACAK", Toast.LENGTH_SHORT).show();
                //HANDLE NEW BASKET
                //data.getSerializableExtra("hashMap");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(View v, int position) {
        if (basketClickable) {
            basketClickable = false;
            Stock stock = _stocks.get(position);
            Intent intent = new Intent(getActivity(), StockDetailActivity.class);
            intent.putExtra("stockId", stock.getId());
            intent.putExtra("title", stock.getName());
            intent.putExtra("position", position);
            intent.putExtra("fav", stock.isFav());
            startActivityForResult(intent, STOCK_DETAIL_REQUEST_CODE);
        }
    }

    @Override
    public void onAddClick(View v, int position) {
        updateBasket(position, true);
        stockAdapter.notifyItemChanged(position, v);
        if (v instanceof FoldingCell) {
            setMenuVisible(true);
            ((FoldingCell) v).toggle(false);
        }
    }

    @Override
    public void onIncreaseClick(View v, int position) {
        updateBasket(position, true);
        stockAdapter.notifyItemChanged(position, v);
    }

    @Override
    public void onDecreaseClick(View v, int position) {
        int count = updateBasket(position, false);
        if (count == 0 && v instanceof FoldingCell) {
            ((FoldingCell) v).toggle(false);
        }
        stockAdapter.notifyItemChanged(position, v);
    }
}