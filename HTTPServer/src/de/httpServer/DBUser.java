package de.httpServer;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.general.Log;

@Entity
@Table(name = "UserAccount")
// Because User is reserve in DerbyDB
public class DBUser {

    /**
     * The Id for the Persistence System, DO NOT TOUCH THIS!
     */
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String password;
    private ArrayList<Route> routes;

    /**
     * @param name
     *            The Name of the user
     * @param eMail
     *            The Email of the user
     * @param pwHash
     *            The hashed password of the user
     */
    public DBUser(String name, String password) {
	this.name = name;
	this.password = password;
	routes = new ArrayList<Route>();
    }

    /**
     * Default Consturctor for the persistence system
     */
    public DBUser() {
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the pwHash
     */
    public String getPassword() {
	return password;
    }

    /**
     * @param pwHash
     *            the pwHash to set
     */
    public void setPassword(String password) {
	this.password = password;
    }

    /**
     * @return the id
     */
    public long getId() {
	return id;
    }

    @Override
    public String toString() {
	String s = "name:" + name;
	s += ", password:" + password;
	return s;
    }

    public void addRoute(Route r) {
	routes.add(r);
    }

    public ArrayList<Route> getRoutes() {
	return routes;
    }

    public void removeRoute(String routeName) {
	for (Route r : routes) {
	    if (r.getName().equals(routeName)) {
		routes.remove(r);
		return;
	    }
	}
    }

    public void addTreckPoint(int routeIndex, TrackPoint trackPoint) {
	routes.get(routeIndex).addTrackPoint(trackPoint);
    }

    public Object getTrackPoints(int index) {
	return routes.get(index).getTrackPoints();
    }
}
