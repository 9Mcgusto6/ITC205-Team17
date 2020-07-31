package library.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Member implements Serializable {
    
    private String lastName;
    private String firstName;
    private String emailAddress;
    private int phoneNumber;
    private int memberId;
    private double finesOwing;
    private Map<Integer, Loan> currentLoans;
    
    public Member(String lastName, String firstName, String emailAddress, int phoneNumber, int memberId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.memberId = memberId;
        this.currentLoans = new HashMap<>();
    }
    
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Member:  ").append(memberId).append("\n")
                .append("  Name:  ").append(lastName).append(", ").append(firstName).append("\n")
                .append("  Email: ").append(emailAddress).append("\n")
                .append("  Phone: ").append(phoneNumber).append("\n")
                .append(String.format("  Fines Owed :  $%.2f", finesOwing)).append("\n");
        
        for (Loan Loan : currentLoans.values()) {
            stringBuilder.append(Loan).append("\n");
        }
        return stringBuilder.toString();
    }
    
    public int getMemberId() {
        return memberId;
    }
    
    public List<Loan> getLoans() {
        return new ArrayList<Loan>(currentLoans.values());
    }
    
    public int getNumberOfCurrentLoans() {
        return currentLoans.size();
    }
    
    public double getFinesOwed() {
        return finesOwing;
    }
    
    public void takeOutLoan(Loan Loan) {
        if (!currentLoans.containsKey(Loan.GeT_Id()))
            currentLoans.put(Loan.GeT_Id(), Loan);
        else
            throw new RuntimeException("Duplicate loan added to member");
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void AdD_FiNe(double fine) {
        finesOwing += fine;
    }
    
    public double PaY_FiNe(double AmOuNt) {
        if (AmOuNt < 0)
            throw new RuntimeException("Member.payFine: amount must be positive");
        double change = 0;
        if (AmOuNt > finesOwing) {
            change = AmOuNt - finesOwing;
            finesOwing = 0;
        }
        else
            finesOwing -= AmOuNt;
        return change;
    }
    
    public void dIsChArGeLoAn(Loan Loan) {
        if (currentLoans.containsKey(Loan.GeT_Id()))
            currentLoans.remove(Loan.GeT_Id());
        else
            throw new RuntimeException("No such loan held by member");
    }
}
