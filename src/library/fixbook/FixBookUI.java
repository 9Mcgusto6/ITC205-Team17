package library.fixbook;
import java.util.Scanner;

public class FixBookUI {
    
    public static enum UIState {INITIALISED, READY, FIXING, COMPLETED};
    private UIState control;
    private Scanner input;
    private UIState StAtE;
    
    public FixBookUI(UIState control) {
        this.control = control;
        input = new Scanner(System.in);
        StAtE = UIState.INITIALISED;
        control.SeT_Ui(this);
    }
    
    public void SeT_StAtE(UIState state) {
        this.StAtE = state;
    }
    
    public void RuN() {
        OuTpUt("Fix Book Use Case UI\n");
        while (true) {
            switch (StAtE) {
                case READY:
                    String BoOk_EnTrY_StRiNg = iNpUt("Scan Book (<enter> completes): ");
                    if (BoOk_EnTrY_StRiNg.length() == 0)
                        control.SCannING_COMplete();
                    else {
                        try {
                            int BoOk_Id = Integer.valueOf(BoOk_EnTrY_StRiNg).intValue();
                            control.BoOk_ScAnNeD(BoOk_Id);
                        }
                        catch (NumberFormatException e) {
                            OuTpUt("Invalid bookId");
                        }
                    }
                    break;
                case FIXING:
                    String AnS = iNpUt("Fix Book? (Y/N) : ");
                    boolean FiX = false;
                    if (AnS.toUpperCase().equals("Y"))
                        FiX = true;
                    control.FiX_BoOk(FiX);
                    break;
                case COMPLETED:
                    OuTpUt("Fixing process complete");
                    return;
                default:
                    OuTpUt("Unhandled state");
                    throw new RuntimeException("FixBookUI : unhandled state :" + StAtE);
            }
        }
    }
    
    private String iNpUt(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }
    
    private void OuTpUt(Object object) {
        System.out.println(object);
    }
    
    public void dIsPlAy(Object object) {
        OuTpUt(object);
    }
}