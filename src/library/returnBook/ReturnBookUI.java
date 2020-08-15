package library.returnBook;
import java.util.Scanner;


public class ReturnBookUI {

	public static enum UiState {INITIALISED, READY, INSPECTING, COMPLETED};

	private rETURN_bOOK_cONTROL CoNtRoL;
	private Scanner iNpUt;
	private UiState StATe;

	
	public ReturnBookUI(rETURN_bOOK_cONTROL cOnTrOL) {
		this.CoNtRoL = cOnTrOL;
		iNpUt = new Scanner(System.in);
		StATe = UiState.INITIALISED;
		cOnTrOL.sEt_uI(this);
	}


	public void RuN() {		
		oUtPuT("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (StATe) {
			
			case INITIALISED:
				break;
				
			case READY:
				String BoOk_InPuT_StRiNg = iNpUt("Scan Book (<enter> completes): ");
				if (BoOk_InPuT_StRiNg.length() == 0) 
					CoNtRoL.scanningComplete();
				
				else {
					try {
						int Book_Id = Integer.valueOf(BoOk_InPuT_StRiNg).intValue();
						CoNtRoL.bookScanned(Book_Id);
					}
					catch (NumberFormatException e) {
						oUtPuT("Invalid bookId");
					}					
				}
				break;				
				
			case INSPECTING:
				String AnS = iNpUt("Is book damaged? (Y/N): ");
				boolean Is_DAmAgEd = false;
				if (AnS.toUpperCase().equals("Y")) 					
					Is_DAmAgEd = true;
				
				CoNtRoL.dischargeLoan(Is_DAmAgEd);
			
			case COMPLETED:
				oUtPuT("Return processing complete");
				return;
			
			default:
				oUtPuT("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + StATe);			
			}
		}
	}

	
	private String iNpUt(String PrOmPt) {
		System.out.print(PrOmPt);
		return iNpUt.nextLine();
	}	
		
		
	private void oUtPuT(Object ObJeCt) {
		System.out.println(ObJeCt);
	}
	
			
	public void DiSpLaY(Object object) {
		oUtPuT(object);
	}
	
	public void sEt_sTaTe(UiState state) {
		this.StATe = state;
	}

	
}
