package com.example.exampleboronin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    private Button bStart,bSignUp,bSignIn,bSignOut;
    private TextView tv_userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Регистрация");

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser!= null){
           showSigned();
            String userName = "Вы вошли как : "+cUser.getEmail();
            tv_userName.setText(userName);

        }
        else{
           notSigned();

        }
    }
        //инициализация переменных
    private void init(){
        tv_userName = findViewById(R.id.tv_userEmail);
        bSignOut = findViewById(R.id.b_signOut);
        bStart = findViewById(R.id.b_Start);
        bSignIn = findViewById(R.id.b_signIn);
        bSignUp = findViewById(R.id.b_signUp);
        edLogin = findViewById(R.id.editEmail);
        edPassword = findViewById(R.id.editPassword);
        mAuth = FirebaseAuth.getInstance();
    }
        //кнопка Зарегистрироваться
    public void onClickSignUp(View view){
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()))
        {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(),edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        sendEmail();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "При регистрации произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
        }
    }
        //кнопка Войти
    public void onClickSignIn(View view){
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())){
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(),edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "User SignIn failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
        //кнопка "Выход"
    public void onClickSignOut(View view){
        FirebaseAuth.getInstance().signOut();
        notSigned();
    }
        //если пользователь зарегистрирован
    private void showSigned(){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if(user.isEmailVerified()) {
            String userName = "Вы вошли как : "+user.getEmail();
            tv_userName.setText(userName);
            bStart.setVisibility(View.VISIBLE);
            tv_userName.setVisibility(View.VISIBLE);
            bSignOut.setVisibility(View.VISIBLE);
            edLogin.setVisibility(View.GONE);
            edPassword.setVisibility(View.GONE);
            bSignUp.setVisibility(View.GONE);
            bSignIn.setVisibility(View.GONE);
        }
        else{
            Toast.makeText(getApplicationContext(), "Подтвердите свой Email", Toast.LENGTH_SHORT).show();
        }
    }
        //если пользователь еще не зарегистрирован
    private void notSigned(){
        bStart.setVisibility(View.GONE);
        bSignOut.setVisibility(View.GONE);
        tv_userName.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
    }
        //код для перехода на основную активити
    public void onClickStart(View view){
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
    }
        //код для отправки кода подтверждения на электронную почту
    private void sendEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Подтвердите ваш Email", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Возникла непредвиденная ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}