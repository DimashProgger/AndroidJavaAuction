package com.example.auctionfinal2.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.auctionfinal2.R;
import com.example.auctionfinal2.models.NewProductsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class CreateAuctionFragment extends Fragment {

    EditText name, img_url, price, rateMove, date;
    TextInputEditText description;
    Button createBtn;
    String eName, eImg, ePrice, eRateMove, eDate, eDesc;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;


    public CreateAuctionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_auction, container, false);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        name = root.findViewById(R.id.create_aname);
        img_url = root.findViewById(R.id.create_aimage);
        price = root.findViewById(R.id.create_aprice);
        rateMove = root.findViewById(R.id.create_bid_move);
        date = root.findViewById(R.id.create_date);
        description = root.findViewById(R.id.create_adescription);
        createBtn = root.findViewById(R.id.create_aitem_btn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eName = name.getText().toString();
                eImg = img_url.getText().toString();
                ePrice = price.getText().toString();
                eRateMove = rateMove.getText().toString();
                eDate = date.getText().toString();
                eDesc = Objects.requireNonNull(description.getText()).toString();

                if (TextUtils.isEmpty(eName)) {
                    name.setError("Please enter Item Name");
                } else if (TextUtils.isEmpty(eDesc)) {
                    description.setError("Please enter Item Description");
                } else if (TextUtils.isEmpty(eImg)) {
                    img_url.setError("Please enter Item Image URL");
                } if (TextUtils.isEmpty(ePrice)) {
                    price.setError("Please enter Item Price");
                } if (TextUtils.isEmpty(eDate)) {
                    date.setError("Please enter Auction End Date");
                } if (TextUtils.isEmpty(eRateMove)) {
                    rateMove.setError("Please enter Item Bid Move");
                } else {
                    addDataToFirestore(eName, eImg, ePrice, eRateMove, eDate, eDesc);
                }

            }
        });

        return root;
    }

    private void addDataToFirestore(String eName, String eImg, String ePrice, String eRateMove, String eDate, String eDesc) {

        CollectionReference dbProducts = firestore.collection("NewProducts");

        NewProductsModel product = new NewProductsModel(eDesc, eName, eImg, ePrice, "auction", eDate, eRateMove, "", "", Objects.requireNonNull(auth.getCurrentUser()).getEmail());

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