package com.tec.inmobile.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tec.inmobile.R;
import com.tec.inmobile.models.Propietario;
import com.tec.inmobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mEstado = new MutableLiveData<>();
    private MutableLiveData<String> mTexto = new MutableLiveData<>();
    private MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getMEstado(){
        return mEstado;
    }
    public LiveData<String> getMTexto(){
        return mTexto;
    }
    public LiveData<Propietario> getPropietario(){
        return propietario;
    }

    public void cambioBoton(String textoBoton, String nombre, String apellido, String dni, String telefono, String email){
        if (textoBoton.equals("EDITAR")){
            mEstado.setValue(true);
            mTexto.setValue("GUARDAR");
        }else{
            mEstado.setValue(false);
            mTexto.setValue("EDITAR");
            Propietario propietarioNew = new Propietario();
            propietarioNew.setIdPropietario(propietario.getValue().getIdPropietario());
            propietarioNew.setNombre(nombre);
            propietarioNew.setApellido(apellido);
            propietarioNew.setDni(dni);
            propietarioNew.setTelefono(telefono);
            propietarioNew.setEmail(email);
            String token = ApiClient.leerToken(getApplication());
            ApiClient.InmoService api = ApiClient.getInmoService();
            Call<Propietario> call = api.actualizarPropietario("Bearer" + token, propietarioNew);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getApplication(), "Propietario actualizado con exito!!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplication(), "Error al actualizar: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable throwable) {
                    Toast.makeText(getApplication(), "Error en la respuesta: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("actualizar perfil", "error en la respuesta: " + throwable.getMessage());
                }
            });
        }
    }
    public void obtenerPerfil(){
        String token= ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<Propietario> call = api.getPropietario("Bearer " + token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful()){
                    propietario.setValue(response.body());
                }else{
                    Log.d("PerfilVM", "error en la respuesta: "+ response.code());
                    Toast.makeText(getApplication(), "Error en la respuesta: on response " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("PerfilVM", "error en la respuesta: " + t.getMessage());
                Toast.makeText(getApplication(), "Error en la respuesta: on failure" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}