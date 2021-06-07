package com.pocket.notes.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pocket.notes.models.LoginModel;

public class OnBoardingViewModel extends ViewModel {

    private MutableLiveData<LoginModel> loginLiveData;

    public MutableLiveData<LoginModel> getLoginLiveData() {
        if (loginLiveData == null) {
            loginLiveData = new MutableLiveData<LoginModel>();
        }
        return loginLiveData;
    }

}
