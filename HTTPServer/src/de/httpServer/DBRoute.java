package de.httpServer;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class DBRoute {

    
    /**
     * The Id for the Persistence System, DO NOT TOUCH THIS!
     */
    @Id
    @GeneratedValue
    private long id;
    private String name;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
