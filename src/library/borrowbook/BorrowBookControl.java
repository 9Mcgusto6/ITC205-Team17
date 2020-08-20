package library.borrowbook;
import java.util.ArrayList;
import java.util.List;

import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;

public class BorrowBookControl {
	
	private BorrowBookUI ui;
	
	private Library library;
	private Member member;
	private enum CONTROL_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private CONTROL_STATE sTaTe;
	
	private List<Book> pEnDiNg_LiSt;
	private List<Loan> cOmPlEtEd_LiSt;
	private Book bOoK;
	
	public BorrowBookControl() {
		this.library = Library.getInstance();
		sTaTe = CONTROL_STATE.INITIALISED;
	}
	

	public void SeT_Ui(BorrowBookUI Ui) {
		if (!sTaTe.equals(CONTROL_STATE.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.ui = Ui;
		Ui.setState(BorrowBookUI.UiState.READY);
		sTaTe = CONTROL_STATE.READY;		
	}

		
	public void SwIpEd(int mEmBeR_Id) {
		if (!sTaTe.equals(CONTROL_STATE.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		member = library.getMember(mEmBeR_Id);
		if (member == null) {
			ui.display("Invalid memberId");
			return;
		}
		if (library.canMemberBorrow(member)) {
			pEnDiNg_LiSt = new ArrayList<>();
			ui.setState(BorrowBookUI.UiState.SCANNING);
			sTaTe = CONTROL_STATE.SCANNING; 
		}
		else {
			ui.display("Member cannot borrow at this time");
			ui.setState(BorrowBookUI.UiState.RESTRICTED); 
		}
	}
	
	
	public void ScAnNeD(int bOoKiD) {
		bOoK = null;
		if (!sTaTe.equals(CONTROL_STATE.SCANNING)) 
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
			
		bOoK = library.getBook(bOoKiD);
		if (bOoK == null) {
			ui.display("Invalid bookId");
			return;
		}
		if (!bOoK.isAvailable()) {
			ui.display("Book cannot be borrowed");
			return;
		}
		pEnDiNg_LiSt.add(bOoK);
		for (Book B : pEnDiNg_LiSt) 
			ui.display(B.toString());
		
		if (library.getNumberOfLoansRemaining(member) - pEnDiNg_LiSt.size() == 0) {
			ui.display("Loan limit reached");
			CoMpLeTe();
		}
	}
	
	
	public void CoMpLeTe() {
		if (pEnDiNg_LiSt.size() == 0) 
			CaNcEl();
		
		else {
			ui.display("\nFinal Borrowing List");
			for (Book bOoK : pEnDiNg_LiSt) 
				ui.display(bOoK.toString());
			
			cOmPlEtEd_LiSt = new ArrayList<Loan>();
			ui.setState(BorrowBookUI.UiState.FINALISING);
			sTaTe = CONTROL_STATE.FINALISING;
		}
	}


	public void CoMmIt_LoAnS() {
		if (!sTaTe.equals(CONTROL_STATE.FINALISING)) 
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
			
		for (Book B : pEnDiNg_LiSt) {
			Loan lOaN = library.issueLoan(B, member);
			cOmPlEtEd_LiSt.add(lOaN);			
		}
		ui.display("Completed Loan Slip");
		for (Loan LOAN : cOmPlEtEd_LiSt) 
			ui.display(LOAN.toString());
		
		ui.setState(BorrowBookUI.UiState.COMPLETED);
		sTaTe = CONTROL_STATE.COMPLETED;
	}

	
	public void CaNcEl() {
		ui.setState(BorrowBookUI.UiState.CANCELLED);
		sTaTe = CONTROL_STATE.CANCELLED;
	}
	
	
}
