package com.krypt.bluecoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottom_navbar);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

            if (id== R.id.account_id)
                Toast.makeText(getApplicationContext(), "my acount", Toast.LENGTH_SHORT).show();
                else if(id==R.id.pay_id)

                Toast.makeText(getApplicationContext(), "add card and pay", Toast.LENGTH_SHORT).show();
            else if(id==R.id.message_id)
                Toast.makeText(getApplicationContext(), "Messages and notifications", Toast.LENGTH_SHORT).show();




        return true;
    }
}