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
import com.rmpsoft.looking.model.Advice;
import com.squareup.picasso.Picasso;

public class AdvicesUserAdapter extends FirestoreRecyclerAdapter<Advice, AdvicesUserAdapter.ViewHolder> {

    ImageView iv_perfil;
    String uri_iv_perfil;

    private OnItemClickListener listener;

    public AdvicesUserAdapter(@NonNull FirestoreRecyclerOptions<Advice> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdvicesUserAdapter.ViewHolder viewHolder, int i, @NonNull Advice advice) {
        viewHolder.tv_equipo.setText((advice.getEquipo()));
        viewHolder.tv_deporte.setText(advice.getDeporte());
        viewHolder.tv_municipio.setText(advice.getMunicipio());
        viewHolder.tv_posicion.setText(advice.getPosicion());

        uri_iv_perfil = advice.getImagen();
        try {
            Picasso.get().load(uri_iv_perfil).placeholder(R.drawable.ic_perfil_equipo).into(iv_perfil);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.ic_perfil_equipo).into(iv_perfil);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_advices_user_home, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_equipo;
        TextView tv_deporte;
        TextView tv_municipio;
        TextView tv_posicion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_equipo = itemView.findViewById(R.id.ListAnunciosUser_lbl_equipo);
            tv_deporte = itemView.findViewById(R.id.ListAnunciosUser_lbl_deporte);
            tv_municipio = itemView.findViewById(R.id.ListAnunciosUser_lbl_municipio);
            tv_posicion = itemView.findViewById(R.id.ListAnunciosUser_lbl_posicion);
            iv_perfil = itemView.findViewById(R.id.ListAnunciosUser_iv);

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

    public void setOnItemClickListener(AdvicesUserAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
