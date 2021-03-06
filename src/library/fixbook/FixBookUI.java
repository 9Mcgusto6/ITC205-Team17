package library.fixbook;
import java.util.Scanner;

public class FixBookUI {
    
    public static enum UIState {INITIALISED, READY, FIXING, COMPLETED};
    private FixBookControl control;
    private Scanner input;
    private UIState state;
    
    public FixBookUI(FixBookControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UIState.INITIALISED;
        control.setUI(this);
    }
    
    public void setState(UIState state) {
        this.state = state;
    }
    
    public void fixBook() {
        printOutput("Fix Book Use Case UI\n");
        while (true) {
            switch (state) {
                case READY:
                    String bookEntryString = input("Scan Book (<enter> completes): ");
                    if (bookEntryString.length() == 0) {
                        control.scanningComplete();
                    }
                    else {
                        try {
                            int BookId = Integer.valueOf(bookEntryString).intValue();
                            control.bookScanned(BookId);
                        }
                        catch (NumberFormatException exception) {
                            printOutput("Invalid bookId");
                        }
                    }
                    break;
                case FIXING:
                    String answer = input("Fix Book? (Y/N) : ");
                    boolean mustFix = false;
                    if (answer.toUpperCase().equals("Y")) {
                        mustFix = true;
                    }
                    control.fixBook(mustFix);
                    break;
                case COMPLETED:
                    printOutput("Fixing process complete");
                    return;
                default:
                    printOutput("Unhandled state");
                    throw new RuntimeException("FixBookUI : unhandled state :" + state);
            }
        }
    }
    
    private String input(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }
    
    private void printOutput(Object object) {
        System.out.println(object);
    }
    
    public void displayOutput(Object object) {
        printOutput(object);
    }
}