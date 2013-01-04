import java.util.List;
import java.util.ArrayList;

public class ContactImpl implements Contact{

	private int iD;
	private String name;
	private String notes;
	private List<MeetingImpl> meetings;

	public ContactImpl(int iD, String name, String notes){
		this.name = name;
		this.iD = iD;
		this.notes = notes;
	}

	public int getId(){
		return iD;
	}

	public String getName(){
		return name;
	}


	public String getNotes(){
		return notes;
	}

	public void addMeetings(MeetingImpl newMeetings){

		if(meetings == null){
			meetings = new ArrayList<MeetingImpl>();
		}

		meetings.add(newMeetings);
	}

	public List<MeetingImpl> getMeetings(){
		return meetings;
	}

	public void addNotes(String note){

		notes += note;
		//When adding notes, I should probably include the date and time they were written

	}
}