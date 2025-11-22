package Main;

import Config.config;
import java.util.*;

public class main {

    // ==== VIEW METHODS ====
    public static void viewStudents() {
        String sql = "SELECT * FROM student";
        String[] headers = {"ID", "Student No", "First Name", "Last Name", "Email", "Year Level", "Status"};
        String[] cols = {"student_id", "student_no", "first_name", "last_name", "email", "year_level", "status"};
        config con = new config();
        con.viewRecords(sql, headers, cols);
    }

    public static void viewTeachers() {
        String sql = "SELECT * FROM teacher";
        String[] headers = {"ID", "First Name", "Last Name", "Email", "Status"};
        String[] cols = {"teacher_id", "first_name", "last_name", "email", "status"};
        config con = new config();
        con.viewRecords(sql, headers, cols);
    }

    public static void viewSubjects() {
        String sql = "SELECT * FROM subject";
        String[] headers = {"ID", "Code", "Description", "Units", "Year Level", "Semester", "Status"};
        String[] cols = {"subject_id", "subject_code", "subject_desc", "units", "year_level", "semester", "status"};
        config con = new config();
        con.viewRecords(sql, headers, cols);
    }

    public static void viewGrades() {
        String sql = "SELECT * FROM grades";
        String[] headers = {"Grade ID", "Student ID", "Subject ID", "Teacher ID", "Prelim", "Midterm", "Prefinal", "Final", "Remarks"};
        String[] cols = {"grade_id", "student_id", "subject_id", "teacher_id", "prelim", "midterm", "prefinal", "final", "remarks"};
        config con = new config();
        con.viewRecords(sql, headers, cols);
    }

    // ==== MAIN METHOD ====
    public static void main(String[] args) {
        config con = new config();
        con.connectDB();
        Scanner sc = new Scanner(System.in);
        int choice;
        char cont;

        do {
            System.out.println("===== STUDENT GRADE PORTAL SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Email: ");
                    String email = sc.next();
                    System.out.print("Enter Password: ");
                    String pass = sc.next();

                    String qry = "SELECT * FROM tbl_user WHERE u_email = ? AND u_pass    = ?";
                    List<Map<String, Object>> result = con.fetchRecords(qry, email, pass);

                    if (result.isEmpty()) {
                        System.out.println("INVALID CREDENTIALS!");
                    } else {
                        Map<String, Object> user = result.get(0);
                        String stat = user.get("u_status").toString();
                        String role = user.get("u_role").toString();

                        if (stat.equalsIgnoreCase("Pending")) {
                            System.out.println("Account is pending. Please contact Registrar.");
                        } else {
                            System.out.println("\nLOGIN SUCCESS! Welcome, " + user.get("u_email"));
                            if (role.equalsIgnoreCase("Registrar")) {
                                registrarDashboard(con, sc);
                            } else if (role.equalsIgnoreCase("Teacher")) {
                                teacherDashboard(con, sc);
                            } else if (role.equalsIgnoreCase("Student")) {
                                studentDashboard(con, sc);
                            }
                        }
                    }
                    break;

                case 2:
                    System.out.println("\nREGISTER ACCOUNT");
                    System.out.print("Enter Email: ");
                    String regEmail = sc.next();
                    System.out.print("Enter Password: ");
                    String regPass = sc.next();
                    System.out.print("Enter Role (Student/Teacher/Registrar): ");
                    String regRole = sc.next();

                    String insert = "INSERT INTO tbl_user(u_email, u_pass, u_role, u_status) VALUES (?, ?, ?, ?)";
                    con.addRecord(insert, regEmail, regPass, regRole, "Pending");
                    System.out.println("Registration successful! Wait for Registrar approval.");
                    break;

                case 3:
                    System.out.println("Exiting system...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

            System.out.print("\nReturn to login menu? (Y/N): ");
            cont = sc.next().charAt(0);

        } while (cont == 'Y' || cont == 'y');

        sc.close();
    }

    // ==== REGISTRAR DASHBOARD ====
    public static void registrarDashboard(config con, Scanner sc) {
        char cont;
        do {
            System.out.println("\n===== REGISTRAR DASHBOARD =====");
            System.out.println("1. Approve Accounts");
            System.out.println("2. Add Student");
            System.out.println("3. Manage Subjects");
            System.out.println("4. View Students");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    String query = "SELECT * FROM tbl_user";
                    String[] h = {"ID", "Email", "Role", "Status"};
                    String[] c = {"u_id", "u_email", "u_role", "u_1status"};
                    con.viewRecords(query, h, c);
                    System.out.print("Enter User ID to Approve: ");
                    int id = sc.nextInt();
                    String sql = "UPDATE tbl_user SET u_status=? WHERE u_id=?";
                    con.updateRecord(sql, "Approved", id);
                    System.out.println("Account approved successfully!");
                    break;
                    
                case 2:
                    addStudent(con, sc);
                case 3:
                    subjectMenu(con, sc);
                    break;

                case 4:
                    viewStudents();
                    break;

                case 5:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }

            System.out.print("\nReturn to Registrar Dashboard? (Y/N): ");
            cont = sc.next().charAt(0);
        } while (cont == 'Y' || cont == 'y');
    }
    
