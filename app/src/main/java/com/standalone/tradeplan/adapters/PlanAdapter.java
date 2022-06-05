package com.standalone.tradeplan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.standalone.tradeplan.R;
import com.standalone.tradeplan.activities.FormActivity;
import com.standalone.tradeplan.models.DataPlan;
import com.standalone.tradeplan.utils.DbHandler;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<DataPlan> data;
    private final DbHandler db;
    private final Context context;

    public PlanAdapter(Context context, DbHandler db) {
        this.db = db;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"DefaultLocale", "RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        this.db.openDb();
        String LB_AMOUNT = "AMT";
        String LB_QUANTITY = "QTY";
        String LB_RRR = "RRR";

        DataPlan item = data.get(pos);

        holder.tvSymbol.setText(item.getSymbol());
        holder.tvDate.setText(item.getDate());
        holder.tvQuantity.setText(String.format("%s: %,d", LB_QUANTITY, item.getQuantity()));
        holder.tvRRR.setText(String.format("%s: %d", LB_RRR, item.getRRR()));
        holder.tvEntryPoint.setText(String.valueOf(item.getEntryPoint()));
        holder.tvTargetPrice.setText(String.valueOf(item.getTargetPrice()));
        holder.tvStopLoss.setText(String.valueOf(item.getStopLoss()));
        holder.tvSetups.setText(item.getSetups());
        holder.tvAmount.setText(String.format("%s: %,.0f", LB_AMOUNT, item.getQuantity() * item.getEntryPoint()));

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(pos);
            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<DataPlan> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    public void editItem(int pos) {
        DataPlan item = data.get(pos);
        Intent intent = new Intent(this.context, FormActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("plan", item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void deleteItem(int pos) {
        DataPlan item = data.get(pos);
        db.deleteItem(item.getId());
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvSymbol;
        TextView tvAmount;
        TextView tvQuantity;
        TextView tvRRR;
        TextView tvEntryPoint;
        TextView tvTargetPrice;
        TextView tvStopLoss;
        TextView tvSetups;

        ImageButton btEdit;
        ImageButton btDelete;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvSymbol = view.findViewById(R.id.tv_symbol);
            tvDate = view.findViewById(R.id.tv_date);
            tvAmount = view.findViewById(R.id.tv_amount);
            tvQuantity = view.findViewById(R.id.tv_quantity);
            tvRRR = view.findViewById(R.id.tv_ratio);
            tvEntryPoint = view.findViewById(R.id.tv_entry);
            tvTargetPrice = view.findViewById(R.id.tv_target);
            tvStopLoss = view.findViewById(R.id.tv_stop);
            tvSetups = view.findViewById(R.id.tv_setups);

            btEdit = view.findViewById(R.id.bt_edit);
            btDelete = view.findViewById(R.id.bt_delete);
        }
    }

    public void showAlertDialog(int pos) {
        final String title = "Alert";
        final String message = "Are you sure you want to delete this item?";
        AlertDialog.Builder builder = new AlertDialog.Builder((context));
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(pos);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
