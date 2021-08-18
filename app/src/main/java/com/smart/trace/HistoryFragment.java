package com.smart.trace;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smart.trace.adapter.ProductAdapterHistory;
import com.smart.trace.model.Product;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {


    private FirebaseAuth mAuth;
    private String userID;

    private RecyclerView recyclerViewHistory;
    private TextView textViewNoResult;

    private ProductAdapterHistory productAdapter;
    private ArrayList<Product> productArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      View root = inflater.inflate(R.layout.fragment_history, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

      intiView(root);
      getdata();
      return root;
    }

    private void intiView(View view){
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistoryProducts);
        textViewNoResult = view.findViewById(R.id.textViewNoResult);
    }

    private void getdata() {

        // creating a variable for
        // our Firebase Database.
        FirebaseDatabase firebaseDatabase;

        // creating a variable for our
        // Database Reference for Firebase.
        DatabaseReference databaseReference;

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("Items");

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productArrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Product product  = new Product();

                    product.setBuyerUserID(ds.child("buyerUserID").getValue().toString());
                    product.setKey(ds.child("key").getValue().toString());
                    product.setPicture(ds.child("picture").getValue().toString());
                    product.setName(ds.child("name").getValue().toString());
                    product.setModel(ds.child("model").getValue().toString());
                    product.setSerialNumber(ds.child("serialNumber").getValue().toString());
                    product.setMaker(ds.child("maker").getValue().toString());
                    product.setSeller(ds.child("seller").getValue().toString());
                    product.setCondition(ds.child("condition").getValue().toString());
                    product.setPrice(ds.child("price").getValue().toString());
                    product.setSellerUserID(ds.child("sellerUserID").getValue().toString());

                    if(product.getSellerUserID().equals(userID)){
                        productArrayList.add(product);
                    }
                }

                productAdapter = new ProductAdapterHistory(getActivity(),productArrayList);
                recyclerViewHistory.setAdapter(productAdapter);
                recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

                if(productArrayList.size()<0){
                    recyclerViewHistory.setVisibility(View.GONE);
                    textViewNoResult.setVisibility(View.VISIBLE);
                }else {
                    recyclerViewHistory.setVisibility(View.VISIBLE);
                    textViewNoResult.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}