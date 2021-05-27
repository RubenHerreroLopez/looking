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
import com.rmpsoft.looking.model.Anuncio;
import com.squareup.picasso.Picasso;

public class AnunciosUserAdapter extends FirestoreRecyclerAdapter<Anuncio, AnunciosUserAdapter.ViewHolder> {

    ImageView iv_perfil;
    String uri_iv_perfil;
    private OnItemClickListener listener;

    public AnunciosUserAdapter(@NonNull FirestoreRecyclerOptions<Anuncio> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnunciosUserAdapter.ViewHolder viewHolder, int i, @NonNull Anuncio anuncio) {
        viewHolder.tv_equipo.setText((anuncio.getEquipo()));
        viewHolder.tv_deporte.setText(anuncio.getDeporte());
        viewHolder.tv_municipio.setText(anuncio.getMunicipio());
        viewHolder.tv_contacto.setText(anuncio.getContacto());
        viewHolder.tv_posicion.setText(anuncio.getPosicion());
        uri_iv_perfil = anuncio.getImagen();
        try {
            Picasso.get().load(uri_iv_perfil).placeholder(R.drawable.ic_perfil_equipo).into(iv_perfil);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.ic_perfil_equipo).into(iv_perfil);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_anuncios_userhome, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_equipo;
        TextView tv_deporte;
        TextView tv_municipio;
        TextView tv_contacto;
        TextView tv_posicion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_equipo = itemView.findViewById(R.id.ListAnunciosUser_lbl_equipo);
            tv_deporte = itemView.findViewById(R.id.ListAnunciosUser_lbl_deporte);
            tv_municipio = itemView.findViewById(R.id.ListAnunciosUser_lbl_municipio);
            tv_contacto = itemView.findViewById(R.id.ListAnunciosUser_lbl_contacto);
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

    public void setOnItemClickListener(AnunciosUserAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
