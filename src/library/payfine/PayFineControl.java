package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
    
    private PayFineUI UI;
    private enum cOnTrOl_sTaTe { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
    private cOnTrOl_sTaTe StAtE;
    private Library LiBrArY;
    private Member MeMbEr;
    
    public PayFineControl() {
        this.LiBrArY = Library.getInstance();
        StAtE = cOnTrOl_sTaTe.INITIALISED;
    }
    
    public void SeT_uI(PayFineUI uI) {
        if (!StAtE.equals(cOnTrOl_sTaTe.INITIALISED)) {
            throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
        }
        this.UI = uI;
        uI.SeT_StAtE(PayFineUI.uI_sTaTe.READY);
        StAtE = cOnTrOl_sTaTe.READY;
    }
    
    public void CaRd_sWiPeD(int MeMbEr_Id) {
        if (!StAtE.equals(cOnTrOl_sTaTe.READY))
            throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
        MeMbEr = LiBrArY.getMember(MeMbEr_Id);
        if (MeMbEr == null) {
            UI.DiSplAY("Invalid Member Id");
            return;
        }
        UI.DiSplAY(MeMbEr.toString());
        UI.SeT_StAtE(PayFineUI.uI_sTaTe.PAYING);
        StAtE = cOnTrOl_sTaTe.PAYING;
    }
    
    public void CaNcEl() {
        UI.SeT_StAtE(PayFineUI.uI_sTaTe.CANCELLED);
        StAtE = cOnTrOl_sTaTe.CANCELLED;
    }
    
    public double PaY_FiNe(double AmOuNt) {
        if (!StAtE.equals(cOnTrOl_sTaTe.PAYING))
            throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
        double ChAnGe = MeMbEr.payFine(AmOuNt); //Changed PaY_FiNe to payFine according to method in Member
        if (ChAnGe > 0)
            UI.DiSplAY(String.format("Change: $%.2f", ChAnGe));
        UI.DiSplAY(MeMbEr.toString());
        UI.SeT_StAtE(PayFineUI.uI_sTaTe.COMPLETED);
        StAtE = cOnTrOl_sTaTe.COMPLETED;
        return ChAnGe;
    }
}