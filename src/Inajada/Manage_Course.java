package Inajada;

import java.util.Scanner;
import java.sql.*;

public class Manage_Course {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    
    public void manage_course(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Manage Courses**","");
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
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Add Course**","");
        System.out.print("|\tEnter Course Name: ");
        String cname = input.next();
        System.out.print("|\tEnter Tuition Fee: ");
        double fee;
        while(true){
            try{
                fee = input.nextDouble();
                if(fee>=0){
                    break;
                }else{
                    System.out.print("|\tEnter Tuition Fee Again: ");
                }
            }catch(Exception e){
                System.out.print("|\tEnter Tuition Fee Again: ");
            }
        }
        String SQL = "INSERT INTO Course_Management (Cm_name, Cm_Tuition_Fee) Values (?,?)";
        conf.addRecord(SQL, cname, fee);
    }
    private void edit(){
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Edit Course**","");
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
        System.out.print("|\tEnter New Course Name: ");
        String cname = input.next();
        System.out.print("|\tEnter New Tuition Fee: ");
        double fee;
        while(true){
            try{
                fee = input.nextDouble();
                if(fee>=0){
                    break;
                }else{
                    System.out.print("|\tEnter New Tuition Fee Again: ");
                }
            }catch(Exception e){
                System.out.print("|\tEnter New Tuition Fee Again: ");
            }
        }
        String SQL = "UPDATE Course_Management SET Cm_name = ?, Cm_Tuition_Fee = ? Where Cm_Id = ?";
        conf.updateRecord(SQL, cname, fee, id);
    }
    public void view(){
        String tbl_view = "SELECT * FROM Course_Management";
        String[] tbl_Headers = {"ID", "Course Name", "Tuition Fee"};
        String[] tbl_Columns = {"Cm_Id", "Cm_name", "Cm_Tuition_Fee"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    private void delete(){
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Delete Course**","");
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
            String SQL = "DELETE FROM Course_Management Where Cm_Id = ?";
            conf.deleteRecord(SQL, id);
            exit = false;
        }
    }
    
    
    //validation sa ubos
    private boolean doesIDexists(int id, config conf) {
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
}
