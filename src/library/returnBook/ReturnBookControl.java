package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

    private ReturnBookUI Ui;
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
        this.Ui = ui;
        ui.setState(ReturnBookUI.UiState.READY);
        state = ControlState.READY;        
    }


    public void bookScanned(int bookId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
        }
        Book currentBook = library.getBook(bookId);
        if (currentBook == null) {
            Ui.display("Invalid Book Id");
            return;
        }
        if (!currentBook.isOnLoan()) {
            Ui.display("Book has not been borrowed");
            return;
        }        
        currentLoan = library.getLoanByBookId(bookId);    
        double overdueFine = 0.0;

        if (currentLoan.isOverDue()) {
            overdueFine = library.calculateOverdueFine(currentLoan);
        }
        Ui.display("Inspecting");
        Ui.display(currentBook.toString());
        Ui.display(currentLoan.toString());
        
        if (currentLoan.isOverDue()) {
            Ui.display(String.format("\nOverdue fine : $%.2f", overdueFine));
        }
        Ui.setState(ReturnBookUI.UiState.INSPECTING);
        state = ControlState.INSPECTING;        
    }


    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
        }
        Ui.setState(ReturnBookUI.UiState.COMPLETED);        
    }


    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(ControlState.INSPECTING)) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        }
        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        Ui.setState(ReturnBookUI.UiState.READY);
        state = ControlState.READY;                
    }


}
