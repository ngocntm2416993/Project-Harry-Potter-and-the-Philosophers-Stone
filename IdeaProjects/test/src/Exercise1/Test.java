package Exercise1;

import java.util.Scanner;

public class Test {
    static void main() { // o java 26 khong can public? chi can main voi?
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap so luong sv trong lop: ");
        int n = scanner.nextInt();
        scanner.nextLine();
        Student[] classsv = new Student[n];
        for(int i=0;i<n;i++) {
            System.out.println("nhap sv thu " + (i + 1));
            System.out.println("Name ");
            String name = scanner.next(); // tai sao lai la next ma khong phai la nextline
            System.out.println("Year ");
            int year = scanner.nextInt();
            scanner.nextLine();
            classsv[i] = new Student(name, year);
        }
        scanner.close();// ket thuc nhap?
        int total=0;
        System.out.println("DS lop sinh vien ");
        for(int i=0;i<n;i++){
            total+=2026 - classsv[i].getYear(); // la sao?
            System.out.println(classsv[i].getName());
        }
        System.out.println("Tong so tuoi la " + total );
    }
}