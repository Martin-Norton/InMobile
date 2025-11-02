package com.tec.inmobile.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobile.MainActivity;
import com.tec.inmobile.models.Propietario;
import com.tec.inmobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> errorMutable = new MutableLiveData<>();
    private MutableLiveData<Propietario> propietarioMutable = new MutableLiveData<>();
    private boolean escuchandoSensor = false;

    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate = 0;
    private float lastX, lastY, lastZ;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getError() {
        return errorMutable;
    }

    public LiveData<Propietario> getPropietario() {
        return propietarioMutable;
    }

    public void validarUsuario(String email, String clave) {
        if (email.isEmpty() || clave.isEmpty()) {
            errorMutable.postValue("Todos los campos son obligatorios");
            return;
        }
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<String> call = api.loginForm(email, clave);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String token = response.body();
                    ApiClient.guardarToken(getApplication(), token);
                    obtenerPropietarioYGuardar(token);
                } else {
                    errorMutable.postValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                errorMutable.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void obtenerPropietarioYGuardar(String token) {
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<Propietario> callProp = api.getPropietario("Bearer " + token); // 🔹 IMPORTANTE: agregar "Bearer "

        callProp.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Propietario p = response.body();
                    propietarioMutable.postValue(p);
                    guardarEnSharedPreferences(p);
                    abrirMainActivity();
                } else {
                    Log.d("LoginVM", "Error en la respuesta: " + response.code());
                    errorMutable.postValue("Error al obtener propietario (" + response.code() + ")");
                    abrirMainActivity();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.e("LoginVM", "Fallo conexión: " + t.getMessage());
                errorMutable.postValue("Error de conexión: " + t.getMessage());
                abrirMainActivity();
            }
        });
    }


    private void guardarEnSharedPreferences(Propietario p) {
        SharedPreferences sp = getApplication().getSharedPreferences("datos_propietario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nombre", p.getNombre() + " " + p.getApellido());
        editor.putString("email", p.getEmail());
        editor.apply();
    }

    private void abrirMainActivity() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void iniciarEscuchaSensor() { escuchandoSensor = true; }

    public void detenerEscuchaSensor() { escuchandoSensor = false; }

    public void actualizarSensor(int tipoSensor, float[] valores, long tiempo) {
        if (!escuchandoSensor || tipoSensor != Sensor.TYPE_ACCELEROMETER) return;

        if ((tiempo - lastUpdate) > 100) {
            long diffTime = tiempo - lastUpdate;
            lastUpdate = tiempo;

            float x = valores[0];
            float y = valores[1];
            float z = valores[2];

            float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                new Handler().postDelayed(this::hacerLlamada, 300);
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    private void hacerLlamada() {
        try {
            String telefono = "tel:1122334455";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(telefono));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        } catch (Exception e) {
            Log.e("Llamada", e.getMessage());
        }
    }
}
