package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginAdmActivity extends AppCompatActivity {


    private String email;
    private EditText editTextLoginEmail, editTextLoginPass;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginAdmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_adm);

        getSupportActionBar().setTitle("Connexion");

        editTextLoginEmail = findViewById(R.id.ed_log_adm_email);
        editTextLoginPass = findViewById(R.id.ed_log_adm_passe);
        progressBar = findViewById(R.id.progressbar_log_adm);

        authProfile = FirebaseAuth.getInstance();

        //Reset Password
        /*Button btnForgotPass = findViewById(R.id.button_forgot_pass);
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"Vous pouvez rénitialiser votre mot de passe maintenant!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        //show hide password using eye icon
        ImageView imageViewShowHidePass = findViewById(R.id.iv_show_hide_pass);
        imageViewShowHidePass.setImageResource(R.drawable.show_pwd);
        imageViewShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextLoginPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //If password is visible then hide it
                    editTextLoginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change eye icon
                    imageViewShowHidePass.setImageResource(R.drawable.hide_pwd);
                } else {
                    editTextLoginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePass.setImageResource(R.drawable.show_pwd);
                }
            }
        });*/

        //Login User
        Button buttonLogin = findViewById(R.id.btn_log_adm_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextLoginEmail.getText().toString();
                String pass = editTextLoginPass.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginAdmActivity.this,"Veuillez saisir votre email",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("L'email est obligatoire");
                    editTextLoginEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(LoginAdmActivity.this,"Veuillez saisir une adresse valide",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("L'email est invalide");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginAdmActivity.this,"Veuillez saisir votre mot de  passe",Toast.LENGTH_LONG).show();
                    editTextLoginPass.setError("Le mot de passe est obligatoire");
                    editTextLoginPass.requestFocus();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email, pass);
                }
            }
        });
    }


    private void loginUser(String userEmail, String userPass) {
        authProfile.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(LoginAdmActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginAdmActivity.this,"Vous êtes connecté maintenant",Toast.LENGTH_LONG).show();

                    //Get instance of the current user
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    //Open User Profile
                    startActivity(new Intent(LoginAdmActivity.this, HomeAdminActivity.class));
                    finish(); //Close LoginActivity

                    //check email if is verified before user can access their profile
                    /*if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginProfActivity.this,"Vous êtes connecté maintenant",Toast.LENGTH_LONG).show();


                        //Open User Profile
                        startActivity(new Intent(LoginProfActivity.this, HomeProfActivity.class));
                        //startActivity(new Intent(LoginEtudActivity.this, HomeActivity.class));
                        finish(); //Close LoginActivity

                    } else {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut(); //Sign out user
                        showAlertDialog();
                    }*/
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("Cet utilisateur n'existe pas ou n'est plus valide. Veuillez vous inscrire à nouveau!");
                        editTextLoginEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        editTextLoginEmail.setError("Les informations invalides. Veuillez vérifier et entrer à nouveau.");
                        editTextLoginEmail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginAdmActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }



    private void showAlertDialog() {
        //setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginAdmActivity.this);
        builder.setTitle("E-mail non vérifié");
        builder.setMessage("Veuillez vérifier votre e-mail. Vous ne pouvez pas vous connecter sans vérification par e-mail.");

        //Open email apps if user clicks/taps Continue button
        builder.setPositiveButton("Continuez", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //To emaill app in new window and not within our app
                startActivity(intent);
            }
        });

        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertDialog
        alertDialog.show();
    }

    //Check if user is already logged in. In such case, straightaway take the user to the user's profile
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null){
            Toast.makeText(LoginAdmActivity.this,"Déja connecté!",Toast.LENGTH_LONG).show();
            //Start the UserProfileActivity
            startActivity(new Intent(LoginAdmActivity.this, HomeAdminActivity.class));
            //startActivity(new Intent(LoginEtudActivity.this, HomeActivity.class));
            System.out.println("here");
            finish(); //Close LoginActivity
        }
        else {
            Toast.makeText(LoginAdmActivity.this,"Vous pouvez vous connecter maintenant!",Toast.LENGTH_LONG).show();
        }
    }
}