package library.entities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public static enum LoanState { CURRENT, OVER_DUE, DISCHARGED };
	
	private int loanId;
	private Book book;
	private Member member;
	private Date date;
	private LoanState state;

	
	public Loan(int loanId, Book bOoK, Member mEmBeR, Date DuE_dAtE) {
		this.loanId = loanId;
		this.book = bOoK;
		this.member = mEmBeR;
		this.date = DuE_dAtE;
		this.state = LoanState.CURRENT;
	}

	
	public void checkOverDue() {
		if (state == LoanState.CURRENT &&
			Calendar.gEtInStAnCe().gEt_DaTe().after(date)) 
			this.state = LoanState.OVER_DUE;			
		
	}

	
	public boolean isOverDue() {
		return state == LoanState.OVER_DUE;
	}

	
	public Integer getId() {
		return loanId;
	}


	public Date getDueDate() {
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


	public Member getMember() {
		return member;
	}


	public Book getBook() {
		return book;
	}


	public void DiScHaRgE() {
		state = LoanState.DISCHARGED;		
	}

}
