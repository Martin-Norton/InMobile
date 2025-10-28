package com.tec.inmobile.ui.inmuebles;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.tec.inmobile.models.Inmueble;
import com.tec.inmobile.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> uriMutableLiveData;
    private MutableLiveData<Inmueble> mInmueble;
    private static Inmueble inmuebleLleno;

    public CrearViewModel(@NonNull Application application) {
        super(application);
        inmuebleLleno = new Inmueble();
        uriMutableLiveData = new MutableLiveData<>();
        mInmueble = new MutableLiveData<>();
    }

    public LiveData<Uri> getUriMutableLiveData() {
        return uriMutableLiveData;
    }

    public LiveData<Inmueble> getmInmueble() {
        return mInmueble;
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Uri uri = data.getData();
            Log.d("salida", uri.toString());
            uriMutableLiveData.setValue(uri);
        }
    }

    public void guardarInmueble(String ambientes, String superficie, String direccion, String uso, String tipo, String latitud, String longitud, String valor) {
        try {
            int amb = Integer.parseInt(ambientes);
            int superf = Integer.parseInt(superficie);
            double val = Double.parseDouble(valor);
            double lat = Double.parseDouble(latitud);
            double lon = Double.parseDouble(longitud);
            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(direccion);
            inmueble.setUso(uso);
            inmueble.setTipo(tipo);
            inmueble.setValor(val);
            inmueble.setSuperficie(superf);
            inmueble.setAmbientes(amb);
            inmueble.setLatitud(lat);
            inmueble.setLongitud(lon);
            inmueble.setDisponible(false);
            //convierto imagen a bytes
            byte[] imagen = transformarImagen();
            if (imagen.length ==0){
                Toast.makeText(getApplication(), "Debe elegir una imagen!!!", Toast.LENGTH_LONG).show();
                return;
            }
            String inmuebleJson = new Gson().toJson(inmueble);
            RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagen);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);

            ApiClient.InmoService api = ApiClient.getInmoService();
            String token = ApiClient.leerToken(getApplication());
            Call<Inmueble> call = api.CargarInmueble("Bearer " + token, imagenPart, inmuebleBody);

            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getApplication(), "Inmueble guardado correctamente", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable throwable) {
                    Toast.makeText(getApplication(), "Error al guardar inmueble", Toast.LENGTH_LONG).show();
                }
            });

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = uriMutableLiveData.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplication(), "No ha elegido ninguna imagen!!!", Toast.LENGTH_LONG).show();
            return new byte[]{};
        }
    }
}