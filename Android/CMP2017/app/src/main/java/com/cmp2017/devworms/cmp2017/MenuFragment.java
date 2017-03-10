package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MenuFragment extends Fragment {


    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ImageView imgbtnProgramas = (ImageView) view.findViewById(R.id.imagbtnPrograma);


        imgbtnProgramas.setOnClickListener(new SecProgram());
        return view;



    }

    class SecProgram implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ProgramFragment()).commit();


        }
    }

}
