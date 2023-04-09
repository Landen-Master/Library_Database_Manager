import java.sql.*;
import java.util.*;

/**
 * This program allows for specific uses on a libray database. A user can search for information, add information,
 * order items, edit items, and generate useful reports.
 * 
 * @author Landen Master
 * @author Robert Lawrence
 * @author David Tochtermann
 *
 */
public class Database_Interface {

	static final String DB_Name = "Library.db";							//name of the database
	
	private static int menu = 0; 										//current menu database is on
	
	/**
	 * Prints options for main menu, and directs to other menus
	 * 
	 * @param scan		input reader
	 * @param conn		connection to database
	 */
	public static void printOptions(Scanner scan, Connection conn) {
		
		//printing menu messages based on the menu
		switch (menu) {
		
		//printing main menu
		case 0:
			System.out.println("\nMain Menu:");
			System.out.println("To search the library, enter 1\nTo add new records, enter 2\nTo order items, enter 3\n"
					+ "To edit reports, enter 4\nFor useful reports, enter 5");
			System.out.print("To quit, enter -1\n\nPlease enter your selection: ");
			break;
			
		//printing search menu
		case 1:
			System.out.println("\nSearch:");
			search(scan, conn); //going to search functions
			break;
		
		//add new records menu
		case 2:
			System.out.println("\nAdd New Records:");
			addRecords(scan, conn); //going to add records functions
			break;
			
		//order items menu
		case 3:
			System.out.println("Order Items:");
			orderItems(scan, conn); //going to order item functions
			break;
		
		//edit records menu
		case 4:
			System.out.println("Edit Records:");
			editRecords(scan, conn); //going to edit records functions
			break;
			
		//useful reports menu
		case 5:
			System.out.println("Useful reports:");
			usefulReports(scan, conn); //going to find useful reports
			break;
			
		//if user does not enter 1-5, asks for valid input
		default:
			System.out.print("That is not a valid option (1-5).\n");
			break;
		}
		
	}
	
	/**
	 * Allows users to search for track information or artist information
	 * 
	 * @param scan		user input
	 */
	public static void search(Scanner scan, Connection conn) {
		
		String search = ""; //artist or track being searched for
		
		//printing search menu info
		System.out.print("To search artists, enter 1\nTo search tracks, enter 2\nTo return to the main menu, enter 0: ");
		
		int choice = scan.nextInt(); //getting user choice
		scan.nextLine();  //clearing line
		
		//if the user does not want to quit, searches for artist or track
		while (choice != 0) {
			
			//repeating prompt until a valid response is given
			while (choice != 0 && choice != 1 && choice != 2) {
				System.out.println("Please enter a valid option.\n");
				System.out.print("To search artists, enter 1\nTo search tracks, enter 2\nTo return to the main menu, enter 0: ");
				
				choice = scan.nextInt(); //getting user choice
				scan.nextLine();  //clearing line

			}

			//if user entered 1, searches artists
			if (choice == 1) {
				
				//prompt for input
				System.out.print("Please type the name of an artist, or type nothing to quit: ");
				search = scan.nextLine(); //get user's search
				
				//if user enters nothing, quits artist search
				if (search.isBlank()) {
					System.out.println("Going back to search menu.");
					
				//otherwise, searches for artists
				} else {
					
					Database_Methods.sqlGet(conn, Database_Methods.createQuery(1, search, 0), "artist named " + search);
					
				}
				
			//if user entered 2, searches tracks
			} else if (choice == 2) {

				//prompt for input
				System.out.print("Please type the name of a track, or type nothing to quit: ");
				search = scan.nextLine(); //get user's search
				
				//if user enters nothing, quits track search
				if (search.isBlank()) {
					System.out.println("Going back to search menu.");
				
				//otherwise, searches tracks
				} else {
					
					Database_Methods.sqlGet(conn, Database_Methods.createQuery(2, search, 0), "track named " + search);

				}
				
			//if user entered 0, goes back to main menu
			} else {
				System.out.println("Back to main menu.");
			}
			
			//if user has not quit or does not want to go to the main menu, reprompts
			if (choice != 0) {
				System.out.print("\nTo search artists, enter 1\nTo search tracks, enter 2\nTo return to the main menu, enter 0: ");
				choice = scan.nextInt();
				scan.nextLine();
			}
		}
	}
	
