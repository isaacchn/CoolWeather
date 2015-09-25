package com.isaac.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.isaac.coolweather.R;
import com.isaac.coolweather.util.LogUtil;
import com.isaac.coolweather.util.Utilities;

public class SettingActivity extends Activity {

    Button buttonSettingsGoHome;
    Button buttonSettingsGoBack;
    Switch switchAutoUpdate;
    Spinner spinnerAutoUpdateInterval;
    ImageButton buttonSaveSettings;

    private ArrayAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        buttonSettingsGoHome = (Button) findViewById(R.id.settings_home);
        buttonSettingsGoBack = (Button) findViewById(R.id.settings_back);
        switchAutoUpdate = (Switch) findViewById(R.id.switchAutoUpdate);
        spinnerAutoUpdateInterval = (Spinner) findViewById(R.id.spinnerAutoUpdateInterval);
        buttonSaveSettings = (ImageButton) findViewById(R.id.buttonSaveSettings);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.intervals, R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerAutoUpdateInterval.setAdapter(spinnerAdapter);
        //spinnerAutoUpdateInterval.setVisibility(View.VISIBLE);

        //初始化switch
        boolean autoUpdateFlag = Utilities.getAutoUpdateFlag();
        if (autoUpdateFlag) {
            switchAutoUpdate.setChecked(true);
        } else {
            switchAutoUpdate.setChecked(false);
        }
        switchAutoUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    LogUtil.d("SettingActivity", "isChecked");
                    spinnerAutoUpdateInterval.setEnabled(true);
                } else {
                    LogUtil.d("SettingActivity", "unChecked");
                    spinnerAutoUpdateInterval.setEnabled(false);
                }
            }
        });
        //初始化spinner
        int autoUpdateInterval = Utilities.getAutoUpdateInterval();
        switch (autoUpdateInterval) {
            case 2:
                spinnerAutoUpdateInterval.setSelection(0);
                break;
            case 5:
                spinnerAutoUpdateInterval.setSelection(1);
                break;
            case 15:
                spinnerAutoUpdateInterval.setSelection(2);
                break;
            case 30:
                spinnerAutoUpdateInterval.setSelection(3);
                break;
            case 60:
                spinnerAutoUpdateInterval.setSelection(4);
                break;
            default:
                spinnerAutoUpdateInterval.setSelection(0);
                break;
        }

        buttonSettingsGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, WeatherDetailActivity.class);
                startActivity(intent);
            }
        });
        buttonSettingsGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, CitySelectionActivity.class);
                startActivity(intent);
            }
        });
        //保存设置按钮事件
        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switchAutoUpdate如果没有被选择则不更新时间间隔
                if (switchAutoUpdate.isChecked()) {
                    Utilities.setAutoUpdateFlag(true);
                    switch (spinnerAutoUpdateInterval.getSelectedItemPosition()) {
                        case 0:
                            Utilities.setAutoUpdateInterval(2);
                            break;
                        case 1:
                            Utilities.setAutoUpdateInterval(5);
                            break;
                        case 2:
                            Utilities.setAutoUpdateInterval(15);
                            break;
                        case 3:
                            Utilities.setAutoUpdateInterval(30);
                            break;
                        case 4:
                            Utilities.setAutoUpdateInterval(60);
                            break;
                        default:
                            Utilities.setAutoUpdateInterval(2);
                            break;
                    }
                }
                else
                {
                    Utilities.setAutoUpdateFlag(false);
                }
                Toast.makeText(SettingActivity.this,"Settings saved.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
