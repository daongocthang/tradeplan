package com.standalone.tradeplan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.standalone.tradeplan.R;
import com.standalone.tradeplan.models.DataPlan;
import com.standalone.tradeplan.utils.DbHandler;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {
    private TextView tvDate;
    private EditText edSymbol;
    private EditText edQuantity;
    private RadioGroup grpRRR;
    private EditText edEntryPoint;
    private EditText edTargetPrice;
    private EditText edStopLoss;
    private EditText edSetups;

    private int RRR;
    private boolean isUpdate;
    private DataPlan currentPlan;

    private DbHandler db;

    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        isUpdate = false;
        if (bundle != null) {
            isUpdate = true;
            currentPlan = (DataPlan) bundle.getSerializable("plan");
        }

        tvDate = findViewById(R.id.ed_date);
        edSymbol = findViewById(R.id.ed_symbol);
        edQuantity = findViewById(R.id.ed_quantity);
        edEntryPoint = findViewById(R.id.ed_entry);
        edTargetPrice = findViewById(R.id.ed_target);
        edStopLoss = findViewById(R.id.ed_stop);
        edSetups = findViewById(R.id.ed_setups);
        grpRRR = findViewById(R.id.rg_ratio);

        db = new DbHandler(this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onStart() {
        super.onStart();
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        db.openDb();

        tvDate.setText(String.format("%02d-%02d-%d", day, month, year).toString());
        if (isUpdate) {
            tvDate.setText(currentPlan.getDate());
            edSymbol.setText(currentPlan.getSymbol());
            edQuantity.setText(String.valueOf(currentPlan.getQuantity()));
            edEntryPoint.setText(String.valueOf(currentPlan.getEntryPoint()));
            edTargetPrice.setText(String.valueOf(currentPlan.getTargetPrice()));
            edStopLoss.setText(String.valueOf(currentPlan.getStopLoss()));
            edSetups.setText(String.valueOf(currentPlan.getSetups()));
        }

        final ImageButton btCal = findViewById(R.id.bt_calendar);
        btCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog(v);
            }
        });

        final Button btSubmit = findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert of update to SQLite
                submit();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void showCalendarDialog(View view) {
        new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    tvDate.setText(String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year));
                }, year, month, day).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    public void submit() {
        String date = tvDate.getText().toString().toUpperCase();
        String symbol = edSymbol.getText().toString();
        int quantity = Integer.parseInt(edQuantity.getText().toString());
        double entry = Double.parseDouble(edEntryPoint.getText().toString());
        double target = Double.parseDouble(edTargetPrice.getText().toString());
        double stop = Double.parseDouble(edStopLoss.getText().toString());
        String setups = edSetups.getText().toString();

        int selectedRadioId = grpRRR.getCheckedRadioButtonId();
        switch (selectedRadioId) {
            case R.id.rd_ratio_0:
                RRR = -1;
                break;
            case R.id.rd_ratio_1:
                RRR = 2;
                break;
            case R.id.rd_ratio_2:
                RRR = 3;
                break;
        }

        if (isUpdate) {
            db.updateItem(currentPlan.getId(), symbol, quantity, entry, target, stop, RRR, date, setups);
        } else {
            DataPlan newPlan = new DataPlan();
            newPlan.setSymbol(symbol);
            newPlan.setQuantity(quantity);
            newPlan.setEntryPoint(entry);
            newPlan.setTargetPrice(target);
            newPlan.setStopLoss(stop);
            newPlan.setRRR(RRR);
            newPlan.setDate(date);
            newPlan.setSetups(setups);

            db.insertItem(newPlan);
        }

        finish();
    }
}