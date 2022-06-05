package com.standalone.tradeplan.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.standalone.tradeplan.models.DataPlan;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {


    private static final int VERSION = 1;
    private static final String DB_NAME = "db_trade_plan";
    private static final String TABLE_NAME = "tbl_plan";
    private static final String ID = "id";
    private static final String SYMBOL = "symbol";
    private static final String QUANTITY = "quantity";
    private static final String ENTRY_POINT = "entry_point";
    private static final String TARGET_PRICE = "target_price";
    private static final String STOP_LOSS = "stop_loss";
    private static final String RRR = "risk_reward_ratio";
    private static final String ENTRY_DATE = "entry_date";
    private static final String SETUPS = "setups";

    private static final String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SYMBOL + " TEXT, "
            + QUANTITY + " TEXT, "
            + ENTRY_POINT + " INTERGER, "
            + TARGET_PRICE + " INTERGER, "
            + STOP_LOSS + " INTERGER, "
            + RRR + " INTERGER, "
            + ENTRY_DATE + " TEXT, "
            + SETUPS + " TEXT)";

    private SQLiteDatabase db;

    public DbHandler(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void openDb() {
        db = this.getWritableDatabase();
    }

    @SuppressLint("Range")
    public List<DataPlan> getAllItems() {
        List<DataPlan> plans = new ArrayList<>();
        Cursor curs = null;
        db.beginTransaction();
        try {
            curs = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (curs != null) {
                if (curs.moveToFirst()) {
                    do {
                        DataPlan plan = new DataPlan();
                        plan.setId(curs.getInt(curs.getColumnIndex(ID)));
                        plan.setSymbol(curs.getString(curs.getColumnIndex(SYMBOL)));
                        plan.setQuantity(curs.getInt(curs.getColumnIndex(QUANTITY)));
                        plan.setEntryPoint(curs.getDouble(curs.getColumnIndex(ENTRY_POINT)));
                        plan.setTargetPrice(curs.getDouble(curs.getColumnIndex(TARGET_PRICE)));
                        plan.setStopLoss(curs.getDouble(curs.getColumnIndex(STOP_LOSS)));
                        plan.setRRR(curs.getInt(curs.getColumnIndex(RRR)));
                        plan.setDate(curs.getString(curs.getColumnIndex(ENTRY_DATE)));
                        plan.setSetups(curs.getString(curs.getColumnIndex(SETUPS)));

                        plans.add(plan);

                    } while (curs.moveToNext());
                }
            }

        } finally {
            db.endTransaction();
            assert curs != null;
            curs.close();
        }
        return plans;
    }

    public void insertItem(DataPlan plan) {
        ContentValues cv = new ContentValues();
        cv.put(SYMBOL, plan.getSymbol());
        cv.put(QUANTITY, plan.getQuantity());
        cv.put(ENTRY_POINT, plan.getEntryPoint());
        cv.put(TARGET_PRICE, plan.getTargetPrice());
        cv.put(STOP_LOSS, plan.getStopLoss());
        cv.put(RRR, plan.getRRR());
        cv.put(ENTRY_DATE, plan.getDate());
        cv.put(SETUPS, plan.getSetups());

        db.insert(TABLE_NAME, null, cv);
    }

    public void updateItem(int id, String symbol, int quantity, Double entry, Double target, Double stop, int ratio, String date, String setups) {
        ContentValues cv = new ContentValues();
        cv.put(SYMBOL, symbol);
        cv.put(QUANTITY, quantity);
        cv.put(ENTRY_POINT, entry);
        cv.put(TARGET_PRICE, target);
        cv.put(RRR, ratio);
        cv.put(ENTRY_DATE, date);
        cv.put(SETUPS, setups);

        db.update(TABLE_NAME, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void deleteItem(int id) {
        db.delete(TABLE_NAME, ID + "= ?", new String[]{String.valueOf(id)});
    }
}
