import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkedHours {
    private int day;
    private Hour startHour;
    private Hour endHour;
    private Hour workedHour;
    private Hour breakHour;
    private String textHours;

    public WorkedHours(String hour) {
        textHours = hour;
        breakHour = new Hour(30);
        encodeHour(0);
        countHour();
    }

    public void encodeHour(int mode) {
        try {
            String text = textHours;
            String date;
            String rest;

            //first find day
            int spaceIndex = text.indexOf(' ');
            date = text.substring(0, spaceIndex);
            date = date.replace(" ", "");
            day = Integer.parseInt(date.replaceFirst("^0*", "").split("/")[0]);

            //find hours, only int
            rest = text.substring(spaceIndex + 1).replaceAll("\\D", "");
            //remove first 0
            rest = rest.replaceFirst("^0+(?!$)", "");
            rest = rest.substring(0, Math.min(rest.length(), 8));
            //only int
            int result = Integer.parseInt(rest);

            //split int
            switch (Math.min(rest.length(), 8)) {
                case 6:
                    startHour = new Hour(result / 1000);
                    endHour = new Hour(result % 1000);
                    break;
                case 7:
                case 8:
                    startHour = new Hour(result / 10000);
                    endHour = new Hour(result % 10000);
                    break;
                default:
                    System.out.println("Error in: " + textHours);
            }

            if(mode == 0) {
                if (!isItSaturday(date))
                    breakHour = new Hour(30);
                else
                    breakHour = new Hour(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem in :" + textHours);
        }
    }

    private boolean isItSaturday(String date) {
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
    public void countHour() {
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
    public void setTextHours(String text, String breakHour) {
        textHours = text;
        this.breakHour = new Hour(Integer.parseInt(breakHour));
        encodeHour(1);
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
