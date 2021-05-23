package com.example.exampleboronin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class PersonalData extends AppCompatActivity {
    private EditText eName, eSecName, eAge;
    private DatabaseReference myDataBase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        getSupportActionBar().setTitle("Настройки");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }
        //инициализация переменных
    public void init(){
        eName = findViewById(R.id.editName);
        eSecName = findViewById(R.id.editYears);
        eAge = findViewById(R.id.editSecName);
        myDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
        //кнопка схранить
    public void onClickSave(View view){
        String id = myDataBase.getKey();
        String secName = eSecName.getText().toString();
        String name = eName.getText().toString();
        String age = eAge.getText().toString();
        User newYouser = new User(id,name,secName,age);
        myDataBase.push().setValue(newYouser);

        // передаем через интент имя пользователя
        Intent intent = new Intent(PersonalData.this,Profile.class);
        intent.putExtra("NewUsersecName", secName);
        intent.putExtra("NewUsername", name);
        intent.putExtra("NewUserage", age);
        startActivity(intent);

    }

    // кнопка назад
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}