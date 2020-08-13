package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class rETURN_bOOK_cONTROL {

	private ReturnBookUI Ui;
	private enum ControlState {INITIALISED, READY, INSPECTING};
	private ControlState state;
	
	private Library library;
	private Loan currentLoan;
	

	public rETURN_bOOK_cONTROL() {
		this.library = Library.getInstance();
		state = ControlState.INITIALISED;
	}
	
	
	public void sEt_uI(ReturnBookUI ui) {
		if (!state.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		
		this.Ui = ui;
		ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
		state = ControlState.READY;		
	}


	public void bOoK_sCaNnEd(int bOoK_iD) {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book cUrReNt_bOoK = library.getBook(bOoK_iD);
		if (cUrReNt_bOoK == null) {
			Ui.DiSpLaY("Invalid Book Id");
			return;
		}
		if (!cUrReNt_bOoK.isOnLoan()) {
			Ui.DiSpLaY("Book has not been borrowed");
			return;
		}		
		currentLoan = library.getLoanByBookId(bOoK_iD);	
		double Over_Due_Fine = 0.0;

		if (currentLoan.isOverDue()) 
			Over_Due_Fine = library.calculateOverdueFine(currentLoan);
		
		Ui.DiSpLaY("Inspecting");
		Ui.DiSpLaY(cUrReNt_bOoK.toString());
		Ui.DiSpLaY(currentLoan.toString());
		
		if (currentLoan.isOverDue()) 
			Ui.DiSpLaY(String.format("\nOverdue fine : $%.2f", Over_Due_Fine));
		
		Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.INSPECTING);
		state = ControlState.INSPECTING;		
	}


	public void sCaNnInG_cOmPlEtE() {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.COMPLETED);		
	}


	public void dIsChArGe_lOaN(boolean iS_dAmAgEd) {
		if (!state.equals(ControlState.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		
		library.dischargeLoan(currentLoan, iS_dAmAgEd);
		currentLoan = null;
		Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
		state = ControlState.READY;				
	}


}
