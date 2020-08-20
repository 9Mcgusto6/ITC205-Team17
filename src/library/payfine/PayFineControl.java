package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
    
    private PayFineUI UI;
    private enum ControlState {INITIALISED, READY, PAYING, COMPLETED, CANCELLED};
    private ControlState state;
    private Library library;
    private Member member;
    
    public PayFineControl() {
        this.library = Library.getInstance();
        state = ControlState.INITIALISED;
    }
    
    public void setUI(PayFineUI UI) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
        }
        this.UI = UI;
        UI.setState(PayFineUI.UIState.READY);
        state = ControlState.READY;
    }
    
    public void cardSwiped(int memberId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
        }
        member = library.getMember(memberId);
        if (member == null) {
            UI.displayOutput("Invalid Member Id");
            return;
        }
        UI.displayOutput(member.toString());
        UI.setState(PayFineUI.UIState.PAYING);
        state = ControlState.PAYING;
    }
    
    public void setCancelled() {
        UI.setState(PayFineUI.UIState.CANCELLED);
        state = ControlState.CANCELLED;
    }
    
    public double payFine(double amount) {
        if (!state.equals(ControlState.PAYING)) {
            throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
        }
        double change = member.payFine(amount); //Changed PaY_FiNe to payFine according to method in Member
        if (change > 0) {
            UI.displayOutput(String.format("Change: $%.2f", change));
        }
        UI.displayOutput(member.toString());
        UI.setState(PayFineUI.UIState.COMPLETED);
        state = ControlState.COMPLETED;
        return change;
    }
}