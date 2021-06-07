package com.rmpsoft.looking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.rmpsoft.looking.R;
import com.rmpsoft.looking.model.Advice;

public class AdvicesTeamAdapter extends FirestoreRecyclerAdapter<Advice, AdvicesTeamAdapter.ViewHolder>  {

    private OnItemClickListener listener;
    
    public AdvicesTeamAdapter(@NonNull FirestoreRecyclerOptions<Advice> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Advice advice) {
        viewHolder.tv_contacto.setText(advice.getContacto());
        viewHolder.tv_posicion.setText(advice.getPosicion());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_advices_team_home, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_contacto;
        TextView tv_posicion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_contacto = itemView.findViewById(R.id.ListAnuncios_lbl_contacto);
            tv_posicion = itemView.findViewById(R.id.ListAnuncios_lbl_posicion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}