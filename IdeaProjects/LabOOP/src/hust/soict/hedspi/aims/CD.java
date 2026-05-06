package hust.soict.hedspi.aims;
import hust.soict.hedspi.aims.Track;

import java.util.ArrayList;

public class CD {
    private int id;
    private String title;
    private String category;
    private String artist;
    private String director;
    private float cost;
    private ArrayList<hust.soict.hedspi.aims.Track> tracks; // chi ro package day du
    private static int nbCDs = 0;

    public CD(String title, String category, String artist, String director, float cost) {
        this.title = title;
        this.category = category;
        this.artist = artist;
        this.director = director;
        this.cost = cost;
        this.tracks = new ArrayList<>();
        nbCDs++;
        this.id = nbCDs;
    }

    public void addTrack(hust.soict.hedspi.aims.Track track) {
        tracks.add(track);
    }

    public int getTotalLength() {
        int total = 0;
        for (hust.soict.hedspi.aims.Track t : tracks) {
            total += t.getLength();
        }
        return total;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getArtist() { return artist; }
    public String getDirector() { return director; }
    public float getCost() { return cost; }

    public void printInfo() {
        System.out.println("CD - ID: " + id);
        System.out.println("  Title: " + title);
        System.out.println("  Category: " + category);
        System.out.println("  Artist: " + artist);
        System.out.println("  Director: " + director);
        System.out.println("  Length: " + getTotalLength() + " secs");
        System.out.println("  Cost: " + cost + "$");
        System.out.println("  Tracks:");
        for (hust.soict.hedspi.aims.Track t : tracks) {
            System.out.println("    - " + t.toString());
        }
    }

    public void play() {
        System.out.println("Playing CD: " + title + " - " + getTotalLength() + " secs");
        for (hust.soict.hedspi.aims.Track t : tracks) {
            t.play(); // goi dung ten method play() cua PackageAimsProject.Track
        }
    }

    public String toString() {
        return "CD - " + title + " - " + category + " - " + artist + " - " + getTotalLength() + " secs : " + cost + "$";
    }
}
