package com.kyl.zflix.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.kyl.zflix.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editEmail, editPassword;
    private Button btnRegister;
    private ImageButton btnBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        // 회원가입 버튼
        btnRegister.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                            finish(); // 회원가입 완료 후 현재 액티비티 종료
                        } else {
                            String errorMessage = "회원가입 실패: " + task.getException().getMessage();
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // 🔹 뒤로가기 버튼 → 단순히 현재 액티비티 닫기
        btnBack.setOnClickListener(v -> finish());
    }
}
