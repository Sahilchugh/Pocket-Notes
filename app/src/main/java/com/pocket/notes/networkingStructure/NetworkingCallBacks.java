package com.pocket.notes.networkingStructure;

import androidx.annotation.Nullable;

public interface NetworkingCallBacks {

    <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error);

    enum MethodType {
        login, getNotes,editNotes,addNote
    }

}
