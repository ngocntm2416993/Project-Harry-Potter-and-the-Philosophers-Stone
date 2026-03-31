package Exercise1;

public class Student {

    private String name;
    private int year;

    public Student (  String name, int year){
        this.year=year;
        this.name=name;
    }
    public int getYear(){
        return year;
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
}
