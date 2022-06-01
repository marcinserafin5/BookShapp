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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookAdapter extends FirebaseRecyclerAdapter<BookModel, BookAdapter.MyViewHolder> {
    Context context;
    FirebaseRecyclerOptions<Book> list;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;


    public BookAdapter(@NonNull FirebaseRecyclerOptions<BookModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book,parent,false);
        return new MyViewHolder(v);
    }

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Book book = list.get(position);
//        holder.bookTitle.setText(book.getBookTitle());
//        holder.bookAuthor.setText(book.getBookAuthor());
//        holder.bookCategory.setText(book.getBookCategory());
//        holder.bookPrice.setText(String.valueOf(book.getBookPrice()));
//        holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity2(holder);
//            }
//        });
//        holder.favButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addToFav(holder);
//            }
//        });
//    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull BookModel bookModel) {
        holder.bookTitle.setText(bookModel.getBookTitle());
        holder.bookAuthor.setText(bookModel.getBookAuthor());
        holder.bookCategory.setText(bookModel.getBookCategory());
        holder.bookPrice.setText(String.valueOf(bookModel.getBookPrice()));
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
                String key = getRef(i).getKey();
                goToInfo(v,key,bookModel.getBookTitle());
            }
        });
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
                favButton = itemView.findViewById(R.id.favButton);
            button =  itemView.findViewById(R.id.addToCartButton);
            infoButton =  itemView.findViewById(R.id.infoButton);


        }


      }

    public void openActivity2(MyViewHolder holder) {
        String timestamp =String.valueOf( System.currentTimeMillis());

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
//        reference.setValue("Hello, World!");
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


    public void addToFav(MyViewHolder holder) {
        String timestamp =String.valueOf( System.currentTimeMillis());

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
//        reference.setValue("Hello, World!");
        reference.child(userId).child("fav").child(timestamp).child("bookTitle").setValue(holder.bookTitle.getText().toString());
        reference.child(userId).child("fav").child(timestamp).child("bookAuthor").setValue(holder.bookAuthor.getText().toString());
        reference.child(userId).child("fav").child(timestamp).child("bookCategory").setValue(holder.bookCategory.getText().toString());
        reference.child(userId).child("fav").child(timestamp).child("bookPrice").setValue(Integer.parseInt(holder.bookPrice.getText().toString()));
//        holder.bookTitle.setText(book.getBookTitle());
//        holder.bookAuthor.setText(book.getBookAuthor());
//        holder.bookCategory.setText(book.getBookCategory());
//        holder.bookPrice.setText(book.getBookPrice());
        Toast.makeText(context, "Dodano do Ulubionych", Toast.LENGTH_SHORT).show();
    }

    public void goToInfo(View holder, String key,String title){
        Intent intent = new Intent(context, Comments.class);
        intent.putExtra("key",key);
        intent.putExtra("bookName",title);
        context.startActivity(intent);
    }
}
