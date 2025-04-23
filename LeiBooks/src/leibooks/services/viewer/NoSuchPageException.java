/**
 * Exception thrown when a requested page does not exist in the viewer service.
 * This exception is used to indicate that an attempt was made to access a page
 * that is not available or does not exist.
 * 
 */
package leibooks.services.viewer;

public class NoSuchPageException extends Exception {
	private static final long serialVersionUID = 1L;
}
