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
	
	private static final String LIBRARY_FILE = "library.obj"; //Changed lIbRaRyFiLe to LIBRARY_FILE
	private static final int LOAN_LIMIT = 2; //Changed lOaNlImIt to LOAN_LIMIT
	private static final int LOAN_PERIOD = 2; //Changed loanPeriod to LOAN_PERIOD
	private static final double FINE_PER_DAY = 1.0; //Changed FiNe_PeR_DaY to FINE_PER_DAY
	private static final double MAX_FINES_OWED = 1.0; //Changed maxFinesOwed to MAX_FINES_OWED
	private static final double DAMAGE_FEE = 2.0; //Changed damageFee to DAMAGE_FEE
	
	private static Library self; //Changed SeLf to self
	private int bookId; //Changed bOoK_Id to bookId
	private int memberId; //Changed mEmBeR_Id to memberId
	private int loanId; //Changed lOaN_Id to loanId
	private Date loanDate; //Changed lOaN_DaTe to loanDate
	
	private Map<Integer, Book> catalog; //Changed CaTaLoG to catalog
	private Map<Integer, Member> members; //Changed MeMbErS to members
	private Map<Integer, Loan> loans; //Changed LoAnS to loans
	private Map<Integer, Loan> currentLoans; //Changed CuRrEnT_LoAnS to currentLoans
	private Map<Integer, Book> damagedBooks; //Changed DaMaGeD_BoOkS to damagedBooks
	

	private Library() {
		catalog = new HashMap<>();
		members = new HashMap<>();
		loans = new HashMap<>();
		currentLoans = new HashMap<>();
		damagedBooks = new HashMap<>();
		bookId = 1;
		memberId = 1;		
		loanId = 1;		
	}

	
	public static synchronized Library GeTiNsTaNcE() {		
		if (self == null) {
			Path PATH = Paths.get(LIBRARY_FILE);			
			if (Files.exists(PATH)) {	
				try (ObjectInputStream LiBrArY_FiLe = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) {
			    
					self = (Library) LiBrArY_FiLe.readObject();
					Calendar.getInstance().setDate(self.loanDate); //Calendar.gEtInStAnCe().SeT_DaTe changed to getInstance().setDate to match method in Calendar
					LiBrArY_FiLe.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else self = new Library();
		}
		return self;
	}

	
	public static synchronized void SaVe() {
		if (self != null) {
			self.loanDate = Calendar.getInstance().getDate(); //gEt_DaTe changed to getDate to match method in Calendar
			try (ObjectOutputStream LiBrArY_fIlE = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) {
				LiBrArY_fIlE.writeObject(self);
				LiBrArY_fIlE.flush();
				LiBrArY_fIlE.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int gEt_BoOkId() {
		return bookId;
	}
	
	
	public int gEt_MeMbEr_Id() {
		return memberId;
	}
	
	
	private int gEt_NeXt_BoOk_Id() {
		return bookId++;
	}

	
	private int gEt_NeXt_MeMbEr_Id() {
		return memberId++;
	}

	
	private int gEt_NeXt_LoAn_Id() {
		return loanId++;
	}

	
	public List<Member> lIsT_MeMbErS() {		
		return new ArrayList<Member>(members.values()); 
	}


	public List<Book> lIsT_BoOkS() {		
		return new ArrayList<Book>(catalog.values()); 
	}


	public List<Loan> lISt_CuRrEnT_LoAnS() {
		return new ArrayList<Loan>(currentLoans.values());
	}


	public Member aDd_MeMbEr(String lastName, String firstName, String email, int phoneNo) {		
		Member member = new Member(lastName, firstName, email, phoneNo, gEt_NeXt_MeMbEr_Id());
		members.put(member.getMemberId(), member);	//Changed GeT_ID to getMemberId according to method in Member	
		return member;
	}

	
	public Book aDd_BoOk(String a, String t, String c) {		
		Book b = new Book(a, t, c, gEt_NeXt_BoOk_Id());
		catalog.put(b.gEtId(), b);		
		return b;
	}

	
	public Member gEt_MeMbEr(int memberId) {
		if (members.containsKey(memberId)) 
			return members.get(memberId);
		return null;
	}

	
	public Book gEt_BoOk(int bookId) {
		if (catalog.containsKey(bookId)) 
			return catalog.get(bookId);		
		return null;
	}

	
	public int gEt_LoAn_LiMiT() {
		return LOAN_LIMIT;
	}

	
	public boolean cAn_MeMbEr_BoRrOw(Member member) {		
		if (member.getNumberOfCurrentLoans() == LOAN_LIMIT ) //Changed gEt_nUmBeR_Of_CuRrEnT_LoAnS to getNumberOfCurrentLoans according to method in Member
			return false;
				
		if (member.getFinesOwed() >= MAX_FINES_OWED) //Changed FiNeS_OwEd to getFinesOwed according to method in Member
			return false;
				
		for (Loan loan : member.getLoans()) //Changed GeT_LoAnS to getLoans according to method in Member
			if (loan.Is_OvEr_DuE()) 
				return false;
			
		return true;
	}

	
	public int gEt_NuMbEr_Of_LoAnS_ReMaInInG_FoR_MeMbEr(Member MeMbEr) {		
		return LOAN_LIMIT - MeMbEr.getNumberOfCurrentLoans();
	}

	
	public Loan iSsUe_LoAn(Book book, Member member) {
		Date dueDate = Calendar.getInstance().getDueDate(LOAN_PERIOD); //Changed gEt_DuE_DaTe to getDueDate according to method in Calendar
		Loan loan = new Loan(gEt_NeXt_LoAn_Id(), book, member, dueDate);
		member.takeOutLoan(loan); //Changed TaKe_OuT_LoAn to takeOutLoan according to method in Member
		book.BoRrOw();
		loans.put(loan.GeT_Id(), loan);
		currentLoans.put(book.gEtId(), loan);
		return loan;
	}
	
	
	public Loan GeT_LoAn_By_BoOkId(int bookId) {
		if (currentLoans.containsKey(bookId)) 
			return currentLoans.get(bookId);
		
		return null;
	}

	
	public double CaLcUlAtE_OvEr_DuE_FiNe(Loan LoAn) {
		if (LoAn.Is_OvEr_DuE()) {
			long DaYs_OvEr_DuE = Calendar.getInstance().getDaysDifference(LoAn.GeT_DuE_DaTe()); //Changed GeT_DaYs_DiFfErEnCe to getDaysDifference according to method in Calendar
			double fInE = DaYs_OvEr_DuE * FINE_PER_DAY;
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
			mEmBeR.addFine(DAMAGE_FEE);
			damagedBooks.put(bOoK.gEtId(), bOoK);
		}
		cUrReNt_LoAn.DiScHaRgE();
		currentLoans.remove(bOoK.gEtId());
	}


	public void cHeCk_CuRrEnT_LoAnS() {
		for (Loan lOaN : currentLoans.values()) 
			lOaN.cHeCk_OvEr_DuE();
				
	}


	public void RePaIr_BoOk(Book cUrReNt_BoOk) {
		if (damagedBooks.containsKey(cUrReNt_BoOk.gEtId())) {
			cUrReNt_BoOk.RePaIr();
			damagedBooks.remove(cUrReNt_BoOk.gEtId());
		}
		else 
			throw new RuntimeException("Library: repairBook: book is not damaged");
		
		
	}
	
	
}
