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
        catch (Exception exception) {
            throw new RuntimeException(exception);
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
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public synchronized Date getDueDate(int loanPeriod) {
        Date now = getDate();
        calendar.add(java.util.Calendar.DATE, loanPeriod);
        Date dueDate = calendar.getTime();
        calendar.setTime(now);
        return dueDate;
    }
    
    public synchronized long getDaysDifference(Date targetDate) {
        long differenceMilliseconds = getDate().getTime() - targetDate.getTime();
        long differenceDays = TimeUnit.DAYS.convert(differenceMilliseconds, TimeUnit.MILLISECONDS);
        return differenceDays;
    }
}
