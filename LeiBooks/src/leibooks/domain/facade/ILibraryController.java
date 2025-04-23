package leibooks.domain.facade;

import java.util.List;
import java.util.Optional;

import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.LBEvent;
import leibooks.utils.Listener;
import leibooks.utils.Subject;

/**
 * Interface representing a library controller that manages documents.
 * A library controller propagates to its observers the events concerning its documents.
 * 
 * LBEvent is used as parameter in Subject so that these controllers can be 
 * observed by objects interested also in LBEvents that are not DocumentEvent.
 * 
 */
public interface ILibraryController extends Subject<LBEvent>, Listener<DocumentEvent> {

	/**
	 * Retrieves all documents in the library.
	 *
	 * @return an Iterable of IDocument representing all documents.
	 */
	Iterable<IDocument> getDocuments();

	/**
	 * Imports a document into the library.
	 *
	 * @param title the title of the document.
	 * @param pathTofile the file path to the document.
	 * @return an Optional containing the imported IDocument if successful, or an empty Optional if not.
	 */
	Optional<IDocument> importDocument(String title, String pathTofile);

	/**
	 * Removes a document from the library.
	 *
	 * @param document the document to be removed.
	 */
	void removeDocument(IDocument document);

	/**
	 * Updates the properties of a document in the library.
	 *
	 * @param document the document to be updated.
	 * @param documentProperties the new properties of the document.
	 */
	void updateDocument(IDocument document, DocumentProperties documentProperties);

	/**
	 * Retrieves a list of documents that match the given regular expression.
	 *
	 * @param regex the regular expression to match documents against.
	 * @return a List of IDocument representing the matching documents.
	 */
	List<IDocument> getMatches(String regex);
}
