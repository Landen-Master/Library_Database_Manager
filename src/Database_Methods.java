import java.sql.*;
import java.util.ArrayList;

public class Database_Methods {

	/**
     * @author Leon Madrid
     * 
     * Connects to the database if it exists, creates it if it does not, and returns the connection object.
     * 
     * @param databaseFileName the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {
    	/**
    	 * The "Connection String" or "Connection URL".
    	 * 
    	 * "jdbc:sqlite:" is the "subprotocol".
    	 * (If this were a SQL Server database it would be "jdbc:sqlserver:".)
    	 */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                System.out.println("The connection to the database was successful. Beginning program:");
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
            System.exit(1);
        }
        return conn;
    }
	
	/**
     * @author Leon Madrid
     * 
     * Queries the database and prints the results.
     * 
     * @param conn 			a connection object
     * @param sql 			a SQL statement that returns rows
     * @param ifnone		helper for message if nothing is found
     * 
     * This query is written with the Statement class, typically 
     * used for static SQL SELECT statements
     */
    public static void sqlGet(Connection conn, String sql, String ifNone){
        try {
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	ResultSet rs = stmt.executeQuery();
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();

        	//information to print in formatted table
        	ArrayList<String> columnNames = new ArrayList<String>(); //column names
        	ArrayList<String> data = new ArrayList<String>(); //data in columns
        	
        	
        	if (rs.isBeforeFirst()) {
	        	for (int i = 1; i <= columnCount; i++) {
	        		String value = rsmd.getColumnName(i);
	        		columnNames.add(value); //adding column names
	        	}
	        	while (rs.next()) {
	        		for (int i = 1; i <= columnCount; i++) {
	        			String columnValue = rs.getString(i);
	            		data.add(columnValue); //getting data
	        		}
	        	}
	        	
	        	//printing column names with equal amount of space between
	        	for (int i = 0; i < columnCount; i++) {
	        		System.out.printf("%-30s", columnNames.get(i));
	        	}
	        	
	        	//printing data in each row, left oriented
	        	for (int i = 0; i < data.size(); i++) {
	        		
	        		//moves to next row when needed
	        		if (i % columnCount == 0) {
	        			System.out.println();
	        		}
	        		
	        		System.out.printf("%-30s", data.get(i)); //printing data
	        	}
	        	
	        	System.out.println();
	        	
	        //if nothing was found in the query, prints message
        	} else {
        		System.out.println("Sorry, no " + ifNone + " found.");
        	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Inserts or updates database information
     * 
     * @param conn		connection to database
     * @param sql		sql statement to execute
     */
    public static void sqlSet(Connection conn, String sql) {
        try {
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Helper method for search and useful queries to create queries based on user input
     * 
     * @param searchCond			specifying which search condition to use
     * @param search				what to search for
     * @param year					what year to search for when applicable
     * 
     * @return an SQL statement for a specific query
     */
    public static String createQuery(int searchCond, String search, int year) {
    	
    	String statement = ""; //search statement being created
    	
    	//creating search statement based on search condition
    	switch (searchCond) {
    		
    		//if the statement condition is to search for an artist, creates artist search
	    	case(1):
	    		statement = "SELECT * FROM ARTIST WHERE First_Name || ' ' || Last_Name = \'" + search + "\' COLLATE NOCASE"
	    				+ " OR First_Name = \'" + search + "\' COLLATE NOCASE OR Band_Name = \'" + search 
	    				+ "\' COLLATE NOCASE;";
	    		break;
	    		
	    	//if the statement condition is to search tracks, creates track search
	    	case(2):
	    		statement = "SELECT * FROM TRACK WHERE NAME = \'" + search + "\' COLLATE NOCASE;";
	    		break;
	    	
	    	//if the statement condition is to search tracks from an artist before a specified year, creates search
	    	case(3):
	    		statement = "SELECT TRACK.Name, TRACK.Year FROM TRACK, ARTIST, ARTIST_SONG WHERE (First_Name"
						+ " || \' \' || Last_Name = \'" + search + "\' COLLATE NOCASE OR First_Name = \'" + search 
						+ "\' COLLATE NOCASE OR Band_Name = \'" + search + "\' COLLATE NOCASE) AND ARTIST.Creator_No ="
						+ " ARTIST_SONG.Creator_No AND ARTIST_SONG.Track_ID = TRACK.Track_ID AND YEAR < " + year 
						+ " GROUP BY Name;";
	    		break;
	    	
	    	//if the statement condition is to find how many albums are checked out by a specified patron, creates search
	    	case(4):
	    		statement = "SELECT PATRON.First_Name || \' \' || PATRON.Last_Name as Name, count(CHECK_OUT.ID_No)"
						+ " as \'Checked Out Albums\' FROM PATRON, CHECK_OUT WHERE CHECK_OUT.Card_Number = \'" + search
						+ "\' AND CHECK_OUT.Card_Number = PATRON.Card_Number AND CHECK_OUT.Media_Type = \'Album\';";
	    		break;
	    		
	    	//if the statement condition is to find what actor has the most checked out movies, creates saerch
	    	case(5):
	    		statement = "SELECT Name, MAX(holder) as \'Movies checked out\' FROM (SELECT ACTOR.First_Name"
						+ " || \' \' || ACTOR.Last_Name AS Name, Count(Actor.Creator_No) as holder FROM CHECK_OUT, CAST,"
						+ " ACTOR WHERE CHECK_OUT.Media_Type = \'Movie\' AND CHECK_OUT.ID_No = \'CAST\'.Movie_ID AND "
						+ "\'CAST'.Creator_No = ACTOR.Creator_No GROUP BY Actor.Creator_No);";
	    		break;
	    	
	    	//if the statement condition is to find the most popular artist, creates search
	    	case(6):
	    		statement = "SELECT Creator, MAX(find)/60 AS \'Hours listened to\'"
	    				+ " FROM (SELECT Creator, CASE WHEN seconds NOT NULL THEN seconds * checkedOut ELSE 1 END as find"
	    				+ " FROM (SELECT CASE WHEN ARTIST.Band_Name IS NOT NULL THEN ARTIST.Band_Name ELSE CASE WHEN"
	    				+ " ARTIST.Last_Name IS NOT NULL THEN ARTIST.First_Name || \' \' || ARTIST.Last_Name ELSE"
	    				+ " ARTIST.First_Name END END AS Creator, CASE WHEN ARTIST.Band_Name IS NULL THEN Count(ALBUM.Name)"
	    				+ " ELSE Count(ALBUM.Name)/Count(ARTIST.Band_Name) END AS checkedOut, ALBUM.Length AS seconds"
	    				+ " FROM ARTIST, CHECK_OUT, ARTIST_ALBUM, ALBUM WHERE CHECK_OUT.Media_Type = \'Album\' AND"
	    				+ " CHECK_OUT.ID_No = ARTIST_ALBUM.Album_ID AND ARTIST_ALBUM.Creator_No = ARTIST.Creator_No AND"
	    				+ " CHECK_OUT.ID_No = ALBUM.Album_ID GROUP BY Creator) GROUP BY Creator)";
	    		break;
	    		
	    	//if the statement condition is to find the patron with the most checked out movies, creates search
	    	case(7):
	    		statement = "SELECT Name, MAX(temp) as \'Checked Out Movies\' FROM (SELECT PATRON.First_Name ||"
						+ "\' \' || PATRON.Last_Name as Name, count(CHECK_OUT.ID_No) as temp FROM PATRON, CHECK_OUT"
						+ " WHERE CHECK_OUT.Card_Number = PATRON.Card_Number AND CHECK_OUT.Media_Type = \'Movie\'"
						+ " GROUP BY Name);";
	    		break;
	    			    		
	    	//if no expected case is selected, prints error and quits
	    	default:
	    		System.err.println("Unexpected search. Exiting.");
	    		System.exit(1);
	    		break;
    	
    	}
    
    	return statement; //returns created statement
    	
    }
    
    /**
     * Helper method for activating a specified order. Gets order information and deletes it from the database
     * 
     * @param conn			connection to the current order
     * @param rowid			order to obtain and delete
     * 
     * @return all information on the specified order
     */
    public static ArrayList<String> getOrderInfo(Connection conn, int rowid) {
    	
    	ArrayList<String> info = new ArrayList<String>(); //all info on current order
    	
        try {
        	PreparedStatement prep = conn.prepareStatement("SELECT * FROM ORDERS WHERE rowid = " + rowid + ";"); 
        	ResultSet rs = prep.executeQuery();
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	
        	//gets all information on the order
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
        			info.add(columnValue);
        		}
        	}
        	
        	//deletes order from table
        	PreparedStatement stmt = conn.prepareStatement("DELETE FROM ORDERS WHERE rowid = " + rowid + ";");
        	stmt.executeUpdate();
        	
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return info; //returns all information on the order
    }
    
    /**
     * Finds the number of rows in a table to create a creator id.
     * 
     * @param conn			connection to database
     * @param fromWhere		table to find rows of
     * 
     * @return number of rows in the table
     */
    public static int getRows(Connection conn, String fromWhere){
        try {
        	PreparedStatement stmt = conn.prepareStatement("SELECT count(rowid) FROM " + fromWhere + ";");
        	ResultSet rs = stmt.executeQuery();
        	return Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Checks if a specified query returns anything, and gets the first value
     * 
     * @param conn			connection to database
     * @param sql			sql query to execute
     * 
     * @return				0 if nothing exists, the first value if it exists
     */
	public static int checkExistsAndFirstValue(Connection conn, String sql) {
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
       	
			//if the query exists, gets first item
			if (rs.isBeforeFirst()) {
				return Integer.parseInt(rs.getString(1));
			
			//if not, returns 0
			} else {
				return 0;
			}
			
      } catch (SQLException e) {
          System.out.println(e.getMessage());
      }
		 return 0;
	}
    
}
