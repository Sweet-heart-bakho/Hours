import java.awt.*;
import java.util.List;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Simple {
    public static void main (String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Hour th = new Hour(0);
        String str = "0";
        TreeMap<Integer, Hour> schedule = new TreeMap<>();

        while (true) {
            System.out.println("Data and Time:");
            str = reader.readLine();

            if (str.equals("-")) {
                end(schedule, th);
                th = new Hour(0);
                str = "0";
                schedule = new TreeMap<>();
                System.out.println("-----Restart-----");
                continue;
            }

            List<String> en = encode(str);

            String data = en.get(0);
            String startHours = en.get(1);
            String endHours = en.get(2);

            StringBuilder numericSubstring = new StringBuilder();
            for (char ch: data.toCharArray()) {
                if (Character.isDigit(ch)) {
                    numericSubstring.append(ch);
                }
                else{
                    break;
                }
            }
            int day = Integer.parseInt(numericSubstring.toString().trim());

            startHours = startHours.substring(0, startHours.length() - 2) + startHours.substring(startHours.length() - 2, startHours.length());

            endHours = endHours.substring(0, endHours.length() - 2) + endHours.substring(endHours.length() - 2, endHours.length());

            Hour sh = new Hour(Integer.parseInt(startHours));
            Hour eh = new Hour(Integer.parseInt(endHours));

            eh.minus(sh);

            if (eh.hour < 0) {
                eh.plus(new Hour(1200));
            }

            if (en.size() > 3) {
                eh.minus(new Hour(Integer.parseInt(en.get(3))));
            }

            boolean isLateStart = Integer.parseInt(startHours) > 1259;
            boolean isEarlyEnd = Integer.parseInt(endHours) < 1301;
            boolean isSaturday = isItSatursay(data);

            if (!isSaturday) {
                if (!isLateStart && !isEarlyEnd) {
                    //System.out.println(!isLateStart + " " + !isEarlyEnd);
                    eh.minus(new Hour(30));
                }
            }

            th.plus(eh);
            System.out.println(eh);
            schedule.put(day, eh);
        }
    }

    public static void end (TreeMap<Integer, Hour> schedule, Hour th) {
        int fk = schedule.firstKey();

        if (fk < 16) {
            for (int i = 1; i < 16; i++) {
                if (!schedule.containsKey(i)) {
                    schedule.put(i, new Hour(0));
                }
            }
        } else {
            for (int i = 16; i < 31; i++) {
                if (!schedule.containsKey(i)) {
                    schedule.put(i, new Hour(0));
                }
            }
        }

//        for (Map.Entry<Integer, Hour> entry : schedule.entrySet()) {
//            Integer key = entry.getKey();
//            Hour value = entry.getValue();
//            System.out.println(key + " " + value);
//        }
        StringBuilder textcopy = new StringBuilder();


        for (Map.Entry<Integer, Hour> entry : schedule.entrySet()) {
            Hour value = entry.getValue();
            System.out.println(value);
            textcopy.append(value).append("\n");
        }
        String coo = textcopy.toString();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(coo);
        clipboard.setContents(stringSelection, null);

        System.out.println(th);
        System.out.println(th.hour + ":" + th.minute);
    }


    public static List<String> encode(String text) {
        int firstIndexSpace = text.indexOf(" ");
        List<String> split = new ArrayList<>();

        split.add(text.substring(0, firstIndexSpace));
        String time = text.replaceAll("\\s", "");
        time = time.substring(firstIndexSpace, time.length());
        StringBuilder clearTime = new StringBuilder("");

        for (int i = 0; i < time.length(); i++) {
            if (isInteger(time.charAt(i))) {
                clearTime.append(time.charAt(i));
            }
        }


        //check 0 first
        if (clearTime.substring(0, 1).equals("0")) {
            clearTime.deleteCharAt(0);
        }

        System.out.println(clearTime);


        // check extra lunch
        String extra = "";
        if (clearTime.length() > 9) {
            extra = clearTime.substring(Math.max(clearTime.length() - 3, 0));
            clearTime.delete(clearTime.length() - 3, clearTime.length());
        }

        if (clearTime.substring(0, 1).equals("1")) {
            split.add(clearTime.substring(0, 4));
            split.add(clearTime.substring(4, 8));
        }
        else {
            split.add(clearTime.substring(0, 3));
            split.add(clearTime.substring(3, clearTime.length()));
        }

        if (!extra.equals("")) {
            split.add(extra);
        }

        System.out.println(split);

        return split;
    }

    public static boolean isInteger(char input) {
        if (Character.isDigit(input)) {
            return true;
        }
        return false;
    }

    public static boolean isItSatursay(String date) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // Форматируем дату из строки
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            // Добавляем текущий год к введенной дате для получения полной даты
            Date parsedDate = dateFormat.parse(date + "/" + currentYear);

            // Устанавливаем дату в Calendar
            calendar.setTime(parsedDate);

            // Проверяем, является ли указанная дата субботой
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return (dayOfWeek == Calendar.SATURDAY);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
