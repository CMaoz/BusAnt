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
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String nombrem, correom, imgurlm, banderam;
    GoogleApiClient googleApiClient;
    private String bandera;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        tv=(TextView) findViewById(R.id.tv);
        if(extras != null){
            banderam = extras.getString("BANDERA");
            nombrem = extras.getString("NAME");
            correom = extras.getString("CORREO");
            imgurlm = extras.getString("IMGURL");
            tv.setText(correom);
        }
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.mMiPerfil:
                intent = new Intent(MainActivity.this,PerfilActivity.class);
                intent.putExtra("NOMBREM",nombrem);
                intent.putExtra("CORREOM", correom);
                intent.putExtra("IMGURLM",imgurlm);
                intent.putExtra("BANDERAM", banderam);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            break;
            case R.id.mCerrar:


                if(banderam.matches("1")){//Facebook
                    LoginManager.getInstance().logOut();
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
                    startActivity(intent);
                    finish();
                }
                else if(banderam.matches("2")){//Google
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                }
                else{ //Registro
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
