import java.sql.SQLException;
import java.util.Scanner;
import com.admin.*;
import com.teacher.*;
import com.student.*;

public class MainApp {

    public static void main(String[] args) {                       //inheritance
        Scanner scanner = new Scanner(System.in);
        AdminInformationSystem studentSystem = new AdminInformationSystem("jdbc:mysql://localhost:3306/mini?user=root&password=Tiger");
        Teacher teacher = new Teacher("jdbc:mysql://localhost:3306/mini?user=root&password=tiger");
   

        while (true) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (studentSystem.login()) {
                        studentSystemMenu(scanner, studentSystem);
                    } else {
                        System.out.println("Admin login failed. Exiting the program.");
                    }
                    break;
                case 2:
                    if (teacher.teacherLogin()) {
                        teacherMenu(scanner, teacher);
                    } else {
                        System.out.println("Teacher login failed. Exiting the program.");
                    }
                    break;
                case 3:
                    System.out.println("Exiting the program.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Admin");
        System.out.println("2. Teacher");
       
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void studentSystemMenu(Scanner scanner, AdminInformationSystem studentSystem) {
        while (true) {
            studentSystem.displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    try {
                        studentSystem.addStudent();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        studentSystem.displayStudents();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        studentSystem.updateStudentGrade();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        studentSystem.deleteStudent();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    System.out.println("Exiting the student system.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void teacherMenu(Scanner scanner, Teacher teacher) {
        while (true) {
            teacher.displayTeacherMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    try {
                        teacher.addStudent();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        teacher.displayStudents();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        teacher.updateStudentGrade();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.out.println("Exiting the teacher system.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }




}


