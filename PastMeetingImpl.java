import java.util.Set;
import java.util.Calendar;
/**
* A meeting that was held in the past.
*
*It includes your notes about what happened and what was agreed.
*/
public class PastMeetingImpl extends MeetingImpl implements Meeting {

	private String notes;

	public PastMeetingImpl(int iD, Calendar date, Set<ContactImpl> attendees){
		super(iD, date, attendees);
	}
	
	String getNotes(){
		return notes;
	}
}