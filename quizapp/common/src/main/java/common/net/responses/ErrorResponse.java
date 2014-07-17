package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Response to notify a client about an error.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class ErrorResponse implements Serializable{
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -8220712855539656224L;
	/**
	 * Error message.
	 */
	private String message;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public ErrorResponse() {
	}
	
	/**
	 * Creates an instance.
	 * @param message the error message
	 */
	public ErrorResponse(String message) {
		this.message = message;
	}
	
	// === getters ===
	
	public String getMessage() {
		return message;
	}
}
