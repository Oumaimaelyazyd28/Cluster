package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfileEtudActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView ivUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_etud);

        getSupportActionBar().setTitle("Photo de profile");

        Button buttonUploadPicChoose = findViewById(R.id.btn_upload_choose_pic);
        Button buttonUplodPic = findViewById(R.id.btn_upload_pic);
        progressBar = findViewById(R.id.progressbar_upload);
        ivUploadPic = findViewById(R.id.iv_display_pic);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        Uri uri = firebaseUser.getPhotoUrl();

        //Set User's current DP in ImageView (if uploaded already). usingn Picasso since ImageViewer setImage
        //Regular URIs
        Picasso.with(UploadProfileEtudActivity.this).load(uri).into(ivUploadPic);

        //Choosing image to upload
        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoser();
            }
        });

        //Upload Image
        buttonUplodPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                uploadPic();
            }
        });

    }

    private void openFileChoser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriImage = data.getData();
            ivUploadPic.setImageURI(uriImage);
        }
    }

    private void uploadPic(){

        if (uriImage != null){
            //save the image with uid of the currently logged user
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid() + "."
                    + getFileExtension(uriImage));

            //Upload image to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authProfile.getCurrentUser();

                            //Finally set the display image of the user after upload
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfileEtudActivity.this, "Image de profile est modifiée", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadProfileEtudActivity.this, EtudProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfileEtudActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(UploadProfileEtudActivity.this, "Acune image est séléctionné", Toast.LENGTH_SHORT).show();
        }


    }

    //Obtain file extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Creating ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh){
            //Refresh activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_acceuil){
            Intent intent = new Intent(UploadProfileEtudActivity.this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_update_profile){
            Intent intent = new Intent(UploadProfileEtudActivity.this,UpdateProfileEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_update_email){
            Intent intent = new Intent(UploadProfileEtudActivity.this,UpdateEmailEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_settings){
            Toast.makeText(UploadProfileEtudActivity.this,"menu_settings",Toast.LENGTH_LONG).show();
        } else if (id == R.id.menu_change_password){
            Intent intent = new Intent(UploadProfileEtudActivity.this,ChangePasswordEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(UploadProfileEtudActivity.this,DeleteProfileEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout){
            authProfile.signOut();
            Toast.makeText(UploadProfileEtudActivity.this,"Déconnecté",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UploadProfileEtudActivity.this,MainActivity.class);

            //Clear stack to prevent user coming back to UserProfileActivity  on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(UploadProfileEtudActivity.this,"Erreur!",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}