package library;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import library.borrowbook.BorrowBookUI;
import library.borrowbook.bORROW_bOOK_cONTROL;
import library.entities.Book;
import library.entities.Calendar;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;
import library.fixbook.FixBookUI;
import library.fixbook.FixBookControl;
import library.payfine.PayFineUI;
import library.payfine.PayFineControl;
import library.returnBook.ReturnBookUI;
import library.returnBook.ReturnBookControl;


public class Main {
    
    private static Scanner in;
    private static Library lib;
    private static String menu;
    private static Calendar cal;
    private static SimpleDateFormat sdf;
    
    
    private static String getMenu() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\nLibrary Main Menu\n\n")
          .append("  M  : add member\n")
          .append("  LM : list members\n")
          .append("\n")
          .append("  B  : add book\n")
          .append("  LB : list books\n")
          .append("  FB : fix books\n")
          .append("\n")
          .append("  L  : take out a loan\n")
          .append("  R  : return a loan\n")
          .append("  LL : list loans\n")
          .append("\n")
          .append("  P  : pay fine\n")
          .append("\n")
          .append("  T  : increment date\n")
          .append("  Q  : quit\n")
          .append("\n")
          .append("Choice : ");
          
        return sb.toString();
    }


    public static void main(String[] args) {        
        try {            
            in = new Scanner(System.in);
            lib = Library.getInstance();
            cal = Calendar.getInstance();
            sdf = new SimpleDateFormat("dd/MM/yyyy");
    
            for (Member m : lib.listMembers()) {
                output(m);
            }
            output(" ");
            for (Book b : lib.listBooks()) {
                output(b);
            }
                        
            menu = getMenu();
            
            boolean e = false;
            
            while (!e) {
                
                output("\n" + sdf.format(cal.getDate()));
                String c = input(menu);
                
                switch (c.toUpperCase()) {
                
                case "M": 
                    addMember();
                    break;
                    
                case "LM": 
                    listMembers();
                    break;
                    
                case "B": 
                    addBook();
                    break;
                    
                case "LB": 
                    listBooks();
                    break;
                    
                case "FB": 
                    fixBooks();
                    break;
                    
                case "L": 
                    borrowBook();
                    break;
                    
                case "R": 
                    returnBook();
                    break;
                    
                case "LL": 
                    listCurrentLoans();
                    break;
                    
                case "P": 
                    payFines();
                    break;
                    
                case "T": 
                    incrementDate();
                    break;
                    
                case "Q": 
                    e = true;
                    break;
                    
                default: 
                    output("\nInvalid option\n");
                    break;
                }
                
                Library.save();
            }            
        } catch (RuntimeException e) {
            output(e);
        }        
        output("\nEnded\n");
    }    

    
    private static void payFines() {
        new PayFineUI(new PayFineControl()).payFine();        
    }


    private static void listCurrentLoans() {
        output("");
        for (Loan loan : lib.listCurrentLoans()) {
            output(loan + "\n");
        }        
    }



    private static void listBooks() {
        output("");
        for (Book book : lib.listBooks()) {
            output(book + "\n");
        }        
    }



    private static void listMembers() {
        output("");
        for (Member member : lib.listMembers()) {
            output(member + "\n");
        }        
    }



    private static void borrowBook() {
        new BorrowBookUI(new bORROW_bOOK_cONTROL()).RuN();        
    }


    private static void returnBook() {
        new ReturnBookUI(new ReturnBookControl()).run();        
    }


    private static void fixBooks() {
        new FixBookUI(new FixBookControl()).fixBook();        
    }


    private static void incrementDate() {
        try {
            int days = Integer.valueOf(input("Enter number of days: ")).intValue();
            cal.incrementDate(days);
            lib.checkCurrentLoans();
            output(sdf.format(cal.getDate()));
            
        } catch (NumberFormatException e) {
             output("\nInvalid number of days\n");
        }
    }


    private static void addBook() {
        
        String author = input("Enter author: ");
        String title  = input("Enter title: ");
        String callNumber = input("Enter call number: ");
        Book book = lib.addBook(author, title, callNumber);
        output("\n" + book + "\n");
        
    }

    
    private static void addMember() {
        try {
            String lastName = input("Enter last name: ");
            String firstName  = input("Enter first name: ");
            String emailAddress = input("Enter email address: ");
            int phoneNumber = Integer.valueOf(input("Enter phone number: ")).intValue();
            Member member = lib.addMember(lastName, firstName, emailAddress, phoneNumber);
            output("\n" + member + "\n");
            
        } catch (NumberFormatException e) {
             output("\nInvalid phone number\n");
        }
        
    }


    private static String input(String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }
    
    
    
    private static void output(Object object) {
        System.out.println(object);
    }
}
