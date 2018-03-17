package com.android.flashbackmusic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.Task;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
//import com.google.api.services.people.v1.People;
//import com.google.api.services.people.v1.PeopleScopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    //private static final int SIGN_IN_CODE = ;
    GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences prefs;
    private SharedPrefsIO prefsIO;

    public static int RC_SIGN_IN = 0;
    public static final String TAG = "SIGNIN EXC";
    GoogleSignInAccount account;
    String clientId = "747230320321-22kvt6fp6knhd6m8enqh9qb1t1av6eib.apps.googleusercontent.com";
    String clientSecret = "fV409tj8jn6Ew53E859IBg70";
    String redirectUrl = "";
    String code;
    String accessToken;
    String refreshToken;
    Long expiresInSec;
    GoogleTokenResponse tokenResponse;
    GoogleCredential credential;

    /*private GoogleApiClient google_api_client;
    private GoogleApiAvailability google_api_availability;
    private ConnectionResult connection_result;
    private ProgressDialog progress_dialog;
    private int request_code;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.d("in onCreate()", "test");

        prefs = getSharedPreferences("info", MODE_PRIVATE);
        prefsIO = new SharedPrefsIO(prefs);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(clientId)
                .requestIdToken(clientId)
                .requestScopes(new Scope(Scopes.PROFILE),
                        new Scope("https://www.googleapis.com/auth/contacts.readonly"))
                        //new Scope(Scopes.CONTACTS_READONLY))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            Log.d("HANDLER", "account - " + account.toString());

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("in onStart()", "test2");
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            if (!GoogleSignIn.hasPermissions(account, new Scope(Scopes.PROFILE), new Scope("https://www.googleapis.com/auth/contacts.readonly"))) {
                Log.d("REQ", "permissions");
                GoogleSignIn.requestPermissions(this, RC_SIGN_IN, account, new Scope(Scopes.PROFILE), new Scope("https://www.googleapis.com/auth/contacts.readonly"));
            }
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
        }
        updateUI(account);
    }

    protected void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            // display Sign-In button
            Log.i("NULL", "must sign in");

        } else {
            // launch MainActivity
            // setResult?
            // Log.d("in updateUI()", "FINISHING");
            try {
                Log.i("SETTING UP", "entering");
                String testCode = account.getServerAuthCode();
                if (testCode != null) {
                    code = testCode;
                    setUpNew(account);
                }
                else {
                    setUpReturning(account);
                }
            } catch (IOException e) {
                e.getStackTrace();
                Log.d("Exception", e.getMessage());
            }
            finish();
        }
    }

    private void setUpNew(GoogleSignInAccount account) throws IOException, NullPointerException {
        Log.i("IN SETUP NEW", "test");
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        try {
            //code = account.getServerAuthCode();
            Log.d("CODE", "val - " + code);

            // Step 2: Exchange --> TODO needs to persist
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                            httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                            .execute();
            // End of Step 2 <--

            Log.d("TOKEN", "val - " + tokenResponse);
            prefsIO.saveTokenResponse(tokenResponse);

            Log.d("SAVED TOK", "val - " + prefsIO.getTokenResponse());

            /*accessToken = tokenResponse.getAccessToken();
            refreshToken = tokenResponse.getRefreshToken();
            expiresInSec = tokenResponse.getExpiresInSeconds();*/

            credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setFromTokenResponse(tokenResponse);

            PeopleService peopleService =
                    new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

            // Set Profile
            Person profile = peopleService.people().get("people/me")
                    .setPersonFields("names,emailAddresses")
                    .execute();

            Log.i("NEW PROFILE", "reached - " + profile.toString());

            // Get Connections
            ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                    .setPersonFields("names,emailAddresses")
                    .execute();
            List<Person> connections = response.getConnections();

            Log.i("NEW CONNECTIONS", "reached - " + connections);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpReturning(GoogleSignInAccount account) throws IOException, NullPointerException {
        Log.i("IN SETUP RETURNING", "test");
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        /*tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                .execute();*/

        tokenResponse = prefsIO.getTokenResponse();
        Log.d("TOKEN", "val - " + tokenResponse);

        /*credential.setAccessToken(tokenResponse.getAccessToken())
                .setRefreshToken(tokenResponse.getRefreshToken())
                .setExpiresInSeconds(tokenResponse.getExpiresInSeconds());*/

        credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

        // Set Profile
        Person profile = peopleService.people().get("people/me")
                .setPersonFields("names,emailAddresses")
                .execute();

        Log.i("RETURNING PROFILE", "reached - " + profile.toString());

        // Get Connections
        ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                .setPersonFields("names,emailAddresses")
                .execute();
        List<Person> connections = response.getConnections();

        Log.i("RETURNING CONNECTIONS", "reached - " + connections);

        /*tokenResponse.getAccessToken();
        tokenResponse.getRefreshToken();
        tokenResponse.getExpiresInSeconds();*/

    }

    /*private void buildNewGoogleApiClient() {
        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        getProfileInfo();
        changeUI(true);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
            return;
        }
        if (!is_intent_inprogress) {
            connection_result = result;
            if (is_signInBtn_clicked) {
                resolveSignInError();
            }
        }
    }

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            progress_dialog.show();
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();
        changeUI(false);
    }

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
            changeUI(false);
        }
    }

    private void gPlusRevokeAcces() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Log.d("MainActivity", "User access revoked!");
                            buildNewGoogleApiClient();
                            google_api_client.connect();
                            changeUI(false);
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sign_in_button:
                Toast.makeText(this, "start sign process", Toast.LENGTH_SHORT).show();
                gPlusSignIn();
                break;
            case R.id.sign_out_button:
                Toast.makeText(this, "sign out from G+", Toast.LENGTH_LONG).show();
                gPlusSignOut();
                break;
            case R.id.disconnect_button:
                Toast.makeText(this, "Revoke access from G+", Toast.LENGTH_LONG).show();
                gPlusRevokeAcces();
                break;
        }
    }
    private void changeUI(boolean signedIn) {
        if (signedIn) {
            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            finish();
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }*/
}
