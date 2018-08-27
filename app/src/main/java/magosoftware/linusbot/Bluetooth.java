package magosoftware.linusbot;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends Fragment implements View.OnClickListener, Runnable{

    ArrayAdapter<String> mArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 12;
    private ListView listView;
    private BluetoothAdapter adapter;
    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayList<String> mArrayList;
    static BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    ProgressDialog mBluetoothConnectProgressDialog;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private InputStream inStream;
    static OutputStream outputStream;
    ProgressDialog procurarDialog;
    IntentFilter filter;
    int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                procurarDialog = ProgressDialog.show(getActivity(), "Procurar Dispositivos","Procurando...", true, false);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                procurarDialog.dismiss();
                listDevices();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if((""+device.getName()).equals("null")) {
                    Log.d("DEV/MAIN", "Dispositivo: "+"Dispositivo sem nome"+" - "+device.getAddress());
                    mArrayList.add("Dispositivo sem nome" + "\n" + device.getAddress());
                } else {
                    Log.d("DEV/MAIN", "Dispositivo: " + device.getName() + " - " + device.getAddress());
                    mArrayList.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bluetooth);
//        mArrayList = new ArrayList<>();
//
//        filter = new IntentFilter();
//
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//
//        registerReceiver(mReceiver, filter);
//
//        listView = findViewById(R.id.lista);
//        findViewById(R.id.list_button).setOnClickListener(this);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mArrayList = new ArrayList<>();

        filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);

        return inflater.inflate(R.layout.bluetooth, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedIntanceState) {
        super.onActivityCreated(savedIntanceState);

        listView = getView().findViewById(R.id.lista);
        getView().findViewById(R.id.list_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.list_button) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            // Register the BroadcastReceiver
            if(mBluetoothAdapter.isEnabled()) {
                mArrayList = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
                    switch (ContextCompat.checkSelfPermission(getActivity().getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        case PackageManager.PERMISSION_DENIED:
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            break;
                        case PackageManager.PERMISSION_GRANTED:
                            if (mBluetoothAdapter.isDiscovering()) {
                                mBluetoothAdapter.cancelDiscovery();
                            }
                            mBluetoothAdapter.startDiscovery();
                            break;
                    }
                }
//                listDevices();
            }

            // Create a BroadcastReceiver for ACTION_FOUND
        }
    }

//    private void setBluetoothData() {
//
//        // Getting the Bluetooth adapter
//        adapter = BluetoothAdapter.getDefaultAdapter();
//        out.append("\nAdapter: " + adapter.toString() + "\n\nName: "
//                + adapter.getName() + "\nAddress: " + adapter.getAddress());
//
//        // Check for Bluetooth support in the first place
//        // Emulator doesn't support Bluetooth and will return null
//
//        if (adapter == null) {
//            Toast.makeText(this, "Bluetooth NOT supported. Aborting.",
//                    Toast.LENGTH_LONG).show();
//        }
//
//        // Starting the device discovery
//        out.append("\n\nStarting discovery...");
//        adapter.startDiscovery();
//        out.append("\nDone with discovery...\n");
//
//        // Listing paired devices
//        out.append("\nDevices Pared:");
//        Set<BluetoothDevice> devices = adapter.getBondedDevices();
//        for (BluetoothDevice device : devices) {
//            out.append("\nFound device: " + device.getName() + " Add: "
//                    + device.getAddress());
//        }
//    }

    public void listDevices(){
//        pairedDevices = mBluetoothAdapter.getBondedDevices();
//        mArrayList = new ArrayList<>();
//        // If there are paired devices
//        if (pairedDevices.size() > 0) {
//            // Loop through paired devices
//            for (BluetoothDevice device : pairedDevices) {
//                // Add the name and address to an array adapter to show in a ListView
//                Log.d("DEV/MAIN", "Endereço:"+device.getAddress());
//                mArrayList.add(device.getName() + "\n" + device.getAddress());
//            }
//        }

        final ArrayAdapter mArrayAdapter = new  ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, mArrayList);

        listView.setAdapter(mArrayAdapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3){
                String nomeEendereco = listView.getItemAtPosition(position).toString();
                Log.d("DEV/MAIN", "Endereço ListView:"+listView.getItemAtPosition(position));
                String endereco = nomeEendereco.split("\n")[1];
                Log.d("SOCKET", endereco);
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(endereco);
                if(mBluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    mBluetoothConnectProgressDialog = ProgressDialog.show(getActivity(), "Conectando...", mBluetoothDevice.getName() + " : " + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(Bluetooth.this);
                    mBlutoothConnectThread.start();
                } else {
                    AlertDialog parear = new AlertDialog.Builder(getActivity())
                            .setTitle("Dispositivo não pareado")
                            .setMessage("Pareie o dispositivo através do menu Bluetooth do seu dispositivo")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
    }

    public void run()
    {
        try
        {
//            Intent intent = new Intent();
//            Parcelable[] uuidExtra = intent.getParcelableArrayExtra("android.bluetooth.device.extra.UUID");
//            ArrayList<UUID> uuidCandidates = new ArrayList<UUID>();
            mBluetoothDevice.fetchUuidsWithSdp();
            ParcelUuid[] uuidCandidates = mBluetoothDevice.getUuids();
//            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
//            ParcelUuid[] uuid = (ParcelUuid[]) getUuidsMethod.invoke(mBluetoothAdapter, null);
            Log.d("SOCKET", "mBluetoothDevice: "+uuidCandidates.length);
            for(ParcelUuid uuids : uuidCandidates) {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuids.getUuid());
                mBluetoothAdapter.cancelDiscovery();
                try {
                    mBluetoothSocket.connect();
                    break;
                }
                catch (IOException e) {
                    Log.d("MERDA", uuids.toString());
                }
            }
            mHandler.sendEmptyMessage(0);
            outputStream = mBluetoothSocket.getOutputStream();
            Intent intent = new Intent(getActivity(), ControleRemoto.class);
            startActivity(intent);
            getActivity().finish();
        }
        catch (Exception eConnectException)
        {
            Log.d("SOCKET", "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    public static OutputStream getOutStream() {
        return outputStream;
    }

    private void closeSocket(BluetoothSocket nOpenSocket)
    {
        try
        {
            nOpenSocket.close();
            Log.d("SOCKET", "SocketClosed");
        }
        catch (IOException ex)
        {
            Log.d("SOCKET", "CouldNotCloseSocket");
        }
    }

    public static BluetoothSocket getSocket() {
        return mBluetoothSocket;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(getActivity(), "Dispositivo Conectado", Toast.LENGTH_LONG).show();
        }
    };

//
//
//    private class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) { }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run() {
//            byte[] buffer = new byte[1024];  // buffer store for the stream
//            int bytes; // bytes returned from read()
//
//            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//                    // Send the obtained bytes to the UI activity
//                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
//                } catch (IOException e) {
//                    break;
//                }
//            }
//        }
//
//        /* Call this from the main activity to send data to the remote device */
//        public void write(byte[] bytes) {
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) { }
//        }
//
//        /* Call this from the main activity to shutdown the connection */
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) { }
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {

        }
    }
}