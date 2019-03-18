package yeptamartino.com.robotku;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private Boolean power = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private SensorEventListener mAccelerometerSensorEventListener;

    //Bluetooth
    BluetoothHandler mBluetoothHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menyetingSensor();
        menyetingBluetooth();
    }

    private void setStatus(String text){
        TextView status = findViewById(R.id.status);
        status.setText(text);
    }

    private void menyetingBluetooth() {

        mBluetoothHandler = new BluetoothHandler(this);

        ImageView connect = findViewById(R.id.connect);
        Switch btnSwitch = findViewById(R.id.btn_switch);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothHandler.onClick();
            }
        });

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                power = isChecked;
            }
        });
    }


    private void controlRobot(String input) {
        Log.e("CONTROL ROBOT", " " + input);

        switch (input) {
            case "PELAN MAJU LURUS":
                mBluetoothHandler.kirimDataKeBluetooth("A");
                break;
            case "PELAN MAJU KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("B");
                break;
            case "PELAN MAJU KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("C");
                break;
            case "PELAN MAJU SERONG KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("D");
                break;
            case "PELAN MAJU SERONG KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("E");
                break;

            case "PELAN MUNDUR LURUS":
                mBluetoothHandler.kirimDataKeBluetooth("F");
                break;
            case "PELAN MUNDUR KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("G");
                break;
            case "PELAN MUNDUR KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("H");
                break;
            case "PELAN MUNDUR SERONG KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("I");
                break;
            case "PELAN MUNDUR SERONG KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("J");
                break;

            case "LAJU MAJU LURUS":
                mBluetoothHandler.kirimDataKeBluetooth("K");
                break;
            case "LAJU MAJU KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("L");
                break;
            case "LAJU MAJU KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("M");
                break;
            case "LAJU MAJU SERONG KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("N");
                break;
            case "LAJU MAJU SERONG KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("O");
                break;

            case "LAJU MUNDUR LURUS":
                mBluetoothHandler.kirimDataKeBluetooth("P");
                break;
            case "LAJU MUNDUR KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("Q");
                break;
            case "LAJU MUNDUR KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("R");
                break;
            case "LAJU MUNDUR SERONG KANAN":
                mBluetoothHandler.kirimDataKeBluetooth("S");
                break;
            case "LAJU MUNDUR SERONG KIRI":
                mBluetoothHandler.kirimDataKeBluetooth("T");
                break;
            default:
                mBluetoothHandler.kirimDataKeBluetooth("0");
                break;
        }

        setGambar(input);
        setStatus(mBluetoothHandler.getStatus());
    }

    private void setGambar(String input){
        ImageView imgControl = findViewById(R.id.img_control);

        String tmpInput = input.replace("PELAN ", "");
        tmpInput = tmpInput.replace("LAJU ", "");

        switch (tmpInput){
            case "MAJU LURUS":
                imgControl.setImageResource(R.drawable.majulurus);
                break;
            case "MAJU KIRI":
                imgControl.setImageResource(R.drawable.belokkiri);
                break;
            case "MAJU KANAN":
                imgControl.setImageResource(R.drawable.belokkanan);
                break;
            case "MAJU SERONG KANAN":
                imgControl.setImageResource(R.drawable.majuserongkanan);
                break;
            case "MAJU SERONG KIRI":
                imgControl.setImageResource(R.drawable.majuserongkiri);
                break;
            case "MUNDUR LURUS":
                imgControl.setImageResource(R.drawable.mundurlurus);
                break;
            case "MUNDUR SERONG KANAN":
                imgControl.setImageResource(R.drawable.mundurserongkanan);
                break;
            case "MUNDUR SERONG KIRI":
                imgControl.setImageResource(R.drawable.mundurserongkiri);
                break;
            default:
                imgControl.setImageResource(R.drawable.diam);
                break;
        }
    }

    private void menyetingSensor(){

        final TextView control = findViewById(R.id.control);
        final TextView sensors = findViewById(R.id.sensors);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        mAccelerometerSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

                    float[] inputs = new float[]{event.values[0],event.values[1],event.values[2]};

                    String eventString = "";

                    if(Math.abs(inputs[2]) > 4){
                        eventString += "LAJU ";
                    }else{
                        eventString += "PELAN ";
                    }

                    if(inputs[2] < 2.0f && inputs[2] > -2.0f){
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                        eventString += "DIAM";
                    }else if(inputs[2] > 2.0f){
                        getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                        eventString += "MAJU";
                    }else{
                        getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                        eventString += "MUNDUR";
                    }

                    if(inputs[0] < 2 && inputs[0] > -2.0f){
                        eventString += " LURUS";
                    }else if(inputs[0] > 2.0f && inputs[0] < 5.0f){
                        eventString += " SERONG KIRI";
                    }else if(inputs[0] < -2.0f && inputs[0] > -5.0f){
                        eventString += " SERONG KANAN";
                    }else if(inputs[0] > 5.0f){
                        eventString += " KIRI";
                    }else if(inputs[0] < -5.0f){
                        eventString += " KANAN";
                    }

                    if(eventString.contains("DIAM") || !power){
                        control.setText("DIAM");
                    }else{
                        control.setText(eventString);
                    }

                    String textSensors = "X : "+round(inputs[0],2)+", Y : "+round(inputs[2],2);
                    sensors.setText(textSensors);

                    if(power){
                        controlRobot(eventString);
                    }else{
                        controlRobot("DIAM");
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mAccelerometerSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccelerometerSensorEventListener);
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


}
