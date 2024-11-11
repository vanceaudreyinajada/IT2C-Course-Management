package Inajada;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Course_Management_System**","");
            System.out.printf("|%-5s%-95s|\n","","1. Student List");
            System.out.printf("|%-5s%-95s|\n","","2. Manage Course");
            System.out.printf("|%-5s%-95s|\n","","3. Mange Student Enrollment");
            System.out.printf("|%-5s%-95s|\n","","4. Reports");
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
                    Student_List sl = new Student_List();
                    sl.list();
                break;
                case 2:
                    Manage_Course mc = new Manage_Course();
                    mc.manage_course();
                break;
                case 3:
                    Enroll_Student el = new Enroll_Student();
                    el.Enrol();
                break;
                case 4:
                    Report r = new Report();
                    r.report_type();
                break;
                default:
                    System.out.printf("|%-5sDo You Really Want to Exit (Yes/No): ","");
                    String exit2;
                    while(true){
                        try{
                            exit2 = input.next();
                            if(exit2.equalsIgnoreCase("yes")){
                                exit = false;
                                break;
                            }else{
                                break;
                            }
                        }catch(Exception e){
                            System.out.print("|\tEnter (Yes/No) Again: ");
                        }
                    }
                break;
            }
        }while(exit);
    }
}
