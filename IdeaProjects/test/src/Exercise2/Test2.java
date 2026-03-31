package Exercise2;

import java.util.Scanner;

public class Test2 {
    public static boolean xemxettangluong(double luongCoBan){
        if(luongCoBan>300){
            return false;
        }
        else return true;
    }
    public static double tangluong(String name, double heSoLuong, double luongCoBan){
        if(xemxettangluong(luongCoBan)) {
            if (heSoLuong < 5) {
                heSoLuong = heSoLuong + 3;
            } else if (heSoLuong > 9) {
                heSoLuong = heSoLuong - 1;
            } else {
                heSoLuong = heSoLuong + 1;
            }
        }
        return heSoLuong;
    }
    public static void inthongtin (String name, double heSoLuong, double luongCoBan, double tongluong){
        System.out.println("Nhan vien "+ name +" co he so luong la "+ heSoLuong +" va luong co ban la "+luongCoBan+" nen tong luong nhan duoc la: "+tongluong);
    }
    public static double tinhluong(String name, double heSoLuong, double luongCoBan){
        double sum = heSoLuong*luongCoBan+luongCoBan;
        return sum;
    }
     public static void main(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap so luong nhan vien: ");
        int n= scanner.nextInt();
        scanner.nextLine();
        NhanVien[] nhanvien = new NhanVien[n];
        for(int i=0;i<n;i++){
            System.out.println("Ten nhan vien thu " +(i+1));
            String name = scanner.nextLine();
            System.out.println("Luong co ban cua nhan vien " +name + " la ");
            double luongCoBan = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("He so luong cua nhan vien " +name + " la ");
            double heSoLuong = scanner.nextDouble();
            nhanvien[i]=new NhanVien(name,heSoLuong,luongCoBan);
            scanner.nextLine();
        }
        scanner.close();
        for (int i=0;i<n;i++){
            tangluong(nhanvien[i].getName(),nhanvien[i].getheSoLuong(), nhanvien[i].getluongCoBan());
            double tongluong = tinhluong(nhanvien[i].getName(),nhanvien[i].getheSoLuong(),nhanvien[i].getluongCoBan());
            inthongtin(nhanvien[i].getName(),nhanvien[i].getheSoLuong(), nhanvien[i].getluongCoBan(),tongluong);
        }
    }
}
