package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.temporal.Temporal;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterUser extends RecyclerView.Adapter<viewHolder> {

    Context context;
    ArrayList<Drivers_user> userArrayList;
    public RecyclerViewAdapterUser(Context context, ArrayList<Drivers_user> userArrayList){
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Drivers_user driversUser = userArrayList.get(position);
        holder.name.setText(driversUser.name);
        holder.status.setText(driversUser.status);
        Picasso.get().load(driversUser.imageUri).into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, msgScreen.class);
                intent.putExtra("img",driversUser.imageUri);
                intent.putExtra("nme",driversUser.name);
                intent.putExtra("id",driversUser.userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}

class viewHolder extends RecyclerView.ViewHolder{

    CircleImageView userImage;
    TextView name;

    TextView status;

    public viewHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_pic);
        name = itemView.findViewById(R.id.user_name);
        status = itemView.findViewById(R.id.user_status);
    }
}


