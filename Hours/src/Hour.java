public class Hour {
    int hour;
    int minute;
    public Hour (int time) {
        if (time == 0)
            return;
        hour = time/100;
        minute = time%100;
    }
    public Hour (Hour hour) {
        this.hour = hour.hour;
        this.minute = hour.minute;
    }

    public Hour minus (Hour a) {
        this.hour = this.hour - a.hour;
        this.minute = this.minute - a.minute;
        if (this.minute < -1) {
            this.hour -= 1;
            this.minute += 60;
        }
        if (this.minute > 59) {
            this.hour += 1;
            this.minute -= 60;
        }
        return this;
    }

    public Hour plus (Hour a) {
        this.hour = this.hour + a.hour;
        this.minute = this.minute + a.minute;
        if (this.minute > 59) {
            this.hour += 1;
            this.minute -= 60;
        }
        if (this.minute < -1) {
            this.hour -= 1;
            this.minute += 60;
        }
        return this;
    }

    @Override
    public String toString() {
        float corr = 100 * minute / 600;
        return (hour + "," + (int)corr);
    }
}
