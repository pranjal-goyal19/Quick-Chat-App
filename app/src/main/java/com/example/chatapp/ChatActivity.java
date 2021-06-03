package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String RevImg,RevUid,RevName;
    List<String> l;

    ImageView profileImg;
     TextView revname;

    FirebaseUser fuser;

   MsgAdapter msgAdapter;
   ArrayList<Messages> clist;
   RecyclerView recyclerView;

     EditText edtMessage;
     ImageView send;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        clist=new ArrayList<>();
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        l=new ArrayList<>();

        RevName=getIntent().getStringExtra("name");
       RevImg=getIntent().getStringExtra("RecieverImg");
        RevUid=getIntent().getStringExtra("uid");

        revname=findViewById(R.id.name);
        edtMessage=findViewById(R.id.editmsz);
        profileImg=findViewById(R.id.profile_pic);

        recyclerView=findViewById(R.id.recycler_chat);
         LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
         linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        revname.setText(RevName);
       Picasso.get().load(RevImg).into(profileImg);





        send=findViewById(R.id.send);



        msgAdapter=new MsgAdapter(ChatActivity.this,clist,RevImg);
        msgAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(msgAdapter);


        readmessages(fuser.getUid(),RevUid,RevImg);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg=edtMessage.getText().toString();

                if (!msg.isEmpty())
                {
                   sendMessage(fuser.getUid(),RevUid,msg);

                }
                else
                {
                     Toast.makeText(ChatActivity.this,"Enter Message",Toast.LENGTH_LONG).show();
                }
                edtMessage.setText("");
            }
        });

    }

    public void sendMessage(String sender,String receiver,String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>map=new HashMap<>();
        map.put("sender",sender);
        map.put("receiver",receiver);
        map.put("message",message);


        reference.child("chats").push().setValue(map);

    }


    public void readmessages(String sender,String receiver,String imgurl)
    {


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chats");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    clist.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    Messages msg=dataSnapshot.getValue(Messages.class);

                    if(msg.getReceiver().equals(sender) && msg.getSender().equals(receiver)
                         ||msg.getReceiver().equals(receiver) && msg.getSender().equals(sender)) {

                           clist.add(msg);

                    }
                }
                msgAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}