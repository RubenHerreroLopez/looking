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
import com.rmpsoft.looking.model.Anuncio;

public class AnunciosUserAdapter extends FirestoreRecyclerAdapter<Anuncio, AnunciosUserAdapter.ViewHolder> {

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
    }

    @NonNull
    @Override
    public AnunciosUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_anuncios_userhome, viewGroup, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        }
    }

}
