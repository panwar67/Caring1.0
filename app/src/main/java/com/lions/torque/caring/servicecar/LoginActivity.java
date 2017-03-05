package com.lions.torque.caring.servicecar;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lions.torque.caring.R;
import com.lions.torque.caring.sessions_manager.SessionManager;

import java.util.Arrays;

import lj_3d.gearloadinglayout.gearViews.TwoGearsLayout;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    ImageView login_with_google, login_with_facebook;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_with_google = (ImageView)findViewById(R.id.sign_in_google);
        login_with_facebook =   (ImageView)findViewById(R.id.sign_in_facebook);

        session = new SessionManager(getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        FacebookSdk.sdkInitialize(getApplicationContext());
       // AppEventsLogger.activateApp(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {


                    if(session.createLoginSession(user.getEmail(),user.getDisplayName(),user.getUid(),user.getPhotoUrl().toString()))
                    {   Log.d("details",""+user.getEmail()+""+user.getPhotoUrl().toString()+""+ user.getDisplayName());
                        Intent intent = new Intent(LoginActivity.this,Home_Screen.class);
                        startActivity(intent);
                        finish();
                    }
                    // User is signed in
                    Log.d("firebase_state", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("firebase_state", "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        mCallbackManager = CallbackManager.Factory.create();

        login_with_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile","email"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        Log.d("inside_token",""+loginResult.getAccessToken().toString());
                        ProfileTracker profileTracker;

                        //                    GraphRequest.newMeRequest()

                        if (Profile.getCurrentProfile()==null)
                        {
                            profileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                                    this.stopTracking();
                                    Profile.setCurrentProfile(currentProfile);
                                    com.facebook.Profile profile = Profile.getCurrentProfile();

                                    Log.d("facebook_name_null",""+profile.getFirstName().toString());

                                    Log.d("facebook_dp_null",""+profile.getProfilePictureUri(400,400).toString());
                                    Log.d("facebook_tok_null", "" +  loginResult.getAccessToken().toString());
                                    Log.d("facebook_id_null", "" +  loginResult.getAccessToken().getUserId().toString());
                                    Log.d("facebook_pid_null", "" +  profile.getId().toString());
                                    Log.d("facebook_spl_null", "" +  profile.getLinkUri().toString());
                                    Log.d("facebook_spn_null", "" +  profile.getName().toString());
                                    handleFacebookAccessToken(loginResult.getAccessToken());

                                    //handleFacebookAccessToken(loginResult.getAccessToken(),loginResult,profile.getName(),profile.getProfilePictureUri(400,400).toString());
                                }
                            };
                            profileTracker.startTracking();
                        }
                        else {
                            Profile pro = Profile.getCurrentProfile();
                            loginResult.getRecentlyGrantedPermissions();
                            AccessToken accessToken = loginResult.getAccessToken();
                            Log.d("facebook_details", "" + loginResult.getRecentlyGrantedPermissions().toString());
                            Log.d("facebook_details",""+pro.getFirstName().toString());
                            Log.d("facebook_details",""+pro.getProfilePictureUri(400,400).toString());
                            Log.d("facebook_details", "" + accessToken.getUserId().toString());
                            Log.d("facebook_detailspid", "" +  pro.getId().toString());
                            Log.d("facebook_detailspl", "" +  pro.getLinkUri().toString());
                            Log.d("facebook_detailspn", "" +  pro.getName().toString());
                            handleFacebookAccessToken(loginResult.getAccessToken());

                            //    handleFacebookAccessToken(loginResult.getAccessToken(),loginResult,pro.getName(),pro.getProfilePictureUri(400,400).toString());
                        }

                    }

                    @Override
                    public void onCancel() {

                        Log.d("cancelled","cancel");


                    }

                    @Override
                    public void onError(FacebookException error) {

                        Toast.makeText(getApplicationContext(),""+error.toString(),Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        login_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });


        // Configure Google Sign In


        TwoGearsLayout threeGearsLayout = new TwoGearsLayout(this);
        threeGearsLayout.setFirstGearColor(Color.WHITE);
        threeGearsLayout.setSecondGearColor(Color.RED);
        threeGearsLayout.setDialogBackgroundColor(Color.GREEN);
        threeGearsLayout.setDialogBackgroundAlpha(0.3f);
        threeGearsLayout.blurBackground(true);
        threeGearsLayout.enableCutLayout(false);
        threeGearsLayout.setCutRadius(80);
        threeGearsLayout.start();



    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       // mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                Log.d("gmail_id",account.getEmail());
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(getApplicationContext(),"Couldn't fetch your google account",Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
        else
        {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("google_firebase", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
       // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("google_firebase", "signInWithCredential:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {

                            Log.w("google_firebase", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Google Authentication failed. Try again.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else
                        {

                            Log.d("google_logged_in",acct.getEmail());

                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_SHORT).show();

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook_token", "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("facebook_task", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("facebook_task", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.d("handle_facebook_details",""+mAuth.getCurrentUser().getEmail());
                            Log.d("handle_facebook_details",""+mAuth.getCurrentUser().getUid());
                            Log.d("handle_facebook_details"," "+mAuth.getCurrentUser().getDisplayName()+" dp "+mAuth.getCurrentUser().getPhotoUrl());

                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }





}
