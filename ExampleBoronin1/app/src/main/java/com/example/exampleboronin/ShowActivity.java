package com.example.exampleboronin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

public class ShowActivity extends AppCompatActivity {
    private TextView tvName, tvSecName, tvAge;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        init();
        getIntentMain();
    }
    private void init()
    {
        tvName = findViewById(R.id.tvName);
        tvSecName = findViewById(R.id.tvSecName);
        tvAge = findViewById(R.id.tvAge);
    }
    private void getIntentMain()
    {
        Intent i = getIntent();
        if(i != null)
        {
            tvName.setText(i.getStringExtra(Constant.USER_NAME));
            tvSecName.setText(i.getStringExtra(Constant.USER_SEC_NAME));
            tvAge.setText(i.getStringExtra(Constant.USER_AGE));
        }
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