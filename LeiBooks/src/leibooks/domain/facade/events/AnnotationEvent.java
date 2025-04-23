package leibooks.domain.facade.events;

/**
 * Represents an event related to annotations in a document.
 * This interface provides  methods specific to annotation events.
 */
public interface AnnotationEvent extends DocumentEvent{

	int getPageNum();

	int getAnnotationNum();

	boolean hasAnnotations();

}