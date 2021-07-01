package com.izhar.admintip;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.admintip.objects.Game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_Game extends AppCompatActivity {
    Spinner sp_league, sp_home, sp_away, sp_tip;
    EditText et_tip, odd, time;
    CheckBox vip;
    DatabaseReference data;
    AutoCompleteTextView league, home, away, tip_type;
    String date, status, tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__game);
        init();
        ArrayAdapter<CharSequence> lea = ArrayAdapter.createFromResource(this, R.array.league, R.layout.list_item);
        league.setAdapter(lea);
        ArrayAdapter<CharSequence> ti = ArrayAdapter.createFromResource(this, R.array.tips, R.layout.list_item);
        tip_type.setAdapter(ti);

        try {
            league.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayAdapter<CharSequence> adapter;
                    switch (position){
                        case 0:
                            adapter = ArrayAdapter.createFromResource(Add_Game.this, R.array.EPL, R.layout.list_item);
                            home.setAdapter(adapter);
                            away.setAdapter(adapter);
                            break;
                        case 1:
                            adapter = ArrayAdapter.createFromResource(Add_Game.this, R.array.Bundesliga, R.layout.list_item);
                            home.setAdapter(adapter);
                            away.setAdapter(adapter);
                            break;
                        case 2:
                            adapter = ArrayAdapter.createFromResource(Add_Game.this, R.array.Spain_laliga, R.layout.list_item);
                            home.setAdapter(adapter);
                            away.setAdapter(adapter);
                            break;
                        case 3:
                            adapter = ArrayAdapter.createFromResource(Add_Game.this, R.array.Seria_A, R.layout.list_item);
                            home.setAdapter(adapter);
                            away.setAdapter(adapter);
                            break;
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        league = findViewById(R.id.league);
        home = findViewById(R.id.home);
        away = findViewById(R.id.away);
        tip_type = findViewById(R.id.tip_type);
        et_tip = findViewById(R.id.tip);
        vip = findViewById(R.id.vip);
        odd = findViewById(R.id.odd);
        time = findViewById(R.id.time);
    }

    public void addGame(View view) {
        if (Check()){
            view.setEnabled(false);
            league.setEnabled(false);
            tip_type.setEnabled(false);
            et_tip.setEnabled(false);
            home.setEnabled(false);
            away.setEnabled(false);
            vip.setEnabled(false);
            odd.setEnabled(false);
            time.setEnabled(false);
            setElements();
            String id = System.currentTimeMillis() + "";
            if (vip.isChecked())
                data = FirebaseDatabase.getInstance().getReference("vip").child(date).child(id);
            else
                data = FirebaseDatabase.getInstance().getReference("tips").child(date).child(id);
            data.setValue(new Game(league.getText().toString(), home.getText().toString(), away.getText().toString(), date, status, tip, tip_type.getText().toString(), odd.getText().toString(), time.getText().toString(), id))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onBackPressed();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add_Game.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void setElements() {
        tip = et_tip.getText().toString();
        status = "pending";
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    private boolean Check() {
        if (league.getText().toString().length() == 0){
            league.setError("");
            Toast.makeText(this, "select league", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tip_type.getText().toString().length() == 0){

            tip_type.setError("");
            Toast.makeText(this, "select tip type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_tip.getText().toString().length() == 0){
            et_tip.setError("");
            Toast.makeText(this, "enter tip", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (odd.getText().toString().length() == 0){
            odd.setError("");
            Toast.makeText(this, "enter odd", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (time.getText().toString().length() == 0){
            odd.setError("");
            Toast.makeText(this, "enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (home.getText().toString().length() == 0){
            home.setError("");
            Toast.makeText(this, "select home", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (away.getText().toString().length() == 0){
            away.setError("");
            Toast.makeText(this, "select away", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tip_type.getText().toString().length() == 0){
            Toast.makeText(this, "select tip type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (home.getText().toString().equalsIgnoreCase(away.getText().toString())){
            away.getText().toString().length();
            Toast.makeText(this, "select different team", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}