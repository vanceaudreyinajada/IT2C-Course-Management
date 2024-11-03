package Inajada;

import java.util.Scanner;
import java.sql.*;

public class Report {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Student_List sl = new Student_List();
    Enroll_Student es = new Enroll_Student();
    
    public void report_type(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Report**","");
            System.out.printf("|%-5s%-95s|\n","","1. General Report");
            System.out.printf("|%-5s%-95s|\n","","2. Individual Report");
            System.out.printf("|%-5s%-95s|\n","","3. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<4){
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
                    es.view();
                    break;
                case 2:
                    sl.view();
                    IndividualView();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    private void IndividualView() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Individual Report**", "");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**!Enter 0 in ID to Exit!**", "");
        System.out.print("|\tEnter ID to View: ");

        int id;
        while (true) {
            try {
                id = input.nextInt();
                if (doesIDexists(id, conf)) {
                    break;
                } else if (id == 0) {
                    exit = false;
                    break;
                } else {
                    System.out.print("|\tEnter ID to View Again: ");
                }
            } catch (Exception e) {
                input.next();
                System.out.print("|\tEnter ID to View Again: ");
            }
        }

        if (exit) {
            try {
                String customerSQL = "SELECT Se_fname, Se_mname, Se_lname, Se_gender, Se_status,Se_Total_Tuition, Se_Last_Update FROM Student_List WHERE Se_Id = ?";
                PreparedStatement StudentStmt = conf.connectDB().prepareStatement(customerSQL);
                StudentStmt.setInt(1, id);
                ResultSet StudentRs = StudentStmt.executeQuery();

                if (StudentRs.next()) {
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    System.out.printf("|%-25s%-50s%-25s|\n", "", "Individual Customer Information", "");
                    System.out.printf("|%-15s: %-60s|\n", "First Name", StudentRs.getString("Se_fname"));
                    System.out.printf("|%-15s: %-60s|\n", "Middle Name", StudentRs.getString("Se_mname"));
                    System.out.printf("|%-15s: %-60s|\n", "Last Name", StudentRs.getString("Se_lname"));
                    System.out.printf("|%-15s: %-60s|\n", "Gender", StudentRs.getString("Se_gender"));
                    System.out.printf("|%-15s: %-60s|\n", "Status", StudentRs.getString("Se_status"));
                    System.out.printf("|%-15s: %-60s|\n", "Total Tuition", StudentRs.getString("Se_Total_Tuition"));
                    System.out.printf("|%-15s: %-60s|\n", "Last Update", StudentRs.getString("Se_Last_Update"));
                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    String transactionSQL = "SELECT Cm_name, Cm_Tuition_Fee, El_Enrollment_Date FROM Enrolled_List WHERE Se_Id = ?";
                    PreparedStatement EnrollmentStmt = conf.connectDB().prepareStatement(transactionSQL);
                    EnrollmentStmt.setInt(1, id);
                    ResultSet EnrollmentRs = EnrollmentStmt.executeQuery();

                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Enrolled In**", "");
                    System.out.println("+-------------------------------+-------------------------------+-------------------------------+");
                    System.out.printf("| %-28s | %-28s | %-28s |\n", "Cm_name", "Cm_Tuition_Fee", "El_Enrollment_Date");
                    System.out.println("+-------------------------------+-------------------------------+-------------------------------+");

                    boolean hasList = false;
                    while (EnrollmentRs.next()) {
                        hasList = true;
                        System.out.printf("| %-28s | %-28s | %-28s |\n", 
                            EnrollmentRs.getString("Cm_name"), 
                            EnrollmentRs.getString("Cm_Tuition_Fee"), 
                            EnrollmentRs.getString("El_Enrollment_Date"));
                    }

                    if (!hasList) {
                        System.out.printf("|%-25s%-50s%-25s|\n","","!!Has not been Enroll yet!!","");
                    }

                    System.out.println("+-------------------------------+-------------------------------+-------------------------------+");

                    EnrollmentRs.close();
                    EnrollmentRs.close();
                } else {
                    System.out.println("|\tNo record found for ID: " + id + " |");
                }

                StudentRs.close();
                StudentRs.close();
            } catch (Exception e) {
                System.out.println("|\tError retrieving data: " + e.getMessage() + " |");
            }
        }
    }

    
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
}
