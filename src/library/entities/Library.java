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

	
	public static synchronized Library getInstance() {	//Changed GeTiNsTaNcE to getInstance 	
		if (self == null) {
			Path path = Paths.get(LIBRARY_FILE);	//Changed Path PATH to PATH path		
			if (Files.exists(path)) {	
				try (ObjectInputStream libraryFile = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) { //Changed LiBrArY_FiLe to libraryFile
			    
					self = (Library) libraryFile.readObject();
					Calendar.getInstance().setDate(self.loanDate); //Calendar.gEtInStAnCe().SeT_DaTe changed to getInstance().setDate to match method in Calendar
					libraryFile.close();
				}
				catch (Exception exception) {
					throw new RuntimeException(exception);
				}
			}
			else {
				self = new Library();
			}
		}
		return self;
	}

	
	public static synchronized void save() { //Changed SaVe to save
		if (self != null) {
			self.loanDate = Calendar.getInstance().getDate(); //gEt_DaTe changed to getDate to match method in Calendar
			try (ObjectOutputStream libraryFile = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) { //Changed LiBrArY_fIlE to libraryFile
				libraryFile.writeObject(self);
				libraryFile.flush();
				libraryFile.close();	
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}
	}

	public int getBookId() { //Changed gEt_BoOkId to getBookId
		return bookId;
	}
	
	
	public int getMemberId() { //Changed gEt_MeMbEr_Id to getMemberId
		return memberId;
	}
	
	
	private int getNextBookId() { //Changed gEt_NeXt_BoOk_Id to getNextBookId
		return bookId++;
	}

	
	private int getNextMemberId() { //Changed gEt_NeXt_MeMbEr_Id to getNextMemberId
		return memberId++;
	}

	
	private int getNextLoanId() { //Changed gEt_NeXt_LoAn_Id to getNextLoanId
		return loanId++;
	}

	
	public List<Member> listMembers() {		//Changed lIsT_MeMbErS to listMembers
		return new ArrayList<Member>(members.values()); 
	}


	public List<Book> listBooks() {		//Changed lIsT_BoOkS to listBooks
		return new ArrayList<Book>(catalog.values()); 
	}


	public List<Loan> listCurrentLoans() { //Changed lISt_CuRrEnT_LoAnS to listCurrentLoans
		return new ArrayList<Loan>(currentLoans.values());
	}


	public Member addMember(String lastName, String firstName, String email, int phoneNo) {		//Changed aDd_MeMbEr to addMember
		Member member = new Member(lastName, firstName, email, phoneNo, getNextMemberId());
		members.put(member.getMemberId(), member);	//Changed GeT_ID to getMemberId according to method in Member	
		return member;
	}


	public Book addBook(String a, String t, String c) {	//Changed aDd_BoOk to addBook	
		Book b = new Book(a, t, c, getNextBookId());
		catalog.put(b.getId(), b);		
		return b;
	}

	
	public Member getMember(int memberId) { //Changed gEt_MeMbEr to getMember
		if (members.containsKey(memberId)) { //Added curly brackets
			return members.get(memberId);
		}
		return null;
	}

	
	public Book getBook(int bookId) { //Changed gEt_BoOk to getBook
		if (catalog.containsKey(bookId)) { //Added curly brackets
			return catalog.get(bookId);		
		}
		return null;
	}

	public int getLoanLimit() { //Changed gEt_LoAn_LiMiT to getLoanLimit
		return LOAN_LIMIT;
	}

	
	public boolean canMemberBorrow(Member member) {		//Changed cAn_MeMbEr_BoRrOw to canMemberBorrow
		if (member.getNumberOfCurrentLoans() == LOAN_LIMIT ) { //Changed gEt_nUmBeR_Of_CuRrEnT_LoAnS to getNumberOfCurrentLoans according to method in Member
			return false;
		} //Added curly brackets
				
		if (member.getFinesOwed() >= MAX_FINES_OWED) { //Changed FiNeS_OwEd to getFinesOwed according to method in Member
			return false;
		} //Added curly brackets
				
		for (Loan loan : member.getLoans()) { //Changed GeT_LoAnS to getLoans according to method in Member
			if (loan.isOverDue()) {

				return false;
			} //Added curly brackets
		} //Added curly brackets	
		return true;
	}

	public int getNumberOfLoansRemaining(Member member) {		//Changed gEt_NuMbEr_Of_LoAnS_ReMaInInG_FoR_MeMbEr to getNumberOfLoansRemaining, changed MeMbEr to member
		return LOAN_LIMIT - member.getNumberOfCurrentLoans();
	}

	
	public Loan issueLoan(Book book, Member member) { //Changed iSsUe_LoAn to issueLoan
		Date dueDate = Calendar.getInstance().getDueDate(LOAN_PERIOD); //Changed gEt_DuE_DaTe to getDueDate according to method in Calendar
		Loan loan = new Loan(getNextLoanId(), book, member, dueDate);
		member.takeOutLoan(loan); //Changed TaKe_OuT_LoAn to takeOutLoan according to method in Member
		book.canBorrow();
		loans.put(loan.getId(), loan);
		currentLoans.put(book.getId(), loan);

		return loan;
	}
	
	
	public Loan getLoanByBookId(int bookId) { //Changed GeT_LoAn_By_BoOkId to getLoanByBookId
		if (currentLoans.containsKey(bookId)) {
			return currentLoans.get(bookId);
		} //Added curly brackets	
		return null;
	}

	
	public double calculateOverdueFine(Loan loan) { //Changed CaLcUlAtE_OvEr_DuE_FiNe to calculateOverdueFine, changed LoAn to loan
		if (loan.isOverDue()) {
			long daysOverdue = Calendar.getInstance().getDaysDifference(loan.getDueDate()); //Changed GeT_DaYs_DiFfErEnCe to getDaysDifference according to method in Calendar
			double fine = daysOverdue * FINE_PER_DAY; //Changed fInE to fine, changed DaYs_OvEr_DuE to daysOverdue
			return fine;
		}
		return 0.0;		
	}


	public void dischargeLoan(Loan currentLoan, boolean isDamaged) { //Changed DiScHaRgE_LoAn to dischargeLoan, cUrReNt_LoAn to currentLoan, iS_dAmAgEd to isDamaged
		Member member = currentLoan.getMember(); //Changed mEmBeR to member
		Book book  = currentLoan.getBook(); //Changed bOoK to book
		
		double overdueFine = calculateOverdueFine(currentLoan); //Changed oVeR_DuE_FiNe to overdueFine
		member.addFine(overdueFine);	//Changed AdD_FiNe to addFine according to method in Member
		
		member.dischargeLoan(currentLoan); //Changed dIsChArGeLoAn to dischargeLoan according to method in Member
		book.canReturn(isDamaged);
		if (isDamaged) {
			member.addFine(DAMAGE_FEE);
			damagedBooks.put(book.getId(), book);
		}
		currentLoan.discharge();
		currentLoans.remove(book.getId());
	}

	
	public void checkCurrentLoans() { //Changed cHeCk_CuRrEnT_LoAnS to checkCurrentLoans
		for (Loan loan : currentLoans.values()) { //Changed lOaN to loan
			loan.checkOverDue();			
		} //Added curly brackets
	}

	
	public void repairBook(Book currentBook) { //Changed RePaIr_BoOk to repairBook, cUrReNt_BoOk to currentBook
		if (damagedBooks.containsKey(currentBook.getId())) {
			currentBook.canRepair();
			damagedBooks.remove(currentBook.getId());
		}
		else 
			throw new RuntimeException("Library: repairBook: book is not damaged");	
	}	
	
}
