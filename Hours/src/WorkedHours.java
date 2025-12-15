import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkedHours {
    private Boolean pass = false;
    private Worker worker;
    private int day;
    private Hour startHour;
    private Hour endHour;
    private Hour workedHour;
    private Hour breakHour;
    private String textHours;
    private final boolean SATURDAY_SHOT_DAY = false;
    private int BREAK_TIME = 30;

    public WorkedHours(String hour, Worker worker) {
        this.worker = worker;
        textHours = hour;

        breakHour = new Hour(BREAK_TIME);
        startHour = new Hour(645);
        endHour = new Hour(1745);
        workedHour = new Hour(0);

        encodeHour();
        countHour();
    }

    public void encodeHour() {
        try {
            String text = textHours;

            // Проверка минимального формата: должен быть день с "/"
            if (!text.matches(".*\\d{1,2}/\\d{1,2}.*")) {
                // Формат не подходит — просто выходим
                return;
            }

            String date;
            String rest;

            // remove all words and spec symbols and spaces
            text = text.replaceAll("[^0-9/()\\s]", "");

            // first find day
            int spaceIndex = text.indexOf(' ');
            date = text.substring(0, spaceIndex);
            date = date.replace(" ", "");
            day = Integer.parseInt(date.replaceFirst("^0*", "").split("/")[0]);

            // find extra break time
            int startIndex = text.indexOf('(');
            int endIndex = text.indexOf(')');

            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                String newBreakTime = text.substring(startIndex + 1, endIndex).trim();

                String digitsOnly = newBreakTime.replaceAll("[^0-9]", "");
                if (!digitsOnly.isEmpty()) {
                    // если нашли число → всегда обновляем
                    breakHour = new Hour(Integer.parseInt(digitsOnly));
                } else {
                    // если число не нашли → ставим дефолт только если breakHour ещё не задан
                    if (breakHour == null) {
                        breakHour = new Hour(30);
                    }
                }

                // удалить скобки
                text = text.substring(0, startIndex) + text.substring(endIndex + 1);
            }

            // find hours
            rest = text.substring(spaceIndex + 1).replaceAll("\\D", "");
            rest = rest.replaceFirst("^0+(?!$)", "");
            rest = rest.substring(0, Math.min(rest.length(), 8));
            int result = Integer.parseInt(rest);

            switch (Math.min(rest.length(), 8)) {
                case 6:
                    startHour = new Hour(result / 1000);
                    endHour = new Hour(result % 1000);
                    pass = true;
                    break;
                case 7:
                case 8:
                    startHour = new Hour(result / 10000);
                    endHour = new Hour(result % 10000);
                    pass = true;
                    break;
                default:
                    System.out.println("Error in: " + textHours);
            }
        } catch (Exception e) {
            // вместо добавления "error" — просто ничего не трогаем
//            startHour = new Hour(645);
//            endHour = new Hour(1745);
//            breakHour = new Hour(30);
            System.out.println("⚠️ Problem in: " + worker.name + " " + textHours);
        }
    }

    public void countHour() {
        if (!pass) {
            return;
        }
        workedHour = new Hour(endHour);
        workedHour.minus(startHour);
        workedHour.minus(breakHour);
        if (workedHour.hour < 0) {
            workedHour.plus(new Hour(1200));
        }
    }
    public Hour countSumHour (Hour hour) {
        return workedHour.plus(hour);
    }
    public int getDay() {
        return day;
    }
    public String getTextHours () {
        return textHours;
    }

    public void setBreakHour(int breakHour) {
        this.breakHour = new Hour(breakHour);
    }

    //from ui correct
    public void setTextHours(String text, String breakHour) {
        textHours = text;
        this.breakHour = new Hour(Integer.parseInt(breakHour));
        encodeHour();
        countHour();
    }

    public String getStartHour() {
        return String.valueOf(startHour.hour * 100 + startHour.minute);
    }
    public String getBreakHour() {
        return String.valueOf(breakHour.hour * 100 + breakHour.minute);
    }
    public String getEndHour() {
        return String.valueOf(endHour.hour * 100 + endHour.minute);
    }
    public String getWorkedHour() {
        return String.valueOf(workedHour.hour * 100 + workedHour.minute);
    }
    public Hour getFinal() {
        return workedHour;
    }
}
