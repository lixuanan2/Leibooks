package leibooks.domain.facade;

import java.util.function.Predicate;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.events.LBEvent;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.utils.Listener;
import leibooks.utils.Subject;

/**
 * Interface representing a controller for managing shelves in the LeiBooks application.
 * A shelves controller propagates to its observers the events concerning its shelfs.
 * 
 * LBEvent is used as parameter in Subject so that these controllers can be 
 * observed by objects interested also in LBEvents that are not ShelfEvent.
 * 
 */
public interface IShelvesController extends Subject<LBEvent>, Listener<ShelfEvent> {

	/**
	 * Adds a normal shelf with the specified name.
	 *
	 * @param name the name of the shelf to be added
	 * @return true if the shelf was successfully added, false otherwise
	 */
	boolean addNormalShelf(String name);

	/**
	 * Adds a smart shelf with the specified name and criteria.
	 *
	 * @param name the name of the shelf to be added
	 * @param criteria the criteria to determine which documents belong to the shelf
	 * @return true if the shelf was successfully added, false otherwise
	 */
	boolean addSmartShelf(String name, Predicate<IDocument> criteria);

	/**
	 * Retrieves an iterable collection of shelf names.
	 *
	 * @return an iterable collection of shelf names
	 */
	Iterable<String> getShelves();

	/**
	 * Removes the shelf with the specified name.
	 *
	 * @param name the name of the shelf to be removed
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	void remove(String name) throws OperationNotSupportedException;

	/**
	 * Removes the specified document from the specified shelf.
	 *
	 * @param shelfName the name of the shelf from which the document will be removed
	 * @param document the document to be removed
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException;

	/**
	 * Adds the specified document to the specified shelf.
	 *
	 * @param shelfName the name of the shelf to which the document will be added
	 * @param document the document to be added
	 * @return true if the document was successfully added, false otherwise
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException;

	/**
	 * Retrieves an iterable collection of documents from the specified shelf.
	 *
	 * @param shelfName the name of the shelf from which to retrieve documents
	 * @return an iterable collection of documents from the specified shelf
	 */
	Iterable<IDocument> getDocuments(String shelfName);
}
