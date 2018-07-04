package ru.startandroid.weather.ui.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity{

    protected void changeFragment(Fragment f, @IdRes int containerId, boolean addBackStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction().replace(containerId, f);
        if(addBackStack){
            transaction.addToBackStack("null");
        }
        transaction.commit();
        fm.executePendingTransactions();
    }

   protected void changeFragment(Fragment f, @IdRes int containerId){
        changeFragment(f, containerId, false);
   }
}
