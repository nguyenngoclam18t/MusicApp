//package com.example.musicapp.View;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.InputType;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.musicapp.R;
//
//public class Register extends AppCompatActivity {
//
//    private boolean passwordShowing = false;
//    private boolean conPasswordShowing = false;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_register);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        final EditText email = findViewById(R.id.emailET);
//        final EditText mobile = findViewById(R.id.mobileET);
//
//        final EditText password = findViewById(R.id.passwordET);
//        final EditText conPassword = findViewById(R.id.conPasswordET);
//        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
//        final ImageView conPasswordIcon = findViewById(R.id.conPasswordIcon);
//
//        final AppCompatButton signUpBtn = findViewById(R.id.signUpBtn);
//        final TextView signInBtn = findViewById(R.id.signInBtn);
//
//        passwordIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(passwordShowing){
//                    passwordShowing = false;
//
//                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    passwordIcon.setImageResource(R.drawable.password_show);
//                }
//                else{
//                    passwordShowing = true;
//
//                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    passwordIcon.setImageResource(R.drawable.password_hide);
//                }
//
//                password.setSelection(password.length());
//            }
//        });
//
//        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(conPasswordShowing){
//                    conPasswordShowing = false;
//
//                    conPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    conPasswordIcon.setImageResource(R.drawable.password_show);
//                }
//                else{
//                    conPasswordShowing = true;
//
//                    conPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    conPasswordIcon.setImageResource(R.drawable.password_hide);
//                }
//
//                conPassword.setSelection(conPassword.length());
//            }
//        });
//
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String getMobileTxt = mobile.getText().toString();
//                final String getEmailTxt = email.getText().toString();
//                Intent intent = new Intent(Register.this, OTPVerification.class);
//                intent.putExtra("mobile", getMobileTxt);
//                intent.putExtra("email", getEmailTxt);
//                startActivity(intent);
//            }
//        });
//
//        signInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }
//}