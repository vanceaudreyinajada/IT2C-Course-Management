package Inajada;

import java.util.Scanner;
import java.sql.*;
import java.time.LocalDate;

public class Student_List {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    
    public void list(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Manage Students List**","");
            System.out.printf("|%-5s%-95s|\n","","1. Add");
            System.out.printf("|%-5s%-95s|\n","","2. Edit");
            System.out.printf("|%-5s%-95s|\n","","3. Delete");
            System.out.printf("|%-5s%-95s|\n","","4. View");
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
                    add();
                    break;
                case 2:
                    view();
                    edit();
                    view();
                    break;
                case 3:
                    view();
                    delete();
                    view();
                    break;
                case 4:
                    view();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    
    private void add(){
        LocalDate cdate = LocalDate.now();
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Add Student**","");
        System.out.print("|\tEnter First Name: ");
        String fname = input.next();
        System.out.print("|\tEnter Middle Name: ");
        String mname = input.next();
        System.out.print("|\tEnter Last Name: ");
        String lname = input.next();
        String gender;
        while(true){
            System.out.print("|\tGender (Male/Female): ");
            try{
                gender = input.next();
                if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                    break;
                }
            }catch(Exception e){

            }
        }
        String stat = "Un-Enrolled";
        String SQL = "INSERT INTO Student_List (Se_fname, Se_mname, Se_lname, Se_gender, Se_status, Se_Last_Update) Values (?,?,?,?,?,?)";
        conf.addRecord(SQL, fname, mname, lname, gender, stat, cdate);
    }
    private void edit(){
        LocalDate cdate = LocalDate.now();
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Edit Student**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter ID to Edit: ");
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
                    System.out.print("|\tEnter ID to Edit Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Edit Again: ");
            }
        }
        while(exit){
            System.out.print("|\tEnter New First Name: ");
            String fname = input.next();
            System.out.print("|\tEnter New Middle Name: ");
            String mname = input.next();
            System.out.print("|\tEnter New Last Name: ");
            String lname = input.next();
            String gender;
            while(true){
                System.out.print("|\tEnter New Gender (Male/Female): ");
                try{
                    gender = input.next();
                    if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                        break;
                    }
                }catch(Exception e){

                }
            }
            String SQL = "UPDATE Student_List SET Se_fname = ?, Se_mname = ?, Se_lname = ?, Se_gender = ?, Se_Last_Update = ?Where Se_Id = ?";
            conf.updateRecord(SQL, fname, mname, lname, gender, cdate, id);
            exit = false;
        }
    }
    public void view(){
        String tbl_view = "SELECT * FROM Student_List";
        String[] tbl_Headers = {"ID", "First Name", "Last Name", "Status"};
        String[] tbl_Columns = {"Se_Id", "Se_fname", "Se_lname", "Se_status"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    private void delete(){
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Delete Listed Student**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter ID to Delete: ");
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
                    System.out.print("|\tEnter ID to Delete Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Delete Again: ");
            }
        }
        while(exit){
            String SQL = "DELETE FROM Student_List Where Se_Id = ?";
            conf.deleteRecord(SQL, id);
            exit = false;
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
}
