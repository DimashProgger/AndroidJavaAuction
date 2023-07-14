package com.example.auctionfinal2.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auctionfinal2.Config.Config;
import com.example.auctionfinal2.R;

import com.example.auctionfinal2.auxClasses.JavaMailAPI;
import com.example.auctionfinal2.models.NewProductsModel;
import com.example.auctionfinal2.models.ShowAllModel;
import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PayPalPayment;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentPageActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView subTotal, discount, shipping, total;
    Button paymentBtn, paypalButton;
    NewProductsModel newProductsModel = null;
    ShowAllModel showAllModel = null;

    String PayPalAmount = "";

    FirebaseAuth auth;

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // use bcs of test
            .clientId(Config.PAYPAL_CLIENT_ID);


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        double amount = 0.0;
        double totalTotal = 0.0;


        Object obj = getIntent().getSerializableExtra("object");

        amount = getIntent().getDoubleExtra("amount",0.0);


        //Start PayPal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        auth = FirebaseAuth.getInstance();
        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        paymentBtn = findViewById(R.id.pay_btn);
        paypalButton = findViewById(R.id.paypal_btn);

        subTotal.setText(amount + "₸");
        total.setText(String.valueOf(amount));

        System.out.println("ETO TOTAL: " + subTotal);

        double finalAmount = amount;
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //processPayment();

                if (obj instanceof NewProductsModel) {
                    newProductsModel = (NewProductsModel) obj;

                    String aucWinnerEmail = newProductsModel.getUserId();
                    String aucWinnerSubj = "Auction notification: " + newProductsModel.getName();
                    String aucWinnerMessageText = "You won the auction and paid for the item " + newProductsModel.getName() +
                            ". Its price is equal to " + finalAmount + "₸. Expect a response from the seller. Regards, D.E.Treasure Administration";

                    sendEmail(aucWinnerEmail, aucWinnerSubj, aucWinnerMessageText);

                    String aucOwnerEmail = newProductsModel.getOwnerId();
                    String aucOwnerSubj = "Auction notification: " + newProductsModel.getName();
                    String aucOwnerMessageText = "Your " + newProductsModel.getName() + " lot is currently being paid for" +
                            ". The winning bid was " + finalAmount + "₸. Now you can send your order to the buyer " + newProductsModel.getUser()
                            + ". If you do not do it within 7 days, the order will be cancelled. Regards, D.E.Treasure Administration";

                    sendEmail(aucOwnerEmail, aucOwnerSubj, aucOwnerMessageText);

                } else if (obj instanceof ShowAllModel) {
                    showAllModel = (ShowAllModel) obj;

                    int quantity = (int) (finalAmount /Integer.parseInt(showAllModel.getPrice()));

                    String buyerEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
                    String buyerSubj = "Auction notification: " + showAllModel.getName();
                    String buyerMessageText = "You paid for an order consisting of " + showAllModel.getName() +
                            ". The number of items purchased is equal to " + quantity + ". The total price is "
                            + finalAmount + "₸. Expect a response from the seller. Regards, D.E.Treasure Administration";

                    sendEmail(buyerEmail, buyerSubj, buyerMessageText);

                    String ownerEmail = showAllModel.getOwnerId();
                    String ownerSubj = "Auction notification: " + showAllModel.getName();
                    String ownerMessageText = "Your " + showAllModel.getName() + " item is currently being paid for. " +
                            "The amount of purchased goods is equal to " + quantity +
                            ". The total price is " + finalAmount + "₸. Now you can send your order to the buyer"
                            + ". If you do not do it within 7 days, the order will be cancelled. Regards, D.E.Treasure Administration";

                    sendEmail(ownerEmail, ownerSubj, ownerMessageText);
                }

                Toast.makeText(PaymentPageActivity.this, "Successfully paid", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(PaymentPageActivity.this, MainActivity.class));
            }
        });

        paypalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });

    }

    private void sendEmail(String prevEmail, String subj, String prevMessageText){
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, prevEmail, subj, prevMessageText);

        javaMailAPI.execute();

    }

    private void processPayment() {
        System.out.println("gettext: " + total.getText());
        String tempTotal = String.valueOf(total.getText());
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(tempTotal), "USD",
                "Payment Transaction D.E.Treasure", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // additional by hints
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, PayPalPaymentDetails.class)
                                         .putExtra("PaymentDetails", paymentDetails)
                                         .putExtra("PaymentAmount", (String.valueOf(total)))
                        );



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(requestCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
}

