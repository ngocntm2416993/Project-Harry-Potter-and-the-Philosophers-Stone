package hust.soict.hedspi.aims;

public class DigitalVideoDisc {
    private String title;
    private String category;
    private String director;
    private int length;
    private float cost;
    private static int nbDigitalVideoDiscs = 0;
    private int id;
    public DigitalVideoDisc(String title, String category, String director, int length, float cost) {
        this.title = title;
        this.category = category;
        this.director = director;
        this.length = length;
        this.cost = cost;

        nbDigitalVideoDiscs ++;
        this.id =nbDigitalVideoDiscs;
    }
    public DigitalVideoDisc(String title){
        this.title = title;
        nbDigitalVideoDiscs ++;
        this.id =nbDigitalVideoDiscs;
    }
//    Nếu khởi tạo 2 constructor bên dưới sẽ bị báo lỗi vì chương trình không phân biệt được 3 construcotr này
//    public DigitalVideoDisc(String category){
//        this.category = category;
//    }
//    public DigitalVideoDisc(String director ){
//        this.director = director;
//    }

    public DigitalVideoDisc(String title, String category, float cost) {
        this.title = title;
        this.category = category;
        this.cost=cost;
        nbDigitalVideoDiscs ++;
        this.id =nbDigitalVideoDiscs;
    }
    public DigitalVideoDisc(int length){
        this.length = length;
        nbDigitalVideoDiscs ++;
        this.id =nbDigitalVideoDiscs;
    }
    public DigitalVideoDisc(float cost) {
        this.cost = cost;
        nbDigitalVideoDiscs ++;
        this.id =nbDigitalVideoDiscs;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDirector() {
        return director;
    }

    public int getLength() {
        return length;
    }

    public float getCost() {
        return cost;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
    public String toString() { // the  return type of this method should be String
        return "DVD - " + title + " - " + category + " - " + director + " - " + length + " mins : " + cost + "$";
    }

    public boolean isMatch(String title) {
        return this.title.equals(title);
    }
    public void play1() {
        if (length <= 0) {
            System.out.println("Cannot play DVD: " + title + " - invalid length!");
        } else {
            System.out.println("Playing DVD: " + title + " - " + length + " mins");
        }
    }
}
