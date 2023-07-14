package com.example.auctionfinal2.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.auctionfinal2.R;
import com.example.auctionfinal2.adapters.ShowAllAdapter;
import com.example.auctionfinal2.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;

    Toolbar toolbar;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        String type = getIntent().getStringExtra("type");



        firestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.show_all_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);



        if(type == null || type.isEmpty()){
            firestore.collection("ShowAll").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("men")){
            firestore.collection("ShowAll").whereEqualTo("type", "men").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("rarity")){
            firestore.collection("ShowAll").whereEqualTo("type", "rarity").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("tv")){
            firestore.collection("ShowAll").whereEqualTo("type", "tv").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("appliances")){
            firestore.collection("ShowAll").whereEqualTo("type", "appliances").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("smartphones")){
            firestore.collection("ShowAll").whereEqualTo("type", "smartphones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("gaming consoles")){
            firestore.collection("ShowAll").whereEqualTo("type", "gaming consoles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("art")){
            firestore.collection("ShowAll").whereEqualTo("type", "art").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("camera")){
            firestore.collection("ShowAll").whereEqualTo("type", "camera").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("ghoul")){
            firestore.collection("ShowAll").whereEqualTo("type", "ghoul").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        } else if(type != null && type.equalsIgnoreCase("icecream")){
            firestore.collection("ShowAll").whereEqualTo("type", "icecream").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc: task.getResult().getDocuments()){
                            ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        }
    }
}