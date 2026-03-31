package Exercise2;

public class NhanVien {
    private String name;
    private double heSoLuong;
    private double luongCoBan;

    public NhanVien(String name, double heSoLuong, double luongCoBan){
        this.name=name;
        this.luongCoBan=luongCoBan;
        this.heSoLuong=heSoLuong;
    }

    public void setName(String name){
        this.name=name;
    }
    public void setheSoLuong(double heSoLuong){
        this.heSoLuong=heSoLuong;
    }
    public void setLuongCoBan(double luongCoBan){
        this.luongCoBan=luongCoBan;
    }

    public String getName(){
        return name;
    }
    public double getluongCoBan(){
        return luongCoBan;
    }

    public double getheSoLuong() {
        return heSoLuong;
    }
}
