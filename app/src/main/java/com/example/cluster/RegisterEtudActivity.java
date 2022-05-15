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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterEtudActivity extends AppCompatActivity {

    private EditText editText_nom, editText_prenom, editText_cne, editText_date, editText_tele,
            editText_email, editText_pass, editText_Cpass;
    private ProgressBar progressBar;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterEtudActivity";
    private Uri filePath;
    private CircleImageView image;
    private Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    private StorageReference storageReference;
    private FirebaseAuth authProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_etud);

        getSupportActionBar().setTitle("Inscription");

        Toast.makeText(RegisterEtudActivity.this,"Vous pouvez s'inscrire maintenant!",Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar_reg);
        editText_nom = findViewById(R.id.ed_reg_nom);
        editText_prenom = findViewById(R.id.ed_reg_prenom);
        editText_cne = findViewById(R.id.ed_reg_cne);
        editText_date = findViewById(R.id.ed_reg_naissance);
        editText_tele = findViewById(R.id.ed_reg_tele);
        editText_email = findViewById(R.id.ed_reg_email);
        editText_pass = findViewById(R.id.ed_reg_passe);
        editText_Cpass = findViewById(R.id.ed_reg_passe_confirm);
        String url = String.valueOf(uriImage);

        image = findViewById(R.id.reg_profile_image);

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        authProfile = FirebaseAuth.getInstance();

        //Setting up datepicker on edittext
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(calendar.DAY_OF_MONTH);
                int month = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(RegisterEtudActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        editText_date.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, year,month,day);
                picker.show();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoser();
            }
        });

        Button buttonRegister = findViewById(R.id.btn_reg_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtain the entered data
                String nom = editText_nom.getText().toString();
                String prenom = editText_prenom.getText().toString();
                String cne = editText_cne.getText().toString();
                String date = editText_date.getText().toString();
                String tele = editText_tele.getText().toString();
                String email = editText_email.getText().toString();

                String pass = editText_pass.getText().toString();
                String cPass = editText_Cpass.getText().toString();

                //validate mobile number using matcher and pattern (regular expression)
                String teleValid = "0[6-7][0-9]{8}";
                Matcher teleMatcher;
                Pattern telePattern = Pattern.compile(teleValid);
                teleMatcher = telePattern.matcher(tele);

                if (TextUtils.isEmpty(nom)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de saisir votre nom", Toast.LENGTH_LONG).show();
                    editText_nom.setError("Le nom est obligatoire");
                    editText_nom.requestFocus();
                } else if (TextUtils.isEmpty(prenom)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de saisir votre prenom", Toast.LENGTH_LONG).show();
                    editText_prenom.setError("Le prenom est obligatoire");
                    editText_prenom.requestFocus();
                } else if (TextUtils.isEmpty(cne)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de saisir votre CNE", Toast.LENGTH_LONG).show();
                    editText_cne.setError("Le CNE est obligatoire");
                    editText_cne.requestFocus();
                } else if (TextUtils.isEmpty(date)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de saisir votre date de naissance", Toast.LENGTH_LONG).show();
                    editText_date.setError("La date de naissance est obligatoire");
                    editText_date.requestFocus();
                } else if (TextUtils.isEmpty(tele)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de saisir votre numero de telephone", Toast.LENGTH_LONG).show();
                    editText_tele.setError("Le numero de telephone est obligatoire");
                    editText_tele.requestFocus();
                } else if (tele.length() != 10){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de resaisir votre numero de telephone", Toast.LENGTH_LONG).show();
                    editText_tele.setError("Le numero de telephone doit comporter 10 chiffres");
                    editText_tele.requestFocus();
                } else if (!teleMatcher.find()){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de resaisir votre numero de telephone", Toast.LENGTH_LONG).show();
                    editText_tele.setError("Le numero de telephone est invalide");
                    editText_tele.requestFocus();
                } else if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de saisir votre email", Toast.LENGTH_LONG).show();
                    editText_email.setError("L'email est obligatoire");
                    editText_email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de resaisir votre email", Toast.LENGTH_LONG).show();
                    editText_prenom.setError("Entrer un email valide");
                    editText_prenom.requestFocus();
                } else if (TextUtils.isEmpty(pass)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de saisir un mot de passe", Toast.LENGTH_LONG).show();
                    editText_pass.setError("Le mot de passe est obligatoire");
                    editText_pass.requestFocus();
                } else if (pass.length() < 6){
                    Toast.makeText(RegisterEtudActivity.this,"Le mot de passe doit comporter au moin 6 caractères", Toast.LENGTH_LONG).show();
                    editText_pass.setError("Mot de passe faible");
                    editText_pass.requestFocus();
                } else if (TextUtils.isEmpty(cPass)){
                    Toast.makeText(RegisterEtudActivity.this,"Merci de confirmer le mot de passe", Toast.LENGTH_LONG).show();
                    editText_Cpass.setError("La confirmation de mot de passe est obligatoire");
                    editText_Cpass.requestFocus();
                } else if (!pass.equals(cPass)){
                    Toast.makeText(RegisterEtudActivity.this,"Ce n'est pas le même mot de passe", Toast.LENGTH_LONG).show();
                    editText_Cpass.setError("La confirmation de mot de passe est obligatoire");
                    editText_Cpass.requestFocus();
                    //Clear the entered passwords
                    editText_pass.clearComposingText();
                    editText_Cpass.clearComposingText();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    registerUser(nom,prenom,cne,date,tele,email,pass,url);
                }
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
            image.setImageURI(uriImage);
        }
    }


    private void registerUser(String nom, String prenom, String cne, String date, String tele, String email, String pass,String url) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterEtudActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            /*//Update display name of user
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nom).build();
                            firebaseUser.updateProfile(profileChangeRequest);*/

                            //Enter user data into the firebase realtime database
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(nom,prenom,cne,date,tele,email,url);

                            //Extracting user reference from database for  "Registered users"
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        //Send Verification Email
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText(RegisterEtudActivity.this,"Etudiant enregistré avec succès, veuillez vérifier votre adresse e-mail",
                                                Toast.LENGTH_LONG).show();
                                        enrigistrerPhoto(nom,prenom,cne,date,tele,email,uriImage,authProfile.getCurrentUser());

                                        //Open user profile after successful registration
                                        Intent intent = new Intent(RegisterEtudActivity.this, HomeActivity.class);

                                        //to prevent user from returning back to register activity on pressing back button after registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); //to close register activity
                                    } else {
                                        Toast.makeText(RegisterEtudActivity.this,"Echec. Veuillez réessayer",
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
                                editText_pass.setError("Votre mot de passe est trop faible");
                                editText_pass.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                editText_email.setError("Votre email est invalide");
                                editText_email.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e){
                                editText_email.setError("Un compte est déja crée avec cet email");
                                editText_email.requestFocus();
                            } catch (Exception e){
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(RegisterEtudActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                            }
                            //Hide progressbar whether user creation is successful or failed
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void enrigistrerPhoto(String nom, String prenom, String cne,String date,String tele,String email, final Uri picUri, FirebaseUser currentUser){

        //upload user photo to firebase storage and get url
        storageReference = FirebaseStorage.getInstance().getReference().child("DisplayPics");
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
                                            //Toast.makeText(RegisterEtudActivity.this, "Register Complete", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                });

            }
        });
    }
}