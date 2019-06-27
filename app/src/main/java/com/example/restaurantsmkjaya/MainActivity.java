package com.example.restaurantsmkjaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static RecyclerView rvMenu;
    static List<PesananModel> ls = new ArrayList<>();
    static TextView tvNone;
    static SwipeRefreshLayout swLayout;
    SharedPreferences pref;

    static void getPesanan() {
        ls.clear();
        rvMenu.setVisibility(View.GONE);
        tvNone.setVisibility(View.VISIBLE);
        AndroidNetworking.get(EndPoint.getOrderDetail).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("detail");
                    for (int x = 0; x < arr.length(); x++) {
                        JSONObject obj = arr.getJSONObject(x);
                        ls.add(new PesananModel(String.valueOf(obj.getInt("DetailId")), obj.getString("Name"), String.valueOf(obj.getInt("Qty")), String.valueOf(obj.getInt("TableId")), Base64.decode(obj.getString("Photo"), Base64.DEFAULT)));
                    }

                    if (ls.size() > 0) {
                        tvNone.setVisibility(View.GONE);
                        rvMenu.setVisibility(View.VISIBLE);
                    }
                    swLayout.setRefreshing(false);
                    rvMenu.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                swLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("RestaurantPref", MODE_PRIVATE);

        getSupportActionBar().setTitle("Siap diantar");
        rvMenu = findViewById(R.id.rvPesanan);
        rvMenu.setAdapter(new PesananAdapter(this, ls));
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        tvNone = findViewById(R.id.tv_none);
        swLayout = findViewById(R.id.swipe_layout);
        swLayout.setRefreshing(true);
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPesanan();

            }
        });
        AndroidNetworking.initialize(this);
        getPesanan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("Login", false);
            editor.apply();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
