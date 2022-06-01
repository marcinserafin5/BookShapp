package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Comments extends AppCompatActivity {
    private DatabaseReference reference;
    private String userId;
    RecyclerView recyclerView;
    DatabaseReference database;
    CommentsAdapter cartAdapter;
    ArrayList<Comment> list;
    int price=0;
    TextView priceTextView;
    private FirebaseUser user;
    private Button sendCommentButton;
    TextView commentText;
    String bookNumber;
    String bookTitle;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Bundle extras = getIntent().getExtras();
        bookNumber=extras.getString("key");
        bookTitle=extras.getString("bookName");
        title= findViewById(R.id.cartTitle);
        title.setText(bookTitle);
        commentText = findViewById(R.id.commentText);
        sendCommentButton = findViewById(R.id.sendCommentButton);
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();




        recyclerView = findViewById(R.id.commentsListView);
        database = FirebaseDatabase.getInstance().getReference("Books").child(bookNumber).child("comments");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        cartAdapter = new CommentsAdapter(this,list);
        recyclerView.setAdapter(cartAdapter);
        price=0;
        database.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment book = snapshot.getValue(Comment.class);
                list.add(book);



                cartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HistoryItem book = snapshot.getValue(HistoryItem.class);

                list.remove(list.size()-1);
                cartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void sendComment() {
        String timestamp =String.valueOf( System.currentTimeMillis());

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference =FirebaseDatabase.getInstance().getReference("Books").child(bookNumber).child("comments");
        userId = user.getUid();
        String username = user.getEmail();

//        reference.setValue("Hello, World!");
        reference.child(timestamp).child("name").setValue(username);
        reference.child(timestamp).child("text").setValue(commentText.getText().toString());
        reference.child(timestamp).child("date").setValue(timestamp);
        commentText.setText("");

//        holder.bookTitle.setText(book.getBookTitle());
//        holder.bookAuthor.setText(book.getBookAuthor());
//        holder.bookCategory.setText(book.getBookCategory());
//        holder.bookPrice.setText(book.getBookPrice());

    }




}