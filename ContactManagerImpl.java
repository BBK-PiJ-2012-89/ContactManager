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

import java.io.File;
import java.io.FileReader;
import java.io.*;

public class ContactManagerImpl {

	private int contactID = 0;
	private int meetingID = 0;
	private int pastMeetingID = 0;
	private int futureCounter = 0;
	private List<ContactImpl> contactList = new ArrayList<ContactImpl>();
	private List<MeetingImpl> meetingList = new ArrayList<MeetingImpl>();
	private List<PastMeetingImpl> pastMeetingList = new ArrayList<PastMeetingImpl>();
	private Calendar theCalendar = new GregorianCalendar();
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public ContactManagerImpl() {
		readIn();
	}

	public void makeMeeting() {

		System.out.println("Is this a future meeting (F), or has it already occured (P): ");
		boolean past = true;
		String pastOrFuture = "";
		while (!pastOrFuture.equals("p") || !pastOrFuture.equals("f")) {
			pastOrFuture = getInput();
			if (pastOrFuture.equals("p")) {
				past = true;
				break;
			} else if (pastOrFuture.equals("f")) {
				past = false;
				break;
			} else {
				System.out.println("That was not an option, please try again.");
			}
		}

		Set<ContactImpl> contacts = new HashSet<ContactImpl>();
		;
		String str = null;
		System.out
				.println("Begin by entering, one by one, the names contacts who will attend the meeting, when you are finished, type F: ");
		boolean finished = false;

		while (!finished) {
			boolean contactExists = false;
			str = getInput();
			try {
				for (int i = 0; i < contactList.size(); i++) {
					if (contactList.get(i).getName().equals(str)) {
						contacts.add(contactList.get(i));
						System.out.println("Thank you, " + str
								+ " has been added to the Meeting.");
						contactExists = true;
					}
				}
			} catch (NullPointerException ex) {
				ex.printStackTrace();
				System.out
						.println("You don't seem to have added any contacts yet!");
			}

			if (str.equals("f")) {
				finished = true;
			} else if (contactExists == false) {
				System.out
						.println("I'm sorry but the contact "
								+ str
								+ ", does not appear to be in our database, please check the name and try again or return to the home screen to enter a new contact.");
			}
		}
		System.out.println("Now please enter the date(DD/MM/YYYY): ");
		String dateString = getInput();
		Calendar calHolder = getDate(dateString);
		
		if (!past) {
			addFutureMeeting(contacts, calHolder, dateString);
		} else if (past) {
			System.out
					.println("Please now enter any additional notes you may have about this meeting.");
			String notes = getInput();
			addNewPastMeeting(contacts, calHolder, notes, dateString);
		}

	}

	public Calendar getDate(String date) {
		Calendar cal = Calendar.getInstance();
		Date newDate = null;
		Calendar calHolder = Calendar.getInstance();
		
		try {
			newDate = df.parse(date);
			cal.setTime(df.parse(date));
			calHolder.setTime(newDate);
		} catch (ParseException e) {
			System.out.println("That was not the correct date format, please try again");
		}
		
		return calHolder;
	}

	public List<ContactImpl> getContacts() {
		return contactList;
	}

	public int addFutureMeeting(Set<ContactImpl> contacts, Calendar date,
			String dateString) {

		try {
			if (date.getTime().before(theCalendar.getTime())) {

				throw new IllegalArgumentException(
						"The time entered was in the past!");
			}
		

			if (date.getTime().after(theCalendar.getTime())) {
				MeetingImpl newFuture = new FutureMeetingImpl(meetingID, date,
						contacts, dateString);
	
				meetingList.add(newFuture);
	
				addMeetingtoContacts(contacts, newFuture);
	
				meetingID++;
				System.out.println("");
				System.out.println("Your new meeting has been added, its ID is "
						+ newFuture.getID());
				System.out.println("");
	
				return newFuture.getID();
			}
		} catch (IllegalArgumentException ex) {
			System.out.println("The date entered was not in the correct format!");
		}
		return 0;
	}

	private void addMeetingtoContacts(Set<ContactImpl> contacts,
			MeetingImpl newMeeting) {

		ContactImpl[] theContacts = contacts.toArray(new ContactImpl[0]);

		for (int i = 0; i < theContacts.length; i++) {
			theContacts[i].addMeetings(newMeeting);
		}
	}

