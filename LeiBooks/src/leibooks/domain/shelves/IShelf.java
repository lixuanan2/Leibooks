package leibooks.domain.shelves;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.utils.Listener;

/**
 * Interface representing a shelf that can hold documents.
 * It provides methods to get the name of the shelf, add 
 * and remove documents from the shelf but these operations
 * are not necessarily supported by a shelf.
 * 
 * The shelf is also a listener for document events.
 */
public interface IShelf extends Listener<DocumentEvent>, Iterable<IDocument> {

	/**
	 * Retrieves the name of the shelf.
	 *
	 * @return the name of the shelf as a String.
	 * @ensures \result != null
	 */
	String getName();

	/**
	 * Adds a document to the shelf.
	 *
	 * @param document the document to be added
	 * @requires document != null
	 * @return true if the document was successfully added, false otherwise
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	boolean addDocument(IDocument document) throws OperationNotSupportedException;

	/**
	 * Removes the specified document from the shelf.
	 *
	 * @param document the document to be removed from the shelf
	 * @requires document != null
	 * @return true if the document was successfully removed, false otherwise
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	boolean removeDocument(IDocument document) throws OperationNotSupportedException;

}