package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    BookAdapter bookAdapter;
    ArrayList<Book> list;
    Button cartButton;
    ImageButton historyButton,navigateFavButton,searchButton;
    TextView searchText;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.bookCat,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchImageButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        cartButton = findViewById(R.id.CartButton);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateHistory();
            }
        });
        navigateFavButton = findViewById(R.id.navigateFavButton);
        navigateFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateFav();
            }
        });

        recyclerView = findViewById(R.id.bookList);
            database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<BookModel> options=
                new FirebaseRecyclerOptions.Builder<BookModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Books"),BookModel.class)
                .build();
        bookAdapter = new BookAdapter(options,this);
        recyclerView.setAdapter(bookAdapter);
//        list = new ArrayList<>();
//        bookAdapter = new BookAdapter(this,list);
//        recyclerView.setAdapter(bookAdapter);

//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Book book = dataSnapshot.getValue(Book.class);
////                    if(book.bookAuthor=="Szylwek")
//                    list.add(book);
//                }
//
//                bookAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bookAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bookAdapter.stopListening();
    }

    public void openActivity2() {
        Intent intent = new Intent(this, Cart.class);
        startActivity(intent);
    }

    public void search(){
        String text = searchText.getText().toString();
        FirebaseRecyclerOptions<BookModel> options=
                new FirebaseRecyclerOptions.Builder<BookModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Books").orderByChild("bookTitle").startAt(text).endAt(text+"~"),BookModel.class)
                        .build();
        bookAdapter = new BookAdapter(options,this);
        bookAdapter.startListening();
        recyclerView.setAdapter(bookAdapter);
    }

    public void navigateHistory() {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);

    }
    public void navigateFav() {
        Intent intent = new Intent(this, FavList.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        if(!text.equals("Filtruj")){
        FirebaseRecyclerOptions<BookModel> options=
                new FirebaseRecyclerOptions.Builder<BookModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Books").orderByChild("bookCategory").startAt(text).endAt(text+"~"),BookModel.class)
                        .build();
        bookAdapter = new BookAdapter(options,this);
        }else{
            FirebaseRecyclerOptions<BookModel> options=
                    new FirebaseRecyclerOptions.Builder<BookModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference("Books"),BookModel.class)
                            .build();
            bookAdapter = new BookAdapter(options,this);

        }
        bookAdapter.startListening();
        recyclerView.setAdapter(bookAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        FirebaseRecyclerOptions<BookModel> options=
                new FirebaseRecyclerOptions.Builder<BookModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Books"),BookModel.class)
                        .build();
        bookAdapter = new BookAdapter(options,this);

        bookAdapter.startListening();
        recyclerView.setAdapter(bookAdapter);
    }
}