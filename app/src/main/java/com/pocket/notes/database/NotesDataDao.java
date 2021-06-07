package com.pocket.notes.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pocket.notes.databaseEntity.NotesData;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NotesDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotesData(NotesData notesData);

    @Query("DELETE FROM notes")
    void removeNotesData();

    @Query("DELETE FROM notes WHERE id = :id")
    void removeNotesById(int id);

    @Query("SELECT * FROM notes")
    List<NotesData> getNotes();

    @Query("UPDATE notes SET title = :title, description = :description WHERE id =:id")
    void updateNote(String title,String description,int id );




}
