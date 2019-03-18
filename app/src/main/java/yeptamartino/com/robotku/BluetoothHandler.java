package yeptamartino.com.robotku;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothHandler {

    private Activity activity;
    private final String DEVICE_ADDRESS = "00:18:E4:40:00:06"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    private String status = "BELUM TERHUBUNG";

    public BluetoothHandler(Activity activity){

        this.activity = activity;

    }

    public void onClick(){
        Toast.makeText(activity.getApplicationContext(), "Initiating bluetooth.", Toast.LENGTH_SHORT).show();

        if(BTinit())
        {
            Toast.makeText(activity.getApplicationContext(), "Init bluetooth succesful.", Toast.LENGTH_SHORT).show();
            BTconnect();
        }else{
            Toast.makeText(activity.getApplicationContext(), "Init bluetooth failed.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean BTinit()
    {
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(activity.getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableAdapter,0);

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
            Toast.makeText(activity.getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(activity.getApplicationContext(),
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
    public void kirimDataKeBluetooth(String input){
        if(outputStream != null){
            status = "SUDAH TERHUBUNG";

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
            status = "BELUM TERHUBUNG";
        }
    }

    public String getStatus(){
        return status;
    }

}
