package server;

import java.net.Socket;

import server.persistence.IPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.net.requests.*;

public abstract class AbstractServerAdapter {
	private final ObjectMapper mapper = new ObjectMapper();
	protected final IPersistence persistence;
	protected final ServerDirectory serverDir;
	
	protected AbstractServerAdapter(IPersistence persistence, ServerDirectory serverDir) {
		this.persistence = persistence;
		this.serverDir = serverDir;
	}
	
	private <T> T getObj(String json, Class<T> classOf) {
		try {
			return mapper.readValue(json, classOf);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void process(Socket client, String json) {
		AuthRequest obj = getObj(json, AuthRequest.class);
		if(obj != null) {
			process(client, obj);
		}
		
		LogoutRequest obj2 = getObj(json, LogoutRequest.class);
		if(obj2 != null) {
			process(client, obj2);
		}
		
		// TODO: rest
	}
	
	abstract protected void process(Socket client, AuthRequest obj);
	abstract protected void process(Socket client, LogoutRequest obj);
}
