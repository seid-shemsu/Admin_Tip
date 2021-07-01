package com.izhar.admintip;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.admintip.objects.Game;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.Holder> {
    Context context;
    List<Game> games;
    String mode;
    public GameAdapter(Context context, List<Game> games, String mode){
        this.context = context;
        this.games = games;
        this.mode = mode;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_tip, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Game game = games.get(position);
        holder.home.setText(game.getHome());
        holder.away.setText(game.getAway());
        holder.tip.setText(game.getTip_type() + " : " + game.getTip());
        holder.date.setText(game.getDate());
        holder.league.setText(game.getLeague());
        holder.odd.setText("odd : " + game.getOdd());
        holder.time.setText(game.getTime());
        if (game.getStatus().equalsIgnoreCase("won")){
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.won));
        }
        else if (game.getStatus().equalsIgnoreCase("lost")){
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setRotation(45.0f);
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.lost));
        }

    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView home, away, tip, date, league, odd, time;
        ImageView status;
        public Holder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            home = itemView.findViewById(R.id.home);
            away = itemView.findViewById(R.id.away);
            tip = itemView.findViewById(R.id.tip);
            date = itemView.findViewById(R.id.date);
            league = itemView.findViewById(R.id.league);
            odd = itemView.findViewById(R.id.odd);
            time = itemView.findViewById(R.id.time);

            home.setOnClickListener(this);
            away.setOnClickListener(this);
            time.setOnClickListener(this);
            tip.setOnClickListener(this);
            date.setOnClickListener(this);
            league.setOnClickListener(this);
            odd.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            String id = games.get(getAdapterPosition()).getId();
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.status);
            if (games.get(getAdapterPosition()).getStatus().equalsIgnoreCase("pending"))
                dialog.show();
            Button won = dialog.findViewById(R.id.won);
            DatabaseReference data;
            if (!mode.equalsIgnoreCase("vip"))
                 data = FirebaseDatabase.getInstance().getReference("tips").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            else
                data = FirebaseDatabase.getInstance().getReference("vip").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

            won.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.child(id).child("status").setValue("won");
                    data.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Game game = snapshot.getValue(Game.class);
                            DatabaseReference history = FirebaseDatabase.getInstance().getReference("history").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                            history.child(id).setValue(game);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
                }
            });
            Button lost = dialog.findViewById(R.id.lost);
            lost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DatabaseReference data = FirebaseDatabase.getInstance().getReference(mode).child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    data.child(id).child("status").setValue("lost");
                    data.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Game game = snapshot.getValue(Game.class);
                            DatabaseReference history = FirebaseDatabase.getInstance().getReference("history").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                            history.child(id).setValue(game);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
                }
            });
        }
    }
}
