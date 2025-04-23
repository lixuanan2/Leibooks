package leibooks.domain.core;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;


import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;
import leibooks.utils.Listener;
import leibooks.utils.AbsSubject;



public class Library extends AbsSubject<DocumentEvent>
					 implements  ILibrary {

	private final List<IDocument> documents = new ArrayList<>();

	/**
	 * Returns an iterator over the documents in this library.
	 *
	 * @return an iterator of IDocument
	 */
	@Override
	public Iterator<IDocument> iterator() {
		return documents.iterator();
	}

	/**
	 * Emits a given event to the listeners
	 *
	 * @param e event that occurred
	 */
	@Override
	public void emitEvent(DocumentEvent e) {
		super.emitEvent(e);
	}

	/**
	 * Registers a new listener
	 *
	 * @param obs listener to be added
	 */
	@Override
	public void registerListener(Listener<DocumentEvent> obs) {
		super.registerListener(obs);
	}

	/**
	 * Removes the registry of the given listener
	 *
	 * @param obs listener to be removed
	 */
	@Override
	public void unregisterListener(Listener<DocumentEvent> obs) {
		super.unregisterListener(obs);
	}

	/**
	 * Returns the total number of documents in this library.
	 *
	 * @return the number of documents
	 */
	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	/**
	 * Adds a document to the library, if it's not already present.
	 *
	 * @param document the document to add
	 * @return true if the document was successfully added, false otherwise
	 */
	@Override
	public boolean addDocument(IDocument document) {
		if (document == null) return false;
		if (!documents.contains(document)) {
			documents.add(document);
			emitEvent(new AddDocumentEvent(document));
			return true;
		}
		return false;
	}

	/**
	 * Removes the specified document from the library, if it exists.
	 *
	 * @param document the document to remove
	 */
	@Override
	public void removeDocument(IDocument document) {
		if (documents.remove(document)) {
			emitEvent(new RemoveDocumentEvent(document));
		}
	}

	/**
	 * Updates a document's properties by applying the values
	 * provided in the given DocumentProperties object.
	 *
	 * @param document the document to update
	 * @param documentProperties the new properties
	 */
	@Override
	public void updateDocument(IDocument document, DocumentProperties documentProperties) {
		if (document == null || documentProperties == null) {
			return;
		}
		if (!documents.contains(document)) {
			return;
		}
		String newTitle = documentProperties.title();
		String newAuthor = documentProperties.author();
		if (newTitle != null) {
			document.setTitle(newTitle);
		}
		if (newAuthor != null) {
			document.setAuthor(newAuthor);
		}
	}

	/**
	 * Returns the documents in the library that match the given regular expression.
	 *
	 * @param regex the regular expression to match
	 * @return a list of matching documents
	 */
	@Override
	public List<IDocument> getMatches(String regex) {
		if (regex == null) {
			return Collections.emptyList();
		}
		List<IDocument> matches = new ArrayList<>();
		for (IDocument doc : documents) {
			if (doc.matches(regex)) {
				matches.add(doc);
			}
		}
		return matches;
	}

	// opcionais, para os testes
	@Override
	public String toString() {
		return "Library with " + getNumberOfDocuments() + " documents";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Library)) return false;
		Library that = (Library) o;
		return Objects.equals(documents, that.documents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(documents);
	}
}
