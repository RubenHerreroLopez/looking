package com.rmpsoft.looking.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {
    static int MESSAGE_SENDER = 1;
    static int MESSAGE_RECEIVE = 2;

    String currentUserID;

    List<Message> messages;

    public MessagesAdapter(@NonNull FirestoreRecyclerOptions<Message> options, String currentID) {
        super(options);
        this.currentUserID = currentID;
        messages = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_mensaje;
        TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_mensaje = itemView.findViewById(R.id.Mensaje_tv_mensaje);
            tv_time = itemView.findViewById(R.id.Mensaje_tv_time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getIdSender().equals(currentUserID)) {
            return MESSAGE_SENDER;
        } else {
            return MESSAGE_RECEIVE;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;

        if (viewType == MESSAGE_SENDER) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_sent_messages, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_receive_messages, viewGroup, false);
        }

            return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Message message) {
        viewHolder.tv_mensaje.setText(message.getMessage());
        viewHolder.tv_time.setText(message.getTime());
    }

    public void setData (List<Message> messages) {
        this.messages = messages;
    }
}