    /**
	 * Allows users to add new tracks and artists
	 * 
	 * @param scan 		user input
	 * 
	 * @updates		databases artists and tracks
	 */
	public static void addRecords(Scanner scan, Connection conn) {
		
		//printing add records menu info
		System.out.print("To add an artist, enter 1\nTo add an audiobook, enter 2\nTo return to the main menu, enter 0: ");
		
		int choice = scan.nextInt(); //getting user choice
		scan.nextLine();  //clearing line

		//if the user does not want to quit, searches for artist or track
		while (choice != 0) {
			
			//repeating prompt until a valid response is given
			while (choice != 0 && choice != 1 && choice != 2) {
				System.out.println("Please enter a valid option.\n");
				System.out.print("To add an artist, enter 1\nTo add an audiobook, enter 2\nTo return to the main menu, enter 0: ");
				
				choice = scan.nextInt(); //getting user choice
				scan.nextLine();  //clearing line

			}

			//if user entered 1, adds an artist
			if (choice == 1) {
				
				//user inputting artist information
				
				System.out.print("Enter artist's first name: ");
				String first_name = scan.nextLine();
				System.out.print("Enter artist's last name (if artist only has one name, press enter): ");
				String last_name = scan.nextLine();
				System.out.print("Enter band name (if artist is a solo artist, press enter): ");
				String bandName = scan.nextLine();
				
				//if the artist has no last name, sets it to null
				if (last_name.isBlank()) {
					last_name = null;
				}
				//if the artist has no band name, sets it to null
				if (bandName.isBlank()) {
					bandName = null;
				}
				
				int id_no = Database_Methods.getRows(conn, "ARTIST") + 1; //creates artist_id
				
				//statement to insert artist
				String insertStatement = "INSERT INTO ARTIST VALUES (" + id_no + ", \'" + first_name + "\', \'" 
						+ last_name + "\', \'" + bandName + "\');";
				
				Database_Methods.sqlSet(conn, insertStatement); //inserts artist into database
				
				if (last_name == null) {
					System.out.println(first_name + " added to database!");
				} else {
					System.out.println(first_name + " " + last_name + " added to database!");
				}
			//if user entered 2, adds an audiobook
			} else if (choice == 2) {
				
				//user inputting audiobook information

				//if length and year are unknown, sets them to null
				Integer len =  null;
				Integer year = null;
				
				System.out.print("Enter audiobook name: ");
				String name = scan.nextLine();
				System.out.print("Enter number of physical copies: ");
				int numPhysical = scan.nextInt();
				scan.nextLine();
				System.out.print("Enter number of digital copies: ");
				int numDigital = scan.nextInt();
				scan.nextLine();
				System.out.print("Enter Audiobook genre (if unknown, press enter): ");
				String genre = scan.nextLine();
				System.out.print("Enter length of audiobook (in seconds)(if unknown, press enter): ");
				String length = scan.nextLine();
				System.out.print("Enter year audiobook was created (if unknown, press enter): ");
				String yearCreated = scan.nextLine();
				
				//if genre is unknown, sets as null
				if (genre.isEmpty()) {
					genre = null;
				}
				
				//if length is known, sets length
				if (!length.isEmpty()) {
					len = Integer.parseInt(length);
				}
				
				//if year is known, sets year
				if (!yearCreated.isEmpty()) {
					year = Integer.parseInt(yearCreated);
				}
				
				int id_no = Database_Methods.getRows(conn, "AUDIOBOOK") + 1; //creates audiobook_id
				
				//statement to insert audiobook
				String insertStatement = "INSERT INTO AUDIOBOOK VALUES (" + id_no + ", \'" + name + "\', " 
						+ numPhysical + ", " + numDigital + ", \'" + genre + "\', " + len + ", " + year + ");";
				
				Database_Methods.sqlSet(conn, insertStatement); //inserts audiobook into database

				System.out.println(name + " added to database!");
				
			//if user entered 0, goes back to main menu
			} else {
				System.out.println("Back to main menu.");
			}
			
			//if user has not quit or does not want to go to the main menu, reprompts
			if (choice != 0) {
				System.out.print("\nTo add an artist, enter 1\nTo add a audiobook, enter 2\nTo return to the main menu, enter 0: ");
				choice = scan.nextInt();
				scan.nextLine();
			}
		}
	}
	
