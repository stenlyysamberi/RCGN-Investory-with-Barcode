package com.example.rcgnwhrsinventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rcgnwhrsinventory.Adapter.Activityadapter;
import com.example.rcgnwhrsinventory.Adapter.ViewallAdapter;
import com.example.rcgnwhrsinventory.Internet.APis;
import com.example.rcgnwhrsinventory.Internet.Endpoints;
import com.example.rcgnwhrsinventory.Model.Mactivity;
import com.example.rcgnwhrsinventory.Model.Main;
import com.example.rcgnwhrsinventory.Model.Mviewall;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullActivity extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    ViewallAdapter viewallAdapter;
    String kata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);

        TextView key = findViewById(R.id.text_key_input);

        key.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    kata = key.getText().toString();
                    getData(kata);
                }
                return false;
            }
        });

        getData(kata);


    }

    private void getData(String keyword) {
        try {
            shimmerFrameLayout = findViewById(R.id.shimer_full_item);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();

            Endpoints endpoint = APis.getRetrofitInstance().create(Endpoints.class);
            Call<Main> info = endpoint.viewAll(keyword);
            info.enqueue(new Callback<Main>() {
                @Override
                public void onResponse(Call<Main> call, Response<Main> response) {
                    List<Mviewall> info = response.body().getViewall();

                    if (response.isSuccessful() && response.body()!=null){
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView = findViewById(R.id.recy_all_material);
                        recyclerView.setEnabled(false);
                        viewallAdapter = new ViewallAdapter(getApplicationContext(),info);
                        recyclerView.setAdapter(viewallAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    }else{
                        Log.e("error", response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<Main> call, Throwable t) {
                    Log.e("errors", t.toString());
                }
            });

        }catch (Exception e){
            Log.e("tryError", String.valueOf(e));
        }
    }


}