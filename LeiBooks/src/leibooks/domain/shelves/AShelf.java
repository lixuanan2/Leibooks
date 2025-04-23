package leibooks.domain.shelves;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;


/**
 * Abstract class AShelf provides a skeleton implementation of the IShelf interface.
 * It is based on a shelf name and stores the documents in a set (to avoid duplicates).
 *
 * It implements:
 * - IShelf: by providing getName(), addDocument(), removeDocument(), and iterator() methods.
 * - Listener<DocumentEvent>: with a default no-op implementation.
 */
public abstract class AShelf implements IShelf {

    // Shelf name, unique identifier
    protected final String name;
    // Use a LinkedHashSet to maintain insertion order and ensure no duplicates.
    protected final Set<IDocument> documents = new LinkedHashSet<>();

    /**
     * Constructs an AShelf with the specified name.
     *
     * @param name the unique name of the shelf; must not be null.
     * @ensures getName() != null
     */
    public AShelf(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Shelf name cannot be null");
        }
        this.name = name;
    }

    /**
     * Retrieves the name of the shelf.
     *
     * @return the shelf name.
     * @ensures \result != null
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Adds a document to the shelf.
     * The default implementation adds the document to an internal set.
     *
     * @param document the document to add; must not be null.
     * @return true if the document was added successfully (i.e. it was not already present), false otherwise.
     * @throws OperationNotSupportedException if the shelf does not support manual addition.
     */
    @Override
    public boolean addDocument(IDocument document) throws OperationNotSupportedException {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        return documents.add(document);
    }

    /**
     * Removes the specified document from the shelf.
     * The default implementation removes the document from the internal set.
     *
     * @param document the document to remove; must not be null.
     * @return true if the document was successfully removed, false otherwise.
     * @throws OperationNotSupportedException if the shelf does not support removal.
     */
    @Override
    public boolean removeDocument(IDocument document) throws OperationNotSupportedException {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        return documents.remove(document);
    }

    /**
     * Returns an iterator over the documents contained in the shelf.
     *
     * @return an iterator of IDocument.
     */
    @Override
    public Iterator<IDocument> iterator() {
        return documents.iterator();
    }

    /**
     * Processes a DocumentEvent.
     * The default implementation does nothing; subclasses may override to react to document changes.
     *
     * @param e the document event to process.
     */
    @Override
    public void processEvent(DocumentEvent e) {
        IDocument doc = e.getDocument();
        if (e instanceof RemoveDocumentEvent) {
            try {
                boolean removed = removeDocument(doc);
                if (removed) {
                	System.out.println("Removed from shelf " + name + ": " + doc);

                }
            } catch (OperationNotSupportedException ex) {
                // default
            }
        } else {
            try {
                boolean added = addDocument(doc);
                if (added) {
                	System.out.println("Added to shelf " + name + ": " + doc);
                }
            } catch (OperationNotSupportedException ex) {
                // default
            }
        }
    }
}
