package com.tec.inmobile.ui.inmuebles;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tec.inmobile.models.Inmueble;
import com.tec.inmobile.models.Propietario;
import com.tec.inmobile.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> listaInmuebles= new MutableLiveData<>();
    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData <List<Inmueble>> getListaInmuebles(){
        return listaInmuebles;
    }

    public void obtenerListaInmuebles(){
        String token= ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Inmueble>> call = api.getImuebles("Bearer " + token);
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()){
                    listaInmuebles.postValue(response.body());
                }else{
                    Toast.makeText(getApplication(), "Error al obtener inmuebles en onResponse", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la respuesta en onFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}