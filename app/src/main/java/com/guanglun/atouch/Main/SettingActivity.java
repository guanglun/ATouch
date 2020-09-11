package com.guanglun.atouch.Main;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.guanglun.atouch.R;

public class SettingActivity extends AppCompatActivity {

    private AppConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        config = new AppConfig();
        config.init(this);


        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rg_setting_offset);
        RadioButton radioButton1 = (RadioButton)findViewById(R.id.rb_setting_offset_1);
        RadioButton radioButton2 = (RadioButton)findViewById(R.id.rb_setting_offset_2);
        RadioButton radioButton3 = (RadioButton)findViewById(R.id.rb_setting_offset_3);
        RadioButton radioButton4 = (RadioButton)findViewById(R.id.rb_setting_offset_4);
        RadioButton radioButton5 = (RadioButton)findViewById(R.id.rb_setting_offset_5);

        Log.e("SETTING", " " + config.getOffsetConfig());
        switch(config.getOffsetConfig())
        {
            case 1:
                radioButton1.setChecked(true);
                break;
            case 2:
                radioButton2.setChecked(true);
                break;
            case 3:
                radioButton3.setChecked(true);
                break;
            case 4:
                radioButton4.setChecked(true);
                break;
            case 5:
                radioButton5.setChecked(true);
                break;
            default:
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.rb_setting_offset_1:
                        config.setOffsetConfig(1);
                        break;
                    case R.id.rb_setting_offset_2:
                        config.setOffsetConfig(2);
                        break;
                    case R.id.rb_setting_offset_3:
                        config.setOffsetConfig(3);
                        break;
                    case R.id.rb_setting_offset_4:
                        config.setOffsetConfig(4);
                        break;
                    case R.id.rb_setting_offset_5:
                        config.setOffsetConfig(5);
                        break;
                    default:
                        break;
                }
                config.store();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_setting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_goback:

                finish();

                break;


        }
        return true;
    }
}
