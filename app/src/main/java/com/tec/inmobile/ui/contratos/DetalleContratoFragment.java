package com.tec.inmobile.ui.contratos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.inmobile.R;
import com.tec.inmobile.databinding.FragmentDetalleContratoBinding;
import com.tec.inmobile.models.Contrato;
import com.tec.inmobile.models.Inquilino;
import com.tec.inmobile.models.Pagos;
import com.tec.inmobile.ui.inquilinos.InquilinosFragment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DetalleContratoFragment extends Fragment {
    private FragmentDetalleContratoBinding binding;
    private DetalleContratoViewModel mv;
    private Inquilino inquilino;
    private Contrato contratoP;

    public static DetalleContratoFragment newInstance() {
        return new DetalleContratoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mv = new ViewModelProvider(this).get(DetalleContratoViewModel.class);
        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Detalle del Contrato");
        View view = binding.getRoot();
        mv.getMContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                inquilino = contrato.getInquilino();
                contratoP = contrato;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                binding.etCodigo.setText(String.valueOf(contrato.getIdContrato()));
                binding.etFechaInicio.setText((contrato.getFechaInicio() != null ? dateFormat.format(contrato.getFechaInicio()) : "-"));
                binding.etFechaFin.setText((contrato.getFechaFinalizacion() != null ? dateFormat.format(contrato.getFechaFinalizacion()) : "-"));
                binding.etMontoAlquiler.setText(String.valueOf(contrato.getMontoAlquiler()));
                binding.etInquilino.setText(contrato.getInquilino().getNombre() + ", " + contrato.getInquilino().getApellido());
                binding.etInmueble.setText(contrato.getInmueble().getDireccion());
            }
        });

        binding.btPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("contratoBundle", contratoP);
                NavController navController = NavHostFragment.findNavController(DetalleContratoFragment.this);
                navController.navigate(R.id.pagosFragment, bundle);
            }
        });
        binding.btInquilinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("inquilinoBundle", inquilino);
                NavController navController = NavHostFragment.findNavController(DetalleContratoFragment.this);
                navController.navigate(R.id.nav_inquilinos, bundle);
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