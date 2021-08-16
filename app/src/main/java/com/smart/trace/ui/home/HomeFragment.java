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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        initView(root);
        getAllAvailableProduct();


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
        //Product(String name, String maker, String model, String serialNumber, String picture, String key)
        productArrayList.add(new Product("Notebook","DELL","10th GEN","SN123456","http://via.placeholder.com/300.png","","150.00","cash crusaders"));
        productArrayList.add(new Product("iPhone","Apple","iPhone 8 Plus","SN123456","http://via.placeholder.com/300.png","","230.00","Thamsanqa Shabalala"));
        productArrayList.add(new Product("Galaxy A32","Samsung Electronics","A10","SN123456","http://via.placeholder.com/300.png","","1500.00","cash converters"));
        productArrayList.add(new Product("LapTop","DELL","10th GEN","SN123456","http://via.placeholder.com/300.png","","25.000","cash converters"));

        productAdapter = new ProductAdapter(getActivity(),productArrayList);
        recyclerViewProducts.setAdapter(productAdapter);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}