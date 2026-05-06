package hust.soict.hedspi.aims;

public class Track {
    private String title;
    private int length;

    public Track(String title, int length) {
        this.title = title;
        this.length = length;
    }

    public String getTitle() { return title; }
    public int getLength() { return length; }

    public void play() {
        if (length <= 0) {
            System.out.println("Cannot play track: " + title + " - invalid length!");
        } else {
            System.out.println("Playing track: " + title + " - " + length + " secs");
        }
    }

    public String toString() {
        return "Track: " + title + " - " + length + " secs";
    }
}
