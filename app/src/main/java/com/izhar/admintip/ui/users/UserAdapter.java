package com.izhar.admintip.ui.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.admintip.R;
import com.izhar.admintip.objects.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {

    Context context;
    List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        User user = users.get(position);
        if (!user.getAccount_status().equalsIgnoreCase("Verified"))
            holder.deactivate.setVisibility(View.GONE);
        holder.name.setText(user.getName());
        holder.phone.setText(user.getPhone());
        holder.status.setText(user.getAccount_status());
        holder.expire.setText(user.getExpire_date());
        Picasso.with(context).load(user.getImage()).into(holder.image);
        holder.deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference data = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                Toast.makeText(context, user.getUid(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, phone, status, expire;
        ImageView image;
        Button deactivate;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            status = itemView.findViewById(R.id.status);
            expire = itemView.findViewById(R.id.expire);
            image = itemView.findViewById(R.id.image);
            deactivate = itemView.findViewById(R.id.deactivate);

        }
    }
}
