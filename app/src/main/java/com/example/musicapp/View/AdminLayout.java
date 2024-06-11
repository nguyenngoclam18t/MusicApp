package com.example.musicapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.musicapp.Controller.UserListAdminLayoutAdapter;
import com.example.musicapp.Model.DataProfilePage;
import com.example.musicapp.Model.OnItemLongClickListenerUser;
import com.example.musicapp.Model.UserModel;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminLayout extends AppCompatActivity implements OnItemLongClickListenerUser {
    LinearLayout layoutCreateUser;
    Button btnCreateAcc, btnShowCreateUser;
    EditText fullname, username, number, pass;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<UserModel> arrUser = new ArrayList<>();
    RecyclerView recyclerViewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_layout);
        fullname = findViewById(R.id.edtFullNameCreate);
        number = findViewById(R.id.edtNumberCreate);
        username = findViewById(R.id.edtUserNameCreate);
        pass = findViewById(R.id.edtPassCreate);
        btnCreateAcc = findViewById(R.id.btnCreate);
        btnShowCreateUser = findViewById(R.id.btnShowCreateUser);
        pass.setTransformationMethod(new PasswordTransformationMethod());
        layoutCreateUser = findViewById(R.id.layoutCreateUserAdminLy);
        recyclerViewUser = findViewById(R.id.RecyclerViewUser);
        Button btnLogout=findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLayout.this,Login.class));
            }
        });
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = pass.getText().toString();
                String fullName = fullname.getText().toString();
                String mobile = number.getText().toString();

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

                                    // Thêm mới người dùng vào Firestore với DocumentID đã chỉ định
                                    db.collection("users").document(userId).set(userMap)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("TAG", "DocumentSnapshot added with ID: " + userId);
                                                arrUser.add(new UserModel(userId, userMap.get("avatarUrl").toString(), fullName, email, mobile));
                                                UserListAdminLayoutAdapter adapterUser = new UserListAdminLayoutAdapter(arrUser,AdminLayout.this);
                                                recyclerViewUser.setAdapter(adapterUser);
                                                adapterUser.notifyDataSetChanged();
                                                Toast.makeText(AdminLayout.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("TAG", "Error adding document", e);
                                                Toast.makeText(AdminLayout.this, "Tạo tài khoản thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Log.d("test", "Đăng ký thất bại: ", task.getException());
                                Toast.makeText(AdminLayout.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                layoutCreateUser.setVisibility(View.GONE);
            }
        });

        btnShowCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutCreateUser.setVisibility(View.VISIBLE);
            }
        });

        getuser();
    }

    void getuser() {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                synchronized (arrUser) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String userId = document.getId();
                                        String avatarUrl = document.getString("avatarUrl");
                                        String email = document.getString("email");
                                        String fullName = document.getString("fullName");
                                        String mobile = document.getString("mobile");
                                        arrUser.add(new UserModel(userId, avatarUrl, fullName, email, mobile));
                                    }
                                }
                                runOnUiThread(() -> {
                                    RecyclerView.Adapter adapterUser = new UserListAdminLayoutAdapter(arrUser,AdminLayout.this);
                                    recyclerViewUser.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                    recyclerViewUser.setAdapter(adapterUser);
                                });
                            } else {
                                Log.w("FirestoreData", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onItemLongClick(int position) {
    }
}
