package com.tec.inmobile.ui.login;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tec.inmobile.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private LoginActivityViewModel viewModel;
    private ActivityLoginBinding binding;
    private SensorManager sensorManager;
    private Sensor acelerometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(LoginActivityViewModel.class);
        setContentView(binding.getRoot());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        binding.btLogin.setOnClickListener(v ->
                viewModel.validarUsuario(
                        binding.etUsuario.getText().toString().trim(),
                        binding.etClave.getText().toString().trim()
                )
        );

        viewModel.getError().observe(this, binding.tvError::setText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        viewModel.iniciarEscuchaSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        viewModel.detenerEscuchaSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        viewModel.actualizarSensor(event.sensor.getType(), event.values, System.currentTimeMillis());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
