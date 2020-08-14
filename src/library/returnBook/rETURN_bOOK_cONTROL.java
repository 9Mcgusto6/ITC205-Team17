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
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
        }
        this.Ui = ui;
        ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
        state = ControlState.READY;        
    }


    public void bookScanned(int bookId) {
        if (!state.equals(ControlState.READY)) 
            throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
        
        Book currentBook = library.getBook(bookId);
        if (currentBook == null) {
            Ui.DiSpLaY("Invalid Book Id");
            return;
        }
        if (!currentBook.isOnLoan()) {
            Ui.DiSpLaY("Book has not been borrowed");
            return;
        }        
        currentLoan = library.getLoanByBookId(bookId);    
        double overdueFine = 0.0;

        if (currentLoan.isOverDue()) 
            overdueFine = library.calculateOverdueFine(currentLoan);
        
        Ui.DiSpLaY("Inspecting");
        Ui.DiSpLaY(currentBook.toString());
        Ui.DiSpLaY(currentLoan.toString());
        
        if (currentLoan.isOverDue()) 
            Ui.DiSpLaY(String.format("\nOverdue fine : $%.2f", overdueFine));
        
        Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.INSPECTING);
        state = ControlState.INSPECTING;        
    }


    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) 
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
            
        Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.COMPLETED);        
    }


    public void dischargeLoan(boolean iS_dAmAgEd) {
        if (!state.equals(ControlState.INSPECTING)) 
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        
        library.dischargeLoan(currentLoan, iS_dAmAgEd);
        currentLoan = null;
        Ui.sEt_sTaTe(ReturnBookUI.uI_sTaTe.READY);
        state = ControlState.READY;                
    }


}
