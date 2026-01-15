package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import pt.ipleiria.estg.dei.projetoandroid.utils.ServerConfig;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import pt.ipleiria.estg.dei.projetoandroid.utils.NotificationUtils;

public class MqttManager {

    private static MqttManager instance;

    private final Context appContext;
    private MqttAndroidClient client;

    // ✅ Tablet / dispositivo físico: IP do teu PC na rede
    // (No emulador Android Studio seria tcp://10.0.2.2:1883)
   // private static final String SERVER_URI = "tcp://172.20.10.2:1883";

    private static final int QOS = 1;

    // Guarda o último tópico para resubscrever após reconnect
    private String lastTopic;

    private MqttManager(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public static synchronized MqttManager getInstance(Context context) {
        if (instance == null) {
            instance = new MqttManager(context);
        }
        return instance;
    }

    /**
     * Conecta (se necessário) e subscreve ao tópico.
     * Re-subscreve automaticamente em reconnects.
     */
    public synchronized void connectAndSubscribe(String topic) {

        String serverUri = ServerConfig.getMqttUri(appContext);
        System.out.println("MQTT serverUri=" + serverUri);


        if (topic == null || topic.trim().isEmpty()) {
            System.out.println("MQTT connectAndSubscribe: topic vazio, ignorado");
            return;
        }

        lastTopic = topic.trim();
        System.out.println("MQTT connectAndSubscribe topic=" + lastTopic);

        // Se já estiver ligado, só subscreve
        if (client != null && client.isConnected()) {
            System.out.println("MQTT already connected, subscribing...");
            subscribe(lastTopic);
            return;
        }

        String clientId = "android-" + UUID.randomUUID();
        client = new MqttAndroidClient(
                appContext,
                serverUri,
                clientId,
                Ack.AUTO_ACK
        );
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);

        // ✅ Melhor para reconnects: mantém sessão no broker e reduz perdas de subscrição
        options.setCleanSession(false);

        // (Opcional) Timeout do connect (segundos)
        options.setConnectionTimeout(10);

        // (Opcional) Keep alive (segundos)
        options.setKeepAliveInterval(30);

        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                System.out.println("MQTT connectComplete reconnect=" + reconnect + " serverURI=" + serverURI);
                // ✅ Sempre que liga/reconecta, garante que está subscrito
                if (lastTopic != null) {
                    subscribe(lastTopic);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("MQTT connectionLost: " + (cause != null ? cause.toString() : "null"));
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload;
                try {
                    payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    payload = "(payload inválido)";
                }

                System.out.println("MQTT MESSAGE ARRIVED: topic=" + topic + " payload=" + payload);

                // ✅ Notificação (vai respeitar permissões no NotificationUtils)
                NotificationUtils.showNotification(
                        appContext,
                        "Nova mensagem",
                        "Recebeste uma nova mensagem"
                );
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // só relevante se publicares mensagens a partir do Android
            }
        });

        try {
            System.out.println("MQTT connecting to " + serverUri + " clientId=" + clientId);

            client.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("MQTT connected OK");
                    // subscribe é também chamado no connectComplete(), mas não faz mal repetir
                    subscribe(lastTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("MQTT connect FAIL: " + (exception != null ? exception.toString() : "null"));
                    if (exception != null) exception.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void subscribe(String topic) {
        if (topic == null || topic.trim().isEmpty()) return;

        try {
            if (client != null && client.isConnected()) {
                System.out.println("MQTT subscribing to " + topic);

                client.subscribe(topic, QOS, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        System.out.println("MQTT subscribed OK: " + topic);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        System.out.println("MQTT subscribe FAIL: " + (exception != null ? exception.toString() : "null"));
                        if (exception != null) exception.printStackTrace();
                    }
                });

            } else {
                System.out.println("MQTT can't subscribe, not connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opcional: chamar no logout/saída para libertar recursos.
     */
    public synchronized void disconnect() {
        try {
            if (client != null) {
                if (client.isConnected()) {
                    client.disconnect();
                }
                client.unregisterResources();
                client.close();
            }
        } catch (Exception e) {
            System.out.println("MQTT disconnect exception: " + e);
        } finally {
            client = null;
            lastTopic = null;
        }
    }
}
