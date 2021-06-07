package com.pocket.notes.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pocket.notes.databaseEntity.NotesData;
import com.pocket.notes.models.LoginModel;
import com.pocket.notes.models.NotesModel;

import java.util.ArrayList;

public class NotesViewModel extends ViewModel {

    MutableLiveData<ArrayList<NotesData>> notesLiveData;

    public MutableLiveData<ArrayList<NotesData>> getNotesLiveData() {
        if (notesLiveData == null) {
            notesLiveData = new MutableLiveData<ArrayList<NotesData>>();
        }
        return notesLiveData;
    }


}
