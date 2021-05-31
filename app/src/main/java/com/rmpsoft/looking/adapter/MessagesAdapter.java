package com.rmpsoft.looking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.model.Message;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {

    public MessagesAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Message message) {
        viewHolder.tv_mensaje.setText(message.getMessage());
        viewHolder.tv_time.setText(message.getTime());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sent_messages, viewGroup, false);
        return new ViewHolder(view);
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
}
