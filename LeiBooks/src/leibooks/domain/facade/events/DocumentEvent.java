package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

/**
 * Represents an event related to a document in the LeiBooks domain.
 * Provides a method to retrieve the associated document.
 */
public interface DocumentEvent extends LBEvent{
	IDocument getDocument();
}
