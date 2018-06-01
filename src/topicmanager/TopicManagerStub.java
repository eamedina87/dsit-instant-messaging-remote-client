package topicmanager;

import apiREST.apiREST_Publisher;
import apiREST.apiREST_TopicManager;
import com.google.gson.Gson;
import java.util.Iterator;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherStub;
import subscriber.Subscriber;
import util.MyEvent;
import util.MySubscription;
import webSocketService.WebSocketClient;
import webSocketService.WebSocketClientFinal;

public class TopicManagerStub implements TopicManager {

  public String user;
    private Set<String> mTopics;
    
  public TopicManagerStub(){
  }
    
    
  public TopicManagerStub(String user) {
    //WebSocketClient.newInstance();
    WebSocketClientFinal.getInstance();
    this.user = user;
  }

  public void close() {
    WebSocketClientFinal.close();
  }

  public Publisher addPublisherToTopic(String topic) {
    PublisherStub publisher = new PublisherStub(topic);
    apiREST_TopicManager.addPublisherToTopic(topic);
    return publisher;

  }

  public int removePublisherFromTopic(String topic) {
    return apiREST_TopicManager.removePublisherFromTopic(topic);
  }

  public boolean isTopic(String topic_name) {
    return apiREST_TopicManager.isTopic(topic_name);
  }

  public Set<String> topics() {
    return apiREST_TopicManager.topics();
  }

  public boolean subscribe(String topic, Subscriber subscriber) {
      //TODO CHECK
    if (!isTopic(topic))
        return false;
    MySubscription subs = new MySubscription();
    subs.topic = topic;
    subs.type = true;
    Gson gson = new Gson();
    String message = gson.toJson(subs);
    WebSocketClientFinal.sendMessage(message);
    WebSocketClientFinal.addSubscriber(topic, subscriber);
    return true;
  }

  public boolean unsubscribe(String topic, Subscriber subscriber) {
    if (!isTopic(topic))
        return false;      
    MySubscription subs = new MySubscription();
    subs.topic = topic;
    subs.type = false;
    Gson gson = new Gson();
    String message = gson.toJson(subs);
    WebSocketClientFinal.sendMessage(message);
    WebSocketClientFinal.removeSubscriber(topic);
    return true;
  }

}
