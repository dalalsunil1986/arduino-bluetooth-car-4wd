package yeptamartino.com.robotku;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private SensorEventListener mAccelerometerSensorEventListener;

    //Bluetooth
    private final String DEVICE_ADDRESS = "00:18:E4:40:00:06"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

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

        Button connect = findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Initiating bluetooth.", Toast.LENGTH_SHORT).show();

                if(BTinit())
                {
                    Toast.makeText(getApplicationContext(), "Init bluetooth succesful.", Toast.LENGTH_SHORT).show();
                    BTconnect();
                }else{
                    Toast.makeText(getApplicationContext(), "Init bluetooth failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean BTinit()
    {
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;
    }

    //SENSORS
    private void kirimDataKeBluetooth(String input){
       if(outputStream != null){
           setStatus("SUDAH TERHUBUNG");

           try
           {
               Log.e("INPUT"," "+input);
               outputStream.write(input.getBytes());
           }
           catch (IOException e)
           {
               e.printStackTrace();
           }

       }else{
           setStatus("BELUM TERHUBUNG");
       }

    }

    private void controlRobot(String input){
        Log.e("CONTROL ROBOT"," "+input);

        switch (input){
            case "PELAN MAJU LURUS":
                kirimDataKeBluetooth("A");
                break;
            case "PELAN MAJU KIRI":
                kirimDataKeBluetooth("B");
                break;
            case "PELAN MAJU KANAN":
                kirimDataKeBluetooth("C");
                break;
            case "PELAN MAJU SERONG_KANAN":
                kirimDataKeBluetooth("D");
                break;
            case "PELAN MAJU SERONG_KIRI":
                kirimDataKeBluetooth("E");
                break;

            case "PELAN MUNDUR LURUS":
                kirimDataKeBluetooth("F");
                break;
            case "PELAN MUNDUR KIRI":
                kirimDataKeBluetooth("G");
                break;
            case "PELAN MUNDUR KANAN":
                kirimDataKeBluetooth("H");
                break;
            case "PELAN MUNDUR SERONG_KANAN":
                kirimDataKeBluetooth("I");
                break;
            case "PELAN MUNDUR SERONG_KIRI":
                kirimDataKeBluetooth("J");
                break;

            case "LAJU MAJU LURUS":
                kirimDataKeBluetooth("K");
                break;
            case "LAJU MAJU KIRI":
                kirimDataKeBluetooth("L");
                break;
            case "LAJU MAJU KANAN":
                kirimDataKeBluetooth("M");
                break;
            case "LAJU MAJU SERONG_KANAN":
                kirimDataKeBluetooth("N");
                break;
            case "LAJU MAJU SERONG_KIRI":
                kirimDataKeBluetooth("O");
                break;

            case "LAJU MUNDUR LURUS":
                kirimDataKeBluetooth("P");
                break;
            case "LAJU MUNDUR KIRI":
                kirimDataKeBluetooth("Q");
                break;
            case "LAJU MUNDUR KANAN":
                kirimDataKeBluetooth("R");
                break;
            case "LAJU MUNDUR SERONG_KANAN":
                kirimDataKeBluetooth("S");
                break;
            case "LAJU MUNDUR SERONG_KIRI":
                kirimDataKeBluetooth("T");
                break;
            default:
                kirimDataKeBluetooth("0");
                break;
        }
    }

//    private void controlRobot(float[] inputs){
//
//        int x = (int)Math.floor(Math.abs(inputs[0]));
//        int y = (int)Math.floor(Math.abs(inputs[2]));
//
//        if(inputs[0] < 0){
//          x = x*-1;
//        }
//
//        if(inputs[2] < 0){
//            y = y*-1;
//        }
//
//        if(x > 10){
//            x = 10;
//        }
//
//        if(y > 10){
//            y = 10;
//        }
//
//        if(x < -10){
//            x = -10;
//        }
//
//        if(y < -10){
//            y = -10;
//        }
//
//        String input = x+","+y+";";
//
//        Log.e("MENGIRIM",input);
//
//        kirimDataKeBluetooth(input);
//
//    }

    //SENSOR

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
                        eventString += " SERONG_KIRI";
                    }else if(inputs[0] < -2.0f && inputs[0] > -5.0f){
                        eventString += " SERONG_KANAN";
                    }else if(inputs[0] > 5.0f){
                        eventString += " KIRI";
                    }else if(inputs[0] < -5.0f){
                        eventString += " KANAN";
                    }


                    control.setText(eventString);

                    String textSensors = "X : "+round(inputs[0],2)+", Y : "+round(inputs[2],2);
                    sensors.setText(textSensors);

                    controlRobot(eventString);
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
