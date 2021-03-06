package com.example.taehun.totalmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PopupActivity extends Activity implements BeaconConsumer{

    TextView distance;
    Button btn;
    BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();
    SharedPreferences preferences;// 자동 로그인 데이터 저장
    SharedPreferences.Editor editor;
    String strMajor;
    String strminor;
    String strUuid;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putBoolean("BeaconTreaceOn", true);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        distance = findViewById(R.id.notification_distance);
        btn = findViewById(R.id.notification_btn);
        Intent intent = getIntent();
        preferences = getSharedPreferences("Beacon",getApplicationContext().MODE_PRIVATE);
        editor = preferences.edit();
        strUuid = intent.getStringExtra("UUID");
        strMajor = intent.getStringExtra("Major");
        strminor = intent.getStringExtra("Minor");

        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215, i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        handler.sendEmptyMessage(0);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("BeaconTreaceOn", true);
                editor.commit();
                finish();
            }
        });
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

                if (collection.size() > 0) {

                    beaconList.clear();
                    for (Beacon beacon : collection) {
                        beaconList.add(beacon);
                    }
                }
            }
        });
        try{
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        }catch(RemoteException e){}
    }
    Handler handler = new Handler(){
        public void handleMessage(Message msg){

            for(Beacon beacon : beaconList){

                if(beacon.getId1().toString().equals(strUuid.toLowerCase())&&beacon.getId2().toString().equals(strMajor.toLowerCase())&&beacon.getId3().toString().equals(strminor.toLowerCase())){
                    distance.setText( String.format("%.3f", beacon.getDistance()) +" m");
                    }
            }
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
}
