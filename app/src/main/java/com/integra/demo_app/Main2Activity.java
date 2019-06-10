package com.integra.demo_app;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {
    MyReceiver mReceiver;

    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {

            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    System.out.println("--->" + "10mps");
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // haveNetworkConnection();
        getnetworkinfo();
        getBluetoothinfo();
        getSignal();
        new PhoneCustomStateListener();
        // ActivityMainBinding  binding = DataBindingUtil.setContentView(this,R.layout.activity_main);


    }

    private void getBluetoothinfo() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //handle the case where device doesn't support Bluetooth
            System.out.println("it has bluetooth");
        } else {
            System.out.println("it has bluetooth");
            //bluetooth supported
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            System.out.println("isConnected info ==>" + ni.getTypeName());
//            System.out.println("isConnected wifi ==>"+ni.getTypeName());
//            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
//                if (ni.isConnected())
//                    System.out.println("isConnected wifi ==>"+ni.getTypeName());
//                    haveConnectedWifi = true;
//            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
//                if (ni.isConnected())
//                    System.out.println("isConnected mobile==>"+ni.getTypeName());
//                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;

    }

    private void getnetworkinfo() {


        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(cm.getActiveNetworkInfo().isConnected()){
//            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//            System.out.println("isConnected ==>"+activeNetwork.getTypeName());
//        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int speedMbps = wifiInfo.getLinkSpeed();
            System.out.println("wifi speed" + speedMbps);
            // do something
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            // check NetworkInfo subtype
            System.out.println("isConnected ==>" + "TYPE_MOBILE" + info.getSubtype());
            isConnectionFast(info.getType(), info.getSubtype());


        }
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getSignal() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        Log.d("linkSpeed", "linkSpeed-->" + linkSpeed);

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
        CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
        cellSignalStrengthGsm.getDbm();
        Log.d("linkSpeed", "linkSpeed-->" + cellSignalStrengthGsm.getLevel());
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new MyReceiver(this);
        registerReceiver(mReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                showtoast("dispatchKeyEvent", "KEYCODE_BACK");
                break;
            case KeyEvent.KEYCODE_HOME:
                showtoast("dispatchKeyEvent", "KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_0:
                showtoast("dispatchKeyEvent", "KEYCODE_0");
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                showtoast("dispatchKeyEvent", "KEYCODE_VOLUME_UP");
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                showtoast("dispatchKeyEvent", "KEYCODE_VOLUME_DOWN");
                break;
            case KeyEvent.KEYCODE_CAMERA:
                showtoast("dispatchKeyEvent", "KEYCODE_CAMERA");
                break;
            case KeyEvent.KEYCODE_POWER:
                showtoast("dispatchKeyEvent", "KEYCODE_POWER");
                break;

        }
        return super.dispatchKeyEvent(event);
    }

    private void showtoast(String dispatchKeyEvent, String keycode_back) {
        Toast.makeText(this, dispatchKeyEvent + " " + keycode_back, Toast.LENGTH_SHORT).show();
    }

    @Override
    // catches the onKeyDown button event
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(this, "Press again back for exit", Toast.LENGTH_SHORT).show();

                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                showtoast("onKeyDown", "VOLUME_UP key pressed");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                showtoast("onKeyDown", "VOLUME_DOWN key pressed");
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // catches the onKeyUp button event
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showtoast("onKeyUp", "KEYCODE_BACK key pressed");
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                showtoast("onKeyUp", "VOLUME_UP key pressed");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                showtoast("onKeyUp", "VOLUME_DOWN key pressed");
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // works for API level 5 and lower
    @Override
    public void onBackPressed() {
        showtoast("onBackPressed", "BACK key pressed");
        super.onBackPressed();
    }

    // catches the long press button event (longer than 2 seconds)
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Toast.makeText(this, "Pressed for a long time", Toast.LENGTH_SHORT).show();
        return true;
    }

    // catches the on touch event on screen and shows the specific pixels
    // touched
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Toast.makeText(this, "Touch press on x: " + x + " y: " + y, Toast.LENGTH_SHORT).show();
        return true;
    }


    public class PhoneCustomStateListener extends PhoneStateListener {

        public int signalSupport = 0;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            signalSupport = signalStrength.getGsmSignalStrength();
            Log.d(getClass().getCanonicalName(), "------ gsm signal --> " + signalSupport);

            if (signalSupport > 30) {
                Log.d(getClass().getCanonicalName(), "Signal GSM : Good");


            } else if (signalSupport > 20 && signalSupport < 30) {
                Log.d(getClass().getCanonicalName(), "Signal GSM : Avarage");


            } else if (signalSupport < 20 && signalSupport > 3) {
                Log.d(getClass().getCanonicalName(), "Signal GSM : Weak");


            } else if (signalSupport < 3) {
                Log.d(getClass().getCanonicalName(), "Signal GSM : Very weak");


            }
        }
    }

    private class CheckInternetAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        private Context context;

        public CheckInternetAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();


            if (isConnected) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    if (urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0)
                        return true;

                } catch (IOException e) {
                    Log.e("TAG", "Error checking internet connection", e);
                    return false;
                }
            } else {
                Log.d("TAG", "No network available!");
                return false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d("TAG", "result" + result);

            if (result) {
                // do ur code
            }

        }

    }
}
