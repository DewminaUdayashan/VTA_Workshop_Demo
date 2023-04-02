package com.example.vtademo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vtademo.helpers.DialogHelper;
import com.example.vtademo.models.User;
import com.example.vtademo.services.FirestoreServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DialogHelper dialogHelper;
    public static FirestoreServices firestoreServices;

    RecyclerView recyclerView;

    ArrayList<User> users;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dialogHelper = new DialogHelper(this);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> dialogHelper.showUserAddingDialog());

        users = new ArrayList<>();

        /// valid for first steps
//        itemAdapter = new ItemAdapter(this, users);

        //Not valid for first steps
        itemAdapter = new ItemAdapter(this, users, clickedUser -> {
            dialogHelper.showUserUpdatingDialog(clickedUser);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        firestoreServices = new FirestoreServices(this, users, itemAdapter);

        firestoreServices.listenForUserDocChanges();

        final EditText searchText = findViewById(R.id.searchText);
        searchText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        //--
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        firestoreServices.searchUser2(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //-
                    }
                }
        );
    }
}