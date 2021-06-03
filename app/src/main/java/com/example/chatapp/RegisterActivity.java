package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    TextView signin;

    ImageView profile;
    EditText reg_name,reg_username,reg_password,reg_confirmpassword;
    Button signup;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    Uri mImageUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         signin= findViewById(R.id.signin);
         reg_name=findViewById(R.id.name);
         reg_username=findViewById(R.id.username);
        reg_password=findViewById(R.id.password);
        reg_confirmpassword=findViewById(R.id.confrimpass);
        signup=findViewById(R.id.signup);
        profile=findViewById(R.id.icon);

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),101);


            }
        });




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=reg_username.getText().toString();
                String pass=reg_password.getText().toString();
                String name=reg_name.getText().toString();
                String confpass=reg_password.getText().toString();



                if(TextUtils.isEmpty(confpass))
                {

                    Toast.makeText(RegisterActivity.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<4)
                {
                    Toast.makeText(RegisterActivity.this, "Length should be >4", Toast.LENGTH_SHORT).show();

                }
                else if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                {
                    Toast.makeText(RegisterActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                }

                else{

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(RegisterActivity.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                            }

                           else if(task.isSuccessful())
                            {
                                startActivity(new Intent(RegisterActivity.this,HomeActivity.class));

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").push();
                                StorageReference  storageReference=storage.getReference().child("images/" + UUID.randomUUID().toString());


                                if(mImageUri!=null)
                                {

                                    storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    if(uri.toString()!=null) {
                                                        Model model=new Model(name,email,uri.toString(),mAuth.getUid());
                                                        reference.setValue(model);


                                                    }
                                                }
                                            });
                                        }
                                    });


                                }

                                else
                                {

                                    String imgurl= "https://firebasestorage.googleapis.com/v0/b/chat-app-b4589.appspot.com/o/profile.png?alt=media&token=4457a89e-4b29-43b1-908b-92836daf59d4";
                                    Model model=new Model(name,email,imgurl,mAuth.getUid());
                                    reference.setValue(model);
                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));


                                }
                            }



                        }
                    });
                }

            }
        });



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null  && data.getData()!=null) {

             mImageUri=data.getData();
             profile.setImageURI(mImageUri);


        }
    }


}