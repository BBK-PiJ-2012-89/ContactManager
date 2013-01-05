import java.util.Set;
import java.util.Calendar;
/**
* A meeting to be held in the future
*/
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
	
	public FutureMeetingImpl(int iD, Calendar date, Set<ContactImpl> attendees){
		
		super(iD, date, attendees);

	}
}