package com.example.musicapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.Controller.UserListAdminLayoutAdapter;
import com.example.musicapp.Model.UserModel;
import com.example.musicapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean conPasswordShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final EditText edtemail = findViewById(R.id.emailET);
        final EditText edtmobile = findViewById(R.id.mobileET);
        final EditText edtpassword = findViewById(R.id.passwordET);
        final EditText edtFullname = findViewById(R.id.fullnameET);
        final EditText conPassword = findViewById(R.id.conPasswordET);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        final ImageView conPasswordIcon = findViewById(R.id.conPasswordIcon);

        final AppCompatButton signUpBtn = findViewById(R.id.signUpBtn);
        final TextView signInBtn = findViewById(R.id.signInBtn);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordShowing){
                    passwordShowing = false;
                    edtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_show);
                }
                else{
                    passwordShowing = true;
                    edtpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_hide);
                }

                edtpassword.setSelection(edtpassword.length());
            }
        });

        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conPasswordShowing){
                    conPasswordShowing = false;
                    conPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.password_show);
                }
                else{
                    conPasswordShowing = true;
                    conPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.password_hide);
                }
                conPassword.setSelection(conPassword.length());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtpassword.getText().toString().equals(conPassword.getText().toString())){
                    Toast.makeText(Register.this, "Mật khẩu nhập lại không trùng khớp", Toast.LENGTH_SHORT).show();
                }else
                {
                    String email = edtemail.getText().toString();
                    String password = edtpassword.getText().toString();
                    String fullName = edtFullname.getText().toString();
                    String mobile = edtmobile.getText().toString();

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        String userId = user.getUid();

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("avatarUrl", "https://scontent.fsgn19-1.fna.fbcdn.net/v/t39.30808-1/285809130_959096242156816_5789182661899828328_n.jpg?stp=dst-jpg_p200x200&_nc_cat=1&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeE-oYiL4eYlpJ67GYg4Gh9wkjhm9PC_HyKSOGb08L8fIqRaZAV2Un-aYbZlwihbc4DeK53CGUcnZYQ-xJdYF9MD&_nc_ohc=d4ZTSwSyahIQ7kNvgHhiQun&_nc_ht=scontent.fsgn19-1.fna&oh=00_AYDdnPfV5_Rknjl3iONNfL99KXf48revxd_RiR4Tq0VQdA&oe=666BE636");
                                        userMap.put("email", email);
                                        userMap.put("fullName", fullName);
                                        userMap.put("mobile", mobile);
                                        db.collection("users").document(userId).set(userMap)
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.d("TAG", "DocumentSnapshot added with ID: " + userId);
                                                    Toast.makeText(Register.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Register.this,Login.class));
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.w("TAG", "Error adding document", e);
                                                    Toast.makeText(Register.this, "Tạo tài khoản thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    Log.d("test", "Đăng ký thất bại: ", task.getException());
                                    Toast.makeText(Register.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
    }
}