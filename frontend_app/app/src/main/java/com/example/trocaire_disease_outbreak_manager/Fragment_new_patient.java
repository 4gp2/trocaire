package com.example.trocaire_disease_outbreak_manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Fragment_new_patient extends Fragment implements View.OnClickListener{


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fragment_new_patient, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageButton add_button = root.findViewById(R.id.add_patient_Button);
        add_button.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        Intent con = new Intent(view.getContext(), Patient_details.class);
        view.getContext().startActivity(con);
    }
}
