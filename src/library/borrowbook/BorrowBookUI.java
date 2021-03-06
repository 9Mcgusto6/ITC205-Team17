package library.borrowbook;
import java.util.Scanner;


public class BorrowBookUI {
	
    public static enum UiState {INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED};

    private BorrowBookControl control;
    private Scanner input;
    private UiState state;

	
    public BorrowBookUI(BorrowBookControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UiState.INITIALISED;
        control.setUi(this);
    }

	
    private String input(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }	
		
		
    private void output(Object object) {
        System.out.println(object);
    }
	
			
    public void setState(UiState state) {
        this.state = state;
    }

	
    public void run() {
        output("Borrow Book Use Case UI\n");
		
        while (true) {
			
            switch (state) {			
			
            case CANCELLED:
                output("Borrowing Cancelled");
                return;

				
            case READY:
                String memberString = input("Swipe member card (press <enter> to cancel): ");
                if (memberString.length() == 0) {
                    control.cancelOperation();
                    break;
                }
                try {
                    int memberId = Integer.valueOf(memberString).intValue();
                    control.cardSwiped(memberId);
                }
                catch (NumberFormatException exception) {
                    output("Invalid Member Id");
                }
                break;

				
            case RESTRICTED:
                input("Press <any key> to cancel");
                control.cancelOperation();
                break;
			
				
            case SCANNING:
                String bookStringInput = input("Scan Book (<enter> completes): ");
                if (bookStringInput.length() == 0) {
                    control.completeOperation();
                    break;
                }
                try {
                    int bookId = Integer.valueOf(bookStringInput).intValue();
                    control.bookScanned(bookId);					
                } 
                catch (NumberFormatException exception) {
                    output("Invalid Book Id");
                } 
                break;
					
				
            case FINALISING:
                String answer = input("Commit loans? (Y/N): ");
                if (answer.toUpperCase().equals("N")) {
                    control.cancelOperation();					
                } 
                else {
                    control.commitLoans();
                    input("Press <any key> to complete ");
                }
                break;
				
				
            case COMPLETED:
                output("Borrowing Completed");
                return;
	
				
            default:
                output("Unhandled state");
                throw new RuntimeException("BorrowBookUI : unhandled state :" + state);			
            }
        }		
    }


    public void display(Object object) {
        output(object);		
    }


}