	/**
	 * Allows user to order new movies to the library.
	 * 
	 * @param scan		user input
	 * @updates			movies in db
	 */
	public static void orderItems(Scanner scan, Connection conn) {
		
		//printing add order items info
		System.out.print("To order a movie, enter 1\nTo activate item recieved, enter 2\nTo return to the main menu, enter 0: ");
				
		int choice = scan.nextInt(); //getting user choice
		scan.nextLine();  //clearing line

		//if the user does not want to quit, searches for artist or track
		while (choice != 0) {
			
			//repeating prompt until a valid response is given
			while (choice != 0 && choice != 1 && choice != 2) {
				System.out.println("Please enter a valid option.\n");
				System.out.print("To order a movie, enter 1\nTo activate item recieved, enter 2\nTo return to the main menu, enter 0: ");
				
				choice = scan.nextInt(); //getting user choice
				scan.nextLine();  //clearing line

			}
			
			//if user chose to order a movie, allows user to input order information
			if (choice == 1) {
				
				//user inputting order information
				
				System.out.print("Enter movie name: ");
				String name = scan.nextLine();
				System.out.print("Enter library location number order is being sent to: ");
				int loc_no = scan.nextInt();
				scan.nextLine();
				System.out.print("Enter copies purchased: ");
				int copies = scan.nextInt();
				scan.nextLine();
				System.out.print("Enter price of purchase: ");
				double price = scan.nextDouble();
				scan.nextLine();
				System.out.print("Enter month of expected arrival: ");
				int month = scan.nextInt();
				scan.nextLine();
				System.out.print("Enter day of expected arrival: ");
				int day = scan.nextInt();
				scan.nextLine();
				System.out.print("Enter year of expected arrival: ");
				int year = scan.nextInt();
				scan.nextLine();
				
				String date = month + "-" + day + "-" + year; //formats data
				
				//statement to insert order
				String insertStatement = "INSERT INTO ORDERS VALUES (" + loc_no + ", \'" 
						+ name + "\', \'Movie\', " + copies + ", " + price + ", \'" + date + "\');";
				
				Database_Methods.sqlSet(conn, insertStatement); //inserts order
				
				//printing order information
				System.out.println("Ordered " + copies + " copies of " + name +" for $" + price);
				System.out.println("Expected date of arrival: " + month + "/" + day + "/" + year);
				
			//if user selects activate item received, allows them to do so
			} else if (choice == 2) {
				
				System.out.println("Activating item. Please select the ID of the order you would like to activate: ");
				
				//showing all orders
				Database_Methods.sqlGet(conn, "SELECT rowid as id, * FROM ORDERS WHERE Type = \'Movie\';", "");
				
				//getting order to activate
				System.out.print("Enter id: ");
				int id = scan.nextInt();
				scan.nextLine();
				
				//gets order information and removes it from database
				ArrayList<String> info = Database_Methods.getOrderInfo(conn, id); 
				
				//checks if the ordered movie is already in the database
				int exists = Database_Methods.checkExistsAndFirstValue(conn, 
						"SELECT 1 FROM MOVIE WHERE Name = \'" + info.get(1) + "\' COLLATE NOCASE;");
				
				//if the movie is in the database, updates its number of copies and location information
				if (exists != 0) {
					
					//updating number of copies
					Database_Methods.sqlSet(conn, "UPDATE MOVIE SET Num_Physical_Copies = Num_Physical_Copies + " + info.get(3)
						+ " WHERE Name = \'" + info.get(1) + "\';");
					
					//checks if the current location has the current movie
					exists = Database_Methods.checkExistsAndFirstValue(conn, 
							"SELECT ID_No FROM ITEM_STORAGE, MOVIE WHERE Location_No = " + info.get(0) 
							+ " AND ITEM_STORAGE.Type = \'Movie\' AND MOVIE.Movie_ID = ITEM_STORAGE.ID_No AND MOVIE.Name = \'"
							+ info.get(1) +"\';");
										
					//if it does, updates information
					if (exists != 0) {
						
						//updating information on movie at library location
						Database_Methods.sqlSet(conn, "UPDATE ITEM_STORAGE SET Num_Physical_Copies = Num_Copies + " + info.get(3)
							+ " WHERE Location_No = \'" + info.get(0) + " AND ITEM_STORAGE.Type = \'Movie\' AND ID_No = "
							+ exists + ";");
						
						System.out.println("Updated library copies of " + info.get(1));
						
					//if the current location does not have the current movie, adds it
					} else {
						
						
						//gets movie's id number
						int id_no = Database_Methods.checkExistsAndFirstValue(conn, "SELECT Movie_ID FROM MOVIE WHERE NAME = \'" 
								+ info.get(1) + "\';");
						
						//adds movie to library
						Database_Methods.sqlSet(conn, "INSERT INTO ITEM_STORAGE VALUES (" + info.get(0) + ", " + id_no
								+ ", " + info.get(3) + ", \'Movie\');");
						
						System.out.println("Movie " + info.get(1) + " now available at library location " + info.get(0));
					}
				
				//if the movie is not in the library's information, adds it
				} else {
					
					//getting the rest of the movie's information
					
					//if length and year are unknown, sets them to null
					Integer len =  null;
					Integer year = null;
					
					System.out.print("Enter movie rating: ");
					String rating = scan.nextLine();
					System.out.print("Enter genre (if unknown press enter) : ");
					String genre = scan.nextLine();
					System.out.print("Enter length of movie (in seconds)(if unknown, press enter): ");
					String length = scan.nextLine();
					System.out.print("Enter year movie was created (if unknown, press enter): ");
					String yearCreated = scan.nextLine();
					
					//if genre is unknown, sets to null
					if (genre.isEmpty()) {
						genre = null;
					}
					
					//if length is known, sets length
					if (!length.isEmpty()) {
						len = Integer.parseInt(length);
					}
					
					//if year is known, sets year
					if (!yearCreated.isEmpty()) {
						year = Integer.parseInt(yearCreated);
					}
					
					int id_no = Database_Methods.getRows(conn, "MOVIE") + 1; //creating new movie_id

					//adding to all movie information
					Database_Methods.sqlSet(conn, "INSERT INTO MOVIE VALUES (" + id_no + ", \'" + info.get(1) + "\', " 
							+ info.get(3) + ", 0, \'" + rating + "\', \'" + genre + "\', " + len + ", " + year + ");");
					
					//adding to specific library location inventory
					Database_Methods.sqlSet(conn, "INSERT INTO ITEM_STORAGE VALUES (" + info.get(0) + ", " + id_no
							+ ", " + info.get(3) + ", \'Movie\');");
				
					System.out.println("New movie " + info.get(1) + " exclusively available at library location " + info.get(0));					
				}
				
				
			//if user wants to go back to the main menu, allows them to
			} else {
				System.out.println("Back to main menu.");
			}
			
			//if user has not quit or does not want to go to the main menu, reprompts
			if (choice != 0) {
				System.out.print("\nTo order a movie, enter 1\nTo activate item recieved, enter 2\nTo return to the main menu, enter 0: ");
				choice = scan.nextInt();
				scan.nextLine();
			}
		}
	}
	
