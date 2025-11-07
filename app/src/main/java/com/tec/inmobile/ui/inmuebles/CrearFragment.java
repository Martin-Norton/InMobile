package com.tec.inmobile.ui.inmuebles;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tec.inmobile.databinding.FragmentCrearBinding;

public class CrearFragment extends Fragment {

    private CrearViewModel mv;
    private FragmentCrearBinding binding;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCrearBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(CrearViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        abrirGaleria();
        binding.btnCargarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);
            }
        });
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarInmueble();
            }
        });
        mv.getUriMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                //binding.imageView2.setImageURI(uri);
                Glide.with(requireContext())
                        .load(uri)
                        .override(800, 800)
                        .centerCrop()
                        .into(binding.imageView2);
            }
        });

    }
    public void cargarInmueble(){
        String ambientes = binding.etAmbientes.getText().toString();
        String superficie = binding.etSuperficie.getText().toString();
        String direccion = binding.etDireccion.getText().toString();
        String uso = binding.etUso.getText().toString();
        String tipo = binding.etTipo.getText().toString();
        String latitud =binding.etLatitud.getText().toString();
        String longitud = binding.etLongitud.getText().toString();
        String valor = binding.etValor.getText().toString();
        mv.guardarInmueble(ambientes, superficie, direccion, uso, tipo, latitud, longitud, valor);
    }


    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                mv.recibirFoto(result);
            }
        });
    }

}