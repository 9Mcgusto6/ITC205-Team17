package library.payfine;
import java.util.Scanner;

public class PayFineUI {
    
    public static enum UI_STATE { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
    private PayFineControl control;
    private Scanner input;
    private UI_STATE StAtE;
    
    public PayFineUI(PayFineControl control) {
        this.control = control;
        input = new Scanner(System.in);
        StAtE = UI_STATE.INITIALISED;
        control.SeT_uI(this);
    }
    
    public void SeT_StAtE(UI_STATE state) {
        this.StAtE = state;
    }
    
    public void RuN() {
        output("Pay Fine Use Case UI\n");
        while (true) {
            switch (StAtE) {
                case READY:
                    String Mem_Str = input("Swipe member card (press <enter> to cancel): ");
                    if (Mem_Str.length() == 0) {
                        control.CaNcEl();
                        break;
                    }
                    try {
                        int Member_ID = Integer.valueOf(Mem_Str).intValue();
                        control.CaRd_sWiPeD(Member_ID);
                    }
                    catch (NumberFormatException e) {
                        output("Invalid memberId");
                    }
                    break;
                case PAYING:
                    double AmouNT = 0;
                    String Amt_Str = input("Enter amount (<Enter> cancels) : ");
                    if (Amt_Str.length() == 0) {
                        control.CaNcEl();
                        break;
                    }
                    try {
                        AmouNT = Double.valueOf(Amt_Str).doubleValue();
                    }
                    catch (NumberFormatException e) {}
                    if (AmouNT <= 0) {
                        output("Amount must be positive");
                        break;
                    }
                    control.PaY_FiNe(AmouNT);
                    break;
                case CANCELLED:
                    output("Pay Fine process cancelled");
                    return;
                case COMPLETED:
                    output("Pay Fine process complete");
                    return;
                default:
                    output("Unhandled state");
                    throw new RuntimeException("FixBookUI : unhandled state :" + StAtE);
            }
        }
    }
    
    private String input(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }
    
    private void output(Object object) {
        System.out.println(object);
    }
    
    public void DiSplAY(Object object) {
        output(object);
    }
}