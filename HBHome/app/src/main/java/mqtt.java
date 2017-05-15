/**
 * Created by MyPC on 15/05/2017.
 */
import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class mqtt {
    public mqtt() {
        super();
    }

    private String clientId;
    private IMqttToken token;
    private MqttAndroidClient client;
    private Context context;
    private String MqttServer;
    private String MqttUsername;
    private String MqttPassword;
    private MqttConnectOptions MqttOptions;

    public void connect(Context context, String server, String port, String username, String password){
        this.context = context;
        this.MqttServer = "tcp://"+server+":"+port;
        this.MqttUsername = username;
        this.MqttPassword = password;
        this.MqttOptions.setUserName(this.MqttUsername);
        this.MqttOptions.setPassword(this.MqttPassword.toCharArray());
        this.clientId = MqttClient.generateClientId();
        this.client = new MqttAndroidClient(this.context, this.MqttServer, clientId);
        try {
            this.token = this.client.connect(this.MqttOptions);
            this.token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.isConnected()
    }

    public void disconnect(){
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String topic, String payload){
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
            return true;
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
            return false;
        }
    }
}
