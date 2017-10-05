package com.example.cristianzapata.busant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.bumptech.glide.Glide.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    /////// LOGIN GOOGLE

    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private String Name="", Correo="", Imgurl="",correoR,contraseñaR,correo, contrseña, correo1,contrseña1,email,
            bandera,photourl,name,si;
    private EditText eCorreo, eContraseña;
    private Button bIniciar;
    CallbackManager callbackManager;

    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Facebook
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        eCorreo = (EditText)findViewById(R.id.eCorreo);
        eContraseña= (EditText) findViewById(R.id.eContraseña);
        bIniciar =(Button)findViewById(R.id.bIniciar);
        si ="1";

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            correoR = extras.getString("correo");
            contraseñaR = extras.getString("contraseña");

        }else if(si.matches("1")){
            Toast.makeText(getApplicationContext(), "registrese",
                    Toast.LENGTH_SHORT).show();
            correoR ="null";
            contraseñaR ="null";
        }

        //Google
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //B8gEd1nWtaLhl9Y9THtxmPgSqFM=
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.cristianzapata.busant",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }*/

    public void Registrarse(View view) {
        Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
        startActivityForResult(intent,1234);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                //tUrl.setText(Imgurl);
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);

    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            //ImageView Foto_perfil = null;
            if(googleSignInAccount.getDisplayName()!=null) {
                Name = googleSignInAccount.getDisplayName();
                bandera="2";
            }
            if (googleSignInAccount.getEmail()!= null) {
                Correo = googleSignInAccount.getEmail();
                bandera="2";
            }
            if(googleSignInAccount.getPhotoUrl()!= null) {
                Imgurl = googleSignInAccount.getPhotoUrl().toString();
                bandera="2";
            }
            updateUI(true);
        }
        else {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLoggin){
        if(isLoggin){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("NAME", Name);
            intent.putExtra("CORREO", Correo);
            intent.putExtra("IMGURL", Imgurl);
            intent.putExtra("BANDERA", bandera);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Error en Login",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }

       else if(requestCode ==1234 && resultCode ==RESULT_OK){
            bandera = "3";
            si = "2";
            Bundle extras = data.getExtras();  //getIntent().getExtras();
            correoR = extras.getString("correo");
            contraseñaR = extras.getString("contraseña");
        }

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void Iniciar(View view) {

        correo = eCorreo.getText().toString();
        contrseña = eContraseña.getText().toString();
        if(correo.matches("null")&&contraseñaR.matches("null")){
            Toast.makeText(getApplicationContext(), "registrese",
                    Toast.LENGTH_SHORT).show();
        }else if(correo.matches("")&&contrseña.matches("")){
            Toast.makeText(getApplicationContext(), "Ingrese datos o registrese",
                    Toast.LENGTH_SHORT).show();
        }else if(correo.matches(correoR)&&contrseña.matches(contraseñaR)) {
            goMainActivity();
        }else{
            Toast.makeText(getApplicationContext(), "Error de contraseña",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void goMainActivity() {

        if(bandera =="1") {//Facebook
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Bandera",bandera);
            intent.putExtra("CORREO", Correo);
            intent.putExtra("NAME", Name);
            intent.putExtra("IMGURL", Imgurl);
            startActivity(intent);
            finish();
        }
        else if(bandera=="2"){//Google
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("NAME", Name);
            intent.putExtra("CORREO", Correo);
            intent.putExtra("IMGURL", Imgurl);
            intent.putExtra("Bandera",bandera);
            startActivity(intent);
            finish();
        }
        else{//Normal
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("CORREO", correoR
            );
            intent.putExtra("contraseña", contraseñaR);
            intent.putExtra("BANDERA",bandera);
            startActivity(intent);
            finish();
        }
    }

    private void RequestData() {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                final JSONObject json = response.getJSONObject();

                try {
                    if(json != null){
                        bandera ="1";
                        Correo = object.getString("email");
                        // birthday = object.getString("birthday");
                        //String gender = object.getString("gender");
                        Name = object.getString("name");
                        //String id = object.getString("id");
                        Imgurl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                        goMainActivity();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
