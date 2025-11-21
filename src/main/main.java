package main;

import config.config;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class main {

    static Scanner sc = new Scanner(System.in);
    static config con = new config();

    // =========================================================
    // VIEW RECORD FUNCTIONS
    // =========================================================
    public static void viewUsers() {
        String Query = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] columns = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        con.viewRecords(Query, headers, columns);
    }

    public static void viewOwners() {
        String Query = "SELECT * FROM tbl_owner";
        String[] headers = {"Owner ID", "Name", "Contact", "Address"};
        String[] columns = {"ownerID", "ownerName", "contactNumber", "address"};
        con.viewRecords(Query, headers, columns);
    }

    public static void viewPets() {
        String Query = 
            "SELECT p.petID, p.petName, o.ownerName, o.contactNumber, o.address " +
            "FROM tbl_pet p INNER JOIN tbl_owner o ON p.ownerID = o.ownerID";

        String[] headers = {"Pet ID", "Pet Name", "Owner", "Contact", "Address"};
        String[] columns = {"petID", "petName", "ownerName", "contactNumber", "address"};
        con.viewRecords(Query, headers, columns);
    }

    public static void viewAppointments() {
        String Query = 
            "SELECT a.appID, p.petName, o.ownerName, a.date, a.time, a.status " +
            "FROM tbl_appointment a " +
            "INNER JOIN tbl_pet p ON a.petID = p.petID " +
            "INNER JOIN tbl_owner o ON p.ownerID = o.ownerID";

        String[] headers = {"App ID", "Pet Name", "Owner", "Date", "Time", "Status"};
        String[] columns = {"appID", "petName", "ownerName", "date", "time", "status"};
        con.viewRecords(Query, headers, columns);
    }

    // =========================================================
    // OWNER MANAGEMENT
    // =========================================================
    public static void addOwner() {
        sc.nextLine();
        System.out.print("Enter Owner Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        String sql = "INSERT INTO tbl_owner(ownerName, contactNumber, address) VALUES(?,?,?)";
        con.addRecord(sql, name, contact, address);

        System.out.println("✅ Owner Added!");
    }

    // =========================================================
    // PET MANAGEMENT
    // =========================================================
    public static void addPet() {
        System.out.println("\n=== OWNER LIST ===");
        viewOwners();

        System.out.print("Enter Owner ID: ");
        int ownerID = sc.nextInt();

        String chk = "SELECT * FROM tbl_owner WHERE ownerID=?";
        List<Map<String, Object>> ownerExists = con.fetchRecords(chk, ownerID);

        if (ownerExists.isEmpty()) {
            System.out.println("❌ Owner does NOT exist! Add owner first.");
            return;
        }

        sc.nextLine();
        System.out.print("Enter Pet Name: ");
        String petName = sc.nextLine();

        String sql = "INSERT INTO tbl_pet(petName, ownerID) VALUES(?,?)";
        con.addRecord(sql, petName, ownerID);

        System.out.println("✅ Pet Added Successfully!");
    }

    public static void updatePet() {
        viewPets();

        System.out.print("Enter Pet ID to Update: ");
        int petID = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Pet Name: ");
        String petName = sc.nextLine();
        
        System.out.println("\n=== OWNER LIST ===");
        viewOwners();
        System.out.print("Enter New Owner ID: ");
        int newOwnerID = sc.nextInt();

        String chk = "SELECT * FROM tbl_owner WHERE ownerID=?";
        if (con.fetchRecords(chk, newOwnerID).isEmpty()) {
            System.out.println("❌ Owner does NOT exist!");
            return;
        }

        String sql = "UPDATE tbl_pet SET petName=?, ownerID=? WHERE petID=?";
        con.updateRecord(sql, petName, newOwnerID, petID);

        System.out.println("✏️ Pet Updated!");
    }

    public static void deletePet() {
        viewPets();

        System.out.print("Enter Pet ID to Delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM tbl_pet WHERE petID=?";
        con.deleteRecord(sql, id);
        System.out.println("🗑️ Pet Deleted!");
    }

    // =========================================================
    // APPOINTMENT MANAGEMENT
    // =========================================================
    public static void addAppointment() {
        viewPets();
        System.out.print("Enter Pet ID: ");
        int petID = sc.nextInt();

        String chk = "SELECT * FROM tbl_pet WHERE petID=?";
        if (con.fetchRecords(chk, petID).isEmpty()) {
            System.out.println("❌ Pet does NOT exist!");
            return;
        }

        sc.nextLine();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter Time (HH:MM): ");
        String time = sc.nextLine();

        String sql = "INSERT INTO tbl_appointment(petID, date, time, status) VALUES(?,?,?,?)";
        con.addRecord(sql, petID, date, time, "Pending");

        System.out.println("✅ Appointment Added!");
    }

    public static void updateAppointment() {
        viewAppointments();
        System.out.print("Enter Appointment ID to Update: ");
        int appID = sc.nextInt();
        sc.nextLine();

        System.out.print("New Date: ");
        String date = sc.nextLine();
        System.out.print("New Time: ");
        String time = sc.nextLine();
        System.out.print("New Status: ");
        String status = sc.nextLine();

        String sql = "UPDATE tbl_appointment SET date=?, time=?, status=? WHERE appID=?";
        con.updateRecord(sql, date, time, status, appID);

        System.out.println("✏️ Appointment Updated!");
    }

    public static void deleteAppointment() {
        viewAppointments();

        System.out.print("Enter Appointment ID to Delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM tbl_appointment WHERE appID=?";
        con.deleteRecord(sql, id);

        System.out.println("🗑️ Appointment Deleted!");
    }

    // =========================================================
    // DASHBOARD — SUPER ADMIN
    // =========================================================
    public static void superAdminDashboard() {
        int choice;

        do {
            System.out.println("\n=== SUPER ADMIN DASHBOARD ===");
            System.out.println("1. Approve Admin Accounts");
            System.out.println("2. Approve Staff Accounts");
            System.out.println("3. View Users");
            System.out.println("4. Owner Management (Add Owner)");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    String q1 = 
                        "SELECT * FROM tbl_user WHERE u_type='Admin' AND u_status='Pending'";
                    con.viewRecords(q1, 
                        new String[]{"ID","Name","Email","Type","Status"},
                        new String[]{"u_id","u_name","u_email","u_type","u_status"});
                    System.out.print("Approve Admin ID: ");
                    int aid = sc.nextInt();
                    con.updateRecord("UPDATE tbl_user SET u_status='Approved' WHERE u_id=?", aid);
                    System.out.println("✅ Admin Approved!");
                    break;

                case 2:
                    String q2 = 
                        "SELECT * FROM tbl_user WHERE u_type='Staff' AND u_status='Pending'";
                    con.viewRecords(q2,
                        new String[]{"ID","Name","Email","Type","Status"},
                        new String[]{"u_id","u_name","u_email","u_type","u_status"});
                    System.out.print("Approve Staff ID: ");
                    int sid = sc.nextInt();
                    con.updateRecord("UPDATE tbl_user SET u_status='Approved' WHERE u_id=?", sid);
                    System.out.println("✅ Staff Approved!");
                    break;

                case 3:
                    viewUsers();
                    break;

                case 4:
                    addOwner();
                    break;

                case 5:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }
        } while (choice != 5);
    }

    // =========================================================
    // DASHBOARD — ADMIN
    // =========================================================
    public static void adminDashboard() {
        int choice;

        do {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. Approve Staff Accounts");
            System.out.println("2. View Users");
            System.out.println("3. View Owners");
            System.out.println("4. View Pets");
            System.out.println("5. View Appointments");
            System.out.println("6. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    String q = 
                        "SELECT * FROM tbl_user WHERE u_type='Staff' AND u_status='Pending'";
                    con.viewRecords(q,
                        new String[]{"ID","Name","Email","Type","Status"},
                        new String[]{"u_id","u_name","u_email","u_type","u_status"});
                    System.out.print("Enter Staff ID to Approve: ");
                    int sid = sc.nextInt();
                    con.updateRecord("UPDATE tbl_user SET u_status='Approved' WHERE u_id=?", sid);
                    System.out.println("✅ Staff Approved!");
                    break;

                case 2:
                    viewUsers();
                    break;

                case 3:
                    viewOwners();
                    break;

                case 4:
                    viewPets();
                    break;

                case 5:
                    viewAppointments();
                    break;

                case 6:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }
        } while (choice != 6);
    }

    // =========================================================
    // DASHBOARD — STAFF
    // =========================================================
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
                case 1: addPet(); break;
                case 2: updatePet(); break;
                case 3: deletePet(); break;
                case 4: addAppointment(); break;
                case 5: updateAppointment(); break;
                case 6: deleteAppointment(); break;
                case 7: viewPets(); break;
                case 8: viewAppointments(); break;
                case 9: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 9);
    }

    // =========================================================
    // LOGIN + REGISTER
    // =========================================================
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

                // LOGIN
                case 1:
                    System.out.print("Email: ");
                    String em = sc.next();
                    System.out.print("Password: ");
                    String pas = sc.next();
                    String hash = con.hashPassword(pas);

                    String sql = "SELECT * FROM tbl_user WHERE u_email=? AND u_pass=?";
                    List<Map<String, Object>> res = con.fetchRecords(sql, em, hash);

                    if (res.isEmpty()) {
                        System.out.println("❌ Invalid Credentials!");
                    } else {
                        Map<String, Object> u = res.get(0);

                        String type = u.get("u_type").toString();
                        String status = u.get("u_status").toString();

                        if (status.equals("Pending")) {
                            System.out.println("⚠️ Account Pending Approval!");
                        } else {
                            System.out.println("✅ Login Successful!");

                            if (type.equals("SuperAdmin")) {
                                superAdminDashboard();
                            } else if (type.equals("Admin")) {
                                adminDashboard();
                            } else if (type.equals("Staff")) {
                                staffDashboard();
                            }
                        }
                    }
                    break;

                // REGISTER
                case 2:
                    sc.nextLine();
                    System.out.print("Enter Full Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.next();

                    // Check duplicate email
                    while (!con.fetchRecords("SELECT * FROM tbl_user WHERE u_email=?", email).isEmpty()) {
                        System.out.print("Email already exists. Enter new: ");
                        email = sc.next();
                    }

                    // User type
                    System.out.print("User Type (1-Admin, 2-Staff): ");
                    int tpChoice = sc.nextInt();
                    String type = (tpChoice == 1) ? "Admin" : "Staff";

                    String status = "Pending";

                    System.out.print("Password: ");
                    String pass = sc.next();
                    String hashed = con.hashPassword(pass);

                    String reg = "INSERT INTO tbl_user(u_name, u_email, u_type, u_status, u_pass) VALUES(?,?,?,?,?)";
                    con.addRecord(reg, name, email, type, status, hashed);

                    System.out.println("✅ Registered Successfully! Status: Pending Approval");
                    break;

                // EXIT
                case 3:
                    System.out.println("Program Ended.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

            System.out.print("Return to menu? (Y/N): ");
            cont = sc.next().charAt(0);

        } while (cont == 'Y' || cont == 'y');
    }
}
