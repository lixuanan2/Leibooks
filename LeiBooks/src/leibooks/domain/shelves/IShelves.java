package leibooks.domain.shelves;

import java.util.function.Predicate;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.utils.Subject;

/**
 * Interface representing a collection of shelves, each of which can hold documents.
 * Provides methods to add and remove shelves (normal and smart), as well as to add 
 * and remove documents from shelves.
 * 
 * Extends {@link Subject} for {@link ShelfEvent}.
 * Shelves emits shelf events
 * 		AddShelfEvent when a shelf is added
 * 		RemoveShelfEvent when a shelf is removed
 * 		RemoveDocumentShelfEvent when a document is removed from a shelf 
 */
public interface IShelves extends Iterable<IShelf>, Subject<ShelfEvent> {

	/**
	 * Adds a normal shelf with the specified name.
	 *
	 * @param shelfName the name of the shelf to add
	 * @requires shelfName != null
	 * @return true if the shelf was added successfully, false otherwise
	 */
	boolean addNormalShelf(String shelfName);

	/**
	 * Adds a smart shelf with the specified name and criteria.
	 * A smart shelf automatically categorizes documents based on the given criteria.
	 *
	 * @param shelfName the name of the shelf to add
	 * @param criteria the criteria used to categorize documents
	 * @requires shelfName != null
	 * @requires criteria != null
	 * @return true if the shelf was added successfully, false otherwise
	 */
	boolean addSmartShelf(String shelfName, Predicate<IDocument> criteria);

	/**
	 * Removes the shelf with the specified name.
	 *
	 * @param shelfName the name of the shelf to remove
	 * @requires shelfName != null
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	void removeShelf(String shelfName) throws OperationNotSupportedException;

	/**
	 * Removes the specified document from the specified shelf.
	 *
	 * @param shelfName the name of the shelf from which to remove the document
	 * @param document the document to remove
	 * @requires shelfName != null
	 * @requires document != null
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException;

	/**
	 * Adds the specified document to the specified shelf.
	 *
	 * @param shelfName the name of the shelf to which the document should be added
	 * @param document the document to add
	 * @requires shelfName != null
	 * @requires document != null
	 * @return true if the document was added successfully, false otherwise
	 * @throws OperationNotSupportedException if the operation is not supported
	 */
	boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException;

	/**
	 * Returns an iterable collection of documents in the specified shelf.
	 *
	 * @param shelfName the name of the shelf from which to retrieve documents
	 * @requires shelfName != null
	 * @return an iterable collection of documents in the specified shelf
	 */
	Iterable<IDocument> getDocuments(String shelfName);

}