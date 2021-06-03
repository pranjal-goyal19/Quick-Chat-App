package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    public static final int Msz_Left=0;
    public static final int Msz_Right=1;
    private Context mContext;
    FirebaseUser fuser;
    private ArrayList<Messages> list;
    private String imgurl;

    public MsgAdapter(Context mContext, ArrayList<Messages> list,String imgurl) {
        this.mContext = mContext;
        this.list = list;
        this.imgurl=imgurl;
    }



    @NonNull
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==Msz_Right) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_ryt, parent, false);
            return new ViewHolder(v);
        }
        else{
            View v = LayoutInflater.from(mContext).inflate(R.layout.chat_item, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MsgAdapter.ViewHolder holder, int position) {

        Messages currentItem=list.get(position);


            holder.show_msg.setText(currentItem.getMessage());
            Picasso.get().load(imgurl).into(holder.img);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView img;
        public TextView show_msg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            show_msg=itemView.findViewById(R.id.show_msg);
            img=itemView.findViewById(R.id.pro_pic);


        }
    }
    public int getItemViewType(int position)
    {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(fuser.getUid()))
        {
            return Msz_Right;
        }
        else
        {
            return Msz_Left;
        }
    }
}
