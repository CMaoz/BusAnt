package com.example.cristianzapata.busant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class PerfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView iFoto_perfil;
    private TextView tNombre, tCorreo;
    private String nombre, correo, imgurl, correoR,contraseñaR, bandera, email,name,photo;;
    GoogleApiClient googleApiClient;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        iFoto_perfil = (ImageView) findViewById(R.id.iFoto_perfil);
        tNombre = (TextView) findViewById(R.id.tNombre);
        tCorreo = (TextView) findViewById(R.id.tCorreo);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            bandera = extras.getString("BANDERAM");
            nombre = extras.getString("NOMBREM");
            correo = extras.getString("CORREOM");
            imgurl = extras.getString("IMGURLM");
            Glide.with(this).load(imgurl).into(iFoto_perfil);
            tNombre.setText(nombre);
            tCorreo.setText(correo);
        }
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.mMPrincipal:

                intent = new Intent(PerfilActivity.this,MainActivity.class);
                intent.putExtra("NAME",nombre);
                intent.putExtra("CORREO", correo);
                intent.putExtra("IMGURL",imgurl);
                startActivity(intent);

                break;

            case R.id.mCerrar:
                if(bandera.matches("1")){//Facebook
                    LoginManager.getInstance().logOut();
                    intent = new Intent(PerfilActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);
                }
                else if(bandera.matches("2")){//Google
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                    intent = new Intent(PerfilActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;
                }
                else{ //Registro
                    intent = new Intent(PerfilActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
