import java.util.Calendar;
import java.util.Set;

/**
* A class to represent meetings
*
* Meetings have unique IDs, scheduled date and a list of participating contacts
*/
public class MeetingImpl implements Meeting{

	private int iD;
	private Calendar date;
	private Set<Contact> attendees;

	MeetingImpl(int iD, Calendar date, Set<Contact> attendees){
		this.iD = iD;
		this.date = date;
		this.attendees = attendees;
		
	}

	public int getID(){
		return iD;
	}

	public Calendar getDate(){
		
		return date;
	}

	public Set<Contact> getContacts(){
		return attendees;
	}
	
	public int getAttendance(){
		if(attendees != null){
			int attendance = attendees.size();
			return attendance;
		} else{
			System.out.println("There are not attendees listed for this meeting!");
		}
		return 0;
	}
	
}