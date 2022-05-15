package com.example.cluster;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class MainAdapterEtudProf extends FirebaseRecyclerAdapter<MainEtudModel,MainAdapterEtudProf.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapterEtudProf(@NonNull FirebaseRecyclerOptions<MainEtudModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainEtudModel model) {
        holder.nom.setText(model.getNom() + " " + model.getPrenom());
        holder.cne.setText(model.getCne());
        holder.email.setText(model.getEmail());

        int PERMISSION_CODE =100;



        Glide.with(holder.img.getContext())
                .load(model.getUri())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus =DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup_etud_prof))
                        .setExpanded(true,1200)
                        .create();

                //dialogPlus.show();

                View view1 = dialogPlus.getHolderView();

                EditText nom = view1.findViewById(R.id.txtNameEtudProf);
                EditText prenom = view1.findViewById(R.id.txtPrenomEtudProf);
                EditText cne = view1.findViewById(R.id.txtCneEtudProf);
                EditText date = view1.findViewById(R.id.txtNaisEtudProf);
                EditText tele = view1.findViewById(R.id.txtTeleEtudProf);
                EditText email = view1.findViewById(R.id.txtEmailUrlEtudProf);



                //ImageView ivShowInfo = view1.findViewById(R.id.iv_info_etud);
                Button annuler = view1.findViewById(R.id.btn_cancelInfo);

                nom.setText(model.getNom());
                prenom.setText(model.getPrenom());
                cne.setText(model.getCne());
                date.setText(model.getDate());
                tele.setText(model.getTele());
                email.setText(model.getEmail());

                dialogPlus.show();

               annuler.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       dialogPlus.dismiss();
                   }
               });

                /*ivShowInfo.setOnClickListener(new View.OnClickListener() {
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
                });*/
            }
        });

        holder.imageViewTele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobileNo = model.getTele();
                String call = "tel:" +mobileNo.trim();
                Intent intent  = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                //startActivity(intent);
                view.getContext().startActivity(intent);

            }
        });

        holder.imageViewGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = model.getEmail();
                String[] adress = email.split(",");
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,adress);
                view.getContext().startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_prof_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView nom,prenom,cne,email;

        ImageView imageViewTele, imageViewGmail,imageViewInfo;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.img_list_etud_2);
            nom = (TextView)itemView.findViewById(R.id.tv_nameEtudProf);
            email = (TextView)itemView.findViewById(R.id.tv_emailEtud);
            cne = (TextView)itemView.findViewById(R.id.tv_tele_etud);


            imageViewTele = (ImageView)itemView.findViewById(R.id.iv_call_etud);
            imageViewGmail = (ImageView)itemView.findViewById(R.id.iv_gmail_etud);
            imageViewInfo = (ImageView)itemView.findViewById(R.id.iv_info_etud);
        }

    }

}
