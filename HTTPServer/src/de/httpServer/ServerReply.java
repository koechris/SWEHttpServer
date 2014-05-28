package de.httpServer;

import java.util.ArrayList;
import java.util.Collection;

public class ServerReply {
    private Collection<Object> replyCollection = new ArrayList<Object>();
    private String serverMessge;
    private boolean logedIn = false;

    public ServerReply(String serverMessge) {
	replyCollection.add(serverMessge);
	this.serverMessge = serverMessge;
    }

    public void extendMessage(String message) {
	serverMessge += "; " + message;
    }

    public void serLogedIn(boolean bool) {
	logedIn = bool;
    }
}
