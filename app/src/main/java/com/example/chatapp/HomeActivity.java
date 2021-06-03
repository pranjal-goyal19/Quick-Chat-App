package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ImageView signout;


    EditText edt_search;
   private RecyclerView recyclerView;

   private  ArrayList<Model> list;
   private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth=FirebaseAuth.getInstance();
      signout=findViewById(R.id.threedot);





      list=new ArrayList<>();

        edt_search=findViewById(R.id.edt_search);


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        recyclerView=findViewById(R.id.recycler);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));


             readUsers();




        if (mAuth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this,RegisterActivity.class));
        }


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        });


    }


    public void readUsers()
    {
        FirebaseUser firebaseUser=mAuth.getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (edt_search.getText().toString().equals("")) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Model model = dataSnapshot.getValue(Model.class);
                        if (!model.getUid().equals(firebaseUser.getUid())) {
                            list.add(model);
                        }

                    }

                    adapter = new Adapter(HomeActivity.this, list);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchUsers(String s)
    {
        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();

        Query query= FirebaseDatabase.getInstance().getReference("users").orderByChild("name")
                .startAt(s).endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Model model = dataSnapshot.getValue(Model.class);
                        if (!model.getUid().equals(fuser.getUid())) {
                            list.add(model);
                        }
                    }

                    adapter = new Adapter(HomeActivity.this, list);
                    recyclerView.setAdapter(adapter);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}