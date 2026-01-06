//package pt.ipleiria.estg.dei.projetoandroid;
//
//import android.content.Context;
//
//import org.eclipse.paho.android.service.MqttAndroidClient;
//import org.eclipse.paho.client.mqttv3.IMqttActionListener;
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.IMqttToken;
//import org.eclipse.paho.client.mqttv3.MqttCallback;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//
//import java.util.UUID;
//
//import pt.ipleiria.estg.dei.projetoandroid.utils.NotificationUtils;
//
//public class MqttManager {
//
//    private static MqttManager instance;
//    private final Context appContext;
//    private MqttAndroidClient client;
//
//    // Mosquitto no host da máquina (emulador usa 10.0.2.2)
//    private static final String SERVER_URI = "tcp://10.0.2.2:1883";
//
//    private MqttManager(Context context) {
//        this.appContext = context.getApplicationContext();
//    }
//
//    public static synchronized MqttManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new MqttManager(context);
//        }
//        return instance;
//    }
//
//    public void connectAndSubscribe(String topic) {
//        if (client != null && client.isConnected()) {
//            subscribe(topic);
//            return;
//        }
//
//        String clientId = "android-" + UUID.randomUUID();
//        client = new MqttAndroidClient(appContext, SERVER_URI, clientId);
//
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setAutomaticReconnect(true);
//        options.setCleanSession(true);
//
//        client.setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) { }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) {
//                String payload = new String(message.getPayload());
//                System.out.println("MQTT MESSAGE ARRIVED: topic=" + topic + " payload=" + payload);
//                // Aqui podes fazer parse ao JSON se quiseres
//                NotificationUtils.showNotification(
//                        appContext,
//                        "Nova mensagem",
//                        "Recebeste uma nova mensagem"
//                );
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) { }
//        });
//
//        try {
//            client.connect(options, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    subscribe(topic);
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    exception.printStackTrace();
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void subscribe(String topic) {
//        try {
//            if (client != null && client.isConnected()) {
//                client.subscribe(topic, 1);
//            }
//        } catch (MqttException e) {
//        }
//    }
//}