    public static void addStudent(config con, Scanner sc) {
      
        System.out.print("Enter First Name: ");
        String fn = sc.next();
        System.out.print("Enter Last Name: ");
        String ln = sc.next();
        System.out.print("Enter Email: ");
        String email = sc.next();
        System.out.print("Enter Year Level: ");
        String yl = sc.next();
        System.out.print("Enter Status: ");
        String stat = sc.next();
        
        String sql = "INSERT INTO tbl_student (first_name, last_name, email, year_level, status) VALUES (?, ?, ?, ?, ?)";
        con.addRecord(sql, fn, ln, email, yl, stat);
        
    }

    // ==== TEACHER DASHBOARD ====
    public static void teacherDashboard(config con, Scanner sc) {
        char cont;
        do {
            System.out.println("\n===== TEACHER DASHBOARD =====");
            System.out.println("1. Encode Grades");
            System.out.println("2. View Grades");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    int sid = sc.nextInt();
                    System.out.print("Enter Subject ID: ");
                    int subid = sc.nextInt();
                    System.out.print("Enter Teacher ID: ");
                    int tid = sc.nextInt();
                    System.out.print("Enter Prelim: ");
                    double pre = sc.nextDouble();
                    System.out.print("Enter Midterm: ");
                    double mid = sc.nextDouble();
                    System.out.print("Enter Prefinal: ");
                    double pref = sc.nextDouble();
                    System.out.print("Enter Final: ");
                    double fin = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter Remarks: ");
                    String rem = sc.nextLine();

                    String sql = "INSERT INTO grades(student_id, subject_id, teacher_id, prelim, midterm, prefinal, final, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    con.addRecord(sql, sid, subid, tid, pre, mid, pref, fin, rem);
                    System.out.println("Grade successfully recorded!");
                    break;

                case 2:
                    viewGrades();
                    break;

                case 3:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }

            System.out.print("\nReturn to Teacher Dashboard? (Y/N): ");
            cont = sc.next().charAt(0);
        } while (cont == 'Y' || cont == 'y');
    }

    // ==== STUDENT DASHBOARD ====
    public static void studentDashboard(config con, Scanner sc) {
        char cont;
        do {
            System.out.println("\n===== STUDENT DASHBOARD =====");
            System.out.println("1. View Grades");
            System.out.println("2. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewGrades();
                    break;
                case 2:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            System.out.print("\nReturn to Student Dashboard? (Y/N): ");
            cont = sc.next().charAt(0);
        } while (cont == 'Y' || cont == 'y');
    }

    // ==== SUBJECT MENU (for Registrar) ====
    public static void subjectMenu(config con, Scanner sc) {
        char sub;
        do {
            System.out.println("\n===== SUBJECT MANAGEMENT =====");
            System.out.println("1. Add Subject");
            System.out.println("2. View Subjects");
            System.out.println("3. Update Subject");
            System.out.println("4. Delete Subject");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Enter Code: ");
                    String code = sc.nextLine();
                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();
                    System.out.print("Enter Units: ");
                    int units = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Year Level: ");
                    String yr = sc.nextLine();
                    System.out.print("Enter Status: ");
                    String stat = sc.nextLine();

                    String sql = "INSERT INTO tbl_subject(subject_code, subject_desc, units, year_level, status) VALUES (?, ?, ?, ?, ?)";
                    con.addRecord(sql, code, desc, units, yr, stat);
                    break;

                case 2:
                    viewSubjects();
                    break;

                case 3:
                    viewSubjects();
                    System.out.print("Enter Subject ID to Update: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter New Description: ");
                    String newDesc = sc.nextLine();
                    sql = "UPDATE subject SET subject_desc=? WHERE subject_id=?";
                    con.updateRecord(sql, newDesc, id);
                    break;

                case 4:
                    viewSubjects();
                    System.out.print("Enter Subject ID to Delete: ");
                    int del = sc.nextInt();
                    sql = "DELETE FROM subject WHERE subject_id=?";
                    con.deleteRecord(sql, del);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice!");
            }

            System.out.print("\nContinue in Subject Menu? (Y/N): ");
            sub = sc.next().charAt(0);
        } while (sub == 'Y' || sub == 'y');
    }
}
         
