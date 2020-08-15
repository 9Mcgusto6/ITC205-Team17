package library.returnBook;
import java.util.Scanner;


public class ReturnBookUI {

	public static enum UiState {INITIALISED, READY, INSPECTING, COMPLETED};

	private rETURN_bOOK_cONTROL control;
	private Scanner input;
	private UiState state;

	
	public ReturnBookUI(rETURN_bOOK_cONTROL cOnTrOL) {
		this.control = cOnTrOL;
		input = new Scanner(System.in);
		state = UiState.INITIALISED;
		cOnTrOL.sEt_uI(this);
	}


	public void run() {		
		output("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case INITIALISED:
				break;
				
			case READY:
				String bookInputString = input("Scan Book (<enter> completes): ");
				if (bookInputString.length() == 0) 
					control.scanningComplete();
				
				else {
					try {
						int Book_Id = Integer.valueOf(bookInputString).intValue();
						control.bookScanned(Book_Id);
					}
					catch (NumberFormatException e) {
						output("Invalid bookId");
					}					
				}
				break;				
				
			case INSPECTING:
				String AnS = input("Is book damaged? (Y/N): ");
				boolean Is_DAmAgEd = false;
				if (AnS.toUpperCase().equals("Y")) 					
					Is_DAmAgEd = true;
				
				control.dischargeLoan(Is_DAmAgEd);
			
			case COMPLETED:
				output("Return processing complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + state);			
			}
		}
	}

	
	private String input(String PrOmPt) {
		System.out.print(PrOmPt);
		return input.nextLine();
	}	
		
		
	private void output(Object ObJeCt) {
		System.out.println(ObJeCt);
	}
	
			
	public void DiSpLaY(Object object) {
		output(object);
	}
	
	public void sEt_sTaTe(UiState state) {
		this.state = state;
	}

	
}
