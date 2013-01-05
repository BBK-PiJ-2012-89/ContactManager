import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.GregorianCalendar;


public class ContactManagerImpl{

	private int contactID = 0;
	private int meetingID = 0;
	private Set<ContactImpl> tempContactIDs = null;
	private List<ContactImpl> contactList = null;
	private List<MeetingImpl> meetingList = null;
	private Calendar theCalendar = new GregorianCalendar();

	public ContactManagerImpl(){
	}

	public void makeMeeting(){
		Set<ContactImpl> contacts;
		int contactNumber = 0;
		String str = null;
		while(str != "F"){
			boolean contactExists = false;
			System.out.println("You have selected add new meeting, begin by entering, one by one, the names contacts who will attend the meeting, when you are finished, type F: ");
			str = System.console().readLine();
			for(int i = 0; i < contactList.size(); i++){
				if(contactList.get(i).getName().equals(str)){
					contacts.add(contactList.get(i));
					System.out.println("Thank you, " + str + " has been added to the Meeting.");
					contactNumber++;
					contactExists = true;
				}
			}
			if(contactExists == false){
				System.out.println("I'm sorry but the contact "+ str + ", does not appear to be in our database, please check the name and try again or return to the home screen to enter a new contact.")
			}
			//here there should be info to find whether the contact exists and if so add it to the set "contacts".
		}

		System.out.print("Now please enter the year of the meeting: ");
		String dateString = System.console().readLine();
		int year = Integer.parseInt(dateString);

		System.out.print("Now please enter the month of the meeting: ");
		String dateString = System.console().readLine();
		int month = Integer.parseInt(dateString);

		System.out.print("Now please enter the day of the meeting: ");
		String dateString = System.console().readLine();
		int day = Integer.parseInt(dateString);

		Calendar date = Calendar.set(year, month, day);
		addFutureMeeting(contacts, date);
	}


	public int addFutureMeeting(Set<ContactImpl> contacts, Calendar date){

		if(date.getTime().before(theCalendar.getTime())){

			throw new IllegalArgumentException("The time entered was in the past!");
		}

		MeetingImpl newFuture = new FutureMeetingImpl(meetingID, date, contacts);

		addMeetingtoContacts(contacts, newFuture);

		meetingID++;

		return newFuture.getID();
	}

	private void addMeetingtoContacts(Set<ContactImpl> contacts, MeetingImpl newMeeting){

		ContactImpl[] theContacts = contacts.toArray(new ContactImpl[0]);

		for(int i = 0; i < theContacts.length; i++){
			theContacts[i].addMeetings(newMeeting);
		}
	}


	public PastMeetingImpl getPastMeeting(int id){

		PastMeetingImpl  returner = (PastMeetingImpl) getMeeting(id);

		if(returner.getDate().getTime().after(theCalendar.getTime())){
			throw new IllegalArgumentException("The meeting corresponding to the ID that you entered has not yet occurred.");
		}
		return returner;
	}


	public FutureMeetingImpl getFutureMeeting(int id){

		FutureMeetingImpl  returner = (FutureMeetingImpl) getMeeting(id);

		if(returner.getDate().getTime().before(theCalendar.getTime())){
			throw new IllegalArgumentException("The meeting corresponding to the ID that you entered has not yet occurred.");
		}
		return returner;
	}

	private MeetingImpl getMeeting(int id){

		MeetingImpl isMeetingNull = null;

		for(int i = 0; i < meetingList.size(); i++){

			if(meetingList.get(i).getID() == id){

				if((meetingList.get(i).getDate().getTime()).before(theCalendar.getTime())){

					throw new IllegalArgumentException();

				} else {

					MeetingImpl returner = meetingList.get(i);

					return returner;
				}
			}
		}

		return isMeetingNull;
	}

	public List<FutureMeetingImpl> getFutureMeetingList(ContactImpl contact){
		List<MeetingImpl> futureMeetings = null;

		try{
			futureMeetings = getMeetings(contact);
		} 
		catch (IllegalArgumentException ex){
			System.out.println("That contact does not exist!");
			ex.printStackTrace();
		}

		List<FutureMeetingImpl> futureReturn = null;


		if(futureMeetings != null){
			for(int i = 0; i < futureMeetings.size(); i++){
				if(futureMeetings.get(i).getDate().getTime().before(theCalendar.getTime())){
					futureMeetings.remove(i);
				}
			}
		}

		if(futureMeetings.isEmpty()){
			return null;
		} else {

			for(int i = 0; i < futureMeetings.size(); i ++){
				FutureMeetingImpl holder  = (FutureMeetingImpl) futureMeetings.get(i);
				futureReturn.add(holder);
			}
			return futureReturn;

		}
	}

