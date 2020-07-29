package library.entities;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar {
	
	private static Calendar self;
	private static java.util.Calendar calendar;
	
	
	private Calendar() {
		calendar = java.util.Calendar.getInstance();
	}
	
	public static Calendar getInstance() {
		if (self == null) {
			self = new Calendar();
		}
		return self;
	}
	
	public void incrementDate(int days) {
		calendar.add(java.util.Calendar.DATE, days);		
	}
	
	public synchronized void setDate(Date date) {
		try {
			calendar.setTime(date);
	        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calendar.set(java.util.Calendar.MINUTE, 0);  
	        calendar.set(java.util.Calendar.SECOND, 0);  
	        calendar.set(java.util.Calendar.MILLISECOND, 0);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	public synchronized Date getDate() {
		try {
	        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calendar.set(java.util.Calendar.MINUTE, 0);  
	        calendar.set(java.util.Calendar.SECOND, 0);  
	        calendar.set(java.util.Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	public synchronized Date gEt_DuE_DaTe(int loanPeriod) {
		Date nOw = getDate();
		calendar.add(java.util.Calendar.DATE, loanPeriod);
		Date dUeDaTe = calendar.getTime();
		calendar.setTime(nOw);
		return dUeDaTe;
	}
	
	public synchronized long GeT_DaYs_DiFfErEnCe(Date targetDate) {
		
		long Diff_Millis = getDate().getTime() - targetDate.getTime();
	    long Diff_Days = TimeUnit.DAYS.convert(Diff_Millis, TimeUnit.MILLISECONDS);
	    return Diff_Days;
	}

}
