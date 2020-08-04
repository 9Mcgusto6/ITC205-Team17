package library.entities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public static enum lOaN_sTaTe { CURRENT, OVER_DUE, DISCHARGED };
	
	private int loanId;
	private Book book;
	private Member member;
	private Date date;
	private lOaN_sTaTe state;

	
	public Loan(int loanId, Book bOoK, Member mEmBeR, Date DuE_dAtE) {
		this.loanId = loanId;
		this.book = bOoK;
		this.member = mEmBeR;
		this.date = DuE_dAtE;
		this.state = lOaN_sTaTe.CURRENT;
	}

	
	public void cHeCk_OvEr_DuE() {
		if (state == lOaN_sTaTe.CURRENT &&
			Calendar.gEtInStAnCe().gEt_DaTe().after(date)) 
			this.state = lOaN_sTaTe.OVER_DUE;			
		
	}

	
	public boolean Is_OvEr_DuE() {
		return state == lOaN_sTaTe.OVER_DUE;
	}

	
	public Integer GeT_Id() {
		return loanId;
	}


	public Date GeT_DuE_DaTe() {
		return date;
	}
	
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(loanId).append("\n")
		  .append("  Borrower ").append(member.GeT_ID()).append(" : ")
		  .append(member.GeT_LaSt_NaMe()).append(", ").append(member.GeT_FiRsT_NaMe()).append("\n")
		  .append("  Book ").append(book.getId()).append(" : " )
		  .append(book.getTitle()).append("\n")
		  .append("  DueDate: ").append(sdf.format(date)).append("\n")
		  .append("  State: ").append(state);		
		return sb.toString();
	}


	public Member GeT_MeMbEr() {
		return member;
	}


	public Book GeT_BoOk() {
		return book;
	}


	public void DiScHaRgE() {
		state = lOaN_sTaTe.DISCHARGED;		
	}

}