	public PastMeetingImpl getPastMeeting(int id) {

		PastMeetingImpl returner = null;

		for (int i = 0; i < pastMeetingList.size(); i++) {

			if (pastMeetingList.get(i).getID() == id) {
				returner = pastMeetingList.get(i);
			}
		}

		if (returner == null) {
			System.out.println("There is no such past meeting");
			return null;
		}

		System.out.println("");
		System.out.println("Meeting: " + returner.getID());
		System.out.println("Date: " + returner.getDateString());
		System.out.print("Attendees: ");
		printContacts(returner.getContacts());
		System.out.println("Notes: " + returner.getNotes());
		return returner;
	}

	public FutureMeetingImpl getFutureMeeting(int id) {
		
		try {
		FutureMeetingImpl returner = (FutureMeetingImpl) getMeeting(id);	
			if (returner.getDate().getTime().before(theCalendar.getTime())) {
				throw new IllegalArgumentException(
						"The meeting corresponding to the ID that you entered has not yet occurred.");
			} else {
				System.out.println("");
				System.out.println("Meeting: " + returner.getID());
				System.out.println("Date: " + (returner.getDateString()));
				System.out.print("Attendees: ");
				printContacts(returner.getContacts());
				return returner;
			}
		} catch (IllegalArgumentException ex) {
			System.out.println("That meeting has not yet occurred!");
		} catch (NullPointerException ex) {
			System.out.println("The ID entered does not exist!");
		} catch (ClassCastException ex){
			System.out.println("The meeting requested is in the past");
		}
		return null;	
	}

	private MeetingImpl getMeeting(int id) {

		MeetingImpl returner = null;

		for (int i = 0; i < meetingList.size(); i++) {

			if (meetingList.get(i).getID() == id) {
				returner = meetingList.get(i);
			}
		}
		return returner;
	}

	public List<FutureMeetingImpl> getFutureMeetingList(ContactImpl contact) {
		List<MeetingImpl> futureMeetings = null;

		try {
			futureMeetings = getMeetings(contact);
		} catch (IllegalArgumentException ex) {
			System.out.println("That contact does not exist!");
			ex.printStackTrace();
		}

		if (futureMeetings != null) {
			for (int i = 0; i < futureMeetings.size(); i++) {
				if (futureMeetings.get(i).getDate().getTime()
						.before(theCalendar.getTime())) {
					futureMeetings.remove(i);
				}
			}
		}

		if (futureMeetings == null) {
			return null;
		} else {
			List<FutureMeetingImpl> futureReturn = new ArrayList<FutureMeetingImpl>();

			for (int i = 0; i < futureMeetings.size(); i++) {
				FutureMeetingImpl holder = (FutureMeetingImpl) futureMeetings
						.get(i);
				futureReturn.add(holder);
			}
			printMeetings(futureMeetings);
			return futureReturn;

		}
	}

	public List<MeetingImpl> getMeetings(ContactImpl contact) {
		List<MeetingImpl> contactMeetings = null;

		if (contact.getMeetings() != null) {

			contactMeetings = contact.getMeetings();
		} else {
			System.out.println("This contact has no meetings!");
		}

		return contactMeetings;
	}

	public List<MeetingImpl> getFutureMeetingList(Calendar date) {
		List<MeetingImpl> futureReturn = new ArrayList<MeetingImpl>();

		for (int i = 0; i < meetingList.size(); i++) {
			if (meetingList.get(i).getDate().equals(date)) {

				futureReturn.add(meetingList.get(i));
			}
		}
		printMeetings(futureReturn);
		return futureReturn;
	}

	public List<PastMeetingImpl> getPastMeetingList(ContactImpl contact)
			throws IllegalArgumentException {

		List<MeetingImpl> pastMeetings = getMeetings(contact);
		List<PastMeetingImpl> pastReturn = new ArrayList<PastMeetingImpl>();
		;

		if (pastMeetings != null) {
			for (int i = 0; i < pastMeetings.size(); i++) {
				if (pastMeetings.get(i).getDate().getTime()
						.after(theCalendar.getTime())) {
					pastMeetings.remove(i);
				}
			}
		}

		if (pastMeetings == null) {
			System.out.println("This contact has no past meetings.");
			return null;
		} else {
			for (int i = 0; i < pastMeetings.size(); i++) {
				PastMeetingImpl holder = (PastMeetingImpl) pastMeetings.get(i);
				pastReturn.add(holder);
			}
		}
		System.out.println("");
		printPastMeetings(pastReturn);
		return pastReturn;
	}

