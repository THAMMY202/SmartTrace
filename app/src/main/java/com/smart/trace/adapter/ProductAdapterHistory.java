package com.smart.trace.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smart.trace.ProductDetailsActivity2;
import com.smart.trace.R;
import com.smart.trace.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapterHistory extends RecyclerView.Adapter<ProductAdapterHistory.ViewHolder> {

    private Context context;
    public ArrayList<Product> products;
    private List<Product> searchQueries = null;

    public ProductAdapterHistory(Context context, ArrayList<Product> products) {
        this.products = products;
        this.searchQueries = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapterHistory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cardview, parent, false);

        ProductAdapterHistory.ViewHolder viewHolder = new ProductAdapterHistory.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductAdapterHistory.ViewHolder holder, final int position) {
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
                intent.putExtra("pBuyerUserID", product.getBuyerUserID());
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
}
