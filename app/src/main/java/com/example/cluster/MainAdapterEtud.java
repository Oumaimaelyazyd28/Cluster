package com.example.cluster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapterEtud extends FirebaseRecyclerAdapter<MainEtudModel,MainAdapterEtud.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapterEtud(@NonNull FirebaseRecyclerOptions<MainEtudModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainEtudModel model) {
        holder.nom.setText(model.getNom() + " " + model.getPrenom());
        holder.cne.setText(model.getCne());
        holder.tele.setText(model.getEmail());


        Glide.with(holder.img.getContext())
                .load(model.getUri())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.imageViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus =DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup_etud))
                        .setExpanded(true,1200)
                        .create();

                //dialogPlus.show();

                View view1 = dialogPlus.getHolderView();

                EditText nom = view1.findViewById(R.id.txtNameEtud);
                EditText prenom = view1.findViewById(R.id.txtPrenomEtud);
                EditText cne = view1.findViewById(R.id.txtCneEtud);
                EditText date = view1.findViewById(R.id.txtNaisEtud);
                EditText tele = view1.findViewById(R.id.txtTeleEtud);
                EditText email = view1.findViewById(R.id.txtImageUrlEtud);


                Button btnUpdate = view1.findViewById(R.id.btn_modifEtud);

                nom.setText(model.getNom());
                prenom.setText(model.getPrenom());
                cne.setText(model.getCne());
                date.setText(model.getDate());
                tele.setText(model.getTele());
                email.setText(model.getEmail());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("nom",nom.getText().toString());
                        map.put("prenom",prenom.getText().toString());
                        map.put("cne",cne.getText().toString());
                        map.put("date",date.getText().toString());
                        map.put("tele",tele.getText().toString());
                        map.put("email",email.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Registered users")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.nom.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.nom.getContext(), "Data Updated Failed", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nom.getContext());
                builder.setTitle("Are you sure ?");
                builder.setMessage("Deleted Data can't be undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Registered users")
                                .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.nom.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_adm_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView nom,prenom,cne,tele,date;

        ImageView imageViewDelete, imageViewUpdate;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.img_list_2);
            nom = (TextView)itemView.findViewById(R.id.tv_nameEtud);
            cne = (TextView)itemView.findViewById(R.id.tv_cne);
            tele = (TextView)itemView.findViewById(R.id.tv_tele);


            imageViewUpdate = (ImageView)itemView.findViewById(R.id.iv_update_etud);
            imageViewDelete = (ImageView)itemView.findViewById(R.id.iv_delete_etud);
        }

    }

}
