package OOPLAB;

import java.util.Scanner;

import static java.lang.Math.sqrt;

public class ex6OOPLAB {
    public static void PT1(double a, double b) {
        if (a == 0 && b != 0) {
            System.out.println("PT Vo Nghiem");
            return;
        } else if (a == 0 && b == 0) {
            System.out.println("PT Vo So Nghiem");
            return;
        } else {
            System.out.println("Nghiem cua PT la: " + (-b / a));
            return;
        }
    }

    public static void PT2(double x1, double y1, double t1, double x2, double y2, double t2) {
        double d = x1 * y2 - x2 * y1;
        double d1 = t1 * y2 - t2 * y1;
        double d2 = x1 * t2 - x2 * t1;
        if (d != 0) {
            System.out.println("Nghiem thu nhat cua pt la: " + (d1 / d));
            System.out.println("Nghiem thu hai cua pt la: " + (d2 / d));
        } else {
            if (d1 == 0 && d2 == 0) {
                System.out.println("PT Vo So Nghiem");
                return;
            } else {
                System.out.println("PT Vo Nghiem");
                return;
            }
        }
        return;
    }

    public static void PT3(double a, double b, double c) {
        if (a == 0 && b == 0) {
            if (c == 0) {
                System.out.println("PT Vo So Nghiem");
                return;
            } else {
                System.out.println("PT Vo Nghiem");
                return;
            }
        } else if (a == 0 && b != 0) {
            System.out.println("Nghiem cua pt la:" + (-c / b));
            return;
        } else {
            double denlta = b * b - 4 * a * c;
            if (denlta < 0) {
                System.out.println("PT Vo Nghiem");
                return;
            } else if (denlta == 0) {
                System.out.println("PT co nghiem kep la: " + (-b / (2 * a)));
                return;
            } else {
                System.out.println("Nghiem thu nhat la: " + (-b + sqrt(denlta)) / (2 * a));
                System.out.println("Nghiem thu hai la: " + (-b - sqrt(denlta)) / (2 * a));
                return;
            }
        }
    }

    static void phuongtrinh(double a, double b) {
        PT1(a, b);
    }

    static void phuongtrinh(double a, double b, double c) {
        PT3(a, b, c);
    }

    static void phuongtrinh(double x1, double y1, double t1, double x2, double y2, double t2) {
        PT2(x1, y1, t1, x2, y2, t2);
    }

    public static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap so luong phuong trinh ");
        int sopt = scanner.nextInt();
        if (sopt == 1) {
            System.out.println("Nhap so an cua pt ");
            int soan = scanner.nextInt();
            if (soan == 1) {
                System.out.println("Phuong trinh dang ax+b=0");
                double a = 0;
                double b = 0;
                System.out.println("Nhap gia tri cua a");
                a = scanner.nextDouble();
                System.out.println("Nhap gia tri cua b");
                b = scanner.nextDouble();
                phuongtrinh(a, b);
                return;
            } else {
                System.out.println("Phuong trinh bac hai dang ax^2 + bx +c");
                double a = 0;
                double b = 0;
                double c = 0;
                System.out.println("Nhap gia tri cua a");
                a = scanner.nextDouble();
                System.out.println("Nhap gia tri cua b");
                b = scanner.nextDouble();
                System.out.println("Nhap gia tri cua c");
                c = scanner.nextDouble();
                phuongtrinh(a, b, c);
                return;
            }
        } else {
            System.out.println("He phuong trinh bac nhat 2 an");
            double x1 = 0, y1 = 0, t1 = 0;
            double x2 = 0, y2 = 0, t2 = 0;
            System.out.println("Nhap gia tri x1");
            x1 = scanner.nextDouble();
            System.out.println("Nhap gia tri y1");
            y1 = scanner.nextDouble();
            System.out.println("Nhap gia tri t1");
            t1 = scanner.nextDouble();
            System.out.println("Nhap gia tri x2");
            x2 = scanner.nextDouble();
            System.out.println("Nhap gia tri y2");
            y2 = scanner.nextDouble();
            System.out.println("Nhap gia tri t2");
            t2 = scanner.nextDouble();
            phuongtrinh(x1, y1, t1, x2, y2, t2);
            return;
        }
    }
}