public class ContactStarter{

    public static void main(String[] args) {
        ContactStarter launcher = new ContactStarter();
        launcher.launch();
    }

    public void launch() {

    	welcome();

    }

	public void welcome(){

		int selection = 0;
		

		System.out.println("Welcome to the new and improved Contact Manager Pro!");
		System.out.println("What would you like to do?");
		System.out.println("");
		System.out.println("Enter 1 to add a new contact");
		System.out.println("Enter 2 to add a new meeting");
		System.out.println("Enter 3 to access a past meeting");
		System.out.println("Enter 4 to add meeting notes");
		System.out.println("Enter 5 to access a future meeting");
		System.out.println("");

		while(selection == 0){

			String str = System.console().readLine();


			try{
				selection = Integer.parseInt(str);

			} catch (NumberFormatException ex){
				System.out.println("That was not an integer!");
				ex.printStackTrace();
			}
			if(selection > 5 || selection < 1){
				System.out.println("That was not an option, please try again.");
				selection = 0;
			}
		}

		mainMenu(selection);
	}

	public void mainMenu(int a){
		ContactManagerImpl newContactManager = new ContactManagerImpl();

		if(a == 1){//Adds contact
			System.out.print("You have selected add new Contact, begin by entering the contacts name or type  EXIT to return to the main menu: ");
			String name = System.console().readLine();
			if(name.equals("EXIT")){
				welcome();
			}
			System.out.print("Now please enter any notes that you may have regarding the contact: ");
			String notes = System.console().readLine();

			newContactManager.addNewContact(name, notes);

			System.out.println("");
			System.out.println("Thank you, your new contact has now been added to the database, you will now be returned to the main menu.");
			System.out.println("");
			welcome();
		}


		if(a == 2){//Adds meeting
			newContactManager.makeMeeting();
			welcome();
		}


		if(a == 3){ //accesses Past Meeting
			System.out.println("You have selected access past meeting, begin by entering the contacts name or type EXIT to return to the main menu: ");
			String str = System.console().readLine();
			if(str.equals("EXIT")){
				welcome();
			}
		}


		if(a == 4){//adds meeting notes
			System.out.println("You have selected add meeting notes, begin by entering the contacts name or type EXIT to return to the main menu: ");
			String str = System.console().readLine();
			if(str.equals("EXIT")){
				welcome();
			}
		}


		if(a == 5){//accesses future meeting
			System.out.println("You have selected access Future Meeting, begin by entering the contacts name or type EXIT to return to the main menu: ");
			String str = System.console().readLine();
			if(str.equals("EXIT")){
				welcome();
			}
		}



	}

}