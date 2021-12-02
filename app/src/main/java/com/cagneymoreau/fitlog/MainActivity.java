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

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.cagneymoreau.fitlog.logic.Controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



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
    //this gaurantees our controller class is built in advance of the menu
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
       if(id == R.id.data_backup_menu)
        {
            view = getWindow().getDecorView().getRootView();
            Navigation.findNavController(this, R.id.fragment).navigate(R.id.action_global_dataBackup);

        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();


        controller.saveUserToDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (controller != null){
            controller.getSubscription().checkPurchase();
        }
    }


    //endregion



}
