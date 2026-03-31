import javax.swing.JOptionPane;
public class ex61 {
    /*
    6.1
    - What happens if users choose “Cancel”? - you choose NO
    - How to customize the options to users, e.g. only two options: “Yes” and “No”, OR “I do” and “I don’t”
    */

    public static void main(String[] args) {
        String[] options = {"I do", "I don't"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Do you agree?",
                "Question",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            System.out.println("I do");
        } else {
            System.out.println("I don't");
        }
    }
}
