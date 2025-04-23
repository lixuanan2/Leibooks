package leibooks.domain.shelves;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.utils.Listener;
import leibooks.domain.core.ILibrary;

public class SmartShelf extends AShelf implements Listener<DocumentEvent> {

    private final ILibrary library;
    private final Predicate<IDocument> criteria;

    public SmartShelf(String name, ILibrary library, Predicate<IDocument> criteria) {
        super(name);
        if (library == null || criteria == null) {
            throw new IllegalArgumentException("Library and criteria cannot be null");
        }
        this.library = library;
        this.criteria = criteria;

        for (IDocument doc : library) {
            if (criteria.test(doc)) {
                documents.add(doc);
            }
            doc.registerListener(this);
        }

        library.registerListener(this);
    }

    /**
     * Adds a document to the shelf.
     * NormalShelf supports manual addition, so it uses the default implementation from AShelf.
     *
     * @param document the document to add; must not be null
     * @return true if the document was added, false if already present
     * @throws OperationNotSupportedException if the operation is not supported
     */
    @Override
    public boolean addDocument(IDocument document) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("SmartShelf does not support manual addition of documents.");
    }

    /**
     * Removes the specified document from the shelf.
     * NormalShelf supports manual removal, so it uses the default implementation from AShelf.
     *
     * @param document the document to remove; must not be null
     * @return true if the document was successfully removed, false otherwise
     * @throws OperationNotSupportedException if the operation is not supported
     */
    @Override
    public boolean removeDocument(IDocument document) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("SmartShelf does not support manual removal of documents.");
    }

    /**
     * Processes a DocumentEvent.
     * If the event is a RemoveDocumentEvent, then the corresponding document is removed from this shelf.
     *
     * @param e the document event to process
     */

    @Override
    public Iterator<IDocument> iterator() {
        List<IDocument> filtered = new ArrayList<>();
        for (IDocument doc : library) {
            if (criteria.test(doc)) {
                filtered.add(doc);
            }
        }
        return filtered.iterator();
    }

    @Override
    public void processEvent(DocumentEvent evt) {
        // System.out.println("SmartShelf received event: " + evt); //teste
        IDocument doc = evt.getDocument();
        boolean shouldBe = criteria.test(doc);

        if (evt.getClass().getSimpleName().equals("AddDocumentEvent")) {
            if (shouldBe) {
                documents.add(doc);
            }
        } else {
            boolean inShelf = documents.contains(doc);
            if (!inShelf && shouldBe) {
                documents.add(doc);
            } else if (inShelf && !shouldBe) {
                documents.remove(doc);
            }
        }
    }

}
