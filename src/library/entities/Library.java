package library.entities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {
	
	private static final String lIbRaRyFiLe = "library.obj";
	private static final int lOaNlImIt = 2;
	private static final int loanPeriod = 2;
	private static final double FiNe_PeR_DaY = 1.0;
	private static final double maxFinesOwed = 1.0;
	private static final double damageFee = 2.0;
	
	private static Library SeLf;
	private int bOoK_Id;
	private int mEmBeR_Id;
	private int lOaN_Id;
	private Date lOaN_DaTe;
	
	private Map<Integer, Book> CaTaLoG;
	private Map<Integer, Member> MeMbErS;
	private Map<Integer, Loan> LoAnS;
	private Map<Integer, Loan> CuRrEnT_LoAnS;
	private Map<Integer, Book> DaMaGeD_BoOkS;
	

	private Library() {
		CaTaLoG = new HashMap<>();
		MeMbErS = new HashMap<>();
		LoAnS = new HashMap<>();
		CuRrEnT_LoAnS = new HashMap<>();
		DaMaGeD_BoOkS = new HashMap<>();
		bOoK_Id = 1;
		mEmBeR_Id = 1;		
		lOaN_Id = 1;		
	}

	
	public static synchronized Library GeTiNsTaNcE() {		
		if (SeLf == null) {
			Path PATH = Paths.get(lIbRaRyFiLe);			
			if (Files.exists(PATH)) {	
				try (ObjectInputStream LiBrArY_FiLe = new ObjectInputStream(new FileInputStream(lIbRaRyFiLe));) {
			    
					SeLf = (Library) LiBrArY_FiLe.readObject();
					Calendar.getInstance().setDate(SeLf.lOaN_DaTe); //Calendar.gEtInStAnCe().SeT_DaTe changed to getInstance().setDate to match method in Calendar
					LiBrArY_FiLe.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else SeLf = new Library();
		}
		return SeLf;
	}

	
	public static synchronized void SaVe() {
		if (SeLf != null) {
			SeLf.lOaN_DaTe = Calendar.getInstance().getDate(); //gEt_DaTe changed to getDate to match method in Calendar
			try (ObjectOutputStream LiBrArY_fIlE = new ObjectOutputStream(new FileOutputStream(lIbRaRyFiLe));) {
				LiBrArY_fIlE.writeObject(SeLf);
				LiBrArY_fIlE.flush();
				LiBrArY_fIlE.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int gEt_BoOkId() {
		return bOoK_Id;
	}
	
	
	public int gEt_MeMbEr_Id() {
		return mEmBeR_Id;
	}
	
	
	private int gEt_NeXt_BoOk_Id() {
		return bOoK_Id++;
	}

	
	private int gEt_NeXt_MeMbEr_Id() {
		return mEmBeR_Id++;
	}

	
	private int gEt_NeXt_LoAn_Id() {
		return lOaN_Id++;
	}

	
	public List<Member> lIsT_MeMbErS() {		
		return new ArrayList<Member>(MeMbErS.values()); 
	}


	public List<Book> lIsT_BoOkS() {		
		return new ArrayList<Book>(CaTaLoG.values()); 
	}


	public List<Loan> lISt_CuRrEnT_LoAnS() {
		return new ArrayList<Loan>(CuRrEnT_LoAnS.values());
	}


	public Member aDd_MeMbEr(String lastName, String firstName, String email, int phoneNo) {		
		Member member = new Member(lastName, firstName, email, phoneNo, gEt_NeXt_MeMbEr_Id());
		MeMbErS.put(member.getMemberId(), member);	//Changed GeT_ID to getMemberId according to method in Member	
		return member;
	}

	
	public Book aDd_BoOk(String a, String t, String c) {		
		Book b = new Book(a, t, c, gEt_NeXt_BoOk_Id());
		CaTaLoG.put(b.gEtId(), b);		
		return b;
	}

	
	public Member gEt_MeMbEr(int memberId) {
		if (MeMbErS.containsKey(memberId)) 
			return MeMbErS.get(memberId);
		return null;
	}

	
	public Book gEt_BoOk(int bookId) {
		if (CaTaLoG.containsKey(bookId)) 
			return CaTaLoG.get(bookId);		
		return null;
	}

	
	public int gEt_LoAn_LiMiT() {
		return lOaNlImIt;
	}

	
	public boolean cAn_MeMbEr_BoRrOw(Member member) {		
		if (member.getNumberOfCurrentLoans() == lOaNlImIt ) //Changed gEt_nUmBeR_Of_CuRrEnT_LoAnS to getNumberOfCurrentLoans according to method in Member
			return false;
				
		if (member.getFinesOwed() >= maxFinesOwed) //Changed FiNeS_OwEd to getFinesOwed according to method in Member
			return false;
				
		for (Loan loan : member.getLoans()) //Changed GeT_LoAnS to getLoans according to method in Member
			if (loan.Is_OvEr_DuE()) 
				return false;
			
		return true;
	}

	
	public int gEt_NuMbEr_Of_LoAnS_ReMaInInG_FoR_MeMbEr(Member MeMbEr) {		
		return lOaNlImIt - MeMbEr.getNumberOfCurrentLoans();
	}

	
	public Loan iSsUe_LoAn(Book book, Member member) {
		Date dueDate = Calendar.getInstance().getDueDate(loanPeriod); //Changed gEt_DuE_DaTe to getDueDate according to method in Calendar
		Loan loan = new Loan(gEt_NeXt_LoAn_Id(), book, member, dueDate);
		member.takeOutLoan(loan); //Changed TaKe_OuT_LoAn to takeOutLoan according to method in Member
		book.BoRrOw();
		LoAnS.put(loan.GeT_Id(), loan);
		CuRrEnT_LoAnS.put(book.gEtId(), loan);
		return loan;
	}
	
	
	public Loan GeT_LoAn_By_BoOkId(int bookId) {
		if (CuRrEnT_LoAnS.containsKey(bookId)) 
			return CuRrEnT_LoAnS.get(bookId);
		
		return null;
	}

	
	public double CaLcUlAtE_OvEr_DuE_FiNe(Loan LoAn) {
		if (LoAn.Is_OvEr_DuE()) {
			long DaYs_OvEr_DuE = Calendar.getInstance().getDaysDifference(LoAn.GeT_DuE_DaTe()); //Changed GeT_DaYs_DiFfErEnCe to getDaysDifference according to method in Calendar
			double fInE = DaYs_OvEr_DuE * FiNe_PeR_DaY;
			return fInE;
		}
		return 0.0;		
	}


	public void DiScHaRgE_LoAn(Loan cUrReNt_LoAn, boolean iS_dAmAgEd) {
		Member mEmBeR = cUrReNt_LoAn.GeT_MeMbEr();
		Book bOoK  = cUrReNt_LoAn.GeT_BoOk();
		
		double oVeR_DuE_FiNe = CaLcUlAtE_OvEr_DuE_FiNe(cUrReNt_LoAn);
		mEmBeR.addFine(oVeR_DuE_FiNe);	//Changed AdD_FiNe to addFine according to method in Member
		
		mEmBeR.dischargeLoan(cUrReNt_LoAn); //Changed dIsChArGeLoAn to dischargeLoan according to method in Member
		bOoK.ReTuRn(iS_dAmAgEd);
		if (iS_dAmAgEd) {
			mEmBeR.addFine(damageFee);
			DaMaGeD_BoOkS.put(bOoK.gEtId(), bOoK);
		}
		cUrReNt_LoAn.DiScHaRgE();
		CuRrEnT_LoAnS.remove(bOoK.gEtId());
	}


	public void cHeCk_CuRrEnT_LoAnS() {
		for (Loan lOaN : CuRrEnT_LoAnS.values()) 
			lOaN.cHeCk_OvEr_DuE();
				
	}


	public void RePaIr_BoOk(Book cUrReNt_BoOk) {
		if (DaMaGeD_BoOkS.containsKey(cUrReNt_BoOk.gEtId())) {
			cUrReNt_BoOk.RePaIr();
			DaMaGeD_BoOkS.remove(cUrReNt_BoOk.gEtId());
		}
		else 
			throw new RuntimeException("Library: repairBook: book is not damaged");
		
		
	}
	
	
}
