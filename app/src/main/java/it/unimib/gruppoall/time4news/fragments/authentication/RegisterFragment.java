package it.unimib.gruppoall.time4news.fragments.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.gruppoall.time4news.R;
import it.unimib.gruppoall.time4news.models.User;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private UserViewModel mUserViewModel;
    private DatabaseReference userRef;
    private FirebaseUser mUser;
    private String userID;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        final EditText editTextEmail = view.findViewById(R.id.email);
        final EditText editTextPassword = view.findViewById(R.id.password);
        final EditText editTextNomeCognome = view.findViewById(R.id.nome_cognome);


        final Button buttonRegister = view.findViewById(R.id.register_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String username = editTextNomeCognome.getText().toString();
                mUserViewModel.signUpWithEmail(email, password).observe(getViewLifecycleOwner(), authenticationResponse -> {
                    if (authenticationResponse != null) {
                      if (authenticationResponse.isSuccess()) {
                           saveUserIntoDatabase(username, email);
                            Navigation.findNavController(v).navigate(R.id.loginFragment);
                        } else {
                            updateUIForFailure(authenticationResponse.getMessage());
                        }
                    }
                });
            }
        });

        final TextView toLoginFragment = view.findViewById(R.id.to_login_frag);
        toLoginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        return view;
    }

    private void updateUIForFailure(String message) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT).show();
        mUserViewModel.clear();
    }

    private void saveUserIntoDatabase(String nomeCognome, String email){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("users/"+mUser.getUid());
        userID = mUser.getUid();
        User user = new User(nomeCognome, email);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRef.child(userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Utente Creato", Toast.LENGTH_SHORT).show();
                        userRef.setValue(user);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

