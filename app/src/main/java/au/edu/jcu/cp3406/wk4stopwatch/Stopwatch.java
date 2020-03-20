package au.edu.jcu.cp3406.wk4stopwatch;

public class Stopwatch {
    private int hours;
    private int minutes;
    private int seconds;

    public Stopwatch() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    public Stopwatch(String current) {
        this.seconds = Integer.parseInt(current.substring(current.length()-2));
        this.minutes = Integer.parseInt(current.substring(3, 5));
        this.hours = Integer.parseInt(current.substring(0, 2));
        System.out.println(current);
        System.out.println(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds));
    }

    public String toString(){
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds);
    }

    public void tick() {
        if (seconds < 59) {
            seconds++;
        }else if (minutes < 59) {
            seconds = 0;
            minutes++;
        }else {
            seconds = 0;
            minutes = 0;
            hours++;
        }
    }

    public void reset() {
        seconds = 0;
        minutes = 0;
        hours = 0;
    }
}
