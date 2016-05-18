package ch.fhnw.taurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.regex.Pattern;

public class ConnectActivity extends AppCompatActivity {

    private static final String LOG_TAG = ConnectActivity.class.getName();
    private EditText ipAddressEditText;
    private EditText ipPortEditText;
    private ConnectionModel model;
    private static final Pattern IP_PATTERN = Pattern.compile("[0-9]{1,3}([\\.][0-9]{1,3}){3}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate()");
        setContentView(R.layout.activity_connect);
        ipAddressEditText = (EditText) this.findViewById(R.id.ip_address);
        ipPortEditText = (EditText) this.findViewById(R.id.ip_port);
        model = new ConnectionModel();
        loadSettings(savedInstanceState);

        ipAddressEditText.setText(model.getIp());
        ipPortEditText.setText(""+model.getPort());

        ipAddressEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateAndUpdateIp();

                }
            }
        });

        ipPortEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateAndUpdatePort();
                }
            }
        });
    }

    private void validateAndUpdateIp() {
        // Update validation fields
        String ip = ipAddressEditText.getText().toString().trim();
        String errorMsg = validateIpAddress(ip);
        ipAddressEditText.setError(errorMsg);
        if(errorMsg == null) {
            model.setIp(ip);
        }
    }

    private void validateAndUpdatePort() {
        String port = ipPortEditText.getText().toString().trim();
        String errorMsg = validateIpPort(port);
        ipPortEditText.setError(errorMsg);
        if(errorMsg == null) {
            int portNumber = Integer.parseInt(port);
            model.setPort(portNumber);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onSaveInstanceState() called");
        savedInstanceState.putSerializable(Contract.TAG_CONNECTION_MODEL, this.model);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onRestoreInstanceState() called");
        loadSettings(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void connect(View view) {
        Log.v(LOG_TAG,"connect()");
        validateAndUpdateIp();
        validateAndUpdatePort();
        if(isValid()) {
            Intent intent = new Intent(this,MainActivity.class);

            intent.putExtra(Contract.TAG_CONNECTION_MODEL, this.model);
            startActivityForResult(intent,Contract.CONNECT_REQUEST_CODE);
        }
        else {
            Log.d(LOG_TAG,"Can not connect yet. Errors detected");
            Toast.makeText(this,R.string.please_fix_input,Toast.LENGTH_LONG).show();
        }
    }

    private void loadSettings(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"loadSettings()");
        if(savedInstanceState == null) {
            Log.d(LOG_TAG,"SavedInstanceState is null");
            return;
        }
        if(!savedInstanceState.containsKey(Contract.TAG_CONNECTION_MODEL)) {
            Log.d(LOG_TAG, "SavedInstanceState does not contain the expected information");
            return;
        }
        ConnectionModel stored = (ConnectionModel)savedInstanceState.getSerializable(Contract.TAG_CONNECTION_MODEL);
        this.model.setIp(stored.getIp());
        this.model.setPort(stored.getPort());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Contract.CONNECT_REQUEST_CODE == requestCode) {
            // TODO
        }
    }

    private boolean isValid() {
        return  ipAddressEditText.getError() == null
                && ipPortEditText.getError() == null;
    }

    private String validateIpAddress(String text) {
        if(text == null || text.length() == 0) {
            Log.d(LOG_TAG,MessageFormat.format("Filed required",text));
            return getResources().getString(R.string.field_required);
        }

        if(!IP_PATTERN.matcher(text).matches()) {
            return getResources().getString(R.string.invalid_format);
        }

        return null;
    }

    private String validateIpPort(String text) {
        if(text == null || text.length() == 0) {
            Log.d(LOG_TAG,MessageFormat.format("Filed required",text));
            return getResources().getText(R.string.field_required).toString();
        }
        try {
            int port = Integer.parseInt(text.toString());
            if(port < 0 || port > 65535) {
                Log.d(LOG_TAG,MessageFormat.format("Invalid range value",text));
                return getResources().getString(R.string.invalid_range);
            }

        } catch(NumberFormatException ex) {
            Log.d(LOG_TAG,MessageFormat.format("Invalid format.Text '{0}' can not be parsed",text));
            return getResources().getString(R.string.invalid_format);
        }
        return null;
    }
}
