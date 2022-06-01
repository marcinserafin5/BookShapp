package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.MyViewHolder> {
    Context context;
    ArrayList<Book> list;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    public FavListAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.book,parent,false);
        return new FavListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavListAdapter.MyViewHolder holder, int position) {
        Book book = list.get(position);
        holder.bookTitle.setText(book.getBookTitle());
        holder.bookAuthor.setText(book.getBookAuthor());
        holder.bookCategory.setText(book.getBookCategory());
        holder.bookPrice.setText(String.valueOf(book.getBookPrice()));
        final String[] key = new String[1];
        FirebaseDatabase.getInstance().getReference("Books").orderByChild("bookTitle").equalTo(book.getBookTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    key[0] = childSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2(holder);
            }
        });
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFav(holder);
            }
        });
        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInfo(v,key[0],book.getBookTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle,bookAuthor,bookCategory,bookPrice;
        Button button;
        ImageButton favButton,infoButton;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookCategory = itemView.findViewById(R.id.bookCategory);
            bookPrice = itemView.findViewById(R.id.bookPrice);

            button =  itemView.findViewById(R.id.addToCartButton);
            infoButton =  itemView.findViewById(R.id.infoButton);

            favButton = itemView.findViewById(R.id.favButton);


        }


    }

    public void openActivity2(FavListAdapter.MyViewHolder holder) {
        String timestamp =String.valueOf( System.currentTimeMillis());

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        reference.child(userId).child("cart").child(timestamp).child("bookTitle").setValue(holder.bookTitle.getText().toString());
        reference.child(userId).child("cart").child(timestamp).child("bookAuthor").setValue(holder.bookAuthor.getText().toString());
        reference.child(userId).child("cart").child(timestamp).child("bookCategory").setValue(holder.bookCategory.getText().toString());
        reference.child(userId).child("cart").child(timestamp).child("bookPrice").setValue(Integer.parseInt(holder.bookPrice.getText().toString()));
//        holder.bookTitle.setText(book.getBookTitle());
//        holder.bookAuthor.setText(book.getBookAuthor());
//        holder.bookCategory.setText(book.getBookCategory());
//        holder.bookPrice.setText(book.getBookPrice());
        Toast.makeText(context, "Dodano do koszyka", Toast.LENGTH_SHORT).show();
    }
    public void addToFav(FavListAdapter.MyViewHolder holder) {
        String timestamp =String.valueOf( System.currentTimeMillis());

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        reference.child(userId).child("fav").child(timestamp).child("bookTitle").setValue(holder.bookTitle.getText().toString());
        reference.child(userId).child("fav").child(timestamp).child("bookAuthor").setValue(holder.bookAuthor.getText().toString());
        reference.child(userId).child("fav").child(timestamp).child("bookCategory").setValue(holder.bookCategory.getText().toString());
        reference.child(userId).child("fav").child(timestamp).child("bookPrice").setValue(Integer.parseInt(holder.bookPrice.getText().toString()));
//        holder.bookTitle.setText(book.getBookTitle());
//        holder.bookAuthor.setText(book.getBookAuthor());
//        holder.bookCategory.setText(book.getBookCategory());
//        holder.bookPrice.setText(book.getBookPrice());
        Toast.makeText(context, "Dodano do ulubionych", Toast.LENGTH_SHORT).show();
    }
    public void goToInfo(View holder, String key,String title){
        Intent intent = new Intent(context, Comments.class);
        intent.putExtra("key",key);
        intent.putExtra("bookName",title);
        context.startActivity(intent);
    }
}
