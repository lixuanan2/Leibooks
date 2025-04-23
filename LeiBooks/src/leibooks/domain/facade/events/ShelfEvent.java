package leibooks.domain.facade.events;

/**
 * Represents an event related to a shelf in the LeiBooks domain.
 * This interface provides a method to retrieve the name of the 
 * shelf associated with the event.
 */
public interface ShelfEvent extends LBEvent{
	String getShelfName();
}