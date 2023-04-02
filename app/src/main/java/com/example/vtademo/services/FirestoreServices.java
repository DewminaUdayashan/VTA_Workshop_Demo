package com.example.vtademo.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vtademo.ItemAdapter;
import com.example.vtademo.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreServices {
    private static final String USERS_COLLECTION = "Users";
    private static final String TAG = "FirestoreServices";
    final private Context context;
    final private FirebaseFirestore firestore;

    final ArrayList<User> users;

    final ItemAdapter itemAdapter;


    public FirestoreServices(Context context, ArrayList<User> users, ItemAdapter itemAdapter) {
        this.context = context;
        this.users = users;
        this.itemAdapter = itemAdapter;
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void saveUser(User user) {
        firestore.collection(USERS_COLLECTION).document().set(user.toMap()).addOnSuccessListener(
                unused -> {
                    Toast.makeText(context, user.getName() + " successfully added!", Toast.LENGTH_SHORT).show();
                }
        ).addOnFailureListener(e -> {
            Log.e(TAG, "saveUser: ", e);
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        });
    }

    public void listenForUserDocChanges() {
        firestore.collection(USERS_COLLECTION).orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "onEvent: ", error);
                        Toast.makeText(context, "Users loading failed!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null) {
                        final List<DocumentChange> docChanges = value.getDocumentChanges();
                        for (DocumentChange docChange : docChanges
                        ) {
                            if (docChange.getType() == DocumentChange.Type.ADDED) {
                                User user = docChange.getDocument().toObject(User.class);
                                user.setId(docChange.getDocument().getId());
                                users.add(user);
                            }
                            if (docChange.getType() == DocumentChange.Type.MODIFIED) {
                                User user = docChange.getDocument().toObject(User.class);
                                user.setId(docChange.getDocument().getId());
                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).getId().equals(user.getId())) {
                                        users.set(i, user);
                                        break;
                                    }
                                }
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void updateUser(User clickedUser) {
        firestore.collection(USERS_COLLECTION).document(clickedUser.getId())
                .update(clickedUser.toMap()).addOnSuccessListener(unused -> {
                    Toast.makeText(context, clickedUser.getName() + " updated!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "updateUser: ", e);
                    Toast.makeText(context, "Updating failed!", Toast.LENGTH_SHORT).show();
                });
    }

    public void searchUser(String searchTerm) {
        firestore.collection(USERS_COLLECTION).whereEqualTo("name", searchTerm).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        users.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()
                        ) {
                            User user = doc.toObject(User.class);
                            user.setId(doc.getId());
                            users.add(user);
                        }
                    }
                    itemAdapter.notifyDataSetChanged();

                }
        );
    }

    public void searchUser2(String searchTerm){
       /* The \uf8ff character is used as a suffix to the search string because it is the last character
        in the Unicode character set. This ensures that the search query returns all documents
         that start with the search string and includes any characters that come after it.

         another way to filter local arrayList inside the app itself
       */
        final CollectionReference userRef = firestore.collection(USERS_COLLECTION);
        userRef.orderBy("name").startAt(searchTerm).endAt(searchTerm+"\uf8ff").limit(5).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            users.clear();
                            for (QueryDocumentSnapshot doc:task.getResult()
                            ) {
                                User user =  doc.toObject(User.class);
                                user.setId(doc.getId());
                                users.add(user);
                            }
                        }
                        itemAdapter.notifyDataSetChanged();
                    }
                }
        );
    }
}
