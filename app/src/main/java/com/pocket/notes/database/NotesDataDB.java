package com.pocket.notes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.pocket.notes.databaseEntity.NotesData;

@Database(entities = NotesData.class, version = 1)
public abstract class NotesDataDB extends RoomDatabase {

    private static final String DATABASE_NAME = "notes_data";
    private static NotesDataDB instance;

    public static NotesDataDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, NotesDataDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract NotesDataDao notesDataDao();

}
