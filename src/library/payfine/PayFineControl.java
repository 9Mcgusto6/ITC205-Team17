package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
    
    private PayFineUI UI;
    private enum ControlState {INITIALISED, READY, PAYING, COMPLETED, CANCELLED};
    private ControlState state;
    private Library library;
    private Member MeMbEr;
    
    public PayFineControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }
    
    public void SeT_uI(PayFineUI UI) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
        }
        this.UI = UI;
        UI.SeT_StAtE(PayFineUI.uI_sTaTe.READY);
        state = ControlState.READY;
    }
    
    public void CaRd_sWiPeD(int MeMbEr_Id) {
        if (!state.equals(ControlState.READY))
            throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
        MeMbEr = library.getMember(MeMbEr_Id);
        if (MeMbEr == null) {
            UI.DiSplAY("Invalid Member Id");
            return;
        }
        UI.DiSplAY(MeMbEr.toString());
        UI.SeT_StAtE(PayFineUI.uI_sTaTe.PAYING);
        state = ControlState.PAYING;
    }
    
    public void CaNcEl() {
        UI.SeT_StAtE(PayFineUI.uI_sTaTe.CANCELLED);
        state = ControlState.CANCELLED;
    }
    
    public double PaY_FiNe(double AmOuNt) {
        if (!state.equals(ControlState.PAYING))
            throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
        double ChAnGe = MeMbEr.payFine(AmOuNt); //Changed PaY_FiNe to payFine according to method in Member
        if (ChAnGe > 0)
            UI.DiSplAY(String.format("Change: $%.2f", ChAnGe));
        UI.DiSplAY(MeMbEr.toString());
        UI.SeT_StAtE(PayFineUI.uI_sTaTe.COMPLETED);
        state = ControlState.COMPLETED;
        return ChAnGe;
    }
}