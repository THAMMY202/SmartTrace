package com.smart.trace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class BlackListItemFragment extends Fragment {

    private EditText editTextMessage,editTextName,editTextSerialNumber,editTextModel;
    private Button buttonReport;

    private FirebaseAuth mAuth;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_black_list_item, container, false);
        intiView(root);
        return root;
    }

    private void intiView(View view){

        editTextMessage = view.findViewById(R.id.etBlack_list_message);
        editTextName = view.findViewById(R.id.etBlack_list_name);
        editTextSerialNumber = view.findViewById(R.id.etBlack_list_serial_number);
        editTextModel = view.findViewById(R.id.etBlack_list_model);
        buttonReport = view.findViewById(R.id.btnUploadReport);


        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postReport();
            }
        });

    }

    private void postReport(){

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        DatabaseReference mItemDatabase = FirebaseDatabase.getInstance().getReference().child("BlackListedItems");

        Map newImage = new HashMap();
        newImage.put("name", editTextName.getText().toString());
        newImage.put("model", editTextModel.getText().toString());
        newImage.put("serialNumber", editTextSerialNumber.getText().toString());
        newImage.put("message", editTextMessage.getText().toString());
        newImage.put("userID", userID);

        mItemDatabase.push().setValue(newImage);
        Toast.makeText(getContext(), "report recorded",Toast.LENGTH_SHORT).show();
    }
}