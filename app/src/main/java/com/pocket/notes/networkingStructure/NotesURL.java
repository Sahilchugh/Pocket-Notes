package com.pocket.notes.networkingStructure;

public class NotesURL {
    private String BASE_URL="BASE_URL";

    private String LOGIN = BASE_URL + "/api/pocketNotes/pocketNotesLogin.php";

    private String ADD_NOTES = BASE_URL+"/api/pocketNotes/createNote.php";

    private String GET_NOTES = BASE_URL+"/api/pocketNotes/getNotes.php?u=";

    private String DELETE_NOTES = BASE_URL+"/api/pocketNotes/deletetNote.php?id=";

    private String EDIT_NOTE = BASE_URL+"/api/pocketNotes/updateNote.php";

    public String getDELETE_NOTES() {
        return DELETE_NOTES;
    }

    public String getGET_NOTES() {
        return GET_NOTES;
    }

    public String getADD_NOTES() {
        return ADD_NOTES;
    }

    public String getLOGIN() {
        return LOGIN;
    }

    public String getEDIT_NOTE() {
        return EDIT_NOTE;
    }
}
