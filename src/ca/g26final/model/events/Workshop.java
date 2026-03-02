package ca.g26final.model.events;

import java.time.LocalDateTime;

public class Workshop extends Event{
    private final String topic;

    public Workshop(String eventID, String title, LocalDateTime dateTime, String location, int capacity, String topic) {
        super(eventID, title, dateTime, location, capacity);

        if (topic == null || topic.isBlank()) {
            System.out.println("[Workshop] Invalid topic. Using 'TBD'.");
            this.topic = "TBD";
        } else {
            this.topic = topic;
        }
    }
    
    //Getter
    public String getTopic() {
        return topic;
    }


    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", topic='" + topic + "'}";
    }


}
