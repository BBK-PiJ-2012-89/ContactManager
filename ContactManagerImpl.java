import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;


public class ContactManagerImpl{

	private int contactID = 0;
	private int meetingID = 0;
	private List<ContactImpl> contactList = new ArrayList<ContactImpl>();;
	private List<MeetingImpl> meetingList = null;
	private Calendar theCalendar = new GregorianCalendar();

	public ContactManagerImpl(){
	}

	public int makeMeeting(){
		//this method needs a selector at the beginning 
		
		System.out.println("Is this a future meeting (FUTR), or has it already occured (PAST):");
		String pastOrFuture = getInput();
		boolean past;
		if(pastOrFuture.equals("PAST")){
			past = true;
		} else {
			past = false;
		}
		
		Set<ContactImpl> contacts = null;
		String str = null;
		boolean finished = false;
		while(!finished){
			boolean contactExists = false;
			System.out.println("Begin by entering, one by one, the names contacts who will attend the meeting, when you are finished, type DONE: ");
			str = getInput();
			try{
				for(int i = 0; i < contactList.size(); i++){
					if(contactList.get(i).getName().equals(str)){
						if(contacts == null){
							contacts = new HashSet<ContactImpl>();
						}
						contacts.add(contactList.get(i));
						System.out.println("Thank you, " + str + " has been added to the Meeting.");
						contactExists = true;
					}
				}
			} catch (NullPointerException ex){
				ex.printStackTrace();
				System.out.println("You don't seem to have added any contacts yet!");
			}
			if(str.equals("DONE")){
				finished = true;
			} else if(contactExists == false){
				System.out.println("I'm sorry but the contact "+ str + ", does not appear to be in our database, please check the name and try again or return to the home screen to enter a new contact.");
			}
		}

		Calendar calHolder = getDate();
		if(!past){
			addFutureMeeting(contacts, calHolder);
		} else if(past){
			System.out.println("Please now enter any additional notes you may have about this meeting.");
			String notes = getInput();
			addNewPastMeeting(contacts, calHolder, notes);
		}
		
		System.out.println("The meeting has been successfully entered, its meeting ID is " + meetingID);
		
		return meetingID;
	}
	
	public Calendar getDate(){
		System.out.print("Now please enter the date of the meeting (DD/MM/YYYY): ");
		String dateString = getInput();
		Calendar cal = Calendar.getInstance();
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
	    Date newDate;
	    Calendar calHolder = Calendar.getInstance();;
	    try {
	        newDate = df.parse(dateString);
	        cal.setTime(df.parse(dateString));
	    	calHolder.setTime(newDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return calHolder;
	}
	
	public List<ContactImpl> getContacts(){
		return contactList;
	}
	
	public int addFutureMeeting(Set<ContactImpl> contacts, Calendar date){

		if(date.getTime().before(theCalendar.getTime())){

			throw new IllegalArgumentException("The time entered was in the past!");
		}

		MeetingImpl newFuture = new FutureMeetingImpl(meetingID, date, contacts);

		addMeetingtoContacts(contacts, newFuture);

		meetingID++;

		System.out.println(newFuture.getID());
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
			List<FutureMeetingImpl> futureReturn = null;
			
			for(int i = 0; i < futureMeetings.size(); i ++){
				FutureMeetingImpl holder  = (FutureMeetingImpl) futureMeetings.get(i);
				futureReturn.add(holder);
			}
			return futureReturn;

		}
	}

	public List<MeetingImpl> getMeetings(ContactImpl contact){
		List<MeetingImpl> contactMeetings = null;

		if(contact.getMeetings() != null){
			
			contactMeetings = contact.getMeetings();
		} else {
			System.out.println("This contact has no meetings!");
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


	public void addNewPastMeeting(Set<ContactImpl> contacts, Calendar date, String text) throws IllegalArgumentException, NullPointerException{
		
		if(date.getTime().after(theCalendar.getTime())){
			throw new IllegalArgumentException("The time entered was in the future!");
		}

		MeetingImpl newPast = new PastMeetingImpl(meetingID, date, contacts);

		addMeetingtoContacts(contacts, newPast);

		meetingID++;

	}

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
		
		try{
			newContact = new ContactImpl(contactID, name, notes);
			contactList.add(newContact);
		} 
		catch (NullPointerException ex){

			System.out.println("Looks like you forgot to enter one of the values, please try again. ");
			ex.printStackTrace();
		}
		System.out.println("The contact " + name + " with the ID " + contactList.get(contactID).getId() + " has now been added to contact manager.");
		
		contactID++;
	}

	public Set<ContactImpl> getContacts(int... contactIDs){
		Set<ContactImpl> returnContacts = new HashSet<ContactImpl>();

		for(int id : contactIDs){
			ContactImpl contact = contactList.get(id);
			if(contact == null){
				throw new IllegalArgumentException("The contact id " + id + "does not exist.");
			}

			returnContacts.add(contact);
		}
		
		Iterator<ContactImpl> it = returnContacts.iterator();
		
		 while (it.hasNext()) {
			ContactImpl holder = it.next();
			System.out.print("ID: ");
            System.out.println(holder.getId());
            System.out.print("Name: ");
            System.out.println(holder.getName());
            System.out.print("Notes: ");
            System.out.println(holder.getNotes());
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
	public Set<ContactImpl> getContacts(String name){
		Set<ContactImpl> returnContacts = new HashSet<ContactImpl>();

		for(int i = 0; i < contactList.size(); i++){
			if(contactList.get(i).getName().toLowerCase().contains(name.toLowerCase())){
				returnContacts.add(contactList.get(i));
			}
		}
		
		return returnContacts;
	}
	
	
	
	public String getInput(){
		String str = "";
		try{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			str = bufferedReader.readLine();
		} catch (IOException ex){
			ex.printStackTrace();
		}
		return str;
	}

	/**
	* Save all data to disk
	*
	* This method must be executed when the program is
	* closed and when/if the user requests it.
	*/
	public void flush(){}
}