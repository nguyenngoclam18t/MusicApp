package com.example.musicapp.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.Model.FirebaseAuthencation;
import com.example.musicapp.R;

public class Profile extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextDOB;
    private EditText editTextAddress;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private Button profileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
//         FirebaseAuthencation. = FirebaseAuthencation.getInstance();
//
//// Lấy người dùng hiện tại
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            // Lấy UID của người dùng hiện tại
//            String userUID = currentUser.getUid();
//            // Sử dụng UID theo nhu cầu của bạn
//            Log.d("FirebaseAuth", "Current user UID: " + userUID);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}