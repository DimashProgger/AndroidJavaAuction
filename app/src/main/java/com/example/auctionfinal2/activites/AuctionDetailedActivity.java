package com.example.auctionfinal2.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auctionfinal2.R;
import com.example.auctionfinal2.auxClasses.JavaMailAPI;
import com.example.auctionfinal2.models.NewProductsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AuctionDetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView name, description, price, rateMove, user, txtHour, txtMinute, txtSecond, eventEnd;
    Button raceTheBid, auctionGetItem, auctionItemReceived;

    Toolbar toolbar;

    private Handler handler;

    NewProductsModel newProductsModel = null;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_detailed);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.auction_detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        detailedImg = findViewById(R.id.auction_detailed_img);
        name = findViewById(R.id.auction_detailed_name);
        description = findViewById(R.id.auction_detailed_desc);
        price = findViewById(R.id.auction_detailed_price);
        rateMove = findViewById(R.id.auction_rate_move);
        raceTheBid = findViewById(R.id.auction_race_bid);
        user = findViewById(R.id.auction_user);
        txtHour = findViewById(R.id.txtHour);
        txtMinute = findViewById(R.id.txtMinute);
        txtSecond = findViewById(R.id.txtSecond);
        eventEnd = findViewById(R.id.eventEnd);
        auctionGetItem = findViewById(R.id.auction_get_item);
        auctionItemReceived = findViewById(R.id.auction_confirmation);

        final Object obj = getIntent().getSerializableExtra("auction");

        newProductsModel = (NewProductsModel) obj;

        Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
        name.setText(newProductsModel.getName());
        description.setText(newProductsModel.getDescription());
        price.setText(newProductsModel.getPrice());
        rateMove.setText(newProductsModel.getRateMove());
        if(newProductsModel.getUser().equals("")){
            user.setText("There were no bids");
        } else {
            if(newProductsModel.getUser().equals(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName())){
                user.setText("You");
            } else
                user.setText(newProductsModel.getUser());
        }

        if(newProductsModel.getOwnerId().equals(auth.getCurrentUser().getEmail())){
            raceTheBid.setVisibility(View.GONE);
        }

        auctionGetItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                winnerItemAdd();
            }
        });

        raceTheBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int beforeBid = Integer.parseInt(newProductsModel.getPrice());
                beforeBid += Integer.parseInt(newProductsModel.getRateMove());

                String prevEmail = newProductsModel.getUserId();
                String subj = "Auction notification: " + newProductsModel.getName();
                String prevMessageText = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName() + " raised the bid on your desired lot. Lot name is "
                        + newProductsModel.getName() + ". Lot current price is " + beforeBid + "₸. Don't miss your chance to get your hands on this item! Regards, D.E.Treasure Administration";

                if(!newProductsModel.getUserId().equals(auth.getCurrentUser().getEmail())){
                    sendEmail(prevEmail, subj, prevMessageText);
                }

                newProductsModel.setUser(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName());
                newProductsModel.setUserId(auth.getCurrentUser().getEmail());
                newProductsModel.setPrice(String.valueOf(beforeBid));
                updatePrice(newProductsModel);

            }
        });

        auctionItemReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ownerEmail = newProductsModel.getOwnerId();
                String subj = "Auction notification: " + newProductsModel.getName();
                String prevMessageText = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName() + " Confirmed receipt of the order. Payment will come within 24 hours. Regards, D.E.Treasure D.E.Treasure Administration";
                sendEmail(ownerEmail, subj, prevMessageText);
                newProductsModel.setSellType("sold");
                updateStatus(newProductsModel);
                finish();
            }
        });

        countdownStart();
    }

    private void sendEmail(String prevEmail, String subj, String prevMessageText){
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, prevEmail, subj, prevMessageText);

        javaMailAPI.execute();

    }

    private void updatePrice(NewProductsModel auctionModel){

        firestore.collection("NewProducts").document(auctionModel.getId()).set(auctionModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText( AuctionDetailedActivity.this, "Your bid has been accepted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuctionDetailedActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateStatus(NewProductsModel auctionModel){

        firestore.collection("NewProducts").document(auctionModel.getId()).set(auctionModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    public void countdownStart(){
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    Date futureDate = dateFormat.parse(newProductsModel.getHours());
                    Date currentTimerDate = new Date();
                    if (!currentTimerDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentTimerDate.getTime();
                        //System.out.println(diff);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtHour.setText("" + String.format("%02d", hours));
                        txtMinute.setText(""
                                + String.format("%02d", minutes));
                        txtSecond.setText(""
                                + String.format("%02d", seconds));
                    } else {
                        if (!newProductsModel.getSellType().equals("paid") && !newProductsModel.getSellType().equals("sold") && !newProductsModel.getSellType().equals("win")) {

                            String winnerEmail = newProductsModel.getUserId();
                            String subj = "Auction notification: " + newProductsModel.getName();
                            String MessageText = Objects.requireNonNull("Congratulations, you won the auction! You can pick up your item " +
                                    newProductsModel.getName() + " at the price of " + newProductsModel.getPrice() + "₸. Regards, D.E. Treasure.");

                            sendEmail(winnerEmail, subj, MessageText);

                            newProductsModel.setSellType("win");
                            updateStatus(newProductsModel);
                        }
                        eventEnd.setVisibility(View.VISIBLE);
                        eventEnd.setText("Bets Closed");
                        textViewGone();
                        if(newProductsModel.getSellType().equals("win")) {
                            auctionGetItem.setVisibility(View.VISIBLE);
                        } else if(newProductsModel.getSellType().equals("paid") && auth.getCurrentUser().equals(newProductsModel.getUser())){
                            auctionGetItem.setVisibility(View.GONE);
                            auctionItemReceived.setVisibility(View.VISIBLE);
                        } else if(newProductsModel.getSellType().equals("sold")){
                            auctionItemReceived.setVisibility(View.GONE);
                            auctionGetItem.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        handler.postDelayed(runnable, 1);
    }

    private void winnerItemAdd(){

        if(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName().equals(newProductsModel.getUser())) {

            String ownerEmail = newProductsModel.getOwnerId();
            String subj = "Auction notification" + newProductsModel.getName();
            String MessageText = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName() + " started the process of buying your item "
                    + newProductsModel.getName() + ". Lot current price is " + newProductsModel.getPrice() +
                    "₸. If the user declines the purchase, we will notify you. Regards, D.E.Treasure Administration";

            sendEmail(ownerEmail, subj, MessageText);

            Intent intent = new Intent(AuctionDetailedActivity.this, AddressActivity.class);
            intent.putExtra("item", newProductsModel);
            startActivity(intent);

            newProductsModel.setSellType("paid");
            updateStatus(newProductsModel);
        }
    }

    public void textViewGone() {
        if(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName().equals(newProductsModel.getUser())){
            auctionGetItem.setVisibility(View.VISIBLE);
        }
        txtHour.setVisibility(View.GONE);
        txtMinute.setVisibility(View.GONE);
        txtSecond.setVisibility(View.GONE);
        findViewById(R.id.txt1dots).setVisibility(View.GONE);
        findViewById(R.id.txt2dots).setVisibility(View.GONE);
        raceTheBid.setVisibility(View.GONE);
        findViewById(R.id.time_left).setVisibility(View.GONE);
    }
}