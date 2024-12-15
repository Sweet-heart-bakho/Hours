import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @JsonProperty("id")
    public int id;
    @JsonProperty("type")
    public String type;
    @JsonProperty("date")
    public String date;
    @JsonProperty("date_unixtime")
    public String date_unixtime;
    @JsonProperty("actor")
    public String actor;
    @JsonProperty("actor_id")
    public String actorId;
    @JsonProperty("action")
    public String action;
    @JsonProperty("members")
    public List<String> members;
    @JsonProperty("from")
    public String from;
    @JsonProperty("from_id")
    public String fromId;
    @JsonProperty("text")
    public List<String> text;
    @JsonProperty("text_entities")
    public List<TextEntity> textEntities;
}
