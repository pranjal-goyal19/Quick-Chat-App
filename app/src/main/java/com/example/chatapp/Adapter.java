package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    private Context mContext;
    FirebaseUser fuser;
    private ArrayList<Model> list;


    public Adapter(Context mContext, ArrayList<Model> list) {
        this.mContext = mContext;
        this.list = list;

    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(mContext).inflate(R.layout.single_row, parent, false);

            return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        Model currentItem=list.get(position);

        holder.name.setText(currentItem.getName());
        Picasso.get().load(currentItem.getImgurl()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(holder.name.getContext(),ChatActivity.class);
                intent.putExtra("name",currentItem.getName());
                intent.putExtra("RecieverImg",currentItem.getImgurl());
                intent.putExtra("uid",currentItem.getUid());
                holder.name.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView img;
        public TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            img=itemView.findViewById(R.id.profile_pic);

        }
    }

}

