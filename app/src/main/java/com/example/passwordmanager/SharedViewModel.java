package com.example.passwordmanager;

import androidx.lifecycle.ViewModel;

// Create a ViewModel class
public class SharedViewModel extends ViewModel {
    private String myVariable = "first_state";

    public String getMyVariable() {
        return myVariable;
    }

    public void setMyVariable(String value) {
        myVariable = value;
    }
}