	public List<MeetingImpl> getMeetings(ContactImpl contact){
		List<MeetingImpl> contactMeetings = null;

		if(contactMeetings != null){

			contactMeetings = contact.getMeetings();
		}

		return contactMeetings;
	}

	/**
	* Returns the list of future meetings scheduled for, or that took 
	* place on the specified date
	*
	* If there are none, the list will be returned empty. Otherwise,
	* the list whill be chronologicall sorted and will not contain 
	* any duplicates.
	*
	* @param date the date
	* @return the list of meetings
	*/
	//public List<Meeting> getFutureMeetingList(Calendar date){}

	/**
	* Returns the list of past meetings in which this contact has participated
	*
	* If there are none, the list will be returned empty. Otherwise,
	* the list whill be chronologicall sorted and will not contain 
	* any duplicates.
	*
	* @param contact one of the user's contacts
	* @return the list of future meeitngs sceduled with the contact (maybe empty)
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<PastMeetingImpl> getPastMeetingList(ContactImpl contact){
		List<MeetingImpl> pastMeetings = getMeetings(contact);
		List<PastMeetingImpl> pastReturn = null;

		if(pastMeetings != null){
			for(int i = 0; i < pastMeetings.size(); i++){
				if(pastMeetings.get(i).getDate().getTime().after(theCalendar.getTime())){
					pastMeetings.remove(i);
				}
			}
		}

		if(pastMeetings.isEmpty()){
			return null;
		} else {
			for(int i = 0; i < pastMeetings.size(); i ++){
				PastMeetingImpl holder  = (PastMeetingImpl) pastMeetings.get(i);
				pastReturn.add(holder);
			}
		}

		return pastReturn;
	}

	/**
	* Create a new record for a meeting that took place in the past
	*
	* @param contacts a list of participants
	* @param date the date on which the meeting took place
	* @param text messages to be added about the meeting
	* @throws IllegalArgumentException if the list of contacts is empty, or any of
	* 		the contacts don't exist
	* @throws NullPointerException if any of the arguments are null
	*/
	//public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text){}

	/**
	*Add notes to a meeting.
	*
	* This method is used when a future meeting takes place, and is
	* then converted to a past meeting (with notes).
	*
	* It can be also used to add ntoes to a past meeting at a later date.
	*
	* @param id the ID of the meeting
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the meeting does not exist
	* @throws IllegalStateException if the meeting is set for a date in the future
	* @throws NullPointerException if the notes are null
	*/
	//public void addMeetingNotes(int id, String text){}

	/**
	* Create a new contact with the specified name and notes.
	*
	* @param name the name of the contact
	* @param notes notes to be added about the contact
	* @throws NullPointerException if the name or the notes are null
	*/
	public void addNewContact(String name, String notes){

		ContactImpl newContact = null;

		if(contactList == null){
			contactList = new ArrayList<ContactImpl>();
		}

		try{

			newContact = new ContactImpl(contactID, name, notes);

			contactList.add(newContact);
		} 
		catch (NullPointerException ex){

			System.out.println("Looks like you forgot to enter one of the values, please try again. ");
			ex.printStackTrace();
		}
		contactID++;
	}

	/**
	* Returns a list containing the contacts that correspond to the IDs.
	*
	* @param ids an arbitrary number of contact IDs
	* @return a list containing the contacts that correspond to the IDs.
	* @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
	*/
	public Set<ContactImpl> getContacts(int... ids){
		Set<ContactImpl> returnContacts = new HashSet<ContactImpl>();

		for(int id : ids){
			ContactImpl contact = contactList.get(id);
			if(contact == null){
				throw new IllegalArgumentException("The contact id " + id + "does not exist.");
			}

			returnContacts.add(contact);
		}

		return returnContacts;

	}

	/**
	* Returns a list with the contacts whose name contains that string.
	*
	* @param name the string to search for
	* @return a list with the contacts whose name contains that string.
	* @throws NullPointerException if the parameter is null
	*/
	//public Set<Contact> getContacts(String name){}

	/**
	* Save all data to disk
	*
	* This method must be executed when the program is
	* closed and when/if the user requests it.
	*/
	public void flush(){}
}