package com.example.auctionfinal2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.auctionfinal2.R;
import com.example.auctionfinal2.models.NewProductsModel;
import com.example.auctionfinal2.models.PopularProductsModel;
import com.example.auctionfinal2.models.ShowAllModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CreateFragment extends Fragment {

    EditText name, img_url, price, type;
    TextInputEditText description;
    Button createBtn;
    String eName, eImg, ePrice, eDesc, eType;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create, container, false);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        name = root.findViewById(R.id.create_name);
        img_url = root.findViewById(R.id.create_image);
        price = root.findViewById(R.id.create_price);
        description = root.findViewById(R.id.create_description);
        type = root.findViewById(R.id.create_type);
        createBtn = root.findViewById(R.id.create_item_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eName = name.getText().toString();
                eImg = img_url.getText().toString();
                ePrice = price.getText().toString();
                eDesc = Objects.requireNonNull(description.getText()).toString();
                eType = type.getText().toString();

                if (TextUtils.isEmpty(eName)) {
                    name.setError("Please enter Item Name");
                } else if (TextUtils.isEmpty(eDesc)) {
                    description.setError("Please enter Item Description");
                } else if (TextUtils.isEmpty(eImg)) {
                    img_url.setError("Please enter Item Image URL");
                } if (TextUtils.isEmpty(ePrice)) {
                    price.setError("Please enter Item Price");
                } if (TextUtils.isEmpty(eType)) {
                    type.setError("Please enter Item Type");
                } else {
                    addDataToFirestore(eName, eImg, ePrice, eDesc, eType);
                }

            }
        });

        return root;
    }

    private void addDataToFirestore(String eName, String eImg, String ePrice, String eDesc, String eType) {

        CollectionReference dbProducts = firestore.collection("ShowAll");

        ShowAllModel product = new ShowAllModel(eDesc, eName, "0", eImg, ePrice, eType, Objects.requireNonNull(auth.getCurrentUser()).getEmail());

        dbProducts.add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Your Item has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Fail to add Item \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

}