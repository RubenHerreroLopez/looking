package com.rmpsoft.looking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rmpsoft.looking.adapter.MessagesAdapter;
import com.rmpsoft.looking.model.Message;
import com.rmpsoft.looking.utils.Toast_Manager;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    EditText et_mensaje;
    ImageButton btn_enviar;

    String idTeam;
    String idUser;
    String chatPath;

    RecyclerView rv_Messages;
    MessagesAdapter messagesAdapter;

    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    private List<Message> listMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();

        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        rv_Messages = findViewById(R.id.Chat_rv_mensajes);
        rv_Messages.setLayoutManager(new LinearLayoutManager(this));

        et_mensaje = findViewById(R.id.Chat_et_mensaje);
        btn_enviar = findViewById(R.id.Chat_btn_enviar);

        idTeam = getIntent().getExtras().getString("idTeam");
        idUser = getIntent().getExtras().getString("idUser");
        chatPath = getIntent().getExtras().getString("chatPath");

        loadMessages();

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_mensaje.getText().toString();

                if(message.isEmpty()) {
                    et_mensaje.setError("No puedes envíar un mensaje vacío");
                    et_mensaje.setFocusable(true);
                }

                sendMessage(message);
            }
        });

    }

    @Override
    protected void onStart() {
        messagesAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        messagesAdapter.stopListening();
    }

    /* Método que carga los mensajes del chat */
    private void loadMessages() {
        Query query = firestore.collection("Message").whereEqualTo("idUser", firebaseuser.getUid()).whereEqualTo("idTeam", idTeam);

        Log.d("value", " valor uid " + firebaseuser.getUid());

        FirestoreRecyclerOptions<Message> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class).build();

        messagesAdapter = new MessagesAdapter(firestoreRecyclerOptions);
        messagesAdapter.notifyDataSetChanged();

        rv_Messages.setAdapter(messagesAdapter);
    }

    /* Método que envía el mensaje al servidor */
    private void sendMessage(String message) {

        Random randomuid = new Random();
        String idMessage = new BigInteger(100, randomuid).toString(32);

        Message messageToSend = new Message(idMessage, idUser, idTeam, message, chatPath);

        firestore.collection("Message").document(idMessage).set(messageToSend).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                et_mensaje.setText(" ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast_Manager.showToast(ChatActivity.this, "No se pudo envíar el mensaje");
            }
        });
    }

}