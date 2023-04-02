package com.example.vtademo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vtademo.models.User;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    Context context;
    ArrayList<User> users;

    OnItemClickListener onItemClickListener;


    public ItemAdapter(Context context, ArrayList<User> users,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.users = users;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view,users,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getName());
        holder.age.setText(String.valueOf(user.getAge()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /*    public static class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView name, age;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                /// initializing textViews so we can set text
                name = itemView.findViewById(R.id.nameTV);
                age = itemView.findViewById(R.id.ageTV);
            }
        }*/
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name, age;
        private ArrayList<User> users;
        private CardView cardView;
        private OnItemClickListener onItemClickListener;

        public ItemViewHolder(@NonNull View itemView, ArrayList<User> users, OnItemClickListener onItemClickListener) {
            super(itemView);
            /// initializing textViews so we can set text
            name = itemView.findViewById(R.id.nameTV);
            age = itemView.findViewById(R.id.ageTV);
            cardView = itemView.findViewById(R.id.cardView);

            this.users = users;
            this.onItemClickListener = onItemClickListener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                User clickedUser = users.get(position);
                onItemClickListener.onItemClick(clickedUser);
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(User clickedUser);
    }
}
