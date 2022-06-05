package com.standalone.tradeplan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.standalone.tradeplan.R;
import com.standalone.tradeplan.adapters.PlanAdapter;
import com.standalone.tradeplan.models.DataPlan;
import com.standalone.tradeplan.utils.DbHandler;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PlanAdapter adapter;
    private DbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHandler(this);
        db.openDb();

        RecyclerView recyclerView = findViewById(R.id.rv_watchlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlanAdapter(this, db);
        recyclerView.setAdapter(adapter);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<DataPlan> plans = db.getAllItems();
        Collections.reverse(plans);
        adapter.setData(plans);

    }
}