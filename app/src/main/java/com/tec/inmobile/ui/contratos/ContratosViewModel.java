package com.tec.inmobile.ui.contratos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.tec.inmobile.models.Contrato;
import com.tec.inmobile.models.Inmueble;
import com.tec.inmobile.request.ApiClient;
import com.tec.inmobile.ui.inmuebles.InmueblesViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> listaInmueblesContratos= new MutableLiveData<>();

    public ContratosViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData <List<Inmueble>> getListaInmueblesContratos(){
        return listaInmueblesContratos;
    }

    public void obtenerListaInmueblesContratos(){
        String token= ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Inmueble>> call = api.getContratoVigente("Bearer " + token);
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()){
                    listaInmueblesContratos.postValue(response.body());
                }else{
                    Toast.makeText(getApplication(), "Error al obtener Contratos en onResponse", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la respuesta en onFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}