	public void addNewPastMeeting(Set<ContactImpl> contacts, Calendar date,
			String notes, String dateString) throws IllegalArgumentException,
			NullPointerException {

		try {
			if (date.getTime().after(theCalendar.getTime())) {
				throw new IllegalArgumentException(
						"The time entered was in the future!");
			}
		} catch (IllegalArgumentException ex) {
			System.out
					.println("The date entered was in the past, please try again");
		}

		if (date.getTime().before(theCalendar.getTime())) {

			MeetingImpl newPast = new PastMeetingImpl(meetingID, date,
					contacts, notes, dateString);
			PastMeetingImpl thePast = (PastMeetingImpl) newPast;
			meetingList.add(newPast);
			pastMeetingList.add(thePast);

			addMeetingtoContacts(contacts, newPast);

			System.out.println("");
			System.out.println("Date: "
					+ meetingList.get(meetingID).getDateString());
			System.out.println("ID: " + meetingList.get(meetingID).getID());
			
			pastMeetingID++;
			meetingID++;
		}
	}

	public void addMeetingNotes(int id, String notes)
			throws NullPointerException {

		try {
			pastMeetingList.get(id - futureCounter).setNotes(notes);

		} catch (IllegalArgumentException ex) {
			System.out
					.println("I'm sorry but the Id entered does not seem to exist, please try again.");
		} catch (IllegalStateException ex) {
			System.out
					.println("I'm sorry but the ID entered corresponds to a future meeting, please try again.");
		} catch (IndexOutOfBoundsException ex) {
			System.out
					.println("Error, the ID entered does not correspond to a past meeting, please try again");
		}

	}

	public void addNewContact(String name, String notes)
			throws NullPointerException {

		ContactImpl newContact = null;

		newContact = new ContactImpl(contactID, name, notes);
		contactList.add(newContact);

		System.out.println("");
		System.out.println("The contact " + name + " with the ID "
				+ contactList.get(contactID).getId()
				+ " has now been added to contact manager.");

		contactID++;
	}

	public Set<ContactImpl> getContacts(int... contactIDs) {

		Set<ContactImpl> returnContacts = new HashSet<ContactImpl>();

		try {
			for (int id : contactIDs) {
				ContactImpl contact = contactList.get(id);
				if (contact == null) {
					throw new IllegalArgumentException("The contact id " + id
							+ "does not exist.");
				}

				returnContacts.add(contact);
			}
		} catch (IllegalArgumentException ex) {
			System.out.println("");
		} catch (IndexOutOfBoundsException ex) {
			System.out
					.println("One or more of the entered id's does not exist");
		}
		printContacts(returnContacts);

		return returnContacts;

	}

	public Set<ContactImpl> getContacts(String name)
			throws NullPointerException {
		Set<ContactImpl> returnContacts = new HashSet<ContactImpl>();

		for (int i = 0; i < contactList.size(); i++) {
			if (contactList.get(i).getName().toLowerCase()
					.contains(name.toLowerCase())) {
				returnContacts.add(contactList.get(i));
			}
		}

		System.out.println("Contacts whose names contain " + name + ": ");
		printContacts(returnContacts);

		return returnContacts;
	}

	public ContactImpl getContact(String name) {
		ContactImpl foundContact = null;
		for (int i = 0; i < contactList.size(); i++) {
			if (contactList.get(i).getName().equals(name)) {
				foundContact = contactList.get(i);
			}
		}
		return foundContact;
	}

	public void setID() {
		meetingID = meetingList.size();
		contactID = contactList.size();
	}

	public void printContacts(Set<ContactImpl> returnContacts) {

		Iterator<ContactImpl> it = returnContacts.iterator();
		while (it.hasNext()) {
			ContactImpl holder = it.next();
			System.out.print("ID: ");
			System.out.print(holder.getId());
			System.out.print(" Name: ");
			System.out.print(holder.getName());
			System.out.print(" Notes: ");
			System.out.print(holder.getNotes());
			System.out.println("");
		}
	}

	public void printMeetings(List<MeetingImpl> contactMeetings) {
		for (int i = 0; i < contactMeetings.size(); i++) {
			System.out.println("Meeting ID: " + contactMeetings.get(i).getID());
			System.out.println("Date: "
					+ contactMeetings.get(i).getDateString());
			System.out.print("Attendees: ");
			printContacts(contactMeetings.get(i).getContacts());

		}
	}

	public void printPastMeetings(List<PastMeetingImpl> contactMeetings) {

		for (int i = 0; i < contactMeetings.size(); i++) {
			System.out.println("Meeting ID: " + contactMeetings.get(i).getID());
			System.out.println("Date: "
					+ contactMeetings.get(i).getDateString());
			System.out.print("Attendees: ");
			printContacts(contactMeetings.get(i).getContacts());
			System.out.println("Meeting Notes: "
					+ contactMeetings.get(i).getNotes());

		}
	}

