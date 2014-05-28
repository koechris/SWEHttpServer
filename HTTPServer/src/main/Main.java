package main;

import de.httpServer.HTTPServer;

public class Main {

    public static void main(String[] args) throws Exception {
    


        String httpPath = System.getProperty("user.dir") + "/http/";
        // httpPath = "C:/Users/ko/Documents/NetBeansProjects/SoftwareProject/"
        // httpPath = "C:/Users/ko/Documents/NetBeansProjects/BeuthWebSpace/"
        int portNumber = 80;
        String keyURI = "serverRequest";
        	    
        new HTTPServer(keyURI, httpPath, portNumber);
    
    }
}
