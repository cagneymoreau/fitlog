package com.cagneymoreau.fitlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cagneymoreau.fitlog.logic.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: 5/17/2021 split screen button
// TODO: 6/16/2021 alternating workouts or split days should be single advice but need to be handled anyways

/**
 * 05/10/21
 *
 *
 */

public class MainActivity extends AppCompatActivity {

    Controller controller;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        buildDisplayDataBase();

    }


    //load the controller and once done display the menu
    //this gaurantees are controller class is built in advance of the menu
    private void buildDisplayDataBase()
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            controller = new Controller(this);
            handler.post(() -> {
                setContentView(R.layout.activity_main);

            });
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        openMenuItem(id);
        return super.onOptionsItemSelected(item);



    }





    // UI access to controller for data etc
    public Controller getConroller()
    {
        return controller;
    }




    private void openMenuItem(int id)
    {
        //noinspection SimplifiableIfStatement
        if (id == R.id.settings_menu) {

            view = getWindow().getDecorView().getRootView();
            Navigation.findNavController(this, R.id.fragment).navigate(R.id.action_global_settings_Fragment);

        }
        else if(id == R.id.data_backup_menu)
        {
            view = getWindow().getDecorView().getRootView();
            Navigation.findNavController(this, R.id.fragment).navigate(R.id.action_global_dataBackup);

        }
        else if(id == R.id.trophies_menu)
        {
            view = getWindow().getDecorView().getRootView();
            Navigation.findNavController(this, R.id.fragment).navigate(R.id.action_global_trophies);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
