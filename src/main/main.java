package main;

import config.config;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class main {
    static Scanner sc = new Scanner(System.in);
    static config con = new config();

 
    public static void viewUsers() {
        String Query = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] columns = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        con.viewRecords(Query, headers, columns);
    }

    public static void viewPets() {
        String Query = "SELECT * FROM tbl_vet";
        String[] headers = {"Pet ID", "Pet Name", "Owner", "Contact", "Address"};
        String[] columns = {"petID", "petName", "ownerName", "contactNumber", "address"};
        con.viewRecords(Query, headers, columns);
    }

    public static void viewAppointments() {
        String Query = "SELECT * FROM tbl_appointment";
        String[] headers = {"App ID", "Pet ID", "Date", "Time", "Status"};
        String[] columns = {"appID", "petID", "date", "time", "status"};
        con.viewRecords(Query, headers, columns);
    }

    // --------------------- PET FUNCTIONS ---------------------
    public static void addPet() {
        System.out.print("Enter Pet ID: ");
        int petId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Pet Name: ");
        String petName = sc.nextLine();
        System.out.print("Enter Owner Name: ");
        String ownerName = sc.nextLine();
        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        String sql = "INSERT INTO tbl_vet(petID, petName, ownerName, contactNumber, address) VALUES(?,?,?,?,?)";
        con.addRecord(sql, petId, petName, ownerName, contact, address);
        System.out.println("✅ Pet Added Successfully!");
    }

    public static void updatePet() {
        System.out.print("Enter Pet ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Pet Name: ");
        String petName = sc.nextLine();
        System.out.print("Enter New Owner Name: ");
        String ownerName = sc.nextLine();
        System.out.print("Enter New Contact: ");
        String contact = sc.nextLine();
        System.out.print("Enter New Address: ");
        String address = sc.nextLine();

        String sql = "UPDATE tbl_vet SET petName=?, ownerName=?, contactNumber=?, address=? WHERE petID=?";
        con.updateRecord(sql, petName, ownerName, contact, address, id);
        System.out.println("✏️ Pet Updated!");
    }

    public static void deletePet() {
        System.out.print("Enter Pet ID to Delete: ");
        int id = sc.nextInt();
        String sql = "DELETE FROM tbl_vet WHERE petID=?";
        con.deleteRecord(sql, id);
        System.out.println("🗑️ Pet Deleted!");
    }

    // --------------------- APPOINTMENT FUNCTIONS ---------------------
    public static void addAppointment() {
        System.out.print("Enter Appointment ID: ");
        int appId = sc.nextInt();
        System.out.print("Enter Pet ID: ");
        int petId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter Time (HH:MM): ");
        String time = sc.nextLine();
        System.out.print("Enter Status: ");
        String status = sc.nextLine();

        String sql = "INSERT INTO tbl_appointment(appID, petID, date, time, status) VALUES(?,?,?,?,?)";
        con.addRecord(sql, appId, petId, date, time, status);
        System.out.println("✅ Appointment Added!");
    }

    public static void updateAppointment() {
        System.out.print("Enter Appointment ID to Update: ");
        int appId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter New Time (HH:MM): ");
        String time = sc.nextLine();
        System.out.print("Enter New Status: ");
        String status = sc.nextLine();

        String sql = "UPDATE tbl_appointment SET date=?, time=?, status=? WHERE appID=?";
        con.updateRecord(sql, date, time, status, appId);
        System.out.println("✏️ Appointment Updated!");
    }

    public static void deleteAppointment() {
        System.out.print("Enter Appointment ID to Delete: ");
        int appId = sc.nextInt();
        String sql = "DELETE FROM tbl_appointment WHERE appID=?";
        con.deleteRecord(sql, appId);
        System.out.println("🗑️ Appointment Deleted!");
    }

    // --------------------- DASHBOARDS ---------------------
    public static void adminDashboard() {
        int choice;
        do {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. Approve Accounts");
            System.out.println("2. View Users");
            System.out.println("3. View Pets");
            System.out.println("4. View Appointments");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewUsers();
                    System.out.print("Enter ID to Approve: ");
                    int id = sc.nextInt();
                    String sql = "UPDATE tbl_user SET u_status=? WHERE u_id=?";
                    con.updateRecord(sql, "Approved", id);
                    System.out.println("✅ User Approved!");
                    break;
                case 2:
                    viewUsers();
                    break;
                case 3:
                    viewPets();
                    break;
                case 4:
                    viewAppointments();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    public static void staffDashboard() {
        int choice;
        do {
            System.out.println("\n=== STAFF DASHBOARD ===");
            System.out.println("1. Add Pet");
            System.out.println("2. Update Pet");
            System.out.println("3. Delete Pet");
            System.out.println("4. Add Appointment");
            System.out.println("5. Update Appointment");
            System.out.println("6. Delete Appointment");
            System.out.println("7. View Pets");
            System.out.println("8. View Appointments");
            System.out.println("9. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addPet();
                    break;
                case 2:
                    updatePet();
                    break;
                case 3:
                    deletePet();
                    break;
                case 4:
                    addAppointment();
                    break;
                case 5:
                    updateAppointment();
                    break;
                case 6:
                    deleteAppointment();
                    break;
                case 7:
                    viewPets();
                    break;
                case 8:
                    viewAppointments();
                    break;
                case 9:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 9);
    }

    // --------------------- MAIN PROGRAM ---------------------
    public static void main(String[] args) {
        con.connectDB();
        char cont;

        do {
            System.out.println("\n===== VET MANAGEMENT SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: // LOGIN
                    System.out.print("Enter Email: ");
                    String em = sc.next();
                    System.out.print("Enter Password: ");
                    String pas = sc.next();

                    String qry = "SELECT * FROM tbl_user WHERE u_email = ? AND u_pass = ?";
                    List<Map<String, Object>> result = con.fetchRecords(qry, em, pas);

                    if (result.isEmpty()) {
                        System.out.println("❌ INVALID CREDENTIALS");
                    } else {
                        Map<String, Object> user = result.get(0);
                        String stat = user.get("u_status").toString();
                        String type = user.get("u_type").toString();

                        if (stat.equals("Pending")) {
                            System.out.println("⚠️ Account Pending! Contact Admin.");
                        } else {
                            System.out.println("✅ LOGIN SUCCESS!");
                            if (type.equals("Admin")) {
                                adminDashboard();
                            } else if (type.equals("Staff")) {
                                staffDashboard();
                            }
                        }
                    }
                    break;

                case 2: // REGISTER
                    System.out.print("Enter Name: ");
                    String name = sc.next();
                    System.out.print("Enter Email: ");
                    String email = sc.next();

                    while (true) {
                        String chk = "SELECT * FROM tbl_user WHERE u_email = ?";
                        List<Map<String, Object>> checkEmail = con.fetchRecords(chk, email);
                        if (checkEmail.isEmpty()) {
                            break;
                        } else {
                            System.out.print("Email exists! Enter another: ");
                            email = sc.next();
                        }
                    }

                    System.out.print("Enter User Type (1-Admin / 2-Staff): ");
                    int tpChoice = sc.nextInt();
                    while (tpChoice < 1 || tpChoice > 2) {
                        System.out.print("Invalid choice. Enter again: ");
                        tpChoice = sc.nextInt();
                    }
                    String tp = (tpChoice == 1) ? "Admin" : "Staff";
                    String status = (tp.equals("Admin")) ? "Approved" : "Pending";

                    System.out.print("Enter Password: ");
                    String pass = sc.next();

                    String sqlInsert = "INSERT INTO tbl_user(u_name, u_email, u_type, u_status, u_pass) VALUES(?,?,?,?,?)";
                    con.addRecord(sqlInsert, name, email, tp, status, pass);
                    System.out.println("✅ Registration successful! Status = " + status);
                    break;

                case 3:
                    System.out.println("👋 Program Ended.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("❌ Invalid choice.");
            }

            System.out.print("Do you want to continue? (Y/N): ");
            cont = sc.next().charAt(0);

        } while (cont == 'Y' || cont == 'y');
    }
}


