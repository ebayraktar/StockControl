package com.bayraktar.stockcontrol.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.database.model.Stock;
import com.bayraktar.stockcontrol.ui.stock.StockViewModel;
import com.bumptech.glide.Glide;

public class StockDetailActivity extends AppCompatActivity {

    ImageView ivStock;
    TextView tvPrice;
    TextView tvName;
    TextView tvDescription;

    private static final String STOCK_ID = "stockId";
    private static final String TITLE = "title";
    private static final String POSITION = "position";
    private static final String FAV = "fav";

    private int position;
    private boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        String title = getIntent().getStringExtra(TITLE);
        int stockId = getIntent().getIntExtra(STOCK_ID, 0);
        position = getIntent().getIntExtra(POSITION, -1);
        fav = getIntent().getBooleanExtra(FAV, false);
        if (savedInstanceState != null) {
            fav = savedInstanceState.getBoolean(FAV);
        }

        ivStock = findViewById(R.id.ivStock);
        tvPrice = findViewById(R.id.tvPrice);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);

        StockViewModel mViewModel = new ViewModelProvider(this).get(StockViewModel.class);
        mViewModel.getById(stockId).observe(this, new Observer<Stock>() {
            @Override
            public void onChanged(Stock stock) {
                if (stock != null) {
                    initializeStock(stock);
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAV, fav);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_detail_toolbar, menu);
        MenuItem item = menu.findItem(R.id.menu_fav);
        if (item != null) {
            updateFav(item);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_fav) {
            fav = !fav;
            updateFav(item);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(FAV, fav);
            intent.putExtra(POSITION, position);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void initializeStock(Stock stock) {
        tvName.setText(stock.getName());
        tvDescription.setText(stock.getDescription());
        tvPrice.setText("₺ " + String.format("%.2f", stock.getPrice()));
        Glide.with(this)
                .load(stock.getImageUrl())
                .fitCenter()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(ivStock);
    }

    void updateFav(MenuItem item) {
        if (fav) {
            item.setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            item.setIcon(R.drawable.ic_unfavorite_black_24dp);
        }
    }
}
