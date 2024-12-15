import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Chat {
    @JsonProperty("name")
    public String name;
    @JsonProperty("type")
    public String type;
    @JsonProperty("id")
    public String id;
    @JsonProperty("messages")
    public List<Message> messages;
}
