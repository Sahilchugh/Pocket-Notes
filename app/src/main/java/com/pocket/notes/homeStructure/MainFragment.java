package com.pocket.notes.homeStructure;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pocket.notes.R;
import com.pocket.notes.adapter.RecyclerAdapterNotes;
import com.pocket.notes.database.NotesDataDB;
import com.pocket.notes.database.NotesDataDao;
import com.pocket.notes.databaseEntity.NotesData;
import com.pocket.notes.models.LoginModel;
import com.pocket.notes.models.NotesModel;
import com.pocket.notes.networkingStructure.NetworkingCalls;
import com.pocket.notes.prefrences.LoginPreference;
import com.pocket.notes.viewModels.NotesViewModel;
import com.pocket.notes.viewModels.OnBoardingViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainFragment extends Fragment {


    LoginPreference loginPreference;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addNote;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText search;

    Button logout;

    public static NotesViewModel notesViewModel;


    // TODO: Rename and change types of parameters
    RecyclerAdapterNotes recyclerAdapterNotes;

    TextView name, greetings;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.search);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayoutRefresh);
        loginPreference = new LoginPreference(getContext());
        logout = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.name);
        greetings = view.findViewById(R.id.greetings);
        addNote = view.findViewById(R.id.addNote);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        name.setText("Hey " + loginPreference.getName() + ",");

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        loginPreference = new LoginPreference(getContext());
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                recyclerAdapterNotes.getFilter().filter(editable);
            }
        });

        final Observer<ArrayList<NotesData>> loginObserver = new Observer<ArrayList<NotesData>>() {
            @Override
            public void onChanged(ArrayList<NotesData> notesModels) {
                recyclerAdapterNotes = new RecyclerAdapterNotes(getContext(), notesModels, getActivity());
                recyclerView.setAdapter(recyclerAdapterNotes);
//                Navigation.findNavController(getView()).navigate(R.id.action_onBoardingFragment_to_mainFragment);

            }
        };

        notesViewModel.getNotesLiveData().observe(getViewLifecycleOwner(), loginObserver);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new NetworkingCalls(getContext()).getNotes(new LoginPreference(getContext()).getId(), 1);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        NotesDataDao notesDataDao = NotesDataDB.getInstance(getContext()).notesDataDao();
        logout.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Warning!!")
                    .setMessage("Do you really want to logout, you will be exited from the app")
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build();

                            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                        }
                                    });
                            notesDataDao.removeNotesData();
                            loginPreference.removeLoginPreference();
                            getActivity().finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        List<NotesData> notesDataList = notesDataDao.getNotes();
        ArrayList<NotesData> notesData = new ArrayList<>(notesDataList);
        notesViewModel.getNotesLiveData().setValue(notesData);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addNotesFragment);
            }
        });

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            greetings.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetings.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greetings.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greetings.setText("Good Night");
        }

    }
}