	/**
	 * Allows user to update artist
	 * 
	 * @param scan		user input
	 *
	 * @updates			artists in database
	 */
	public static void editRecords(Scanner scan, Connection conn) {
		
		//prompting for input
		System.out.print("Enter name of a solo artist to edit, or enter 0 to go back to the main menu: ");
		String editor = scan.nextLine(); //storing input
		
		int choice = 0; //item being edited
		
		//if something was entered, finds the artist to edit
		if (!editor.equals("0")) {
			
			int exists = Database_Methods.checkExistsAndFirstValue(conn, 
					"SELECT 1 FROM ARTIST WHERE First_Name || ' ' || Last_Name = \'" + editor 
	      			+ "\' COLLATE NOCASE" + " OR (First_Name = \'" + editor + "\' AND Last_Name IS NULL) COLLATE NOCASE;");
			
			//if the artist was found, allows for edits
			if (exists != 0) {
				
				//updated values
				String fname = null;
				String lname = null;
				String band = null;

				System.out.println("\nAll information on " + editor);
				
				//prints all information on current artist
				Database_Methods.sqlGet(conn, "SELECT * FROM ARTIST WHERE First_Name || ' ' || Last_Name = \'" + editor 
		      			+ "\' COLLATE NOCASE" + " OR (First_Name = \'" + editor + "\' AND Last_Name IS NULL)"
		      					+ " COLLATE NOCASE;", "");
				
				//prompting for input
				System.out.print("\nEnter 1 to edit first name\nEnter 2 to edit last name\n"
						+ "Enter 3 to edit band name\nEnter -1 to quit: ");
				choice = scan.nextInt(); //getting user choice
				scan.nextLine(); //clearing next line
				
				//while user does not want to exit, allows for editing
				while (choice != -1) {
					
					//repeating prompt until a valid response is given
					while (choice != -1 && choice != 1 && choice != 2 && choice != 3) {
						System.out.println("Please enter a valid option.\n");
						System.out.print("\nEnter 1 to edit first name\nEnter 2 to edit last name\n"
								+ "Enter 3 to edit band name\nEnter -1 to quit: ");
						
						choice = scan.nextInt(); //getting user choice
						scan.nextLine();  //clearing line

					}
					
					//if user wants to edit artists first name, allows for editing
					if (choice == 1) {
						System.out.print("Enter new first name for " + editor + ": ");
						fname = scan.nextLine();
						
					//if user wants to edit artists last name, allows for editing
					} else if (choice == 2) {
						System.out.print("Enter new last name for " + editor + ": ");
						lname = scan.nextLine();
						
					//if user wants to edit band name, allows for editing
					} else if (choice == 3) {
						System.out.print("Enter new band name for " + editor + ": ");
						band = scan.nextLine();
						
					}
					
					//reprompting user
					System.out.print("\nEnter 1 to edit first name\nEnter 2 to edit last name\n"
							+ "Enter 3 to edit band name\nEnter -1 to quit: ");
					
					choice = scan.nextInt(); //getting user choice
					scan.nextLine();  //clearing line
				}
				
				//if any updates have occured, updates the database
				if (fname != null || lname != null || band != null) {
						String updateStatement = "UPDATE ARTIST SET "; //update statement
					
					//if the first name has changed, adds to update statement
					if (fname != null) {
						updateStatement += "First_Name = \'" + fname + "\'";
						
						//if there is more to update, adds comma
						if (lname != null || band != null) {
							updateStatement += ", ";
						}
						
					//if the last name has changed, adds to update statement
					}
					if (lname != null) {
						updateStatement += "Last_Name = \'" + lname + "\'";
						
						//if there is more to update, adds comma
						if (band != null) {
							updateStatement += ", ";
						}
					}
					
					//if the band name has changed, adds to update statement
					if (band != null) {
						updateStatement += "Band_Name = \'" + band + "\'";
					}
					
					//finishes update statement for selecting current artist
					updateStatement += " WHERE First_Name || ' ' || Last_Name = \'" + editor 
			      			+ "\' COLLATE NOCASE" + " OR First_Name = \'" + editor + "\' COLLATE NOCASE;";
					
					Database_Methods.sqlSet(conn, updateStatement); //updates information
					
					//printing updated information
					System.out.println("\nUpdated artist!");
				
				}
				
			//if the artist was not found, prints message
			} else {
				System.out.println("Sorry, there is no artist named " + editor + " in our database");
			}
		}
		
	}
	
