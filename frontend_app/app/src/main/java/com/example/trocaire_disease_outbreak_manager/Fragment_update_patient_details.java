package com.example.trocaire_disease_outbreak_manager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class Fragment_update_patient_details extends Fragment implements View.OnClickListener{
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fragment_update_patient_details, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ImageButton save = root.findViewById(R.id.imageButtonsave);
        save.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        Intent con = new Intent(view.getContext(), Update_patient_details.class);
        view.getContext().startActivity(con);
    }
}
