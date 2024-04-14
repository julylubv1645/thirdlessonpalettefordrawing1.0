package com.example.thirdlessonpalettefordrawing10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    // поля
    private ImageView buttonMenu;
    private LinearLayout buttons;
    private boolean buttonsCheck = false; // поле включения кнопок
    private ImageView buttonPallet, buttonBrush;
    private ArtView art;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    // слушатель для кнопок
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.buttonMenu:
                    if (buttonsCheck) {
                        buttonsCheck = false;
                        buttons.setVisibility(View.INVISIBLE);
                    } else {
                        buttonsCheck = true;
                        buttons.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.buttonBrush:
                    // код для очистки View
                    AlertDialog.Builder broomDialog = new AlertDialog.Builder(MainActivity.this); // создание диалогового окна типа AlertDialog
                    broomDialog.setTitle("Очистка рисунка"); // заголовок диалогового окна
                    broomDialog.setMessage("Очистить область рисования (имеющийся рисунок будет удалён)?"); // сообщение диалога

                    broomDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() { // пункт выбора "да"
                        public void onClick(DialogInterface dialog, int which) {
                            art.brush(); // метод очистки кастомизированного View
                            dialog.dismiss(); // закрыть диалог
                        }
                    });
                    broomDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {  // пункт выбора "нет"
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel(); // выход из диалога
                        }
                    });
                    broomDialog.show(); // отображение на экране данного диалога
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // привязка кнопок к разметке
        buttonMenu = findViewById(R.id.buttonMenu);
        buttons = findViewById(R.id.buttons);
        buttonPallet = findViewById(R.id.buttonPallet);
        buttonBrush = findViewById(R.id.buttonBrush);
        art = findViewById(R.id.art);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // обработка нажатия кнопок
        buttonMenu.setOnClickListener(listener);
        buttonPallet.setOnClickListener(listener);
        buttonBrush.setOnClickListener(listener);

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Sensor multiSensor = sensorEvent.sensor;
            if (multiSensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float xAccelerometer = sensorEvent.values[0];
                float yAccelerometer = sensorEvent.values[1];
                float zAccelerometer = sensorEvent.values[2];
                float medianAccelerometer = (xAccelerometer + yAccelerometer + zAccelerometer) / 3;
                if (medianAccelerometer > 10){
                    if (buttonsCheck){
                        buttonsCheck = false;
                        buttons.setVisibility(View.INVISIBLE);
                    } else {
                        buttonsCheck = true;
                        buttons.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onResume(){
        super.onResume();

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(sensorEventListener);
    }}