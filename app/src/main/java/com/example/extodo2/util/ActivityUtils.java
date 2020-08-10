package com.example.extodo2.util;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ActivityUtils {

    public static void addFragmentActivity(FragmentManager fragmentManager, Fragment fragment, int idFrg){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(idFrg, fragment);
        fragmentTransaction.commit();
    }
}
