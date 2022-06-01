package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class BuyModal extends AppCompatDialogFragment {
    TextView price;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.buymodal,null);
        Bundle bundle = getArguments();
        price = view.findViewById(R.id.price);
        price.setText("Do zapłaty " + bundle.getString("price")+ " zł");
        builder.setView(view)
                .setTitle("Kup teraz")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Kup", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openActivity2(bundle.getString("price"));
                        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Users");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String userId = user.getUid();

                        Query applesQuery = ref.child(userId).child("cart");

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });
        return builder.create();

    }
    public void openActivity2(String price) {
         FirebaseUser user;
         DatabaseReference reference;
         String userId;
        String timestamp =String.valueOf( System.currentTimeMillis());

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        reference.child(userId).child("history").child(timestamp).child("price").setValue(price);
        reference.child(userId).child("history").child(timestamp).child("date").setValue(timestamp);


//        Toast.makeText(null, "Dodano do koszyka", Toast.LENGTH_SHORT).show();
    }
}
