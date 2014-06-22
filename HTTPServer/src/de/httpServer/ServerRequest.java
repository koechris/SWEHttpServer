package de.httpServer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import de.general.Log;
import de.httpServer.ClientClasses.ClientAddTreckPoint;
import de.httpServer.ClientClasses.ClientRoute;
import de.httpServer.ClientClasses.ClientUser;

public class ServerRequest extends Request {

    private String jsonString;
    private final HttpExchange httpExchange;
    private final String encoding = "UTF-8";
    private final Gson gsonOut = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private final Gson gsonIn = new Gson();
    Map<String, Object> replyJson = new HashMap<String, Object>();
    private User user;

    public ServerRequest(HttpExchange httpExchange, UserManager userManager)
	    throws Exception {

	mediaType = "application/json";

	this.httpExchange = httpExchange;
	
	final String SID = getSessionID(httpExchange);
	user = userManager.getUser(SID);
	// was a new user created?
	if (SID == null || ! SID.equals(user.sessionID)) {
	    Log.debugLog("Send new SID");
	    sendNewSID(user.sessionID);
	}

	final String uri = httpExchange.getRequestURI().toString();
	handleURICommand(uri, userManager);

	replyJson.put("logedIn", user.isLogedIn());

	// convert serverReply to sendable content
	String json = gsonOut.toJson(replyJson);
	content = json.getBytes();
    }
    
    private void handleURICommand ( String uri, UserManager userManager ) throws Exception {

	if (uri.indexOf("registration") != -1) {
	    readInputStream();
	    ClientUser clientUser = convertToClientUser(jsonString);
	    userManager.register(clientUser.name, clientUser.password, user);
	}
	else if (uri.indexOf("logIn") != -1) {
	    readInputStream();
	    ClientUser clientUser = convertToClientUser(jsonString);
	    userManager.logIn(clientUser.name, clientUser.password, user);
	}
	else if (uri.indexOf("logOut") != -1) {
	    userManager.logOut(user);
	}
	else if (uri.indexOf("addRoute") != -1) {
	    readInputStream();
	    ClientRoute clientRoute = convertToClientRoute(jsonString);
	    Route r = new Route(clientRoute.name);
	    user.getDBUser().addRoute(r);
	    userManager.upDate(user);
	}
	else if (uri.indexOf("getRoutes") != -1) {
	    replyJson.put("routes", user.getDBUser().getRoutes());
	}
	else if (uri.indexOf("getTrackPoints") != -1) {
	    readInputStream();
	    ClientRoute clientRoute = convertToClientRoute(jsonString);
	    replyJson.put("trackPoint", user.getDBUser().getTrackPoints(clientRoute.index));
	}
	else if (uri.indexOf("addTrackPoint") != -1) {
	    readInputStream();
	    ClientAddTreckPoint catp = gsonIn.fromJson(jsonString, ClientAddTreckPoint.class);
	    user.getDBUser().addTreckPoint( catp.routeIndex, catp.trackPoint );
	    userManager.upDate(user);
	} else if ( uri.indexOf("uploadFile") != -1 ) {
	    saveFile();
	}
	else {
	    replyJson.put("Unexpectrd URI", uri);
	}
	
    }
    
    private ClientUser convertToClientUser ( String jsonString ) {
	return ( gsonIn.fromJson(jsonString, ClientUser.class) );
    }
    private ClientRoute convertToClientRoute ( String jsonString ) {
	return ( gsonIn.fromJson(jsonString, ClientRoute.class) );
    }

    private String getSessionID(HttpExchange httpExchange) {
	final String SID;
	
	// if browser send a sessionID, find user with this sessionID
	final String key = "SessionID=";
	Map m = httpExchange.getRequestHeaders();
	// get a list with all cookies from browser
	LinkedList<String> headerList = (LinkedList<String>) m.get("Cookie");

	if (headerList != null) { // no browser cookie
	    for (String s : headerList) {
		if (s.indexOf(key) == 0) { // cookie has a SessionID=
		    SID = s.substring(key.length());
		    
		    return ( SID );
		}
	    }
	}
	
	// no session id or browser cookie
	return ( null );
    }

    private void sendNewSID(String SID) {
	// send the sessionID to browser
	httpExchange.getResponseHeaders().add("Set-Cookie", "SessionID=" + SID);
    }
    
    private void saveFile () throws IOException {
	
	Map test = httpExchange.getRequestHeaders();
	
	String subFolder;
	InputStream in = httpExchange.getRequestBody();
	byte[] byteArray;
	String fileNane;

	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte buf[] = new byte[4096];
	    for (int n = in.read(buf); n > 0; n = in.read(buf)) {
		out.write(buf, 0, n);
	    }
	    
	    byteArray = out.toByteArray();
	    fileNane = new String(out.toByteArray(), encoding);
	}
	finally {
	    in.close();
	}
	
	String fn = "filename=";
	int index = fileNane.indexOf ( fn ) + fn.length() + 1;
	fileNane = fileNane.substring( index );
	index = fileNane.indexOf ( '"' );
	fileNane = fileNane.substring( 0, index );
	
	Log.log(fileNane);
	
	int start = 0;
	int end = 0;
	for (int i = 0; i < byteArray.length - 4; i++) {
	    if ((byteArray[i] == 13 && byteArray[i+2] == 13) && (byteArray[i+1] == 10 && byteArray[i+3] == 10)) {
		start = i+4;
		break;
	    }
	}
	
	for (int i = byteArray.length - 2; i > 0; i--) {
	    if ( (byteArray[i-1] == 13 && byteArray[i] == 10)) {
		end = i-1;
		break;
	    }
	}
	
	byteArray = Arrays.copyOfRange(byteArray, start, end);
	
	if (fileNane.indexOf( ".jpg" ) != -1) {
	    subFolder = "images/";
	} else {
	    subFolder = "kmls/";
	}
	
	FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/http/" + subFolder + fileNane);
	fos.write(byteArray);
	fos.close();
	
    }

    private void readInputStream() throws IOException {

	InputStream in = httpExchange.getRequestBody();

	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte buf[] = new byte[4096];
	    for (int n = in.read(buf); n > 0; n = in.read(buf)) {
		out.write(buf, 0, n);
	    }
	    jsonString = new String(out.toByteArray(), encoding);
	}
	finally {
	    in.close();
	}
    }
}
