package com.tec.inmobile.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobile.models.Inmueble;
import com.tec.inmobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble;
    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Inmueble> getMInmueble(){
        if (mInmueble == null){
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }
    public void recuperarInmueble(Bundle bundle){
        Inmueble inmueble = (Inmueble) bundle.get("inmuebleBundle");
        if (inmueble!= null){
            mInmueble.setValue(inmueble);
        }
    }
    public void actualizarInmueble(Inmueble inmueble){
        ApiClient.InmoService api = ApiClient.getInmoService();
        String token = ApiClient.leerToken(getApplication());
        inmueble.setIdInmueble(mInmueble.getValue().getIdInmueble());
        Call<Inmueble> call = api.actualizarInmueble("Bearer "+token, inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplication(), "Inmueble actualizado correctamente" + response.code(), Toast.LENGTH_LONG).show();
                }else{
                    Log.d("InmuebleVM", "error en la respuesta: " + response.code());
                    Toast.makeText(getApplication(), "No se pudo actulizar el inmueble" + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.d("errorActualizar", "Error: " + t.getMessage());
            }
        });
    }


}