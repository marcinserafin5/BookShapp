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

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    Context context;
    ArrayList<Comment> list;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    public CommentsAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        return new CommentsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.MyViewHolder holder, int position) {
        Comment book = list.get(position);
        Date date;
        if(book.getDate()!=null){
            date=new Date(Long.parseLong(book.getDate()));
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
            holder.date.setText((sf.format(date)).toString());

        }


        holder.commentText.setText(book.getText() );
        holder.name.setText(book.getName());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,commentText,date;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            commentText = itemView.findViewById(R.id.commentText);




        }


    }


}