	/**
	 * Menu for useful reports
	 * 
	 * @param scan		user input
	 */
	public static void usefulReports(Scanner scan, Connection conn) {
		
		//prompts user input
		System.out.print("Enter 1 for Tracks by ARTIST released before YEAR\nEnter 2 for Number of albums checked out"
				+ " by a single patron\nEnter 3 for Most popular actor in the database\nEnter 4 for Most listened to"
				+ " artist in the database\nEnter 5 for Patron who has checked out the most videos\nEnter 0 to go back"
				+ " to the main menu: ");
		
		//getting user choice
		int choice = scan.nextInt();
		scan.nextLine();
		
		//while user does not want to go to the main menu, shows reports
		while (choice != 0) {
			
			//repeating prompt until a valid response is given
			while (choice != 0 && choice != 1 && choice != 2 && choice != 3 && choice != 4 && choice != 5) {
				System.out.println("Please enter a valid option.\n");
				System.out.print("Enter 1 for Tracks by ARTIST released before YEAR\nEnter 2 for Number of albums checked out"
						+ " by a single patron\nEnter 3 for Most popular actor in the database\nEnter 4 for Most listened to"
						+ " artist in the database\nEnter 5 for Patron who has checked out the most videos\nEnter 0 to go back"
						+ " to the main menu: ");
				
				choice = scan.nextInt(); //getting user choice
				scan.nextLine();  //clearing line

			}
			
			//showing the user the report they requested
			
			//showing user tracks by an artist before a certain year
			if (choice == 1) {
				
				//prompting user input for artist and year
				System.out.print("Enter name of artist to search: ");
				String artist = scan.nextLine();
				System.out.print("Enter year to search before: ");
				int year = scan.nextInt();
				scan.nextLine();
				
				//finding and executing specified query
				Database_Methods.sqlGet(conn, Database_Methods.createQuery(3, artist, year), "information on songs by " 
						+ artist + " before " + year);
				
			//showing user albums checked out by a patron
			} else if (choice == 2) {
				
				//prompting user input for card number
				System.out.print("Enter patron card number: ");
				String cardno = scan.nextLine();
				
				//finding and executing specified query
				Database_Methods.sqlGet(conn, Database_Methods.createQuery(4, cardno, 0), "albums checked out by card number " + cardno);
				
			//showing user the most popular actor
			} else if (choice == 3) {
				Database_Methods.sqlGet(conn, Database_Methods.createQuery(5, "", 0), "");
				
			//showing user the most listened to artist
			} else if (choice == 4) {
				Database_Methods.sqlGet(conn, Database_Methods.createQuery(6, "", 0), "");
			
			//showing user patron who checked out most movies
			} else if (choice == 5) {
				Database_Methods.sqlGet(conn, Database_Methods.createQuery(7, "", 0), "");

			//if user selected 0, returns them to the main menu
			} else {
				System.out.println("Back to main menu.");
			}
			
			//if user has not quit or does not want to go to the main menu, reprompts
			if (choice != 0) {
				System.out.print("\nEnter 1 for Tracks by ARTIST released before YEAR\nEnter 2 for Number of albums checked out"
						+ " by a single patron\nEnter 3 for Most popular actor in the database\nEnter 4 for Most listened to"
						+ " artist in the database\nEnter 5 for Patron who has checked out the most videos\nEnter 0 to go back"
						+ " to the main menu: ");
				choice = scan.nextInt();
				scan.nextLine();
			}
			
		}
	}
	
