package publisher;

import apiREST.apiREST_Publisher;

import util.MyEvent;
import webSocketService.WebSocketClient;

public class PublisherStub implements Publisher {

  String topic;

  public PublisherStub(){
  
  }
  
  public PublisherStub(String topic) {
    this.topic = topic;
  }

  public void publish(String topic, String event) {
      //DONE
      MyEvent mEvent = new MyEvent(topic, event);
    apiREST_Publisher.publish(mEvent);
    
      
  }

}
