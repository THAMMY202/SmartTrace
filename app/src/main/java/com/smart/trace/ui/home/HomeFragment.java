package com.smart.trace.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smart.trace.MainActivity;
import com.smart.trace.R;
import com.smart.trace.adapter.ProductAdapter;
import com.smart.trace.model.Product;

import java.util.ArrayList;

public class HomeFragment extends Fragment  {

    private FirebaseAuth mAuth;
    private DatabaseReference mAlbumsDatabase;
    private String userID;

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mAlbumsDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        getdata();

        initView(root);
        //getAllAvailableProduct();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               /*for(int i =0; i < productArrayList.size();i++){

                   if(productArrayList.get(i).getName().contains(query)){
                       productAdapter.filter(query);
                   }else{
                       Toast.makeText(getActivity(), "No Match found",Toast.LENGTH_LONG).show();
                   }
               }*/

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                return false;
            }
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // It's important here
    }


    private void initView(View view){
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        searchView = (SearchView) view.findViewById(R.id.searchView);
    }

    private void getAllAvailableProduct(){
        productArrayList.add(new Product("Notebook","DELL","10th GEN","SN123456","http://via.placeholder.com/300.png","","150.00","cash crusaders","New",""));
        productArrayList.add(new Product("iPhone","Apple","iPhone 8 Plus","SN123456","http://via.placeholder.com/300.png","","230.00","Thamsanqa Shabalala","second hand",""));
        productArrayList.add(new Product("Galaxy A32","Samsung Electronics","A10","SN123456","http://via.placeholder.com/300.png","","1500.00","cash converters","New",""));
        productArrayList.add(new Product("LapTop","DELL","10th GEN","SN123456","http://via.placeholder.com/300.png","","25.000","cash converters","Good condition",""));

        productAdapter = new ProductAdapter(getActivity(),productArrayList);
        recyclerViewProducts.setAdapter(productAdapter);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
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

                    Product  product  = new Product();

                    product.setKey(ds.getKey());
                    product.setPicture(ds.child("picture").getValue().toString());
                    product.setName(ds.child("name").getValue().toString());
                    product.setModel(ds.child("model").getValue().toString());
                    product.setSerialNumber(ds.child("serialNumber").getValue().toString());
                    product.setMaker(ds.child("maker").getValue().toString());
                    product.setSeller(ds.child("seller").getValue().toString());
                    product.setCondition(ds.child("condition").getValue().toString());
                    product.setPrice(ds.child("price").getValue().toString());
                    product.setSellerUserID(ds.child("sellerUserID").getValue().toString());

                    productArrayList.add(product);
                }

                productAdapter = new ProductAdapter(getActivity(),productArrayList);
                recyclerViewProducts.setAdapter(productAdapter);
                recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}