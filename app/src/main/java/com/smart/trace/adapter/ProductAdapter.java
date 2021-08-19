package com.smart.trace.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smart.trace.ProductDetailsActivity2;
import com.smart.trace.R;
import com.smart.trace.model.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    public ArrayList<Product> products;
    private List<Product> searchQueries = null;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.products = products;
        this.searchQueries = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product product = products.get(position);
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductMaker.setText(product.getMaker());
        holder.textViewProductModel.setText(product.getModel());
        holder.textViewProductPrice.setText("R " + product.getPrice());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.dell)
                .error(R.drawable.dell);
        Glide.with(context).load(product.getPicture()).apply(options).into(holder.imageViewProductImage);


        holder.imageViewProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity2.class);
                intent.putExtra("pName", product.getName());
                intent.putExtra("pPrice", product.getPrice());
                intent.putExtra("pPicture", product.getPicture());
                intent.putExtra("pMaker", product.getMaker());
                intent.putExtra("pModel", product.getModel());
                intent.putExtra("pSeller", product.getSeller());
                intent.putExtra("pBuyerUserID", "");
                intent.putExtra("pSerialNumber", product.getSerialNumber());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewProductImage;
        public TextView textViewProductName;
        public TextView textViewProductMaker;
        public TextView textViewProductModel;
        public TextView textViewProductPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductMaker = itemView.findViewById(R.id.textViewProductMaker);
            textViewProductModel = itemView.findViewById(R.id.textViewProductModel);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            imageViewProductImage = itemView.findViewById(R.id.imageViewProduct);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchQueries.clear();
        if (charText.length() == 0) {
            searchQueries.addAll(products);
        } else {

            for (int i = 0; i < products.size(); i++) {

                if (products.get(i).getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchQueries.add(products.get(i));
                    notifyDataSetChanged();
                }
            }
        }
    }
}
