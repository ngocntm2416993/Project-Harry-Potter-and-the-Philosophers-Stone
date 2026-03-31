package OOPLAB;

import java.util.Scanner;

public class ex5OOPLAB1 {
    public static int main() {
        Scanner number = new Scanner(System.in);
        System.out.println("Nhap so thu nhat: ");
        String number1= number.nextLine();
        System.out.println("Nhap so thu hai: ");
        String number2= number.nextLine();
        double num1 = Double.parseDouble(number1);
        double num2 = Double.parseDouble(number2);

        double sum = num1+num2;
        System.out.println("sum: "+sum);
        double difference = num1-num2;
        System.out.println("difference: "+difference);
        double product = num1*num2;
        System.out.println("product: "+product);
        if(num2!=0){
            double quotient = num1/num2;
            System.out.println("quotient: "+quotient);
        }
        else {
            System.out.println("phuong trinh chia khong");
            return 0;
        }
        number.close();
        return 0;
    }
}
