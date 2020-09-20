package com.bayraktar.stockcontrol.ui.basket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayraktar.stockcontrol.view.MainActivity;
import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.adapters.BasketAdapter;
import com.bayraktar.stockcontrol.database.model.Stock;
import com.bayraktar.stockcontrol.view.StockDetailActivity;

import java.util.HashMap;
import java.util.List;

public class BasketActivity extends AppCompatActivity implements BasketAdapter.IStockListener {

    private static final String HASH_MAP = "hashMap";

    TextView tvContinue;
    TextView tvTotalPrice;
    BasketViewModel mViewModel;
    BasketAdapter basketAdapter;
    RecyclerView rvBasket;
    private HashMap<Integer, Integer> basket;
    List<Stock> _stocks;
    private boolean basketClickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        basketClickable = true;

        rvBasket = findViewById(R.id.rvBasket);
        tvContinue = findViewById(R.id.tvContinue);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        rvBasket.setLayoutManager(new LinearLayoutManager(this));
        rvBasket.setHasFixedSize(true);
        basketAdapter = new BasketAdapter(this);
        rvBasket.setAdapter(basketAdapter);

        mViewModel = new ViewModelProvider(this).get(BasketViewModel.class);
        basket = (HashMap<Integer, Integer>) getIntent().getSerializableExtra(HASH_MAP);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_basket);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (basket != null && !basket.isEmpty()) {
            initializeBasket();
        }

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BasketActivity.this, "DAHA SONRA İLGİLENİLECEK!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basket_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            showDeleteMessage();
        } else {
            finishActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    void finishActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(HASH_MAP, basket);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    void initializeBasket() {
        int[] ids = new int[basket.size()];
        int i = 0;

        for (Integer key : basket.keySet()) {
            ids[i] = key;
            i++;
        }
        mViewModel.getAllByIds(ids).observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                setStockCount(stocks);
            }
        });
    }

    void setStockCount(List<Stock> stocks) {
        for (Stock stock : stocks) {
            int tempQuantity = basket.get(stock.getId());
            stock.setQuantity(tempQuantity);
        }
        _stocks = stocks;
        basketAdapter.setStocks(stocks);
        calculateTotalPrice();
    }

    void calculateTotalPrice() {
        float totalPrice = 0f;
        if (!basket.isEmpty()) {
            for (Integer key : basket.keySet()) {
                float stockPrice = findStockPriceById(key);
                int tempCount = basket.get(key);
                totalPrice += stockPrice * tempCount;
            }
        }
        tvTotalPrice.setText("₺ " + String.format("%.2f", totalPrice));
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

    void showDeleteMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ürünleri sepetinizden silmek istediğinize emin misiniz?")
                .setNegativeButton("İPTAL", null)
                .setPositiveButton("SİL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        basket.clear();
                        Intent intent = new Intent(BasketActivity.this, MainActivity.class);
                        intent.putExtra(HASH_MAP, basket);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                })
                .create().show();
    }

    int updateBasket(int position, boolean isAdd) {

        int count = 0;
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
            finishActivity();
        } else {
            calculateTotalPrice();
        }
    }


    @Override
    public void onItemClick(View v, int position) {
        if (basketClickable) {
            basketClickable = false;
            Stock stock = _stocks.get(position);
            Intent intent = new Intent(this, StockDetailActivity.class);
            intent.putExtra("stockId", stock.getId());
            intent.putExtra("title", stock.getName());
            intent.putExtra("position", position);
            intent.putExtra("fav", stock.isFav());
            startActivityForResult(intent, stock.getId());
        }
    }

    @Override
    public void onIncreaseClick(View v, int position) {
        updateBasket(position, true);
        basketAdapter.notifyItemChanged(position, v);
    }

    @Override
    public void onDecreaseClick(View v, int position) {
        int count = updateBasket(position, false);
        if (count == 0) {
            _stocks.remove(position);
            basketAdapter.notifyItemRemoved(position);
        }
        basketAdapter.notifyItemChanged(position, v);
    }
}