package it.unimib.gruppoall.time4news.fragments.authentication;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.database.DatabaseReference;

import it.unimib.gruppoall.time4news.models.AuthenticationResponse;
import it.unimib.gruppoall.time4news.repositories.user.IUserRepository;
import it.unimib.gruppoall.time4news.repositories.user.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private static DatabaseReference userReference;
    private MutableLiveData<AuthenticationResponse> mAuthenticationResponseLiveData;

    private final IUserRepository mUserRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.mUserRepository = new UserRepository(application);
    }

    public MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.signInWithEmail(email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<AuthenticationResponse> signUpWithEmail(String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.createUserWithEmail(email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<AuthenticationResponse> signUpWithGoogle(Intent intent) {
        mAuthenticationResponseLiveData = mUserRepository.createUserWithGoogle(intent);
        return mAuthenticationResponseLiveData;
    }

    public void clear() {
        mAuthenticationResponseLiveData.postValue(null);
    }

}

