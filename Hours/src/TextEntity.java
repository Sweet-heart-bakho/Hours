import com.fasterxml.jackson.annotation.JsonProperty;

public class TextEntity {
        @JsonProperty("type")
        public String type;
        @JsonProperty("text")
        public String text;
}
