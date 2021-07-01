package com.izhar.admintip.ui.requests;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.admintip.R;
import com.izhar.admintip.objects.Request;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Holder> {
    Context context;
    List<Request> requests;

    public RequestAdapter(Context context, List<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.single_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Request request = requests.get(position);
        holder.name.setText(request.getPhone());
        holder.time.setText(request.getTime());
        Picasso.with(context).load(request.getPhoto()).into(holder.image);
        holder.activate.setOnClickListener(v -> {
            String uid = request.getId().substring(request.getId().lastIndexOf('_') + 1, request.getId().length());
            DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(uid);
            Calendar current = Calendar.getInstance();
            current.add(Calendar.DATE, 15);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date resultdate = new Date(current.getTimeInMillis());
            String dueudate = df.format(resultdate);
            user.child("expire_date").setValue(dueudate);
            user.child("account_status").setValue("Verified");
            DatabaseReference req = FirebaseDatabase.getInstance().getReference("payment_requests").child(request.getId());
            req.removeValue();
            deleteItem(position);
            
        });
        holder.decline.setOnClickListener(v -> {
            DatabaseReference req = FirebaseDatabase.getInstance().getReference("payment_requests").child(request.getId());
            req.removeValue();
            deleteItem(position);
        });
        holder.image.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.image_zoom);
            ImageView imageView = dialog.findViewById(R.id.image);
            Picasso.with(context).load(request.getPhoto()).into(imageView);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void deleteItem(int adapterPosition) {
        requests.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(0, requests.size());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name, time;
        ImageView image;
        Button activate, decline;
        public Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            activate = itemView.findViewById(R.id.activate);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            decline = itemView.findViewById(R.id.decline);
        }
    }
}
