package com.smart.trace;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProductDetailsActivity2 extends AppCompatActivity {

    private ImageView imageViewProductImage;
    private TextView textViewProductName;
    private TextView textViewProductMaker;
    private TextView textViewProductModel;
    private TextView textViewProductPrice;
    private TextView textViewProductSeller;
    private Button buttonBuy,buttonTransfer;
    private String buyer,buyerFullname;
    private boolean showBuyerInfo;

    private EditText editTextName,editTextSurname,editTextEmail,editTextPhone,editTextOrgName,editTextOrgPhone,editTextOrgEmail;
    private CircleImageView mPicture;
    private CardView cardViewStoreInfo;

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
        buttonTransfer = findViewById(R.id.buttonTransferProduct);

        buyer = getIntent().getStringExtra("pBuyerUserID");

        textViewProductName.setText(getIntent().getStringExtra("pName"));
        textViewProductMaker.setText(getIntent().getStringExtra("pMaker"));
        textViewProductModel.setText(getIntent().getStringExtra("pModel"));
        textViewProductPrice.setText("R " + getIntent().getStringExtra("pPrice"));


        if(buyer.isEmpty()){
            textViewProductSeller.setText("By: " + getIntent().getStringExtra("pSeller"));
            buttonTransfer.setVisibility(View.GONE);
        }else {
            buttonBuy.setVisibility(View.GONE);
            buttonTransfer.setVisibility(View.VISIBLE);
            getBuyerUserInfo(buyer);
        }

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

        buttonTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ProductDetailsActivity2.this)
                        .setTitle("Transfer OF Ownership")
                        .setMessage("Please note that " + buyerFullname +" will become the new legal owner of this item. press Continue to confirm")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(ProductDetailsActivity2.this, "Yaay", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        textViewProductSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buyer.isEmpty()){

                    showBuyerInfo = true;
                    getBuyerUserInfo(buyer);

                    LayoutInflater li = LayoutInflater.from(ProductDetailsActivity2.this);
                    View view1 = li.inflate(R.layout.pop_up_user_profile, null);

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailsActivity2.this);
                    alertDialogBuilder.setView(view1);

                    editTextName = view1.findViewById(R.id.name);
                    editTextSurname = view1.findViewById(R.id.etLastname);
                    editTextEmail = view1.findViewById(R.id.etEmail);
                    editTextPhone = view1.findViewById(R.id.etPhone);
                    editTextOrgName = view1.findViewById(R.id.etOName);
                    editTextOrgPhone = view1.findViewById(R.id.etPhone);
                    editTextOrgEmail = view1.findViewById(R.id.etEmail);
                    mPicture = view1.findViewById(R.id.picture);
                    cardViewStoreInfo = view1.findViewById(R.id.cardViewStoreInfo);


                    AlertDialog alertDialogCongratulations = alertDialogBuilder.create();
                    alertDialogCongratulations.show();
                    alertDialogCongratulations.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                }
            }
        });

    }

    private void getBuyerUserInfo(String buyerUserID) {
        DatabaseReference mBuyerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(buyerUserID);
        mBuyerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("firstName") != null &&  map.get("lastName") != null) {
                        buyerFullname = map.get("firstName").toString().trim() + " " + map.get("lastName");
                        String mystring = new String("Bought by : " + map.get("firstName").toString().trim() + " " + map.get("lastName"));
                        SpannableString content = new SpannableString(mystring);
                        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                        textViewProductSeller.setText(content);
                        textViewProductSeller.setTextColor(getResources().getColor(R.color.linkColor));
                    }

                    if(showBuyerInfo){
                        if(map.get("firstName")!=null){
                            editTextName.setText(map.get("firstName").toString());
                        }

                        if(map.get("lastName")!=null){
                            editTextSurname.setText(map.get("lastName").toString());
                        }

                        if(map.get("email")!=null){
                            editTextEmail.setText(map.get("email").toString());
                        }

                        if(map.get("email")!=null){
                            editTextEmail.setText(map.get("email").toString());
                        }
                        if(map.get("phone")!=null){
                            editTextPhone.setText(map.get("phone").toString());
                        }

                        if(map.get("type")!=null){

                            if(map.get("type").toString().trim() == "Organisation"){

                                if(map.get("organisationName")!=null){
                                    editTextOrgName.setText(map.get("organisationName").toString());
                                }

                                else if(map.get("organisationPhone")!=null){
                                    editTextOrgPhone.setText(map.get("organisationPhone").toString());
                                }

                                else  if(map.get("organisationEmail")!=null){
                                    editTextOrgEmail.setText(map.get("organisationEmail").toString());
                                }

                                cardViewStoreInfo.setVisibility(View.VISIBLE);
                            }
                        }

                        if(map.get("picture")!=null && !map.get("picture").toString().trim().equals("0")){
                            Glide.with(ProductDetailsActivity2.this).load(map.get("picture").toString()).into(mPicture);
                        }
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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