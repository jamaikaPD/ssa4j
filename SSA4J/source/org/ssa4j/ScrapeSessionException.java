package org.ssa4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This exception is thrown when one or more ScrapeSessionError session variables are
 * found when a ScrapeSession completes.  It is possible that one or more errors where 
 * found; in such cases use ScrapeSessionException.getErrors() to get a full list of all 
 * Errors.
 * 
 * @author rodneya
 *
 */
public class ScrapeSessionException extends ScrapeException {

	private static final long serialVersionUID = -6197189604833931627L;
	private Map<Integer, String> errors = new HashMap<Integer, String>();

	public ScrapeSessionException(int code, String rawText) {
		addError(code, rawText);
	}

	public void addError(int code, String rawText) {
		errors.put(code, rawText);
	}
	
	public Map<Integer, String> getErrors() {
		return errors;
	}

	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Number of Errors: %d", errors.size()));
		
		for (Entry<Integer, String> entry : errors.entrySet()) {
			sb.append(String.format("\n>> [%d]: %s", entry.getKey(), entry.getValue()));
		}
		return sb.toString();
	}
	
	public String toString() {
		return getMessage();
	}
}
