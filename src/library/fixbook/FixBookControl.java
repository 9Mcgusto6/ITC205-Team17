package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class FixBookControl {
    
    private FixBookUI uI;
    private enum ControlState { INITIALISED, READY, FIXING };
    private CoNtRoL_StAtE StAtE;
    private Library LiBrArY;
    private Book CuRrEnT_BoOk;
    
    public FixBookControl() {
        this.LiBrArY = Library.getInstance();
        StAtE = ControlState.INITIALISED;
    }
    
    public void SeT_Ui(FixBookUI ui) {
        if (!StAtE.equals(ControlState.INITIALISED))
            throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
        this.uI = ui;
        ui.SeT_StAtE(FixBookUI.uI_sTaTe.READY);
        StAtE = ControlState.READY;
    }
    
    public void BoOk_ScAnNeD(int BoOkId) {
        if (!StAtE.equals(ControlState.READY))
            throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
        CuRrEnT_BoOk = LiBrArY.getBook(BoOkId);
        if (CuRrEnT_BoOk == null) {
            uI.dIsPlAy("Invalid bookId");
            return;
        }
        if (!CuRrEnT_BoOk.isDamaged()) {
            uI.dIsPlAy("Book has not been damaged");
            return;
        }
        uI.dIsPlAy(CuRrEnT_BoOk.toString());
        uI.SeT_StAtE(FixBookUI.uI_sTaTe.FIXING);
        StAtE = ControlState.FIXING;
    }
    
    public void FiX_BoOk(boolean mUsT_FiX) {
        if (!StAtE.equals(ControlState.FIXING))
            throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
        if (mUsT_FiX)
            LiBrArY.repairBook(CuRrEnT_BoOk);
        CuRrEnT_BoOk = null;
        uI.SeT_StAtE(FixBookUI.uI_sTaTe.READY);
        StAtE = ControlState.READY;
    }
    
    public void SCannING_COMplete() {
        if (!StAtE.equals(ControlState.READY))
            throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
        uI.SeT_StAtE(FixBookUI.uI_sTaTe.COMPLETED);
    }
}