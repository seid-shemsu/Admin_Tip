package com.izhar.admintip.ui.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.admintip.GameAdapter;
import com.izhar.admintip.R;
import com.izhar.admintip.objects.Game;
import com.izhar.admintip.objects.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsersFragment extends Fragment {
    private View root;
    private List<User> users;
    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference data;
    private ProgressBar progress;
    private TextView not_found;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_users, container, false);
        init();
        load();
        return root;
    }

    private void init() {
        recyclerView = root.findViewById(R.id.recycler);
        progress = root.findViewById(R.id.progress);
        not_found = root.findViewById(R.id.not_found);
    }

    private void load(){
        users = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        data = FirebaseDatabase.getInstance().getReference().child("users");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    not_found.setVisibility(View.GONE);
                    users.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        users.add(dataSnapshot.getValue(User.class));
                    }
                    userAdapter = new UserAdapter(getContext(), users);
                    recyclerView.setAdapter(userAdapter);
                } else {
                    not_found.setVisibility(View.VISIBLE);
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}