package com.callioni.peter.fieldlogger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Profile_Fragment extends Fragment {
    private String username; //TODO stuff with these in next assignment
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button saveBtn = (Button) getActivity().findViewById(R.id.saveProfileBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText usernameField = (EditText) getActivity().findViewById(R.id.enterUsername);
                EditText passwordField = (EditText) getActivity().findViewById(R.id.enterPassword);
                EditText repeatPasswordField = (EditText) getActivity().findViewById(R.id.enterRepeatPassword);

                if (!usernameField.getText().toString().isEmpty()) {
                    if (!passwordField.getText().toString().isEmpty()) {
                        if (!repeatPasswordField.getText().toString().isEmpty()) {
                            if (passwordField.getText().toString().equals(repeatPasswordField.getText().toString())) {
                                MainActivity.username = usernameField.getText().toString();
                                password = passwordField.getText().toString();
                                Toast.makeText(getActivity().getBaseContext(), "Profile saved", Toast.LENGTH_SHORT).show();
                                Home_Fragment home_fragment = new Home_Fragment();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, home_fragment).commit();
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "Passwords do not match, try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Please type password again", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Please enter username", Toast.LENGTH_SHORT).show();
                }
                }
        });
        Button cancelBtn = (Button) getActivity().findViewById(R.id.cancelProfileBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Home_Fragment home_fragment = new Home_Fragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, home_fragment).commit();
            }
        });
    }
}
