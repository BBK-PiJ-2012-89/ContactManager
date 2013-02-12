import java.util.List;
import java.util.ArrayList;

public class ContactImpl implements Contact{

	private int iD;
	private String name;
	private String notes;
	private List<Meeting> meetings;

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

	public void addMeetings(Meeting newMeetings){

		if(meetings == null){
			meetings = new ArrayList<Meeting>();
		}

		meetings.add(newMeetings);
	}

	public List<Meeting> getMeetings(){
		return meetings;
	}

	public void addNotes(String note){

		notes = note;

	}
}