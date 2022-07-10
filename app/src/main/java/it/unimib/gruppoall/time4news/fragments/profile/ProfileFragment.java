package it.unimib.gruppoall.time4news.fragments.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


import it.unimib.gruppoall.time4news.R;
import it.unimib.gruppoall.time4news.activities.AuthenticationActivity;
import it.unimib.gruppoall.time4news.activities.MainActivity;
import it.unimib.gruppoall.time4news.database.FbDatabase;
import it.unimib.gruppoall.time4news.models.User;

public class ProfileFragment extends Fragment {

    private static final String TAG = "Profile Fragment";

    // recupero l'utente con i relativi dati dal db
    private User theUser;

    // oggetti activity
    private EditText usernameET;
    private TextView username;

    private boolean userDeleted;

    public ProfileFragment() {

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView logout = view.findViewById(R.id.cv_logout);
        CardView deleteaccount = view.findViewById(R.id.cv_deleteaccount);
        usernameET = view.findViewById(R.id.Username);

        userDeleted = false;

        // assegno l'azione di SignOut alla Cardview
        logout.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Intent intent = new Intent(getContext(), AuthenticationActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // associo azione delete
        deleteaccount.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setTitle("Eliminazione account")
                .setMessage("Sei sicuro di voler eliminare il tuo account?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                    FbDatabase.setUserDeletingTrue();

                    Log.d(TAG, "DELETE -> Cancello utente.");
                    deleteAccount();

                    Log.d(TAG, "DELETE -> Sign out.");
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();

                    Log.d(TAG, "DELETE -> Start mainActivity intent.");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                    Log.d(TAG, "DELETE -> Finish sulla vecchia mainActivity.");
                    requireActivity().finish();
                })
                .setNeutralButton(android.R.string.no, null).show());

        Log.d(TAG, "Start Profile");

        // recupero l'user
        Log.d(TAG, "Dentro getUserOnDb()");

        // collego un listener all'user su Db
        FbDatabase.getUserReference().addListenerForSingleValueEvent(postListener);

    }

    private void deleteAccount() {
        userDeleted = true;

        String uid = FbDatabase.getUserAuth().getUid();

        // Cancello credenziali autenticazione
        Log.d(TAG, "Sto cancellando le credenziali di auth.");
        FirebaseUser FBAuthUserToDel = FbDatabase.getUserAuth();
        FBAuthUserToDel
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                    }
                });

        // Cancello dati utente database
        Log.d(TAG, "Sto cancellando i dati del realtime database.");
        FbDatabase.getUserReference().removeValue();

    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
            if (!userDeleted) {
                theUser = dataSnapshot.getValue(User.class);

                // quando leggo l'user da db, chiamo il motodo che inizializza l'activity
                setUp(requireActivity().findViewById(android.R.id.content).getRootView());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };


    private void setUp(View view) {

        // se ho uno user
        if (theUser != null) {

            Log.d(TAG, "theUser: " + theUser.toString());

            username = view.findViewById((R.id.Username));
            TextView email = view.findViewById((R.id.Email));

            // setto i valori
            username.setText(theUser.getUsername());
            setEditUser();
            email.setText(theUser.getEmail());

        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private void setEditUser() {
        // setto l'username
        username.setText(theUser.getUsername());

        // Rimuovo il chack
        usernameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        usernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Rimuovo il chack
                usernameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                    usernameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_username_check_24, 0);
                else
                    usernameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_24, 0);
            }
        });

        usernameET.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (usernameET.getCompoundDrawables()[DRAWABLE_RIGHT] != null)
                    if (event.getRawX() >= (usernameET.getRight() - usernameET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Log.d(TAG, "Premuto check Right");

                        // Rimuovo il check
                        usernameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        Snackbar sbUsernameChange;
                        if (!usernameET.getText().toString().isEmpty()) {
                            updateUsername(usernameET.getText());
                            sbUsernameChange = Snackbar.make(requireView(), "Nome modificato", Snackbar.LENGTH_LONG);
                            theUser.setUsername(usernameET.getText().toString());
                        } else {
                            usernameET.setText(theUser.getUsername());
                            sbUsernameChange = Snackbar.make(requireView(), "Errore: nome vuoto!", Snackbar.LENGTH_LONG);
                        }
                        sbUsernameChange.setAnchorView(R.id.nav_view);
                        sbUsernameChange.show();
                        return true;
                    }
            }
            return false;
        });
    }

    private void updateUsername(Editable newUSername) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(String.valueOf(newUSername))
                .build();

        FbDatabase.getUserAuth().updateProfile(profileUpdates);

        // set the name in the database
        FbDatabase.getUserReference().child("username").setValue(newUSername.toString());

    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