	public static void main(String args[]) {

		//0 for main menu
		//1 for search
		//2 for add new records
		//3 for order items
		//4 for edit reports
		//5 for useful reports
		
		//declaring variables
		Scanner inputReader = new Scanner(System.in); //file reader
		Connection conn = Database_Methods.initializeDB(DB_Name); //creating a connection to the database
		
		//printing welcome information
		System.out.println("Hello! Welcome to our library.");
		printOptions(inputReader, conn); //printing main menu
		
		menu = inputReader.nextInt(); //getting current menu user is in
		inputReader.nextLine(); //cleaning next line
		
		//while the user has not quit, continues prompting for input
		while (menu != -1) {
			
			//if user attempts to go to main menu in main menu, stops them from doing so
			while (menu == 0) {
				System.out.println("Already in main menu");
				printOptions(inputReader, conn); //printing main menu
				menu = inputReader.nextInt(); //getting current menu user is in
				inputReader.nextLine(); //clearing next line
			}
			
			printOptions(inputReader, conn); //prints new menu
			menu = 0; //returning to main menu
			printOptions(inputReader, conn); //printing main menu
			
			menu = inputReader.nextInt(); //getting current menu user is in
			inputReader.nextLine(); //clearing next line
			
			
		}
		
		System.out.println("\nGoodbye!"); //printing goodbye message
		
	}
	

}