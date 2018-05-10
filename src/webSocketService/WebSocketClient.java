package webSocketService;

import apiREST.Cons;
import com.google.gson.Gson;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import subscriber.Subscriber;
import util.MyEvent;
import util.MySubscription;

@ClientEndpoint
public class WebSocketClient {

  static Map<String, Subscriber> subscriberMap;
  static Session session;

  public static void newInstance() {
    subscriberMap = new HashMap<String, Subscriber>();
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(WebSocketClient.class,
        URI.create(Cons.SERVER_WEBSOCKET));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //only one subscriber per topic allowed:
  public static synchronized void addSubscriber(String topic_name, Subscriber subscriber) {
      //TODO CHECK
    if (subscriber!=null)
        subscriberMap.put(topic_name, subscriber);
    
  }

  public static synchronized void removeSubscriber(String topic_name) {
      //TODO CHECK
    if(subscriberMap!=null && subscriberMap.containsKey(topic_name))
            subscriberMap.remove(topic_name);
  }

  public static void close() {
    try {
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  public void onMessage(String message) {

    Gson gson = new Gson();
    MyEvent myEvent = gson.fromJson(message, MyEvent.class);
    String topic = myEvent.topic;

    //message to warn closing a topic:
    if (topic.equals("CLOSED")) {
      
      //TODO CHECK
      subscriberMap.get(topic).onClose(topic, message);
      
    } 
    //ordinary message from topic:
    else {
      
      //TODO CHECK
      subscriberMap.get(topic).onEvent(topic, message);
      
    }
  }

}
