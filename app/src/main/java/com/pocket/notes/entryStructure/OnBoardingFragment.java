package com.pocket.notes.entryStructure;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.pocket.notes.R;
import com.pocket.notes.viewModels.OnBoardingViewModel;
import com.pocket.notes.models.LoginModel;
import com.pocket.notes.networkingStructure.NetworkingCalls;
import com.pocket.notes.prefrences.LoginPreference;


public class OnBoardingFragment extends Fragment {


    public static OnBoardingViewModel onBoardingViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private int SIGN_IN_CODE = 1;

    LoginPreference loginPreference;
    SignInButton signInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBoardingViewModel = new ViewModelProvider(this).get(OnBoardingViewModel.class);
        loginPreference = new LoginPreference(getContext());

        signInButton = view.findViewById(R.id.sign_in_button);


        final Observer<LoginModel> loginObserver = new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                loginPreference.setLoggedIn(true);
                loginPreference.setEmail(loginModel.getEmail());
                loginPreference.setName(loginModel.getName());
                loginPreference.setId(loginModel.getId());
                Navigation.findNavController(getView()).navigate(R.id.action_onBoardingFragment_to_mainFragment);
            }
        };
        onBoardingViewModel.getLoginLiveData().observe(getViewLifecycleOwner(), loginObserver);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        setGooglePlusButtonText(signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    protected void setGooglePlusButtonText(SignInButton signInButton) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Login to Pocket Notes");
                return;
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.e("google signin success", account.getDisplayName()+ ":" + account.getEmail());
            new NetworkingCalls(getContext()).loginRequest(account.getEmail() , account.getDisplayName());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("google signin failed", "signInResult:failed code=" + e.getStatusCode());

        }
    }
}