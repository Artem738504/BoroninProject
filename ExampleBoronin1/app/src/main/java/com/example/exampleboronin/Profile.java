package com.example.exampleboronin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class Profile extends AppCompatActivity {
    private Button bName, bSecName,bAge, bMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Личный кабинет");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        Intent i  = getIntent();
        String bSecName1 =  i.getStringExtra("NewUsersecName");
        String bName1 =  i.getStringExtra("NewUsername");
        String bAge1 =  i.getStringExtra("NewUserage");


        bSecName.setText(bAge1);
        bName.setText(bName1);
        bAge.setText(bSecName1+" лет");


            //переход на редактирование профиля
        Button b2 = (Button)findViewById(R.id.button_edit);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Profile.this, PersonalData.class );
                startActivityForResult(intent1,1);
            }
        });
    }

        //инициализируем переменные
    private void init(){
        bName = findViewById(R.id.bName);
        bSecName = findViewById(R.id.bSecName);
        bAge = findViewById(R.id.bAge);
    }
        //восстанавливаем данные
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String name = savedInstanceState.getString("name1");
        String secName = savedInstanceState.getString("secName1");
        String age1 = savedInstanceState.getString("age1");
    }
    public void onClickMain(View view){
        Intent intent = new Intent(Profile.this, MainActivity.class);
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