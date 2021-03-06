import java.util.Set;
import java.util.Calendar;
/**
* A meeting that was held in the past.
*
*It includes your notes about what happened and what was agreed.
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

	private String notes;

	public PastMeetingImpl(int iD, Calendar date, Set<Contact> attendees, String notes){
		super(iD, date, attendees);
		this.notes = notes;
	}
	
	public String getNotes(){
		return notes;
	}
	
	public void setNotes(String notes){
		this.notes = notes;
	}
}