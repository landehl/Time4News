package it.unimib.gruppoall.time4news.repositories.user;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import it.unimib.gruppoall.time4news.models.AuthenticationResponse;


public interface IUserRepository {
    MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password);
    MutableLiveData<AuthenticationResponse> createUserWithGoogle(Intent data);
    MutableLiveData<AuthenticationResponse> createUserWithEmail(String email, String password);

}
