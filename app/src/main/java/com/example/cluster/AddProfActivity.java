package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProfActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private ImageView imageViewAdd, imageViewCancel;
    private EditText edNom,edPrenom,edTele, edEmail,edPass;
    private ProgressBar progressBar;
    private DatePickerDialog picker;
    private static final String TAG = "AddProfActivity";
    private Uri filePath;
    private Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prof);

        circleImageView = findViewById(R.id.iv_display_pic);
        imageViewAdd = findViewById(R.id.iv_add);
        imageViewCancel = findViewById(R.id.iv_cancel);
        edNom = findViewById(R.id.ed_admin_nom);
        edPrenom = findViewById(R.id.ed_admin_prenom);
        edTele = findViewById(R.id.ed_admin_tele);
        edEmail = findViewById(R.id.ed_admin_email);
        edPass = findViewById(R.id.ed_admin_pass);
        progressBar = findViewById(R.id.progressbar_addProf);

        storageReference = FirebaseStorage.getInstance().getReference("ProfsPics");

        authProfile = FirebaseAuth.getInstance();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoser();
            }
        });

        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtain the entered data
                String nom = edNom.getText().toString();
                String module = edPrenom.getText().toString();
                String tele = edTele.getText().toString();
                String email = edEmail.getText().toString();
                String passe = edPass.getText().toString();
                String urlImage = "url";

                //validate mobile number using matcher and pattern (regular expression)
                String teleValid = "0[6-7][0-9]{8}";
                Matcher teleMatcher;
                Pattern telePattern = Pattern.compile(teleValid);
                teleMatcher = telePattern.matcher(tele);

                if (TextUtils.isEmpty(nom)){
                    edNom.setError("Le nom est obligatoire");
                    edNom.requestFocus();
                } else if (TextUtils.isEmpty(module)){
                    edPrenom.setError("Le prenom est obligatoire");
                    edPrenom.requestFocus();
                } else if (TextUtils.isEmpty(tele)){
                    edTele.setError("Le numero de telephone est obligatoire");
                    edTele.requestFocus();
                } else if (tele.length() != 10){
                    edTele.setError("Le numero de telephone doit comporter 10 chiffres");
                    edTele.requestFocus();
                } else if (!teleMatcher.find()){
                    edTele.setError("Le numero de telephone est invalide");
                    edTele.requestFocus();
                } else if (TextUtils.isEmpty(email)){
                    edEmail.setError("L'email est obligatoire");
                    edEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edEmail.setError("Entrer un email valide");
                    edEmail.requestFocus();
                } else if (TextUtils.isEmpty(passe)){
                    edPass.setError("Le mot de passe est obligatoire");
                    edPass.requestFocus();
                } else if (passe.length() < 6){
                    edPass.setError("Mot de passe faible, il doit comporter au moin 6 caractères");
                    edPass.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerProf(nom,module,tele,email,urlImage,passe);
                }
            }
        });

        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProfActivity.this, GestionProfsActivity.class);
                finish();
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
            circleImageView.setImageURI(uriImage);
        }
    }
/*
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
                    Toast.makeText(AddProfActivity.this, "Image de profile est modifiée", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProfActivity.this, EtudProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProfActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(AddProfActivity.this, "Acune image est séléctionné", Toast.LENGTH_SHORT).show();
        }
    }

    //Obtain file extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }*/


    private void registerProf(String nom, String module,String tele, String email,String url, String pass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(AddProfActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            /*//Update display name of user
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nom).build();
                            firebaseUser.updateProfile(profileChangeRequest);*/

                            //Enter user data into the firebase realtime database

                            String t = String.valueOf(uriImage);
                            ProfDetails profDetails = new ProfDetails(nom,module,tele,email,t);

                            //Extracting user reference from database for  "Registered Profs"
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Profs");


                            referenceProfile.child(firebaseUser.getUid()).setValue(profDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        //Send Verification Email
                                        //firebaseUser.sendEmailVerification();
                                        Toast.makeText(AddProfActivity.this,"Professeur enregistré avec succès",
                                                Toast.LENGTH_LONG).show();
                                        enrigistrerPhoto(nom,module,tele,uriImage,authProfile.getCurrentUser());

                                        //Open user profile after successful registration
                                        //Intent intent = new Intent(AddProfActivity.this, EtudProfileActivity.class);

                                        /*to prevent user from returning back to register activity on pressing back button after registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); //to close register activity*/
                                    } else {
                                        Toast.makeText(AddProfActivity.this,"Echec. Veuillez réessayer",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    //Hide progressbar whether user creation is successful or failed
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e){
                                edPass.setError("Votre mot de passe est trop faible");
                                edPass.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                edEmail.setError("Votre email est invalide");
                                edEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e){
                                edEmail.setError("Un compte est déja crée avec cet email");
                                edEmail.requestFocus();
                            } catch (Exception e){
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(AddProfActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                            }
                            //Hide progressbar whether user creation is successful or failed
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void enrigistrerPhoto(String nom, String prenom, String tele,Uri picUri, FirebaseUser currentUser){

        //upload user photo to firebase storage and get url
        storageReference = FirebaseStorage.getInstance().getReference().child("Profs_Pictures");
        StorageReference imageFilePath = storageReference.child(picUri.getLastPathSegment());
        imageFilePath.putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //image uploaded successfully
                //get image uri

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //url contain image user url

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri).build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(AddProfActivity.this, "Register Complete", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                });

            }
        });
    }
}