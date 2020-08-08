package com.example.facebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private ProfilePictureView imgProfilePictureView;
    private LoginButton loginButton;
    private Button btnLogout;
    private TextView tvName;
    private TextView tvEmaill;
    private TextView tvFirstName;
    private Button btnFunctionFaceBook;
    CallbackManager callbackManager;

    String id, name, firstName, email;

    private void init() {
        imgProfilePictureView = (ProfilePictureView) findViewById(R.id.imgProfilePictureView);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmaill = (TextView) findViewById(R.id.tvEmaill);
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        btnFunctionFaceBook = (Button) findViewById(R.id.btnFunctionFaceBook);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        init();
        callbackManager = CallbackManager.Factory.create();
        tvName.setVisibility(View.INVISIBLE);
        tvEmaill.setVisibility(View.INVISIBLE);
        tvFirstName.setVisibility(View.INVISIBLE);
        btnFunctionFaceBook.setVisibility(View.INVISIBLE);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        // If you are using in a fragment, call loginButton.setFragment(this);
        setLoginButton();
        setLogOutButton();
        btnFunctionFaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screenSwitching();
            }
        });

    }

    private void screenSwitching() {
        Intent intent = new Intent(MainActivity.this, FunctionFBActivity.class);
        startActivity(intent);
    }

    private void setLogOutButton() {
        LoginManager.getInstance().logOut();
        btnLogout.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.INVISIBLE);
        tvEmaill.setVisibility(View.INVISIBLE);
        tvFirstName.setVisibility(View.INVISIBLE);
        btnFunctionFaceBook.setVisibility(View.INVISIBLE);
        tvEmaill.setText("");
        tvFirstName.setText("");
        tvName.setText("");
        imgProfilePictureView.setProfileId(null);
        loginButton.setVisibility(View.VISIBLE);

    }

    private void setLoginButton() {
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                tvName.setVisibility(View.VISIBLE);
                tvEmaill.setVisibility(View.VISIBLE);
                tvFirstName.setVisibility(View.VISIBLE);
                btnFunctionFaceBook.setVisibility(View.VISIBLE);
                result();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("JSON", response.getJSONObject().toString());
                try {
                    email = object.getString("email");
                    name = object.getString("name");
                    firstName = object.getString("first_name");
                    id = object.getString("id");
                    //Profile.getCurrentProfile().getId()
                    imgProfilePictureView.setProfileId(id);
                    tvEmaill.setText(email);
                    tvFirstName.setText(firstName);
                    tvName.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }
}