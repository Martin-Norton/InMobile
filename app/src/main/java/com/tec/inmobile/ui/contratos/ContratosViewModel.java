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
    private List<Contrato> listaContratos;
    private List<Inmueble> inmueblesConContrato;

    public ContratosViewModel(@NonNull Application application) {
        super(application);
        listaContratos = new ArrayList<>();
        inmueblesConContrato = new ArrayList<>();
    }
    public LiveData <List<Inmueble>> getListaInmueblesContratos(){
        return listaInmueblesContratos;
    }

    public void obtenerListaInmueblesContratos(){
        String token= ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<Contrato>> call = api.getContratos("Bearer " + token);
        call.enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (response.isSuccessful()){
                    listaContratos.clear();
                    inmueblesConContrato.clear();
                    listaContratos.addAll(response.body());
                    for (Contrato c: listaContratos) {
                       inmueblesConContrato.add(c.getInmueble());
                    }
                    listaInmueblesContratos.postValue(inmueblesConContrato);
                    Toast.makeText(getApplication(), "Cantidad de inmuebles con contratos: " + inmueblesConContrato.size(), Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplication(), "Error al obtener Contratos en onResponse", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en la respuesta en onFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}