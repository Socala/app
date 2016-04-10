package socala.app.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

import retrofit2.Response;
import socala.app.R;
import socala.app.contexts.AppContext;
import socala.app.models.User;
import socala.app.services.ISocalaService;
import socala.app.services.SocalaClient;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String CLIENT_ID = "254021896940-jc5hg452sa8j7uacr5q82hu2b7lg8fup.apps.googleusercontent.com";
    private final ISocalaService service = SocalaClient.getClient();
    private final AppContext appContext = AppContext.getInstance();
    private GoogleApiClient googleApiClient;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        // TODO: Move server client id to a constants file that isn't checked in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(SignInActivity.CLIENT_ID)
                .build();

        // Create Client to authenticate
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set on click listener for sign in button
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        // TODO: Uncomment when not testing

        if (!result.isSuccess()) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Authenticated Failed", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        GoogleSignInAccount acct = result.getSignInAccount();

        try {
            Response<User> response = service.getUser(acct.getIdToken()).execute();
            appContext.setUser(response.body());

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();

        } catch (IOException e) {
            // TODO: Handle failure
            e.printStackTrace();
        }
    }
}
