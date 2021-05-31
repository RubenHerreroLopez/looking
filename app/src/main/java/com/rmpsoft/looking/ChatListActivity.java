package com.rmpsoft.looking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rmpsoft.looking.adapter.ChatListAdapter;
import com.rmpsoft.looking.model.Chat;
import com.rmpsoft.looking.model.Equipo;

public class ChatListActivity extends AppCompatActivity {

    RecyclerView rv_chats;
    ChatListAdapter chatListAdapter;

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        rv_chats = findViewById(R.id.ChatList_rv_contactList);
        rv_chats.setLayoutManager(new LinearLayoutManager(this));

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        getContacts();
    }


    @Override
    protected void onStart() {
        chatListAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatListAdapter.stopListening();
    }

    private void getContacts() {

        Query query = firestore.collection("Chat").whereEqualTo("idUser", firebaseuser.getUid());

        FirestoreRecyclerOptions<Chat> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class).build();

        chatListAdapter = new ChatListAdapter(firestoreRecyclerOptions);
        chatListAdapter.notifyDataSetChanged();

        rv_chats.setAdapter(chatListAdapter);
    }


}