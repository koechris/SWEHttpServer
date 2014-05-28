package de.httpServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.general.Log;
import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

public class UserManager {

    private List<User> userList = new ArrayList<User>();
    public final CRUDIF db;
    private final SessionManager sessionManager;

    public UserManager() {

	sessionManager = new SessionManager();
	// Start Database
	PersistenceFacade.startDBSystem();
	// Get Database Controller
	db = PersistenceFacade.getDBController();

    }

    public User getUser(String sessionID) {

	if (sessionID != null) {
	    for (User u : userList) {
		if (u.sessionID.equals(sessionID)) {
		    return (u);
		}
	    }
	}

	return (createUser());
    }
    
    public String register (String userName, String password, User user) throws NoSuchAlgorithmException {

	// check if username exist
	List<DBUser> users = db.readAll(DBUser.class);
	for (DBUser dbUser : users) {
	    if (dbUser.getName().equals(userName)) {
		Log.debugLog("Registration failes. Username exist: " + userName);
		return ("Username bereits Vergeben");
	    }
	}
	
	// create new user in database
	String passwordHash = convertToMD5Hash(password);
	DBUser dbUser = new DBUser(userName, passwordHash);
	db.insert(dbUser);
	user.setDBUser(dbUser);
	Log.debugLog("User registed: " + userName);
	return ("Registrierung erfolgreich");
    }

    public String logIn(String userName, String password, User user) throws NoSuchAlgorithmException {
	
	// get all users from db
	List<DBUser> users = db.readAll(DBUser.class);
	// find the user with this name
	for (DBUser dbUser : users) {
	    if (dbUser.getName().equals(userName)) {
		// test the password
		final String passwordHash = convertToMD5Hash(password);
		if (dbUser.getPassword().equals(passwordHash)) {
		    user.logIn(dbUser);
		    Log.debugLog("Log in erfolgreich. User: " + user.toString());
		    return ("Log in erfolgreich");
		} else {
		    break;
		}
	    }
	}
	
	Log.debugLog("Log in fehlgeschlagen. User: " + user.toString());
	return ("User Name oder Passwort falsch");
    }
    
    public void logOut (User user) {
	userList.remove(user);
	user.logOut();
	Log.debugLog("User loged out");
    }

    private User createUser() {
	final String sessionID = sessionManager.getSessionID();

	final User u = new User(sessionID);
	userList.add(u);

	Log.debugLog("create User: " + u.toString());

	return (u);
    }

    private String convertToMD5Hash(String string)
	    throws NoSuchAlgorithmException {

	// generate md5 String
	byte[] pwBytes = (string).getBytes();

	MessageDigest md = MessageDigest.getInstance("MD5");

	byte[] byteHash = md.digest(pwBytes);

	String md5String = "";

	for (byte b : byteHash) {
	    md5String += b;
	}

	return (md5String);
    }
}
