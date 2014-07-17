package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Response to notify a client that the opponent left.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class OpponentLeftResponse implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 6095344477898179721L;
}
