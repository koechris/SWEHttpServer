package de.httpServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;

public class DocumentRequest extends Request {

    public DocumentRequest(final HttpExchange httpExchange,
	    final String httpPath) {

	String uri = httpExchange.getRequestURI().toString();

	if (uri.equals("/")) {
	    uri = "index.html";
	}

	String tmpType = uri.substring(uri.lastIndexOf('.') + 1);

	switch (tmpType) {
	case "html":
	    mediaType = "text/html";
	    break;
	case "css":
	    mediaType = "text/css";
	    break;
	case "jpg":
	    mediaType = "image/jpeg";
	    break;
	case "gif":
	    mediaType = "image/gif";
	    break;
	case "png":
	    mediaType = "image/png";
	    break;
	case "ico":
	    mediaType = "image/x-icon";
	    break;
	case "js":
	    mediaType = "application/javascript";
	    break;

	default:
	    System.out.println(tmpType + " Media Type not defined: " + uri);
	    mediaType = "text/text";
	    break;
	}
	
	uri = httpPath + uri;

	byte[] byeBuffer;

	try {
	    byeBuffer = Files.readAllBytes(Paths.get(uri));
	}
	catch (IOException e) {
	    e.printStackTrace();
	    byeBuffer = new byte[(byte) 0];
	}

	content = byeBuffer;
    }
}