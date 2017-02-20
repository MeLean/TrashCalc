package com.milen.trashcalc.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.milen.trashcalc.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //todo implement better auth
        String user = "milen18@gmail.com";

        mAuth.signInWithEmailAndPassword(user, user + "Tr@#Tr@#")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if(firebaseUser != null && firebaseUser.getDisplayName() != null){
                                Toast.makeText(SplashActivity.this, "Здравей, " + firebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(SplashActivity.this, "Здравей, колега", Toast.LENGTH_LONG).show();
                                //todo add nickname
                            }

                            startActivity(new Intent(SplashActivity.this, MainActivity.class)
                                    .putExtra("start_scanner",true)
                            );
                        }else{

                            Toast.makeText(SplashActivity.this, "Не сте разпознат като администратор!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SplashActivity.this, ReportActivity.class));
                        }

                        SplashActivity.this.finish();
                    }
                });
    }
}
