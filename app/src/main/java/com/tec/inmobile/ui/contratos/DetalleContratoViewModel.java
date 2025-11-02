package com.tec.inmobile.ui.contratos;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.tec.inmobile.R;
import com.tec.inmobile.models.Contrato;
import com.tec.inmobile.models.Inmueble;
import com.tec.inmobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleContratoViewModel extends AndroidViewModel {
    private Contrato contrato = new Contrato();
    private MutableLiveData<Contrato> mContrato = new MutableLiveData<>();
    private MutableLiveData<Bundle> irAInquilino = new MutableLiveData<>();

    public DetalleContratoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getMContrato() {
        return mContrato;
    }
    public LiveData<Bundle> getIrAInquilino() {
        return irAInquilino;}

    public void irApagos(NavController navController, Contrato contrato) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("contratoBundle", contrato);
        navController.navigate(R.id.action_detalleContratoFragment_to_pagosFragment, bundle);
    }

    public void irAinquilinos() {
        Contrato contrato = mContrato.getValue();
        if (contrato != null && contrato.getInquilino() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inquilino", contrato.getInquilino());
            irAInquilino.setValue(bundle);
        }
    }
    public void limpiarNavegacion() {
        irAInquilino.setValue(null);
    }
    public void recuperarContrato(Bundle bundle) {
        Inmueble inmuebleContrato = (Inmueble) bundle.get("inmuebleBundle");
        obtenerContratoPorInmueble(inmuebleContrato);
    }

    public void obtenerContratoPorInmueble(Inmueble inmueble) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<Contrato> call = api.getContratoPorInmueble("Bearer " + token, inmueble.getIdInmueble());
        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mContrato.postValue(response.body());
                } else {
                    contrato = null;
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Log.e("API", "Fallo en la conexión: " + t.getMessage());
                contrato = null;
            }
        });
    }
}