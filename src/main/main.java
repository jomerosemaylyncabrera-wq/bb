package main;

import config.*;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class main {
    // ---------- HARD-CODED SUPER ADMIN (single permanent use) ----------
    static final String SUPER_EMAIL = "superadmin@system.com";
    static final String SUPER_PASS = "supersecret";
    // ------------------------------------------------------------------
    
    static Scanner sc = new Scanner(System.in);
    static config con = new config();
    
    // --------------------- VIEW HELPERS ---------------------
    public static void viewAdmins() {
        String Query = "SELECT * FROM tbl_admin";
        String[] headers = {"Admin ID", "Name", "Email", "Type", "Status"};
        String[] columns = {"admin_id", "name", "email", "type", "status"};
        con.viewRecords(Query, headers, columns);
    }
    
    public static void viewOwners() {
        String Query = "SELECT * FROM tbl_owner";
        String[] headers = {"Owner ID", "Name", "Email", "Status"};
        String[] columns = {"owner_id", "name", "email", "status"};
        con.viewRecords(Query, headers, columns);
    }
    
    public static void viewStaffs() {
        String Query = "SELECT * FROM tbl_staff";
        String[] headers = {"Staff ID", "Owner ID", "Name", "Email", "Status"};
        String[] columns = {"staff_id", "owner_id", "name", "email", "status"};
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
        sc.nextLine(); // consume leftover newline
        System.out.print("Enter Pet Name: ");
        String petName = sc.nextLine();
        System.out.print("Enter Owner Name: ");
        String ownerName = sc.nextLine();
        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        
        String sql = "INSERT INTO tbl_vet(petName, ownerName, contactNumber, address) VALUES(?,?,?,?)";
        con.addRecord(sql, petName, ownerName, contact, address);
        System.out.println("✅ Pet Added Successfully!");
    }
    
    public static void updatePet() {
        System.out.println("\nAvailable Pets:");
        viewPets();
        System.out.print("Enter Pet ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline
        
        System.out.print("Enter New Pet Name: ");
        String petName = sc.nextLine();
        System.out.print("Enter New Owner Name: ");
        String ownerName = sc.nextLine();
        System.out.print("Enter New Contact: ");
        String contact = sc.nextLine();
        System.out.print("Enter New Address: ");
        String address = sc.nextLine();
        
        String sql = "UPDATE tbl_vet SET petName=?, ownerName=?, contactNumber=?, address=? WHERE petID=?";
        con.updateRecord(sql, petName, ownerName, contact, address, String.valueOf(id));
        System.out.println("✏️ Pet Updated!");
    }
    
    public static void deletePet() {
        System.out.println("\nAvailable Pets:");
        viewPets();
        System.out.print("Enter Pet ID to Delete: ");
        int id = sc.nextInt();
        
        String sql = "DELETE FROM tbl_vet WHERE petID=?";
        con.deleteRecord(sql, String.valueOf(id));
        System.out.println("🗑️ Pet Deleted!");
    }
    
    // --------------------- APPOINTMENT FUNCTIONS ---------------------
    public static void addAppointment() {
        viewPets();
        System.out.print("Enter Pet ID: ");
        int petId = sc.nextInt();
        sc.nextLine(); // consume newline
        
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter Time (HH:MM): ");
        String time = sc.nextLine();
        String status = "Pending";
        
        String sql = "INSERT INTO tbl_appointment(petID, date, time, status) VALUES(?,?,?,?)";
        con.addRecord(sql, String.valueOf(petId), date, time, status);
        System.out.println("✅ Appointment Added! Status = Pending");
    }
    
    public static void updateAppointment() {
        System.out.println("\nAvailable Appointments:");
        viewAppointments();
        System.out.print("Enter Appointment ID to Update: ");
        int appId = sc.nextInt();
        sc.nextLine(); // consume newline
        
        System.out.print("Enter New Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter New Time (HH:MM): ");
        String time = sc.nextLine();
        System.out.print("Enter New Status: ");
        String status = sc.nextLine();
        
        String sql = "UPDATE tbl_appointment SET date=?, time=?, status=? WHERE appID=?";
        con.updateRecord(sql, date, time, status, String.valueOf(appId));
        System.out.println("✏️ Appointment Updated!");
    }
    
    public static void deleteAppointment() {
        System.out.println("\nAvailable Appointments:");
        viewAppointments();
        System.out.print("Enter Appointment ID to Delete: ");
        int appId = sc.nextInt();
        
        String sql = "DELETE FROM tbl_appointment WHERE appID=?";
        con.deleteRecord(sql, String.valueOf(appId));
        System.out.println("🗑️ Appointment Deleted!");
    }
    
    // --------------------- REGISTRATION HELPERS ---------------------
    public static void registerAdmin() {
        sc.nextLine(); // consume newline
        System.out.print("Enter Admin Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Admin Email: ");
        String email = sc.nextLine();
        
        // Check duplicate
        String chk = "SELECT * FROM tbl_admin WHERE email = ?";
        List<Map<String, Object>> already = con.fetchRecords(chk, email);
        if (!already.isEmpty()) {
            System.out.println("Email exists for admin. Registration cancelled.");
            return;
        }
        
        System.out.print("Is this Admin 'Super' or 'Normal'? (1-Super / 2-Normal): ");
        int t = sc.nextInt();
        sc.nextLine(); // consume newline
        
        String type = (t == 1) ? "SuperAdmin" : "Admin";
        String status = (type.equals("SuperAdmin")) ? "Approved" : "Pending";
        
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();
        String hashed = con.hashPassword(pass);
        
        String sql = "INSERT INTO tbl_admin(name, email, password, type, status) VALUES(?,?,?,?,?)";
        con.addRecord(sql, name, email, hashed, type, status);
        System.out.println("✅ Admin Registered. Status = " + status);
    }
    
    public static void registerOwner() {
        sc.nextLine(); // consume newline
        System.out.print("Enter Owner Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Owner Email: ");
        String email = sc.nextLine();
        
        String chk = "SELECT * FROM tbl_owner WHERE email = ?";
        List<Map<String, Object>> already = con.fetchRecords(chk, email);
        if (!already.isEmpty()) {
            System.out.println("Email exists for owner. Registration cancelled.");
            return;
        }
        
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();
        String hashed = con.hashPassword(pass);
        
        String sql = "INSERT INTO tbl_owner(name, email, password, status) VALUES(?,?,?,?)";
        con.addRecord(sql, name, email, hashed, "Pending");
        System.out.println("✅ Owner Registered. Status = Pending");
    }
    
    public static void registerStaff() {
        sc.nextLine(); // consume newline
        System.out.print("Enter Staff Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Staff Email: ");
        String email = sc.nextLine();
        
        String chk = "SELECT * FROM tbl_staff WHERE email = ?";
        List<Map<String, Object>> already = con.fetchRecords(chk, email);
        if (!already.isEmpty()) {
            System.out.println("Email exists for staff. Registration cancelled.");
            return;
        }
        
        System.out.print("Enter Owner ID to assign staff under (or 0 for none): ");
        int ownerId = sc.nextInt();
        sc.nextLine(); // consume newline
        
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();
        String hashed = con.hashPassword(pass);
        
        String sql;
        if (ownerId == 0) {
            sql = "INSERT INTO tbl_staff(name, email, password, status) VALUES(?,?,?,?)";
            con.addRecord(sql, name, email, hashed, "Pending");
        } else {
            sql = "INSERT INTO tbl_staff(owner_id, name, email, password, status) VALUES(?,?,?,?,?)";
            con.addRecord(sql, String.valueOf(ownerId), name, email, hashed, "Pending");
        }
        System.out.println("✅ Staff Registered. Status = Pending");
    }
    
    // --------------------- DASHBOARDS ---------------------
    public static void superAdminDashboard() {
        int choice;
        do {
            System.out.println("\n=== SUPER ADMIN DASHBOARD ===");
            System.out.println("1. Register Admin");
            System.out.println("2. Register Owner");
            System.out.println("3. Register Staff");
            System.out.println("4. View Admins");
            System.out.println("5. View Owners");
            System.out.println("6. View Staff");
            System.out.println("7. View Pets");
            System.out.println("8. View Appointments");
            System.out.println("9. Approve Accounts");
            System.out.println("10. Pets CRUD");
            System.out.println("11. Appointments CRUD");
            System.out.println("12. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 1: registerAdmin(); break;
                case 2: registerOwner(); break;
                case 3: registerStaff(); break;
                case 4: viewAdmins(); break;
                case 5: viewOwners(); break;
                case 6: viewStaffs(); break;
                case 7: viewPets(); break;
                case 8: viewAppointments(); break;
                case 9: approveAccountsAcrossTables(); break;
                case 10: rolePetsMenu(); break;
                case 11: roleAppointmentsMenu(); break;
                case 12: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 12);
    }
    
    public static void adminDashboard(int adminId) {
        int choice;
        do {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. View Admins");
            System.out.println("2. View Owners");
            System.out.println("3. View Staff");
            System.out.println("4. View Pets");
            System.out.println("5. View Appointments");
            System.out.println("6. Approve Accounts");
            System.out.println("7. Pets CRUD");
            System.out.println("8. Appointments CRUD");
            System.out.println("9. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 1: viewAdmins(); break;
                case 2: viewOwners(); break;
                case 3: viewStaffs(); break;
                case 4: viewPets(); break;
                case 5: viewAppointments(); break;
                case 6: approveAccountsAcrossTables(); break;
                case 7: rolePetsMenu(); break;
                case 8: roleAppointmentsMenu(); break;
                case 9: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 9);
    }
    
    public static void ownerDashboard(int ownerId) {
        int choice;
        do {
            System.out.println("\n=== OWNER DASHBOARD ===");
            System.out.println("1. View Staff");
            System.out.println("2. View Pets");
            System.out.println("3. View Appointments");
            System.out.println("4. Pets CRUD");
            System.out.println("5. Appointments CRUD");
            System.out.println("6. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 1: viewStaffs(); break;
                case 2: viewPets(); break;
                case 3: viewAppointments(); break;
                case 4: rolePetsMenu(); break;
                case 5: roleAppointmentsMenu(); break;
                case 6: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 6);
    }
    
    public static void staffDashboard(int staffId) {
        int choice;
        do {
            System.out.println("\n=== STAFF DASHBOARD ===");
            System.out.println("1. Pets CRUD");
            System.out.println("2. Appointments CRUD");
            System.out.println("3. View Pets");
            System.out.println("4. View Appointments");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 1: rolePetsMenu(); break;
                case 2: roleAppointmentsMenu(); break;
                case 3: viewPets(); break;
                case 4: viewAppointments(); break;
                case 5: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }
    
    // --------------------- GENERIC APPROVALS ---------------------
    public static void approveAccountsAcrossTables() {
        System.out.println("\nPending Admins:");
        String qAdmin = "SELECT * FROM tbl_admin WHERE status='Pending'";
        con.viewRecords(qAdmin, new String[] {"Admin ID","Name","Email","Type"}, 
                       new String[] {"admin_id","name","email","type"});
        
        System.out.println("\nPending Owners:");
        String qOwner = "SELECT * FROM tbl_owner WHERE status='Pending'";
        con.viewRecords(qOwner, new String[] {"Owner ID","Name","Email"}, 
                       new String[] {"owner_id","name","email"});
        
        System.out.println("\nPending Staff:");
        String qStaff = "SELECT * FROM tbl_staff WHERE status='Pending'";
        con.viewRecords(qStaff, new String[] {"Staff ID","Owner ID","Name","Email"}, 
                       new String[] {"staff_id","owner_id","name","email"});
        
        System.out.print("\nApprove which table? (1-Admin, 2-Owner, 3-Staff, 0-Cancel): ");
        int t = sc.nextInt();
        if (t == 0) return;
        
        System.out.print("Enter ID to approve: ");
        int id = sc.nextInt();
        String sql = "";
        
        switch (t) {
            case 1:
                sql = "UPDATE tbl_admin SET status='Approved' WHERE admin_id=?";
                con.updateRecord(sql, String.valueOf(id));
                System.out.println("✅ Admin Approved!");
                break;
            case 2:
                sql = "UPDATE tbl_owner SET status='Approved' WHERE owner_id=?";
                con.updateRecord(sql, String.valueOf(id));
                System.out.println("✅ Owner Approved!");
                break;
            case 3:
                sql = "UPDATE tbl_staff SET status='Approved' WHERE staff_id=?";
                con.updateRecord(sql, String.valueOf(id));
                System.out.println("✅ Staff Approved!");
                break;
            default:
                System.out.println("Invalid table choice.");
        }
    }
    
    // --------------------- ROLE-SPECIFIC CRUD MENUS ---------------------
    public static void rolePetsMenu() {
        int ch;
        do {
            System.out.println("\n--- PETS CRUD ---");
            System.out.println("1. Add Pet");
            System.out.println("2. Update Pet");
            System.out.println("3. Delete Pet");
            System.out.println("4. View Pets");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            ch = sc.nextInt();
            
            switch (ch) {
                case 1: addPet(); break;
                case 2: updatePet(); break;
                case 3: deletePet(); break;
                case 4: viewPets(); break;
                case 5: break;
                default: System.out.println("Invalid.");
            }
        } while (ch != 5);
    }
    
    public static void roleAppointmentsMenu() {
        int ch;
        do {
            System.out.println("\n--- APPOINTMENTS CRUD ---");
            System.out.println("1. Add Appointment");
            System.out.println("2. Update Appointment");
            System.out.println("3. Delete Appointment");
            System.out.println("4. View Appointments");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            ch = sc.nextInt();
            
            switch (ch) {
                case 1: addAppointment(); break;
                case 2: updateAppointment(); break;
                case 3: deleteAppointment(); break;
                case 4: viewAppointments(); break;
                case 5: break;
                default: System.out.println("Invalid.");
            }
        } while (ch != 5);
    }
    
    // --------------------- LOGIN HANDLING ---------------------
    public static void handleLogin() {
        sc.nextLine(); // consume any leftover newline
        System.out.print("Enter Email: ");
        String em = sc.nextLine().trim();
        System.out.print("Enter Password: ");
        String pas = sc.nextLine();
        
        String hashpass = con.hashPassword(pas);
        
        // 1) Check hard-coded Super Admin first
        String superHashed = con.hashPassword(SUPER_PASS);
        if (em.equalsIgnoreCase(SUPER_EMAIL) && hashpass.equals(superHashed)) {
            System.out.println("🔑 SUPER ADMIN LOGIN SUCCESS!");
            superAdminDashboard();
            return;
        }
        
        // 2) Check tbl_admin
        String qAdmin = "SELECT * FROM tbl_admin WHERE email = ? AND password = ?";
        List<Map<String, Object>> adminRes = con.fetchRecords(qAdmin, em, hashpass);
        if (!adminRes.isEmpty()) {
            Map<String, Object> admin = adminRes.get(0);
            String status = admin.get("status").toString();
            if (!status.equalsIgnoreCase("Approved")) {
                System.out.println("⚠️ Admin account not approved yet.");
                return;
            }
            System.out.println("✅ ADMIN LOGIN SUCCESS!");
            int adminId = Integer.parseInt(admin.get("admin_id").toString());
            adminDashboard(adminId);
            return;
        }
        
        // 3) Check tbl_owner
        String qOwner = "SELECT * FROM tbl_owner WHERE email = ? AND password = ?";
        List<Map<String, Object>> ownerRes = con.fetchRecords(qOwner, em, hashpass);
        if (!ownerRes.isEmpty()) {
            Map<String, Object> owner = ownerRes.get(0);
            String status = owner.get("status").toString();
            if (!status.equalsIgnoreCase("Approved")) {
                System.out.println("⚠️ Owner account not approved yet.");
                return;
            }
            System.out.println("✅ OWNER LOGIN SUCCESS!");
            int ownerId = Integer.parseInt(owner.get("owner_id").toString());
            ownerDashboard(ownerId);
            return;
        }
        
        // 4) Check tbl_staff
        String qStaff = "SELECT * FROM tbl_staff WHERE email = ? AND password = ?";
        List<Map<String, Object>> staffRes = con.fetchRecords(qStaff, em, hashpass);
        if (!staffRes.isEmpty()) {
            Map<String, Object> staff = staffRes.get(0);
            String status = staff.get("status").toString();
            if (!status.equalsIgnoreCase("Approved")) {
                System.out.println("⚠️ Staff account not approved yet.");
                return;
            }
            System.out.println("✅ STAFF LOGIN SUCCESS!");
            int staffId = Integer.parseInt(staff.get("staff_id").toString());
            staffDashboard(staffId);
            return;
        }
        
        System.out.println("❌ INVALID CREDENTIALS");
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
                case 1:
                    handleLogin();
                    break;
                case 2:
                    System.out.println("\nRegister as:");
                    System.out.println("1. Admin");
                    System.out.println("2. Owner");
                    System.out.println("3. Staff");
                    System.out.print("Choose: ");
                    int r = sc.nextInt();
                    switch (r) {
                        case 1: registerAdmin(); break;
                        case 2: registerOwner(); break;
                        case 3: registerStaff(); break;
                        default: System.out.println("Invalid registration option.");
                    }
                    break;
                case 3:
                    System.out.println("👋 Program Ended.");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Invalid choice.");
            }
            
            System.out.print("Do you want to continue? (Y/N): ");
            cont = sc.next().charAt(0);
        } while (cont == 'Y' || cont == 'y');
        
        sc.close();
    }
}