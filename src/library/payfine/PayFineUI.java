package library.payfine;
import java.util.Scanner;

public class PayFineUI {
    
    public static enum UI_STATE { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
    private PayFineControl control;
    private Scanner input;
    private UI_STATE state;
    
    public PayFineUI(PayFineControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UI_STATE.INITIALISED;
        control.setUI(this);
    }
    
    public void setState(UI_STATE state) {
        this.state = state;
    }
    
    public void payFine() {
        output("Pay Fine Use Case UI\n");
        while (true) {
            switch (state) {
                case READY:
                    String memberString = input("Swipe member card (press <enter> to cancel): ");
                    if (memberString.length() == 0) {
                        control.setCancelled();
                        break;
                    }
                    try {
                        int memberId = Integer.valueOf(memberString).intValue();
                        control.cardSwiped(memberId);
                    }
                    catch (NumberFormatException exception) {
                        output("Invalid memberId");
                    }
                    break;
                case PAYING:
                    double AmouNT = 0;
                    String Amt_Str = input("Enter amount (<Enter> cancels) : ");
                    if (Amt_Str.length() == 0) {
                        control.setCancelled();
                        break;
                    }
                    try {
                        AmouNT = Double.valueOf(Amt_Str).doubleValue();
                    }
                    catch (NumberFormatException exception) {}
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
                    throw new RuntimeException("FixBookUI : unhandled state :" + state);
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