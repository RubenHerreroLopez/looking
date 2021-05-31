package com.rmpsoft.looking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.model.Chat;
import com.squareup.picasso.Picasso;

public class ChatListAdapter extends FirestoreRecyclerAdapter<Chat, ChatListAdapter.ViewHolder> {

    ImageView iv_perfil;
    String uri_iv_perfil;

    private ChatListAdapter.OnItemClickListener listener;

    public ChatListAdapter(@NonNull FirestoreRecyclerOptions<Chat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder viewHolder, int i, @NonNull Chat chat) {
        viewHolder.tv_nombre.setText(chat.getTeamName());
        uri_iv_perfil = chat.getTeamImage();
        try {
            Picasso.get().load(uri_iv_perfil).placeholder(R.drawable.ic_perfil_equipo).into(iv_perfil);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.ic_perfil_equipo).into(iv_perfil);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_chat, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nombre = itemView.findViewById(R.id.ChatList_tv_nombreUser);
            iv_perfil = itemView.findViewById(R.id.ChatList_iv_perfil);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(ChatListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
