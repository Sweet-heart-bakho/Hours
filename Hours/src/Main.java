import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;



public class Main {
    DataBase db = new DataBase();
    List<Worker> workers;
    UI frame = new UI();
    public Main() {
        //get data <UserName, Messages>
        frame.setMain(this);
    }
    public List<String> Calculate(Map<String, List<String>> hours) {

        List<String> result = new ArrayList<>();

        return result;
    }

    public void formatHours() {
        Map<String, List<String>> hours = db.getHours();
        workers = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry: hours.entrySet()) {
            Worker worker = new Worker(entry.getKey());
            for (String hour : entry.getValue()) {
                WorkedHours workedHours = new WorkedHours(hour);
                worker.addHour(workedHours);
            }
            workers.add(worker);
        }
        frame.getWorkers(workers);
    }

    public void getFile(File file) {
        db.getData(file.getAbsolutePath());

        formatHours();
    }
}