	public String getInput() {
		String str = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(System.in));
			str = bufferedReader.readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String returnStr = str.toLowerCase();
		return returnStr;
	}

	public void readIn() {
		String dataRow = "";
		String filename = "ContactManager.csv";
		File file = new File(filename);
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			while (!dataRow.equals("Meetings")|| dataRow!= null) {
				dataRow = in.readLine();

				if (dataRow.equals("Contacts"))
					dataRow = in.readLine();
				if (dataRow.equals("Meetings"))
					break;

				String[] dataArray = new String[3];
				dataArray = dataRow.split(",");

				ContactImpl contactFromMemory = new ContactImpl(
						Integer.parseInt(dataArray[0]), dataArray[1],
						dataArray[2]);
				contactList.add(contactFromMemory);
			}


			do {
				String[] contactArray = null;
				String[] dataArray = null;
				String[] holder = new String[2];
				dataRow = in.readLine();
				
				if(dataRow == null){
					break;
				} else if(dataRow.equals("Meetings")){
					dataRow = in.readLine();
				}
				holder = dataRow.split(";");

				String meetingPart = holder[0];
				String contactPart = holder[1];
				dataArray = meetingPart.split(",");
				contactArray = contactPart.split(",");
				Set<ContactImpl> savedContacts = new HashSet<ContactImpl>();
		
				for (int i = 0; i < contactList.size(); i++) {
					for(int j = 0; j < contactArray.length; j++){
						if (contactList.get(i).getId() == Integer.parseInt(contactArray[j])) {
							savedContacts.add(contactList.get(i));
						}
					}
				}
								
				MeetingImpl meetingFromMemory;
			
				if (getDate(dataArray[1]).getTime().before(
						theCalendar.getTime())) { //checks if past meeting and therefore whether or not notes must be added
					 meetingFromMemory = new PastMeetingImpl(
							Integer.parseInt(dataArray[0]),
							getDate(dataArray[1]), savedContacts, dataArray[2],
							dataArray[1]);
					meetingList.add(meetingFromMemory);
					PastMeetingImpl pastMeeting = (PastMeetingImpl) meetingFromMemory;
					pastMeetingList.add(pastMeeting);
				} else {
					 meetingFromMemory = new FutureMeetingImpl(
							Integer.parseInt(dataArray[0]),
							getDate(dataArray[1]), savedContacts, dataArray[1]);
					meetingList.add(meetingFromMemory);
				}
				
				Iterator<ContactImpl> it = meetingFromMemory.getContacts().iterator();
				while(it.hasNext()){ //The newly loaded meetings are added to the relevant contacts
					it.next().addMeetings(meetingFromMemory);
				}
				
			} while (dataRow != null);
			
			setID();
			in.close();
		} catch (FileNotFoundException ex) {
			System.out.println("File " + file + " does not exist.");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void flush() {

		String filename = "ContactManager.csv";
		File file = new File(filename);
		PrintWriter out = null;
		int pastMeetings = 0;

		if (file.exists()) {
			try {
				out = new PrintWriter(file);
				out.println("Contacts");
				for (int i = 0; i < contactList.size(); i++) {
					out.print(contactList.get(i).getId());
					out.print(",");
					out.print(contactList.get(i).getName());
					out.print(",");
					out.println(contactList.get(i).getNotes());
				}
				out.println("Meetings");
				for (int i = 0; i < meetingList.size(); i++) {
					if(meetingList.get(i).getDate().getTime().after(theCalendar.getTime()))
							{
						out.print(meetingList.get(i).getID());
						out.print(",");
						out.print(meetingList.get(i).getDateString());
						Iterator<ContactImpl> it = meetingList.get(i).getContacts()
								.iterator();
						if(it.hasNext()) out.print(";");
						while (it.hasNext()) {
							ContactImpl holder = it.next();
							out.print(holder.getId());
							out.print(",");
						}
					} else if(meetingList.get(i).getDate().getTime().before(theCalendar.getTime())) {
						out.print(meetingList.get(i).getID());
						out.print(",");
						out.print(meetingList.get(i).getDateString());
						out.print(",");
						out.print(pastMeetingList.get(pastMeetings).getNotes());
						pastMeetings++;
						Iterator<ContactImpl> it = meetingList.get(i).getContacts()
								.iterator();
						if(it.hasNext()) out.print(";");
						while (it.hasNext()) {
							ContactImpl holder = it.next();
							out.print(holder.getId());
							out.print(",");
						}
					}
					out.println("");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				out.close();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			flush();
		}
	}
}