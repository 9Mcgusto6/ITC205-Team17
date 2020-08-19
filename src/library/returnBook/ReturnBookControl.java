package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

    private ReturnBookUI ui;
    private enum ControlState {INITIALISED, READY, INSPECTING};
    private ControlState state;
    
    private Library library;
    private Loan currentLoan;
    

    public ReturnBookControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }
    
    
    public void setUi(ReturnBookUI ui) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
        }
        this.ui = ui;
        ui.setState(ReturnBookUI.UiState.READY);
        state = ControlState.READY;        
    }


    public void bookScanned(int bookId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
        }
        Book currentBook = library.getBook(bookId);
        if (currentBook == null) {
            ui.display("Invalid Book Id");
            return;
        }
        if (!currentBook.isOnLoan()) {
            ui.display("Book has not been borrowed");
            return;
        }        
        currentLoan = library.getLoanByBookId(bookId);    
        double overdueFine = 0.0;

        if (currentLoan.isOverDue()) {
            overdueFine = library.calculateOverdueFine(currentLoan);
        }
        ui.display("Inspecting");
        ui.display(currentBook.toString());
        ui.display(currentLoan.toString());
        
        if (currentLoan.isOverDue()) {
            ui.display(String.format("\nOverdue fine : $%.2f", overdueFine));
        }
        ui.setState(ReturnBookUI.UiState.INSPECTING);
        state = ControlState.INSPECTING;        
    }


    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
        }
        ui.setState(ReturnBookUI.UiState.COMPLETED);        
    }


    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(ControlState.INSPECTING)) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        }
        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        ui.setState(ReturnBookUI.UiState.READY);
        state = ControlState.READY;                
    }


}
