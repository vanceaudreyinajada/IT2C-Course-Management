package Inajada;

import java.time.LocalDate;
import java.util.Scanner;
import java.sql.*;

public class Enroll_Student {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Student_List sl = new Student_List();
    Manage_Course mc = new Manage_Course();
    
    public void Enrol(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Manage Student's Enrollment**","");
            System.out.printf("|%-5s%-95s|\n","","1. Enroll");
            System.out.printf("|%-5s%-95s|\n","","2. Un-Enroll");
            System.out.printf("|%-5s%-95s|\n","","3. View Enrolled Student");
            System.out.printf("|%-5s%-95s|\n","","4. View Un-Enrolled Student");
            System.out.printf("|%-5s%-95s|\n","","5. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<6){
                        break;
                    }else{
                        System.out.printf("|%-5sEnter Choice Again: ","");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.printf("|%-5sEnter Choice Again: ","");
                }
            }
            switch(choice){
                case 1:
                    sl.view();
                    enroll();
                    break;
                case 2:
                    sl.view();
                    un_enroll();
                    break;
                case 3:
                    viewEnrolled();
                    break;
                case 4:
                    viewUnEnrolled();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    private void enroll(){
        LocalDate cdate = LocalDate.now();
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Enroll Student**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter ID to Enroll: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID to Enroll Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Enroll Again: ");
            }
        }
        while(exit){
            mc.view();
            System.out.print("|\tEnter Course ID: ");
            int Cid;
            while (true) {
                try {
                    Cid = input.nextInt();
                    if (doesIDexists2(Cid, conf)) {
                        if (!isAlreadyEnrolled(id, Cid)) {
                            break;
                        } else {
                            System.out.printf("|%-25s%-50s%-25s|\n","","**!Warning, Cant Enroll in the Same Course Twice!**","");
                            return;
                        }
                    } else {
                        System.out.print("|\tEnter Course ID Again: ");
                    }
                } catch (Exception e) {
                    input.next();
                    System.out.print("|\tEnter Course ID Again: ");
                }
            }
            double Tuition = getCurrentBalance3(Cid);
            double CTuition = getCurrentBalance2(id);
            double sum = Tuition + CTuition;
            String Cname = getCourseName(Cid);
            String stat = "Enrolled";
            String fname = getStudentFname(id);
            String lname = getStudentLname(id);
            
            String SQL = "UPDATE Student_List SET Se_status = ?, Se_Total_Tuition = ?, Se_Last_Update = ? Where Se_Id = ?";
            conf.updateRecord(SQL, stat, sum, cdate, id);
            
            String SQL2 = "INSERT INTO Enrolled_List (Se_Id, Cm_Id, Se_fname, Se_lname, Cm_name, Cm_Tuition_Fee, El_Enrollment_Date) Values (?,?,?,?,?,?,?)";
            conf.addRecord(SQL2, id, Cid, fname, lname, Cname, Tuition, cdate);
            exit = false;
        }
    }
    private void un_enroll(){
        LocalDate cdate = LocalDate.now();
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Un-Enroll Student**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter Student ID to Un-Enroll: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID to Un-Enroll Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Un-Enroll Again: ");
            }
        }
        while(exit){
            boolean exit2 = true;
            view2(id);
            System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
            System.out.print("|\tEnter Course ID to Un-Enroll: ");
            int Cid;
            while(true){
                try{
                    Cid = input.nextInt();
                    if(doesIDexists3(Cid, conf)){
                        break;
                    }else if(Cid == 0){
                        exit2 = false;
                        break;
                    }else{
                        System.out.print("|\tEnter ID to Enroll Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("|\tEnter ID to Enroll Again: ");
                }
            }
            while(exit2){
                double Tuition = getCurrentBalance(Cid);
                double CTuition = getCurrentBalance2(id);
                double newTotal = CTuition - Tuition;

                String SQL = "DELETE FROM Enrolled_List WHERE El_Id = ?";
                conf.deleteRecord(SQL, Cid);

                if(hasCoursesEnrolled(id)){
                    String SQL2 = "UPDATE Student_List SET Se_Total_Tuition = ?, Se_Last_Update = ? WHERE Se_Id = ?";
                    conf.updateRecord(SQL2, newTotal, cdate, id);
                } else {
                    String SQL3 = "UPDATE Student_List SET Se_status = ?, Se_Total_Tuition = ?, Se_Last_Update = ? WHERE Se_Id = ?";
                    conf.updateRecord(SQL3, "Un-Enrolled", 0.0, cdate, id);
                }
                exit2 = false;
            }
            view2(id);
            exit = false;
        }
    }
    public void view(){
        String tbl_view = "SELECT * FROM Enrolled_List";
        String[] tbl_Headers = {"ID", "First Name", "Last Name", "Course Name"};
        String[] tbl_Columns = {"El_Id", "Se_fname", "Se_lname", "Cm_name"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    public void viewEnrolled() {
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Enrolled Student**", "");

        String tbl_view = "SELECT Enrolled_List.El_Id, Enrolled_List.Se_fname, Enrolled_List.Se_lname, "
                        + "Enrolled_List.Cm_name, Student_List.Se_status "
                        + "FROM Enrolled_List "
                        + "JOIN Student_List ON Enrolled_List.Se_Id = Student_List.Se_Id "
                        + "WHERE Student_List.Se_status = 'Enrolled'";

        String[] tbl_Headers = {"First Name", "Last Name", "Course Name", "Status"};
        String[] tbl_Columns = {"Se_fname", "Se_lname", "Cm_name", "Se_status"};

        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }

    public void viewUnEnrolled() {
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Un-Enrolled Student**", "");
        String tbl_view = "SELECT Enrolled_List.El_Id, Student_List.Se_fname, Student_List.Se_lname, "
                        + "Enrolled_List.Cm_name, Student_List.Se_status "
                        + "FROM Student_List "
                        + "LEFT JOIN Enrolled_List ON Enrolled_List.Se_Id = Student_List.Se_Id "
                        + "WHERE Student_List.Se_status = 'Un-Enrolled'";

        String[] tbl_Headers = {"First Name", "Last Name", "Status"};
        String[] tbl_Columns = {"Se_fname", "Se_lname","Se_status"};

        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }



    public void view2(int id){
        try {
            String transactionSQL = "SELECT El_Id, Se_fname, Se_lname, Cm_name FROM Enrolled_List WHERE Se_Id = ?";
            PreparedStatement EnrollmentStmt = conf.connectDB().prepareStatement(transactionSQL);
            EnrollmentStmt.setInt(1, id);
            ResultSet EnrollmentRs = EnrollmentStmt.executeQuery();

            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Enrolled In**", "");
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-20s|%-20s|%-20s|%-20s|\n", "ID", "First Name", "Last Name", "Course Name");
            System.out.println("+----------------------------------------------------------------------------------------------------+");

            boolean hasList = false;
            while (EnrollmentRs.next()) {
                hasList = true;
                System.out.printf("|%-20s|%-20s|%-20s|%-20s|\n", 
                    EnrollmentRs.getString("El_Id"), 
                    EnrollmentRs.getString("Se_fname"), 
                    EnrollmentRs.getString("Se_lname"),
                    EnrollmentRs.getString("Cm_name"));
            }

            if (!hasList) {
                System.out.printf("|%-25s%-50s%-25s|\n","","!!Has not been Enrolled yet!!","");
            }

            System.out.println("+----------------------------------------------------------------------------------------------------+");
            EnrollmentRs.close();
        } catch (Exception e) {
            System.out.println("|\tError retrieving data: " + e.getMessage() + " |");
        }
    }
    
    //validation sa ubos
    private boolean doesIDexists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM Student_List Where Se_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
    private boolean doesIDexists2(int id, config conf) {
        String query = "SELECT COUNT(*) FROM Course_Management Where Cm_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
    private boolean doesIDexists3(int id, config conf) {
        String query = "SELECT COUNT(*) FROM Enrolled_List Where El_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
    private double getCurrentBalance(int id) {
        String query = "SELECT Cm_Tuition_Fee FROM Enrolled_List WHERE El_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("Cm_Tuition_Fee");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving balance: " + e.getMessage());
        }
        return 0.0;
    }
    private double getCurrentBalance2(int id) {
        String query = "SELECT Se_Total_Tuition FROM Student_List WHERE Se_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("Se_Total_Tuition");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving balance: " + e.getMessage());
        }
        return 0.0;
    }
    private double getCurrentBalance3(int id) {
        String query = "SELECT Cm_Tuition_Fee FROM Course_Management WHERE Cm_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("Cm_Tuition_Fee");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving balance: " + e.getMessage());
        }
        return 0.0;
    }
    private String getCourseName(int courseId) {
        String query = "SELECT Cm_name FROM Course_Management WHERE Cm_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Cm_name");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving course name: " + e.getMessage());
        }
        return null;
    }
    private boolean hasCoursesEnrolled(int id) {
        String query = "SELECT COUNT(*) FROM Enrolled_List WHERE Se_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking enrolled courses: " + e.getMessage());
        }
        return false;
    }
    private boolean isAlreadyEnrolled(int studentId, int courseId) {
        String query = "SELECT COUNT(*) FROM Enrolled_List WHERE Se_Id = ? AND Cm_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking enrollment status: " + e.getMessage());
        }
        return false;
    }
    private String getStudentFname(int courseId) {
        String query = "SELECT Se_fname FROM Student_List WHERE Se_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Se_fname");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving course name: " + e.getMessage());
        }
        return null;
    }
    private String getStudentLname(int courseId) {
        String query = "SELECT Se_lname FROM Student_List WHERE Se_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Se_lname");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving course name: " + e.getMessage());
        }
        return null;
    }
}
