package com.pocket.notes.homeStructure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pocket.notes.R;
import com.pocket.notes.database.NotesDataDB;
import com.pocket.notes.database.NotesDataDao;
import com.pocket.notes.networkingStructure.NetworkingCallBacks;
import com.pocket.notes.networkingStructure.NetworkingCalls;

public class EditNotesFragment extends Fragment implements NetworkingCallBacks {

    EditText enterTitle,enterDescription;
    Button update;
    NetworkingCalls networkingCalls;
    String title,description;
    int id;
    View view1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enterTitle = view.findViewById(R.id.enterTitle);
        enterDescription = view.findViewById(R.id.enterDescription);
        update = view.findViewById(R.id.update);
        networkingCalls = new NetworkingCalls(getContext(),this);
        view1 = view;

        if (getArguments() != null) {

            title = getArguments().getString("title","");
            description = getArguments().getString("description","");
            id = getArguments().getInt("id",0);

            enterTitle.setText(title);
            enterDescription.setText(description);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                networkingCalls.updateNotes(enterTitle.getText().toString(),enterDescription.getText().toString(),String.valueOf(id));

                NotesDataDao notesDataDao = NotesDataDB.getInstance(getContext()).notesDataDao();
                Log.e("id", ""+id);
                notesDataDao.updateNote(enterTitle.getText().toString(),enterDescription.getText().toString(),id);

            }
        });

    }

    @Override
    public <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error) {

        if (type==MethodType.editNotes && status){
            Navigation.findNavController(view1).navigate(R.id.action_editNotesFragment_to_mainFragment);
        }
        else if (type==MethodType.editNotes && !status){
            Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}