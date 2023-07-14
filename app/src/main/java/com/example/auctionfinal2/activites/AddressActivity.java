package com.example.auctionfinal2.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.auctionfinal2.R;
import com.example.auctionfinal2.adapters.AddressAdapter;
import com.example.auctionfinal2.models.AddressModel;
import com.example.auctionfinal2.models.NewProductsModel;
import com.example.auctionfinal2.models.PopularProductsModel;
import com.example.auctionfinal2.models.ShowAllModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress{

    Button addAddress;
    RecyclerView recyclerView;
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button paymentBtn;
    Toolbar toolbar;
    String mAddress = "";
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Object obj = getIntent().getSerializableExtra("item");

        //prinimaem toalbill
        Object totalBill = getIntent().getSerializableExtra("totalBill");
        Double totalBill2 = (Double) totalBill;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        addAddress = findViewById(R.id.add_address_btn);
        radioButton = findViewById(R.id.select_address);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(getApplicationContext(), addressModelList, this);
        recyclerView.setAdapter(addressAdapter);



        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                addressModelList.clear();
                if (value != null) {
                    for (DocumentSnapshot doc: value) {
                        AddressModel addressModel = doc.toObject(AddressModel.class);
                        addressModelList.add(addressModel);
                        addressAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        /*firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (DocumentSnapshot doc: task.getResult().getDocuments()) {

                        AddressModel addressModel = doc.toObject(AddressModel.class);
                        addressModelList.add(addressModel);
                        addressAdapter.notifyDataSetChanged();
                    }
                }

            }
        });*/

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, PaymentPageActivity.class);
                double totalPrice = 0.0;
                if(obj instanceof PopularProductsModel || obj instanceof ShowAllModel){
                    totalPrice = getIntent().getDoubleExtra("totalPrice",0.0);
                    ShowAllModel showAllModel = (ShowAllModel) obj;
                    intent.putExtra("object", showAllModel);
                } else {
                    if (obj instanceof NewProductsModel) {
                        NewProductsModel newProductsModel = (NewProductsModel) obj;
                        totalPrice = Double.parseDouble(newProductsModel.getPrice());
                        intent.putExtra("object", newProductsModel);
                    }
                }

                intent.putExtra("amount", totalPrice);
                startActivity(intent);

                System.out.println("TotalBIll2 = " + totalBill2);
                System.out.println("TotalBIll1 = " + totalBill);

            }
        });

        addAddress = findViewById(R.id.add_address_btn);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, AddAddressActivity.class));
            }
        });

        checkButtonClicked();
    }

    public void checkButtonClicked(){
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);
                paymentBtn.setClickable(addressAdapter.isButtonChecked());

            }
        };

        handler.postDelayed(runnable, 100);
    }

    @Override
    public void setAddress(String address) {

        mAddress = address;

    }
}