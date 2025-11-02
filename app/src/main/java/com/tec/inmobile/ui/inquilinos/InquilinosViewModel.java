package com.tec.inmobile.ui.inquilinos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.inmobile.models.Inquilino;

public class InquilinosViewModel extends AndroidViewModel {
    public InquilinosViewModel(@NonNull Application application) {
        super(application);
    }
    private MutableLiveData<Inquilino> mInquilino = new MutableLiveData<>();

    public LiveData<Inquilino> getMInquilino() {
        return mInquilino;
    }
    public void traerInquilino(Inquilino inquilino) {
        if (inquilino != null
                && inquilino.getNombre() != null
                && inquilino.getDni() != null
                && !inquilino.getNombre().isEmpty()
                && !inquilino.getDni().isEmpty()) {
            mInquilino.setValue(inquilino);
        } else {
            Toast.makeText(getApplication(), "Hubo un error al obtener los datos del inquilino", Toast.LENGTH_LONG).show();
            mInquilino.setValue(null);
        }
    }
}
