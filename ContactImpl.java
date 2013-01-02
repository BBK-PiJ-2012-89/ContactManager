/**
* A contact is a person we are making business with or may do in the future
*
* Contacts have an ID (unique), a name (probably uniques, but maybe
* not), and notes that the user may want to save about them.
*/
public class ContactImpl implements Contact{

	private int iD;
	private String name;
	private String notes;

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

	public void addNotes(String note){

		notes += note;
		//When adding notes, I should probably include the date and time they were written

	}
}