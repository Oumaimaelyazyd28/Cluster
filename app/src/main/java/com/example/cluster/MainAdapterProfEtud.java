package com.example.cluster;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapterProfEtud extends FirebaseRecyclerAdapter<MainModel, MainAdapterProfEtud.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapterProfEtud(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {
        holder.nom.setText(model.getNom());
        holder.prenom.setText(model.getModule());
        holder.email.setText(model.getEmail());

        int PERMISSION_CODE =100;



        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus =DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup_prof_etud))
                        .setExpanded(true,1200)
                        .create();

                //dialogPlus.show();

                View view1 = dialogPlus.getHolderView();

                EditText nom = view1.findViewById(R.id.txtName1);
                EditText prenom = view1.findViewById(R.id.txtPrenom1);
                EditText tele = view1.findViewById(R.id.txtTele1);
                EditText email = view1.findViewById(R.id.txtEmail1);



                //ImageView ivShowInfo = view1.findViewById(R.id.iv_info_etud);
                Button annuler = view1.findViewById(R.id.btn_annuler1);

                nom.setText(model.getNom());
                prenom.setText(model.getModule());
                tele.setText(model.getTele());
                email.setText(model.getEmail());

                dialogPlus.show();

               annuler.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       dialogPlus.dismiss();
                   }
               });
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_etud_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView nom,prenom,email;

        ImageView imageViewTele, imageViewGmail,imageViewInfo;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.img_prof_1);
            nom = (TextView)itemView.findViewById(R.id.tv_nameProf1);
            prenom = (TextView)itemView.findViewById(R.id.tv_teleProf1);
            email = (TextView)itemView.findViewById(R.id.tv_emailProf);


            imageViewTele = (ImageView)itemView.findViewById(R.id.iv_call_prof);
            imageViewGmail = (ImageView)itemView.findViewById(R.id.iv_gmail_prof);
            imageViewInfo = (ImageView)itemView.findViewById(R.id.iv_info_prof);
        }

    }

}
