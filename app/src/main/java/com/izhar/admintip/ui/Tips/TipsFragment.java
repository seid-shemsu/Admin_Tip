package com.izhar.admintip.ui.Tips;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.admintip.GameAdapter;
import com.izhar.admintip.MainActivity;
import com.izhar.admintip.R;
import com.izhar.admintip.objects.Game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TipsFragment extends Fragment {
    View root;
    private String today = "";
    private List<Game> games;
    private GameAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference data;
    private ProgressBar progress;
    private TextView not_posted;
    private CheckBox checkBox;
    private String type = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tips, container, false);
        setHasOptionsMenu(true);
        init();
        today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        data = FirebaseDatabase.getInstance().getReference("vip").child(today).child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        load("vip");
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    type = "";
                }
                else {
                    type = "vip";
                }
                load(today);
            }
        });
        return root;
    }

    private void init() {
        recyclerView = root.findViewById(R.id.recycler);
        progress = root.findViewById(R.id.progress);
        not_posted = root.findViewById(R.id.not_posted);
        checkBox = root.findViewById(R.id.checkbox);
    }

    private void load(String mode){
        games = new ArrayList<>();
        if (checkBox.isChecked())
            data = FirebaseDatabase.getInstance().getReference("tips").child(today);
        else
            data = FirebaseDatabase.getInstance().getReference("vip").child(today);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(null);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    not_posted.setVisibility(View.GONE);
                    games.clear();
                    recyclerView.removeAllViews();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        games.add(dataSnapshot.getValue(Game.class));
                    }
                    adapter = new GameAdapter(getContext(), games, mode);
                    recyclerView.setAdapter(adapter);
                    checkBox.setVisibility(View.VISIBLE);
                } else {
                    not_posted.setVisibility(View.VISIBLE);
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.date, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.date){
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.date_picker);
            final DatePicker datePicker = dialog.findViewById(R.id.date_picker);
            Button ok = dialog.findViewById(R.id.ok);
            ok.setOnClickListener(v -> {
                String day = datePicker.getDayOfMonth() + "";
                String month = datePicker.getMonth()+1 + "";
                if (day.length() == 1 )
                    day = "0" + day;
                if (month.length() == 1 )
                    month = "0" + month;
                today = day + "-" + month + "-" + datePicker.getYear();
                load(type);
                dialog.dismiss();
            });
            dialog.show();
        }
        return true;
    }

}