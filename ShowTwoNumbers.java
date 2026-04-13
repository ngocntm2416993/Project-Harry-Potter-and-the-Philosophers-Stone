import javax.swing.JOptionPane;

public class CalculateTwoNumbers {
    public static void main(String[] args) {

        // Nhập 2 số dạng String
        String strNum1 = JOptionPane.showInputDialog("Enter the first number:");
        String strNum2 = JOptionPane.showInputDialog("Enter the second number:");

        // Chuyển sang double
        double num1 = Double.parseDouble(strNum1);
        double num2 = Double.parseDouble(strNum2);

        // Tính toán
        double sum = num1 + num2;
        double difference = num1 - num2;
        double product = num1 * num2;

        String result;

        // Kiểm tra chia cho 0
        if (num2 != 0) {
            double quotient = num1 / num2;
            result = "Sum: " + sum +
                     "\nDifference: " + difference +
                     "\nProduct: " + product +
                     "\nQuotient: " + quotient;
        } else {
            result = "Sum: " + sum +
                     "\nDifference: " + difference +
                     "\nProduct: " + product +
                     "\nQuotient: Cannot divide by 0!";
        }

        // Hiển thị kết quả
        JOptionPane.showMessageDialog(null, result);

        System.exit(0);
    }
}