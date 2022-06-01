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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    CartListAdapter cartAdapter;
    ArrayList<Book> list;
    int price=0;
    TextView priceTextView;
    private FirebaseUser user;
    private String userId;
    private Button buyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        priceTextView=findViewById(R.id.price);
        buyButton = findViewById(R.id.buyButton);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });



        recyclerView = findViewById(R.id.cartListView);
        database = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("cart");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        cartAdapter = new CartListAdapter(this,list);
        recyclerView.setAdapter(cartAdapter);
        price=0;
        database.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book book = snapshot.getValue(Book.class);
                list.add(book);
                price+=book.bookPrice;

                priceTextView.setText(String.valueOf(price)+" zł");

                cartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Book book = snapshot.getValue(Book.class);
                price-=book.bookPrice;
                priceTextView.setText(String.valueOf(price)+" zł");

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

//        database.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            price=0;
//            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                Book book = dataSnapshot.getValue(Book.class);
//                list.add(book);
//                price+=book.bookPrice;
//            }
//
//
//            cartAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//        }
//    });
}
    public void openDialog(){
        BuyModal buyModal = new BuyModal();
        Bundle bundle = new Bundle();
        bundle.putString("price",String.valueOf(price));
        buyModal.setArguments(bundle);
        buyModal.show(getSupportFragmentManager(),"Example");
    }




}