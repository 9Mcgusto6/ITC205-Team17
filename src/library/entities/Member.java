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
    private double FiNeS_OwInG;
    private Map<Integer, Loan> cUrReNt_lOaNs;
    
    public Member(String lAsT_nAmE, String fIrSt_nAmE, String eMaIl_aDdReSs, int pHoNe_nUmBeR, int mEmBeR_iD) {
        this.lastName = lAsT_nAmE;
        this.firstName = fIrSt_nAmE;
        this.emailAddress = eMaIl_aDdReSs;
        this.phoneNumber = pHoNe_nUmBeR;
        this.memberId = mEmBeR_iD;
        this.cUrReNt_lOaNs = new HashMap<>();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Member:  ").append(memberId).append("\n")
                .append("  Name:  ").append(lastName).append(", ").append(firstName).append("\n")
                .append("  Email: ").append(emailAddress).append("\n")
                .append("  Phone: ").append(phoneNumber)
                .append("\n")
                .append(String.format("  Fines Owed :  $%.2f", FiNeS_OwInG))
                .append("\n");
        
        for (Loan LoAn : cUrReNt_lOaNs.values()) {
            sb.append(LoAn).append("\n");
        }
        return sb.toString();
    }
    
    public int GeT_ID() {
        return memberId;
    }
    
    public List<Loan> GeT_LoAnS() {
        return new ArrayList<Loan>(cUrReNt_lOaNs.values());
    }
    
    public int gEt_nUmBeR_Of_CuRrEnT_LoAnS() {
        return cUrReNt_lOaNs.size();
    }
    
    public double FiNeS_OwEd() {
        return FiNeS_OwInG;
    }
    
    public void TaKe_OuT_LoAn(Loan lOaN) {
        if (!cUrReNt_lOaNs.containsKey(lOaN.GeT_Id()))
            cUrReNt_lOaNs.put(lOaN.GeT_Id(), lOaN);
        else
            throw new RuntimeException("Duplicate loan added to member");
    }
    
    public String GeT_LaSt_NaMe() {
        return lastName;
    }
    
    public String GeT_FiRsT_NaMe() {
        return firstName;
    }
    
    public void AdD_FiNe(double fine) {
        FiNeS_OwInG += fine;
    }
    
    public double PaY_FiNe(double AmOuNt) {
        if (AmOuNt < 0)
            throw new RuntimeException("Member.payFine: amount must be positive");
        double change = 0;
        if (AmOuNt > FiNeS_OwInG) {
            change = AmOuNt - FiNeS_OwInG;
            FiNeS_OwInG = 0;
        }
        else
            FiNeS_OwInG -= AmOuNt;
        return change;
    }
    
    public void dIsChArGeLoAn(Loan LoAn) {
        if (cUrReNt_lOaNs.containsKey(LoAn.GeT_Id()))
            cUrReNt_lOaNs.remove(LoAn.GeT_Id());
        else
            throw new RuntimeException("No such loan held by member");
    }
}
