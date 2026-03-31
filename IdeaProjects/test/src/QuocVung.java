import javax.swing.*;
import java.util.Scanner;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class QuocVung {
    static void main() {
        Scanner scanner= new Scanner(System.in);
        double MinhNgoc=Double.parseDouble((JOptionPane.showInputDialog("do dai canh thu nhat la: ")));
        double QuynhAnh=Double.parseDouble((JOptionPane.showInputDialog("do dai canh thu hai la: ")));
        double QuynhTrang =Double.parseDouble((JOptionPane.showInputDialog("do dai canh thu ba la: ")));
        double p = (MinhNgoc +QuynhAnh+QuynhTrang)/2;
        double Minh = sqrt(p*(p- QuynhTrang)*(p-MinhNgoc)*(p-QuynhAnh));
        JOptionPane.showMessageDialog(null,"Ngoc Minh ( dien tich tam giac ) la: " + Minh);
        System.exit(0);
    }
}
