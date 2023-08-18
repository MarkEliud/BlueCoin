package com.krypt.bluecoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.krypt.bluecoin.Main.Deposit;
import com.krypt.bluecoin.Main.Home;
import com.krypt.bluecoin.Main.Profile;
import com.krypt.bluecoin.Main.Send;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    Deposit deposit=new Deposit();
    Send send=new Send();
    Home home=new Home();
    ProgressBar progressBar;
    Profile profile=new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=new ProgressBar(this);
        bottomNavigationView=findViewById(R.id.bottom_navbar);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

            if (id== R.id.home_id)
                getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
                else if(id==R.id.send_id)

                getSupportFragmentManager().beginTransaction().replace(R.id.container,send).commit();
            else if(id==R.id.deposit_id)

                getSupportFragmentManager().beginTransaction().replace(R.id.container,deposit).commit();
            else if(id==R.id.account_id)
                getSupportFragmentManager().beginTransaction().replace(R.id.container,profile).commit();




        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.idlogout){
            logout();



        }else{
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));

        }
        return true;
    }

    private void logout() {
         AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setMessage("Are sure you want exit?")

                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();

                    }
                }) //Set to null. We override the onclick
                .setNegativeButton("No", null)
                .create();
         dialog.show();
    }
}