import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    Map<String, List<String>> hours = new HashMap<>();

    public void getData(String path) {
        List<Message> messages = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            File file = new File(path); //path
            Chat chat = objectMapper.readValue(file, Chat.class);

            messages = chat.messages;

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Message message : messages) {
            for (String line : message.text) {
                if ((line.contains("/") || line.contains(".")) && containsNumbers(line) && !line.matches(".*https\\b.*") && line.length() <= 150) {
                    String[] lines = line.split("\\r?\\n"); // Разделить текст на строки
                    for (String text : lines) {
                        if (text.contains("/")) { // Добавить только строки, содержащие "/"
                            List<String> textList = hours.getOrDefault(message.from, new ArrayList<>());
                            textList.add(text);
                            hours.put(message.from, textList);
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : hours.entrySet()) {
            String actor = entry.getKey();
            List<String> message = entry.getValue();
            System.out.println(actor + " " + message);
        }
    }

    private boolean containsNumbers(String line) {
        for (char c : line.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, List<String>> getHours() {
        return hours;
    }
}
