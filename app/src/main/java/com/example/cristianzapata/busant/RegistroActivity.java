package com.example.cristianzapata.busant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    private String correo, contraseña, Recontraseña, bandera;
    private EditText eCorreo, eContraseña,eRContraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        eCorreo = (EditText)findViewById(R.id.eCorreo);
        eContraseña = (EditText)findViewById(R.id.eContraseña);
        eRContraseña = (EditText)findViewById(R.id.eRContraseña);
    }

    public void onclick(View view) {
        correo = eCorreo.getText().toString();
        contraseña = eContraseña.getText().toString();
        Recontraseña = eRContraseña.getText().toString();

        switch (view.getId()) {
            case R.id.bRegistrar:


                if(correo.matches("")){
                    eCorreo.setError("ingrese un dato");
                }
                if(contraseña.matches("")){
                    eContraseña.setError("ingrese un dato");
                }
                if(Recontraseña.matches("")){
                    eRContraseña.setError("ingrese un dato");
                }

                if (!Email(correo)) {
                    eCorreo.setError("correo invalido ");

                }
                else if(contraseña.equals(Recontraseña)){
                    Intent intent = new Intent();
                    intent.putExtra("correo", correo);
                    intent.putExtra("contraseña", contraseña);
                    setResult(RESULT_OK, intent);
                    finish();
                } else{

                    eContraseña.setError("passwords don't match");
                }
                break;
            case R.id.bCancelar:

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();

                break;
        }

    }
    public static boolean Email(String email) {
        boolean isValid;
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }
}
