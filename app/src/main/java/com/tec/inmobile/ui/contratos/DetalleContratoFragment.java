package com.tec.inmobile.ui.contratos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tec.inmobile.R;
import com.tec.inmobile.databinding.FragmentDetalleContratoBinding;
import com.tec.inmobile.databinding.FragmentDetalleInmuebleBinding;
import com.tec.inmobile.models.Contrato;
import com.tec.inmobile.models.Inmueble;
import com.tec.inmobile.request.ApiClient;
import com.tec.inmobile.ui.inmuebles.DetalleInmuebleViewModel;

public class DetalleContratoFragment extends Fragment {
    private FragmentDetalleContratoBinding binding;
    private DetalleContratoViewModel mv;

    public static DetalleContratoFragment newInstance() {
        return new DetalleContratoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mv = new ViewModelProvider(this).get(DetalleContratoViewModel.class);
        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mv.getMContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                binding.etCodigo.setText(String.valueOf(contrato.getIdContrato()));
                binding.etFechaInicio.setText(
                        contrato.getFechaInicio() != null
                                ? contrato.getFechaInicio().toString()
                                : "Sin fecha");
                binding.etFechaFin.setText(contrato.getFechaFinalizacion() != null
                        ? contrato.getFechaFinalizacion().toString()
                        : "Sin fecha");
                binding.etMontoAlquiler.setText(String.valueOf(contrato.getMontoAlquiler()));
                binding.etInquilino.setText(contrato.getInquilino().getNombre() + ", " + contrato.getInquilino().getApellido());
                binding.etInmueble.setText(contrato.getInmueble().getDireccion());
            }
        });

        binding.btPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mv.recuperarContrato(getArguments());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mv = new ViewModelProvider(this).get(DetalleContratoViewModel.class);
    }

}