package com.smart.trace;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smart.trace.model.BlackListedItem;
import com.smart.trace.model.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadFragment extends Fragment {

    String[] condition = {"Brand new", "Second hand", "Fair", "Good", "Other"};
    private EditText editTextItemName, editTextItemPrice, editTextItemMaker, editTextItemModel, editTextItemSerialNumber;
    private Spinner spinner;
    private String selectedCondition = null;
    private Button buttonUpload;
    private String seller;
    private FloatingActionButton mFabChoosePic;

    private ImageView imageViewItem;
    private Uri itemCoverUri;
    private FirebaseAuth mAuth;
    private String userID;
    protected String latitude, longitude;

    private String blackListerPhone,blackListerEmail,blackListerFullname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_upload, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        //sendSMS("0824382247","Testing sms");

        initView(root);
        getUserInfo();
        clickLisners();
        
        return root;
    }

    private void initView(View view) {

        spinner =  view.findViewById(R.id.spinner);
        imageViewItem =  view.findViewById(R.id.imgItemCover);
        mFabChoosePic = view.findViewById(R.id.fabChooseItemPic);
        editTextItemName =  view.findViewById(R.id.etItemname);
        editTextItemPrice =  view.findViewById(R.id.etItemPrice);
        editTextItemMaker =  view.findViewById(R.id.etMaker);
        editTextItemModel =  view.findViewById(R.id.etModel);
        editTextItemSerialNumber =  view.findViewById(R.id.etSerialNumber);
        buttonUpload =  view.findViewById(R.id.btnUploadItem);

       
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,condition);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

    }

    private void clickLisners(){

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = editTextItemName.getText().toString().trim();
                final String price = editTextItemPrice.getText().toString().trim();
                final String maker = editTextItemMaker.getText().toString().trim();
                final String model = editTextItemModel.getText().toString().trim();
                final String serial_number = editTextItemSerialNumber.getText().toString().trim();

                if(!isItemBlackListed(model,serial_number).isEmpty() && !isItemBlackListed(model,serial_number).equals("")){
                    //send out an email
                    //send out an sms
                    //send out saps notification
                    String msg = "";
                    String no = "";
                    getBlackListerInfo(isItemBlackListed(model,serial_number));

                }


                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Items").child(userID);

                filePath.putFile(itemCoverUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference  mItemDatabase = FirebaseDatabase.getInstance().getReference().child("Items");

                            progressDialog.dismiss();
                            Uri downUri = task.getResult();

                            Product product = new Product();
                            product.setCondition(selectedCondition);
                            product.setSeller(seller);
                            product.setName(name);
                            product.setMaker(maker);
                            product.setPrice(price);
                            product.setModel(model);
                            product.setSerialNumber(serial_number);

                            if(!downUri.toString().isEmpty()){
                                product.setPicture(downUri.toString());
                            }

                            String mGroupId = mItemDatabase.push().getKey();

                            Map newImage = new HashMap();
                            newImage.put("name", product.getName());
                            newImage.put("maker", product.getMaker());
                            newImage.put("model", product.getModel());
                            newImage.put("serialNumber", product.getSerialNumber());
                            newImage.put("picture", product.getPicture());
                            newImage.put("price", product.getPrice());
                            newImage.put("condition", product.getCondition());
                            newImage.put("seller", product.getSeller());
                            newImage.put("key", mGroupId);
                            newImage.put("sellerUserID", userID);
                            newImage.put("latitude", latitude);
                            newImage.put("longitude", longitude);

                            mItemDatabase.child(mGroupId).setValue(newImage);
                            Toast.makeText(getContext(), "Item uploaded",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "upload failed, please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    selectedCondition = item.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCondition = "Unspecified";
            }
        });

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

    }

    private void getUserInfo() {
        
        FirebaseAuth   mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("type") != null ) {

                       if(map.get("type").toString().trim().equals("Organisation")){
                           seller = map.get("organisationName").toString().trim();
                       }else {
                           seller = map.get("firstName").toString().trim() +" "+ map.get("lastName").toString().trim() ;
                       }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    String id = "";
    private String isItemBlackListed(String model,String serial_number){

        FirebaseDatabase.getInstance().getReference().child("BlackListedItems")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                BlackListedItem  blackListedItem  = new BlackListedItem();
                                blackListedItem.setName(snapshot.child("name").getValue().toString());
                                blackListedItem.setModel(snapshot.child("model").getValue().toString());
                                blackListedItem.setSerialNumber(snapshot.child("serialNumber").getValue().toString());
                                blackListedItem.setUserID(snapshot.child("userID").getValue().toString());

                                if(blackListedItem.getModel().equals(model) && blackListedItem.getSerialNumber().equals(serial_number)){
                                    id = blackListedItem.getUserID();
                                }else {
                                    id = "";
                                }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        return id;
    }

    private void getBlackListerInfo(String blackListerUserID) {
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(blackListerUserID);
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("phone") != null ) {
                        blackListerPhone = map.get("phone").toString();
                    }

                    if (map.get("email") != null && !map.get("picture").toString().trim().equals("0")) {
                        blackListerEmail = map.get("email").toString();
                    }

                    if (map.get("firstName") != null &&  map.get("lastName") != null) {
                        blackListerFullname = map.get("firstName").toString().trim() + " " + map.get("lastName");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        sendSMS(blackListerPhone,"Hi "+ blackListerFullname + "your reported stolen item is being soiled by " + seller);

    }


    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getActivity(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getActivity(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select item Image"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            itemCoverUri = imageUri;

            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageViewItem.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}