package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class FixBookControl {
    
    private FixBookUI ui;
    private enum ControlState {INITIALISED, READY, FIXING};
    private ControlState state;
    private Library library;
    private Book currentBook;
    
    public FixBookControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }
    
    public void setUI(FixBookUI ui) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
        }
        this.ui = ui;
        ui.setState(FixBookUI.UIState.READY);
        state = ControlState.READY;
    }
    
    public void bookScanned(int BoOkId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
        }
        currentBook = library.getBook(BoOkId);
        if (currentBook == null) {
            ui.dIsPlAy("Invalid bookId");
            return;
        }
        if (!currentBook.isDamaged()) {
            ui.dIsPlAy("Book has not been damaged");
            return;
        }
        ui.dIsPlAy(currentBook.toString());
        ui.setState(FixBookUI.UIState.FIXING);
        state = ControlState.FIXING;
    }
    
    public void FiX_BoOk(boolean mUsT_FiX) {
        if (!state.equals(ControlState.FIXING))
            throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
        if (mUsT_FiX)
            library.repairBook(currentBook);
        currentBook = null;
        ui.setState(FixBookUI.UIState.READY);
        state = ControlState.READY;
    }
    
    public void SCannING_COMplete() {
        if (!state.equals(ControlState.READY))
            throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
        ui.setState(FixBookUI.UIState.COMPLETED);
    }
}