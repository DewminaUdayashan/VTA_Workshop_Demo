package com.example.vtademo.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.vtademo.MainActivity;
import com.example.vtademo.R;
import com.example.vtademo.models.User;

public class DialogHelper {
    final Context context;
    final AlertDialog.Builder builder;

    public DialogHelper(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
    }

    public void showUserAddingDialog() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        builder.setTitle("Add New User");

        final EditText nameTxt = dialogView.findViewById(R.id.nameText);
        final EditText ageTxt = dialogView.findViewById(R.id.ageText);

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            final String name = nameTxt.getText().toString();
            final int age = Integer.parseInt(ageTxt.getText().toString());
            MainActivity.firestoreServices.saveUser(new User(name, age));
            dialogInterface.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showUserUpdatingDialog(User clickedUser) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        builder.setTitle("Add New User");

        final EditText nameTxt = dialogView.findViewById(R.id.nameText);
        final EditText ageTxt = dialogView.findViewById(R.id.ageText);
        nameTxt.setText(clickedUser.getName());
        ageTxt.setText(String.valueOf(clickedUser.getAge()));

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        builder.setPositiveButton("Update", (dialogInterface, i) -> {
            final String name = nameTxt.getText().toString();
            final int age = Integer.parseInt(ageTxt.getText().toString());
            clickedUser.setName(name);
            clickedUser.setAge(age);
            MainActivity.firestoreServices.updateUser(clickedUser);
            dialogInterface.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
