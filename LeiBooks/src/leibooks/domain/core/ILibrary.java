package leibooks.domain.core;

import java.util.List;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.utils.Subject;

/**
 * Interface representing a library of documents.
 * It extends the Iterable interface to allow iteration over the documents.
 * 
 * Extends {@link Subject} for {@link DocumentEvent}.
 * A library emits document events
 * 		AddADocumentEvent when a document is added
 * 		RemoveDocumentEvent when a document is removed
 */
public interface ILibrary extends Iterable<IDocument>, Subject<DocumentEvent>{

	/**
	 * The number of documents in the library
	 *
	 * @return  the number of documents in the library
	 * @ensures \result >= 0
	 */
	int getNumberOfDocuments();

	/**
	 * Adds a document to the library
	 * 
	 * @param document the document to add to the library
	 * @return  true if the document was added successfully, false otherwise
	 */
	boolean addDocument(IDocument document);
     
	/**
	 * Removes a given documents from the library
	 * 
	 * @param document the document to remove from the library
	 */
	void removeDocument(IDocument document);

	/**
	 * Updates the properties of a given document
	 * 
	 * @param document the document to update
	 * @param documentProperties the new properties of the document
	 */
	void updateDocument(IDocument document, DocumentProperties documentProperties);

	/**
	 * Returns the documents in the library that match the expression 
	 * 
	 * @param regex A regular expression to match the documents	
	 * @return a list with the documents that match the regular expression
	 */
	List<IDocument> getMatches(String regex);

}