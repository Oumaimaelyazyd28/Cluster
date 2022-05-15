package com.example.cluster;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BookAdapter extends FirebaseRecyclerAdapter<BookModel, BookAdapter.myviewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BookAdapter(@NonNull FirebaseRecyclerOptions<BookModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull BookModel model) {

        holder.tvfileName.setText(model.getName());



        holder.imgReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.imgReadFile.getContext(), ViewPlanning.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("url",model.getUrl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.imgReadFile.getContext().startActivity(intent);
            }
        });

        holder.imgDownloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadFile(holder.tvfileName.getContext(),model.getName(),".pdf",DIRECTORY_DOWNLOADS,model.getUrl());
            }
        });

    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_bib,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView tvfileName;
        ImageView imgReadFile, imgDownloadFile;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.ic_bib);
            tvfileName = itemView.findViewById(R.id.tv_book_name);
            imgReadFile = itemView.findViewById(R.id.iv_open_book);
            imgDownloadFile = itemView.findViewById(R.id.ic_download_book);
        }
    }

}
