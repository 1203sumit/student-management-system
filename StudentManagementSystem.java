import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASS = ""; // default for XAMPP

    static Connection conn;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            createTable();
            int choice;
            do {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Search Student");
                System.out.println("6. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewStudents();
                    case 3 -> updateStudent();
                    case 4 -> deleteStudent();
                    case 5 -> searchStudent();
                    case 6 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice!");
                }
            } while (choice != 6);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                     "roll_no INT PRIMARY KEY, " +
                     "name VARCHAR(100), " +
                     "course VARCHAR(50), " +
                     "marks INT)";
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    static void addStudent() throws SQLException {
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Course: ");
        String course = sc.nextLine();
        System.out.print("Enter Marks: ");
        int marks = sc.nextInt();

        String sql = "INSERT INTO students VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roll);
        pstmt.setString(2, name);
        pstmt.setString(3, course);
        pstmt.setInt(4, marks);
        pstmt.executeUpdate();

        System.out.println("Student added successfully.");
    }

    static void viewStudents() throws SQLException {
        String sql = "SELECT * FROM students";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- Student List ---");
        while (rs.next()) {
            System.out.println("Roll No: " + rs.getInt("roll_no") +
                               ", Name: " + rs.getString("name") +
                               ", Course: " + rs.getString("course") +
                               ", Marks: " + rs.getInt("marks"));
        }
    }

    static void updateStudent() throws SQLException {
        System.out.print("Enter Roll No to update: ");
        int roll = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Name: ");
        String name = sc.nextLine();
        System.out.print("Enter New Course: ");
        String course = sc.nextLine();
        System.out.print("Enter New Marks: ");
        int marks = sc.nextInt();

        String sql = "UPDATE students SET name=?, course=?, marks=? WHERE roll_no=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, course);
        pstmt.setInt(3, marks);
        pstmt.setInt(4, roll);
        pstmt.executeUpdate();

        System.out.println("Student updated successfully.");
    }

    static void deleteStudent() throws SQLException {
        System.out.print("Enter Roll No to delete: ");
        int roll = sc.nextInt();

        String sql = "DELETE FROM students WHERE roll_no=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roll);
        pstmt.executeUpdate();

        System.out.println("Student deleted successfully.");
    }

    static void searchStudent() throws SQLException {
        System.out.print("Enter Roll No to search: ");
        int roll = sc.nextInt();

        String sql = "SELECT * FROM students WHERE roll_no=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roll);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Roll No: " + rs.getInt("roll_no") +
                               ", Name: " + rs.getString("name") +
                               ", Course: " + rs.getString("course") +
                               ", Marks: " + rs.getInt("marks"));
        } else {
            System.out.println("Student not found.");
        }
    }
}
