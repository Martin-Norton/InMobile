package com.tec.inmobile.ui.contratos;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobile.models.Contrato;
import com.tec.inmobile.models.Pagos;
import com.tec.inmobile.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pagos>> mPagos = new MutableLiveData<>();

    public PagosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Pagos>> getMPagos() {
        return mPagos;
    }

    public void recuperarPagos(Bundle bundle) {
        if (bundle != null && bundle.containsKey("contratoBundle")) {
            Contrato contrato = (Contrato) bundle.getSerializable("contratoBundle");
            if (contrato != null) {
                Log.d("PagosVM", "Recibido contrato con ID: " + contrato.getIdContrato());
                obtenerPagosPorContrato(contrato);
            } else {
                Log.e("PagosVM", "Contrato es null dentro del bundle");
            }
        } else {
            Log.e("PagosVM", "No se encontró el contrato en el bundle");
        }
    }

    private void obtenerPagosPorContrato(Contrato contrato) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();

        Call<List<Pagos>> call = api.getPagosPorContratos("Bearer " + token, contrato.getIdContrato());

        Log.d("PagosVM", "Solicitando pagos del contrato: " + contrato.getIdContrato());

        call.enqueue(new Callback<List<Pagos>>() {
            @Override
            public void onResponse(Call<List<Pagos>> call, Response<List<Pagos>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPagos.postValue(response.body());
                    Log.d("PagosVM", "Pagos obtenidos: " + response.body().size());
                } else {
                    Log.e("PagosVM", "Error al obtener pagos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pagos>> call, Throwable t) {
                Log.e("PagosVM", "Error de red: " + t.getMessage());
            }
        });
    }
}