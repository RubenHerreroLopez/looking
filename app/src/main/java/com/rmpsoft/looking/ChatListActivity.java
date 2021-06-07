package com.rmpsoft.looking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rmpsoft.looking.adapter.ChatListAdapter;
import com.rmpsoft.looking.adapter.ChatListTeamAdapter;
import com.rmpsoft.looking.model.Chat;

public class ChatListActivity extends AppCompatActivity {

    RecyclerView rv_chats;
    ChatListTeamAdapter chatListTeamAdapter;
    ChatListAdapter chatListAdapter;
    Chat chatSelected;

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    String idSender;
    String idReceiver;
    String chatPath;
    String receiverName;
    String tipoUsuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle(" ");
        actionbar.setIcon(R.drawable.ic_actionbar_logo);

        rv_chats = findViewById(R.id.ChatList_rv_contactList);
        rv_chats.setLayoutManager(new LinearLayoutManager(this));

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        tipoUsuario = getIntent().getExtras().getString("tipoUsuario");

        if (tipoUsuario.equals("usuario")) {
            getUserContacts();
        } else if (tipoUsuario.equals("equipo")) {
            getTeamContacts();
        }


    }

    @Override
    protected void onStart() {
        if (tipoUsuario.equals("usuario")) {
            chatListAdapter.startListening();
        } else if (tipoUsuario.equals("equipo")) {
            chatListTeamAdapter.startListening();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tipoUsuario.equals("usuario")) {
            chatListAdapter.stopListening();
        } else if (tipoUsuario.equals("equipo")) {
            chatListTeamAdapter.stopListening();
        }
    }

    private void getTeamContacts() {
        Query query = firestore.collection("Chat").whereEqualTo("idTeam", firebaseuser.getUid());

        FirestoreRecyclerOptions<Chat> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class).build();

        chatListTeamAdapter = new ChatListTeamAdapter(firestoreRecyclerOptions);
        chatListTeamAdapter.notifyDataSetChanged();

        rv_chats.setAdapter(chatListTeamAdapter);

        chatListTeamAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                chatSelected = null;
                chatSelected = documentSnapshot.toObject(Chat.class);

                idSender = chatSelected.getIdTeam();
                idReceiver = chatSelected.getIdUser();
                chatPath = chatSelected.getChatPath();
                receiverName = chatSelected.getUserName();
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("idSender", idSender);
                intent.putExtra("idReceiver", idReceiver);
                intent.putExtra("chatPath", chatPath);
                intent.putExtra("receiverName", receiverName);
                intent.putExtra("tipoUsuario", tipoUsuario);
                startActivity(intent);

            }
        });
    }

    private void getUserContacts() {

        Query query = firestore.collection("Chat").whereEqualTo("idUser", firebaseuser.getUid());

        FirestoreRecyclerOptions<Chat> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class).build();

        chatListAdapter = new ChatListAdapter(firestoreRecyclerOptions);
        chatListAdapter.notifyDataSetChanged();

        rv_chats.setAdapter(chatListAdapter);

        chatListAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                chatSelected = null;
                chatSelected = documentSnapshot.toObject(Chat.class);

                idSender = chatSelected.getIdUser();
                idReceiver = chatSelected.getIdTeam();
                chatPath = chatSelected.getChatPath();
                receiverName = chatSelected.getTeamName();
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("idSender", idSender);
                intent.putExtra("idReceiver", idReceiver);
                intent.putExtra("chatPath", chatPath);
                intent.putExtra("receiverName", receiverName);
                startActivity(intent);

            }
        });
    }

}