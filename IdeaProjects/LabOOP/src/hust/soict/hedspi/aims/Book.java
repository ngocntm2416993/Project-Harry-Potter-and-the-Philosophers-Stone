package hust.soict.hedspi.aims;

import java.util.ArrayList;

public class Book {
    private int id;
    private String title;
    private String category;
    private float cost;
    //private String artist;
    //private String director;
    //private String track_list;
    private ArrayList<String> list_of_authorsc;
    private static int nbBook = 0;

    public Book(String title, String category, float cost, ArrayList<String> list_of_authors) {
        this.title = title;
        this.category = category;
        this.list_of_authorsc = list_of_authors;
        this.cost = cost;
        nbBook++;
        this.id = nbBook;
    }

    public Book(String title){
        this.title = title;
        nbBook ++;
        this.id =nbBook;
    }

    public Book(String title, float cost) {
        this.title = title;
        this.cost=cost;
    }
    public Book(float cost) {
        this.cost = cost;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public float getCost() { return cost; }
    public ArrayList<String> getAuthors() { return list_of_authorsc; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setList_of_authorsc(ArrayList<String> list_of_authorsc) {
        this.list_of_authorsc = list_of_authorsc;
    }

    public static void setNbBook(int nbBook) {
        Book.nbBook = nbBook;
    }

    public String toString() {
        return "Book - " + title + " - " + category + " - " + list_of_authorsc + " : " + cost + "$";
    }
}
