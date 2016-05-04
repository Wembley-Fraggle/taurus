package ch.fhnw.taurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class ConnectActivity extends AppCompatActivity {

    private static final String LOG_TAG = ConnectActivity.class.getName();
    private EditText ipAddressEditText;
    private EditText ipPortEditText;
    private static final Pattern IP_PATTERN = Pattern.compile("[0-9]{1,3}([\\.][0-9]{1,3}){3}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate()");
        setContentView(R.layout.activity_connect);
        ipAddressEditText = (EditText) this.findViewById(R.id.ip_address);
        ipPortEditText = (EditText) this.findViewById(R.id.ip_port);
        if(savedInstanceState != null) {
            loadSettings(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onSaveInstanceState() called");
        savedInstanceState.putString(Contract.TAG_IP_ADDRESS, ipAddressEditText.getText().toString());
        try {
            savedInstanceState.putInt(Contract.TAG_IP_PORT, Integer.parseInt(ipPortEditText.getText().toString()));
        }
        catch(NumberFormatException ex) {
            Log.d(LOG_TAG, MessageFormat.format("Failed to parse integer:{0}",ipPortEditText.getText()));
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateSettingsToBundle(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"updateSettingsToBundle() called");
        savedInstanceState.putString(Contract.TAG_IP_ADDRESS, ipAddressEditText.getText().toString());
        try {
            savedInstanceState.putInt(Contract.TAG_IP_PORT, Integer.parseInt(ipPortEditText.getText().toString()));
        }
        catch(NumberFormatException ex) {
            Log.d(LOG_TAG,"Failed to parse integer");
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onRestoreInstanceState() called");
        loadSettings(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void connect(View view) {
        Log.v(LOG_TAG,"connect()");
        List<View> errors = validate();
        if(errors.isEmpty()) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra(Contract.TAG_IP_ADDRESS, ipAddressEditText.getText());
            intent.putExtra(Contract.TAG_IP_PORT,Integer.parseInt(ipPortEditText.getText().toString()));
            startActivityForResult(intent,Contract.CONNECT_REQUEST_CODE);
        }
        else {
            Log.d(LOG_TAG,"Can not connect yet. Errors detected");
            Toast.makeText(this,R.string.please_fix_input,Toast.LENGTH_LONG).show();
        }
    }

    private void loadSettings(Bundle savedInstanceState) {
        ipAddressEditText.setText(savedInstanceState.getString(Contract.TAG_IP_ADDRESS,""));
        ipPortEditText.setText(savedInstanceState.getInt(Contract.TAG_IP_PORT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Contract.CONNECT_REQUEST_CODE == requestCode) {
            // TODO
        }
    }
    private List<View> validate() {
        List<View> errorList = new LinkedList<View>();
        errorList = validateIpAddress(errorList);
        return validateIpPort(errorList);
    }

    private List<View> validateIpAddress(List<View> errorList ) {
        String text = ipAddressEditText.getText().toString();
        if(text == null || text.isEmpty()) {
            ipAddressEditText.setError(getResources().getText(R.string.field_required));
            return addError(ipAddressEditText,errorList);
        }

        if(! IP_PATTERN.matcher(text).matches()) {
            ipAddressEditText.setError(ConnectActivity.this.getResources().getText(R.string.invalid_ip_format));
            return addError(ipAddressEditText,errorList);
        }

        // Valid
        ipAddressEditText.setError(null);
        return errorList;
    }

    private List<View> addError(View view, List<View> errorList) {
        List<View> newErrors = new LinkedList<>(errorList);
        newErrors.add(view);
        return newErrors;
    }

    private List<View> validateIpPort(List<View> errorList) {
        String text = ipPortEditText.getText().toString();
        if(text == null || text.isEmpty()) {
            Log.d(LOG_TAG,MessageFormat.format("Port is null or empty",text));
            ipPortEditText.setError(getResources().getText(R.string.field_required));
            return addError(ipPortEditText,errorList);
        }
        try {
            int port = Integer.parseInt(text);
            if(port < 0) {
                ipPortEditText.setError(getResources().getString(R.string.positive_int_requried));
                return addError(ipPortEditText,errorList);
            }

        } catch(NumberFormatException ex) {
            Log.d(LOG_TAG,MessageFormat.format("Invalid port format. Value '{0}' can not be parsed",text));
            ipPortEditText.setError(getResources().getText(R.string.invvalid_port_format));
            return addError(ipPortEditText,errorList);
        }

        ipPortEditText.setError(null);
        return errorList;
    }
}
