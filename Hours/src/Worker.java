import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.List;

public class Worker {
    public String name;
    private List<WorkedHours> hours = new ArrayList<>();

    public Worker (String name) {
        this.name = name;
    }

    public void addHour (WorkedHours hour) {
        hours.add(hour);
    }

    public List<WorkedHours> getHours() {
        return hours;
    }

    public String countedHours;

    public String countedHoursToCopy;

    public void countHours() {
        Hour th = new Hour(0);
        TreeMap<Integer, String> schedule = new TreeMap<>();
        for (WorkedHours hour : hours) {
            th.plus(hour.getFinal());
            schedule.put(hour.getDay(), hour.getFinal().toString());
        }
        int fk = schedule.firstKey();

        if (fk < 16) {
            for (int i = 1; i < 17; i++) {
                if (!schedule.containsKey(i)) {
                    schedule.put(i, new Hour(0).toString());
                }
            }
        } else {
            for (int i = 16; i < 32; i++) {
                if (!schedule.containsKey(i)) {
                    schedule.put(i, new Hour(0).toString());
                }
            }
        }

        StringBuilder textcopy = new StringBuilder();


        for (Map.Entry<Integer, String> entry : schedule.entrySet()) {
            String value = entry.getValue();
            System.out.println(value);
            textcopy.append(value).append("\n");
        }
        textcopy.append(th.toString()).append("\n");

        countedHours = textcopy.toString();

        /*(textcopy.append("\n\n\n");
        for (WorkedHours hour : this.getHours()) {
            textcopy.append("\n" + hour.getTextHours() +
                    " Start: " + hour.getStartHour() +
                    " End: " + hour.getEndHour() +
                    " Break:" + hour.getBreakHour() +
                    " Work" + hour.getWorkedHour());
        }*/
        countedHoursToCopy = textcopy.toString();
    }
}
