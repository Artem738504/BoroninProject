package com.example.exampleboronin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<User> listTemp;

    User user11;

    private DatabaseReference mDataBase;
    private String USER_KEY = "User";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        init();
        getDataFromDB();
        setOnClickItem();
        // если поля пустые то
    //    for (int i = 0; i < listTemp.size(); i ++){
    //       user11 = listTemp.get(i);
    //       String name = user11.name;
    //       if ( name.equals(name11)){
               // через интент передаешь user
    //      }
    //  }
    }

    private void init() {
        listView = findViewById(R.id.listView);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(listData.size() > 0)listData.clear();
                if(listTemp.size() > 0)listTemp.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    listData.add(user.name);
                    listTemp.add(user);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDataBase.addValueEventListener(vListener);
    }


    private void setOnClickItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //сохраняем в интент

                User user = listTemp.get(position);
                Intent i = new Intent(ReadActivity.this, ShowActivity.class);
                i.putExtra(Constant.USER_NAME,user.name);
                i.putExtra(Constant.USER_AGE,user.age);
                i.putExtra(Constant.USER_SEC_NAME,user.secName);
                startActivity(i);
            }
        });
    }
}