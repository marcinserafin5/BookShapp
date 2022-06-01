package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder> {
    Context context;
    ArrayList<HistoryItem> list;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    public HistoryListAdapter(Context context, ArrayList<HistoryItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_item,parent,false);
        return new HistoryListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListAdapter.MyViewHolder holder, int position) {
        HistoryItem book = list.get(position);
        Date date=new Date(Long.parseLong(book.getDate()));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        holder.historyDate.setText((sf.format(date)).toString());
        holder.historyPrice.setText(book.getPrice() + " zł");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView historyDate,historyPrice,bookCategory,bookPrice;
        Button button;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            historyDate = itemView.findViewById(R.id.historyDate);
            historyPrice = itemView.findViewById(R.id.historyPrice);





        }


    }


}
