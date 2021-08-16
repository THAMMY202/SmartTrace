package com.smart.trace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class ProductDetailsActivity2 extends AppCompatActivity {

    private ImageView imageViewProductImage;
    private TextView textViewProductName;
    private TextView textViewProductMaker;
    private TextView textViewProductModel;
    private TextView textViewProductPrice;
    private TextView textViewProductSeller;
    private Button buttonBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details2);
        findViewById();
    }

    private void findViewById(){

        textViewProductName = findViewById(R.id.textViewProductName);
        textViewProductMaker = findViewById(R.id.textViewProductMaker);
        textViewProductModel =  findViewById(R.id.textViewProductModel);
        textViewProductPrice =  findViewById(R.id.textViewProductPrice);
        imageViewProductImage = findViewById(R.id.imageViewProduct);
        textViewProductSeller = findViewById(R.id.textViewProductSeller);
        buttonBuy = findViewById(R.id.buttonBuyProduct);


        textViewProductName.setText(getIntent().getStringExtra("pName"));
        textViewProductMaker.setText(getIntent().getStringExtra("pMaker"));
        textViewProductModel.setText(getIntent().getStringExtra("pModel"));
        textViewProductPrice.setText("R " + getIntent().getStringExtra("pPrice"));
        textViewProductSeller.setText("By: " + getIntent().getStringExtra("pSeller"));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.dell)
                .error(R.drawable.dell);
        Glide.with(this).load(getIntent().getStringExtra("pPicture")).apply(options).into(imageViewProductImage );


        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                startFlutterwave();
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }*/

    }
}