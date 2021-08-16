package com.smart.trace;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    private EditText editTextName,editTextSurname,editTextEmail,editTextPhone,editTextOrgName,editTextOrgPhone,editTextOrgEmail;
    private CircleImageView mPicture;
    private CardView cardViewStoreInfo;
    private FloatingActionButton mFabChoosePic;
    private Button btnSave;

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String userID;

    private String mProfileImageUrl;
    private Uri resultUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        initView(root);
        getUserInfo();

        return root;
    }

    private void getUserInfo(){
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("firstName")!=null){
                        editTextName.setText(map.get("firstName").toString());
                    }
                    if(map.get("lastName")!=null){
                        editTextSurname.setText(map.get("lastName").toString());
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
                        mProfileImageUrl = map.get("picture").toString();
                        Glide.with(getActivity()).load(mProfileImageUrl).into(mPicture);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initView(View view){

        editTextName = view.findViewById(R.id.name);
        editTextSurname = view.findViewById(R.id.etLastname);
        editTextEmail = view.findViewById(R.id.etEmail);
        editTextPhone = view.findViewById(R.id.etPhone);
        editTextOrgName = view.findViewById(R.id.etOrgName);
        editTextOrgPhone = view.findViewById(R.id.etOrgPhone);
        editTextOrgEmail = view.findViewById(R.id.etOrgEmail);
        cardViewStoreInfo = view.findViewById(R.id.cardViewStoreInfo);
        mFabChoosePic = view.findViewById(R.id.fabChoosePic);
        mPicture = view.findViewById(R.id.picture);
        btnSave = view.findViewById(R.id.btn_save);

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveUserInformation();
            }
        });
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;

            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                mPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}