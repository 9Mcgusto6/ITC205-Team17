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
	private enum ControlState {INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED};
	private ControlState state;
	
	private List<Book> pendingList;
	private List<Loan> completedList;
	private Book book;
	
	public BorrowBookControl() {
		this.library = Library.getInstance();
		state = ControlState.INITIALISED;
	}
	

	public void SeT_Ui(BorrowBookUI Ui) {
		if (!state.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.ui = Ui;
		Ui.setState(BorrowBookUI.UiState.READY);
		state = ControlState.READY;		
	}

		
	public void SwIpEd(int mEmBeR_Id) {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		member = library.getMember(mEmBeR_Id);
		if (member == null) {
			ui.display("Invalid memberId");
			return;
		}
		if (library.canMemberBorrow(member)) {
			pendingList = new ArrayList<>();
			ui.setState(BorrowBookUI.UiState.SCANNING);
			state = ControlState.SCANNING; 
		}
		else {
			ui.display("Member cannot borrow at this time");
			ui.setState(BorrowBookUI.UiState.RESTRICTED); 
		}
	}
	
	
	public void ScAnNeD(int bOoKiD) {
		book = null;
		if (!state.equals(ControlState.SCANNING)) 
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
			
		book = library.getBook(bOoKiD);
		if (book == null) {
			ui.display("Invalid bookId");
			return;
		}
		if (!book.isAvailable()) {
			ui.display("Book cannot be borrowed");
			return;
		}
		pendingList.add(book);
		for (Book B : pendingList) 
			ui.display(B.toString());
		
		if (library.getNumberOfLoansRemaining(member) - pendingList.size() == 0) {
			ui.display("Loan limit reached");
			CoMpLeTe();
		}
	}
	
	
	public void CoMpLeTe() {
		if (pendingList.size() == 0) 
			CaNcEl();
		
		else {
			ui.display("\nFinal Borrowing List");
			for (Book bOoK : pendingList) 
				ui.display(bOoK.toString());
			
			completedList = new ArrayList<Loan>();
			ui.setState(BorrowBookUI.UiState.FINALISING);
			state = ControlState.FINALISING;
		}
	}


	public void CoMmIt_LoAnS() {
		if (!state.equals(ControlState.FINALISING)) 
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
			
		for (Book B : pendingList) {
			Loan lOaN = library.issueLoan(B, member);
			completedList.add(lOaN);			
		}
		ui.display("Completed Loan Slip");
		for (Loan LOAN : completedList) 
			ui.display(LOAN.toString());
		
		ui.setState(BorrowBookUI.UiState.COMPLETED);
		state = ControlState.COMPLETED;
	}

	
	public void CaNcEl() {
		ui.setState(BorrowBookUI.UiState.CANCELLED);
		state = ControlState.CANCELLED;
	}
	
	
}
