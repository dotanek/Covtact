package com.bigpharma.covtact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button regButton;
    EditText input_name, input_email, input_password, input_passwordcheck;
    TextView label_login;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name, email;


    public void toLogin(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        input_name = (EditText) findViewById(R.id.input_name);
        input_passwordcheck = (EditText) findViewById(R.id.input_passwordcheck);
        regButton = (Button) findViewById(R.id.regButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = input_email.getText().toString().trim();
                String password = input_password.getText().toString().trim();
                String passwordCheck = input_passwordcheck.getText().toString().trim();
                name = input_name.getText().toString().trim();
                if (name.isEmpty()) {
                    input_name.setError("Name is required!");
                    input_name.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    input_email.setError("Email is required!");
                    input_email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    input_email.setError("Email is not valid!");
                    input_email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    input_password.setError("Password is required!");
                    input_password.requestFocus();
                    return;
                }
                if (passwordCheck.isEmpty()) {
                    input_passwordcheck.setError("Password check is required!");
                    input_passwordcheck.requestFocus();
                    return;
                } else if (!password.equals(passwordCheck)) {
                    input_passwordcheck.setError("Passwords didn't match");
                    input_passwordcheck.requestFocus();
                    return;
                } else if (password.length() < 6) {
                    input_password.setError("Password should be at least 6 characters long!");
                    input_password.requestFocus();
                    return;
                }
                // ogniobaza reje
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("lastChecked", "empty");
                            FirebaseFirestore.getInstance().collection("users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }
                                    });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to create user account", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
                //view.getContext().startActivity(loginIntent);
            }
        });

    }
}