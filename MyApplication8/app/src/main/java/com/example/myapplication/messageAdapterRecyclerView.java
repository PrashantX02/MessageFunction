package com.example.myapplication;

import static com.example.myapplication.msgScreen.senderImg;
import static com.example.myapplication.msgScreen.reciverImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdapterRecyclerView extends RecyclerView.Adapter {

    Context context;
    ArrayList<MsgModel> arrayList;

    int item_send = 1;
    int item_recieve = 2;

    public messageAdapterRecyclerView(Context context, ArrayList<MsgModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == item_send) {
            view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.reciever_layout, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MsgModel msgModel = arrayList.get(position);

        if (holder instanceof senderViewHolder) {
            senderViewHolder senderHolder = (senderViewHolder) holder;
            senderHolder.msgtext.setText(msgModel.getMsg());
            Picasso.get().load(senderImg).into(senderHolder.circleImageView);
        } else if (holder instanceof receiverViewHolder) {
            receiverViewHolder receiverHolder = (receiverViewHolder) holder;
            receiverHolder.msgtext.setText(msgModel.getMsg());
            Picasso.get().load(reciverImg).into(receiverHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList == null) ? 0 : arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MsgModel msgModel = arrayList.get(position);
        if (msgModel.getUserUid() != null && msgModel.getUserUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return item_send;
        } else {
            return item_recieve;
        }
    }
}

class senderViewHolder extends  RecyclerView.ViewHolder{

    CircleImageView circleImageView;

    TextView msgtext;
    public senderViewHolder(@NonNull View itemView) {
        super(itemView);
        circleImageView = itemView.findViewById(R.id.sender_side_img);
        msgtext = itemView.findViewById(R.id.sender_side_msg);

    }
}

class receiverViewHolder extends  RecyclerView.ViewHolder{
    CircleImageView circleImageView;

    TextView msgtext;
    public receiverViewHolder(@NonNull View itemView) {
        super(itemView);
        circleImageView = itemView.findViewById(R.id.reciever_side_img);
        msgtext = itemView.findViewById(R.id.reciever_side_text);

    }
}
