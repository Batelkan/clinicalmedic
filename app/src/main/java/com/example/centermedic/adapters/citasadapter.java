package com.example.centermedic.adapters;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.centermedic.R;
import com.example.centermedic.models.citas;
import java.util.ArrayList;

public class citasadapter extends RecyclerView.Adapter<citasadapter.CitaViewHolder> {

    private ArrayList<citas>citaslist;


    public citasadapter(ArrayList<citas>citaslist)
    {
        this.citaslist = citaslist;

    }

    public static class CitaViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView hospital;
        TextView doctor;
        TextView fecha;
        ImageView avatar;
        Button maps;
        CitaViewHolder(View itemview)
        {
            super(itemview);
            cv = (CardView) itemview.findViewById(R.id.cv);
            hospital = (TextView) itemview.findViewById(R.id.cv_hospital);
            doctor = (TextView) itemview.findViewById(R.id.cv_medico);
            avatar =  (ImageView) itemview.findViewById(R.id.cv_avatar);
            maps =(Button) itemview.findViewById(R.id.cv_mapa);
            fecha = (TextView) itemview.findViewById(R.id.cv_fecha);
        }
    }


    @Override
    public int getItemCount()
    {
        return citaslist.size();
    }

    @Override
    public CitaViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext() ).inflate(R.layout.platilla_cardview,viewGroup, false);
        CitaViewHolder pvh = new CitaViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final citasadapter.CitaViewHolder personViewHolder, final int i) {

        personViewHolder.hospital.setText(citaslist.get(i).getHospital().toString());
        personViewHolder.doctor.setText(citaslist.get(i).getMedico().toString() +" - "+ citaslist.get(i).getArea().toString());
        personViewHolder.fecha.setText("Fecha: "+citaslist.get(i).getFecha().toString() +" - Horario: "+  citaslist.get(i).getHorario().toString());


       /* personViewHolder.tel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentoLlamada = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel",citaslist.get(i).getHorario(),null));
                        v.getContext().startActivity(intentoLlamada);
                    }
                }
        );

        personViewHolder.whats.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri uri = Uri.parse("smsto:" + citaslist.get(i).getTelefono());
                        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                        i.setPackage("com.whatsapp");
                        v.getContext().startActivity(Intent.createChooser(i, ""));
                    }
                }
        );*/

        personViewHolder.maps.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri gmmIntentUri = Uri.parse("geo:"+ citaslist.get(i).getLatitud()+","+citaslist.get(i).getLongitud()+"?z=10&q="+citaslist.get(i).getHospital()+", Merida Yucatan");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        v.getContext().startActivity(mapIntent);
                    }
                }
        );


    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
