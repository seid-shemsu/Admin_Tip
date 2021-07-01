package com.izhar.admintip.ui.requests;

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
import com.izhar.admintip.R;
import com.izhar.admintip.objects.Request;
import com.izhar.admintip.objects.User;
import com.izhar.admintip.ui.users.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {
    List<Request> requests;
    RequestAdapter requestAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference data;
    private ProgressBar progress;
    private TextView not_found;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_requests, container, false);
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
        requests = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        data = FirebaseDatabase.getInstance().getReference().child("payment_requests");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    not_found.setVisibility(View.GONE);
                    requests.clear();
                    recyclerView.removeAllViews();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        requests.add(dataSnapshot.getValue(Request.class));
                    }
                    requestAdapter = new RequestAdapter(getContext(), requests);
                    recyclerView.setAdapter(requestAdapter);
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