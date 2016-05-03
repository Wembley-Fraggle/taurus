package ch.fhnw.taurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class ConnectActivity extends AppCompatActivity {

    private static final String LOG_TAG = ConnectActivity.class.getName();
    private static final String TAG_IP_ADDRESS = "ip_address";
    private static final String TAG_IP_PORT    = "ip_port";
    private EditText ipAddressEditText;
    private EditText ipPortEditText;
    private static final Pattern IP_PATTERN = Pattern.compile("[0-9]{1,3}([\\.][0-9]{1,3}){3}");
    private static final int CONNECT_REQUEST_CODE = 1;
    private static final int RESULT_OK = 0;
    private static final String TAG_ERROR_MSG = "errorMsg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate()");
        setContentView(R.layout.activity_connect);
        ipAddressEditText = (EditText) this.findViewById(R.id.ip_address);
        ipPortEditText = (EditText) this.findViewById(R.id.ip_port);

        ipAddressEditText.addTextChangedListener(new TextValidator(ipAddressEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if(!IP_PATTERN.matcher(textView.getText()).matches()){
                    CharSequence msg = ConnectActivity.this.getResources().getText(R.string.invalid_ip_format);
                    textView.setError(msg);
                }
            }
        });
        if(savedInstanceState != null) {
            loadSettings(savedInstanceState);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onSaveInstanceState() called");
        savedInstanceState.putString(TAG_IP_ADDRESS, ipAddressEditText.getText().toString());
        savedInstanceState.putInt(TAG_IP_PORT, Integer.parseInt(ipPortEditText.getText().toString()));
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateSettingsToBundle(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"updateSettingsToBundle() called");
        savedInstanceState.putString(TAG_IP_ADDRESS, ipAddressEditText.getText().toString());
        savedInstanceState.putInt(TAG_IP_PORT, Integer.parseInt(ipPortEditText.getText().toString()));

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onRestoreInstanceState() called");
        loadSettings(savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    public void connect(View view) {
        Log.v(LOG_TAG,"connect()");
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(TAG_IP_ADDRESS, ipAddressEditText.getText());
        intent.putExtra(TAG_IP_PORT,Integer.parseInt(ipPortEditText.getText().toString()));
        startActivityForResult(intent,CONNECT_REQUEST_CODE);
    }

    private void loadSettings(Bundle savedInstanceState) {
        ipAddressEditText.setText(savedInstanceState.getString(TAG_IP_ADDRESS,""));
        ipPortEditText.setText(savedInstanceState.getInt(TAG_IP_PORT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(CONNECT_REQUEST_CODE == requestCode) {
            if(resultCode != RESULT_OK && data.hasExtra(TAG_ERROR_MSG)) {
                Toast.makeText(this,data.getStringExtra(TAG_ERROR_MSG),Toast.LENGTH_LONG);
            }
        }

    }
}
