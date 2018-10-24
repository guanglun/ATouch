package com.guanglun.atouch.Bluetooth;

import android.os.Bundle;
import android.app.Activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.guanglun.atouch.Bluetooth.BuleDevice;
import com.guanglun.atouch.R;

public class ScanBlueActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_blue);


        listView = (ListView) findViewById(R.id.listView);
        final BuleDevice bule_device = new BuleDevice(new BuleDevice.blue_callback(){


            @Override
            public void on_connect() {
                showToast("连接成功");
                printf("连接成功");
            }

            @Override
            public void on_disconnect() {

            }

            @Override
            public void on_receive(byte[] buffer) {
                printf(new String(buffer));
            }
        });

        if(bule_device.init(this,listView))
        {
            Log.e("DEBUG","设备初始化成功");
        }

        final Button bt_scan = (Button) findViewById(R.id.bt_scan);
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_scan.getText().equals("停止扫描"))
                {
                    bule_device.stop_scan();
                    bt_scan.setText("开始扫描");
                }else{

                    bule_device.start_scan();
                    bt_scan.setText("停止扫描");
                }
            }
        });

        bule_device.start_scan();
    }

    private void showToast(String str)
    {
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }

    private void printf(String str)
    {
        System.out.printf(str);
    }
}
