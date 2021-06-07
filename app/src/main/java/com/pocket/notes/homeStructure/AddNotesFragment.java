package com.pocket.notes.homeStructure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pocket.notes.R;
import com.pocket.notes.database.NotesDataDB;
import com.pocket.notes.database.NotesDataDao;
import com.pocket.notes.databaseEntity.NotesData;
import com.pocket.notes.networkingStructure.NetworkingCallBacks;
import com.pocket.notes.networkingStructure.NetworkingCalls;


public class AddNotesFragment extends Fragment implements NetworkingCallBacks {

    EditText enterTitle,enterDescription;
    Button submit;
    NetworkingCalls networkingCalls;
    View view1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enterTitle = view.findViewById(R.id.enterTitle);
        enterDescription = view.findViewById(R.id.enterDescription);
        submit = view.findViewById(R.id.submit);
        networkingCalls = new NetworkingCalls(getContext(),this);
        view1 = view;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInput()){

                    networkingCalls.addNote(enterTitle.getText().toString(),enterDescription.getText().toString());

                }
            }
        });

    }

    public boolean checkInput(){

        if (enterTitle.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Please enter title!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (enterDescription.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Please enter description!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error) {

        if (type==MethodType.addNote && status){
            Navigation.findNavController(view1).navigate(R.id.action_addNotesFragment_to_mainFragment);
        }
        else if (type==MethodType.addNote && !status){
            Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}