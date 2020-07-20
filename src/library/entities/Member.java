package library.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Member implements Serializable {

	private String LaSt_NaMe;
	private String FiRsT_NaMe;
	private String EmAiL_AdDrEsS;
	private int PhOnE_NuMbEr;
	private int MeMbEr_Id;
	private double FiNeS_OwInG;
	
	private Map<Integer, Loan> cUrReNt_lOaNs;

	
	public Member(String lAsT_nAmE, String fIrSt_nAmE, String eMaIl_aDdReSs, int pHoNe_nUmBeR, int mEmBeR_iD) {
		this.LaSt_NaMe = lAsT_nAmE;
		this.FiRsT_NaMe = fIrSt_nAmE;
		this.EmAiL_AdDrEsS = eMaIl_aDdReSs;
		this.PhOnE_NuMbEr = pHoNe_nUmBeR;
		this.MeMbEr_Id = mEmBeR_iD;
		
		this.cUrReNt_lOaNs = new HashMap<>();
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Member:  ").append(MeMbEr_Id).append("\n")
		  .append("  Name:  ").append(LaSt_NaMe).append(", ").append(FiRsT_NaMe).append("\n")
		  .append("  Email: ").append(EmAiL_AdDrEsS).append("\n")
		  .append("  Phone: ").append(PhOnE_NuMbEr)
		  .append("\n")
		  .append(String.format("  Fines Owed :  $%.2f", FiNeS_OwInG))
		  .append("\n");
		
		for (Loan LoAn : cUrReNt_lOaNs.values()) {
			sb.append(LoAn).append("\n");
		}		  
		return sb.toString();
	}

	
	public int GeT_ID() {
		return MeMbEr_Id;
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
		return LaSt_NaMe;
	}

	
	public String GeT_FiRsT_NaMe() {
		return FiRsT_NaMe;
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
