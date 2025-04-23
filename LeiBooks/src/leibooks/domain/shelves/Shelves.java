package leibooks.domain.shelves;

import java.util.Iterator;
import java.util.function.Predicate;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.core.ILibrary;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.utils.Listener;
import leibooks.utils.AbsSubject;
import leibooks.domain.facade.events.AddShelfEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentShelfEvent;
import leibooks.domain.facade.events.RemoveShelfEvent;

public class Shelves extends AbsSubject<ShelfEvent>
					 implements IShelves {

	private final ILibrary library;
	private final Map<String, IShelf> shelfMap = new HashMap<>();

	private static final String SHELF_NAME_CANNOT_BE_NULL = "Shelf name cannot be null";


	/**
	 * Constructs a Shelves instance associated with the given library.
	 *
	 * @param library the library to which these shelves are linked; must not be null.
	 * @throws IllegalArgumentException if library is null.
	 */
	public Shelves(ILibrary library) {
		if (library == null) {
			throw new IllegalArgumentException("Library cannot be null");
		}
		this.library = library;

		addUnremovableSmartShelf("Recent", d ->
						(d.getMimeType().equals("application/pdf") || d.getMimeType().equals("text/plain"))
				 && d.getLastModifiedDate().equals(LocalDate.now())
		);

		addUnremovableSmartShelf("Bookmarked", IDocument::isBookmarked);


		for (IDocument doc : library) {
			for (IShelf shelf : shelfMap.values()) {
				if (shelf instanceof Listener) {
					doc.registerListener((Listener<DocumentEvent>) shelf);
				}
			}
		}
	}

	/**
	 * Returns an iterator over all shelves.
	 *
	 * @return an iterator over IShelf instances.
	 */
	@Override
	public Iterator<IShelf> iterator() {
		return shelfMap.values().iterator();
	}

	@Override
	public void emitEvent(ShelfEvent e) {
		super.emitEvent(e);
	}
	@Override
	public void registerListener(Listener<ShelfEvent> obs) {
		super.registerListener(obs);
	}
	@Override
	public void unregisterListener(Listener<ShelfEvent> obs) {
		super.unregisterListener(obs);
	}


	/**
	 * Adds a normal shelf with the specified name.
	 *
	 * @param shelfName the name of the shelf to add; must not be null.
	 * @return true if the shelf was added successfully, false if a shelf with the same name already exists.
	 */
	@Override
	public boolean addNormalShelf(String shelfName) {
		if (shelfName == null) {
			throw new IllegalArgumentException(SHELF_NAME_CANNOT_BE_NULL);
		}
		if (shelfMap.containsKey(shelfName)) {
			return false;
		}
		NormalShelf newShelf = new NormalShelf(shelfName);
		shelfMap.put(shelfName, newShelf);
		emitEvent(new AddShelfEvent(shelfName));
		System.out.println("-------->> Shelves: AddShelfEvent [shelfName=" + shelfName + "]<<--------");

		for (IDocument doc : library) {
			doc.registerListener(newShelf);
		}

		library.registerListener(newShelf);

		return true;
	}

	/**
	 * Adds a smart shelf with the specified name and criteria.
	 *
	 * @param shelfName the name of the shelf to add; must not be null.
	 * @param criteria the predicate used to filter documents; must not be null.
	 * @return true if the shelf was added successfully, false if a shelf with the same name already exists.
	 */
	@Override
	public boolean addSmartShelf(String shelfName, Predicate<IDocument> criteria) {
		if (shelfName == null || criteria == null) {
			throw new IllegalArgumentException("Shelf name and criteria cannot be null");
		}
		if (shelfMap.containsKey(shelfName)) {
			return false;
		}

		Predicate<IDocument> filteredCriteria = d ->
				(d.getMimeType().equals("application/pdf") || d.getMimeType().equals("text/plain"))
						&& criteria.test(d);

		IShelf newShelf = new SmartShelf(shelfName, library, filteredCriteria);
		shelfMap.put(shelfName, newShelf);
		emitEvent(new AddShelfEvent(shelfName));
		System.out.println("-------->> Shelves: AddShelfEvent [shelfName=" + shelfName + "]<<--------");

		for (IDocument doc : library) {
			doc.registerListener((Listener<DocumentEvent>) newShelf);
		}
		library.registerListener(newShelf);
		return true;
	}

	/**
	 * Removes the shelf with the specified name.
	 *
	 * @param shelfName the name of the shelf to remove; must not be null.
	 * @throws OperationNotSupportedException if the operation is not supported (e.g., shelf is unremovable).
	 */
	@Override
	public void removeShelf(String shelfName) throws OperationNotSupportedException {
		if (shelfName == null) {
			throw new IllegalArgumentException(SHELF_NAME_CANNOT_BE_NULL);
		}
		IShelf shelf = shelfMap.get(shelfName);
		if (shelf == null) {
			return;
		}
		if (shelf instanceof UnremovableSmartShelf) {
			throw new OperationNotSupportedException("This shelf cannot be removed.");
		}
		shelfMap.remove(shelfName);
		// RemoveShelfEvent
		emitEvent(new RemoveShelfEvent(shelfName));
	}

	/**
	 * Auxiliary function that adds an unremovable smart shelf to the shelves collection.
	 *
	 * This method creates a new {@link UnremovableSmartShelf} with the specified shelf name and
	 * criteria, and adds it to the internal shelf map if no shelf with the same name already exists.
	 * It then emits an {@link AddShelfEvent} with the shelf name to notify registered listeners.
	 *
	 * @param shelfName the unique name for the shelf; must not be null.
	 * @param criteria a {@link java.util.function.Predicate} used to filter documents that should belong to this shelf; must not be null.
	 * @return {@code true} if the shelf was added successfully, or {@code false} if a shelf with the same name already exists.
	 * @throws IllegalArgumentException if {@code shelfName} or {@code criteria} is null.
	 */
	public boolean addUnremovableSmartShelf(String shelfName, Predicate<IDocument> criteria) {
		if (shelfName == null || criteria == null) {
			throw new IllegalArgumentException("Shelf name and criteria cannot be null");
		}
		if (shelfMap.containsKey(shelfName)) {
			return false;
		}
		IShelf newShelf = new UnremovableSmartShelf(shelfName, library, criteria);
		shelfMap.put(shelfName, newShelf);
		// AddShelfEvent
		emitEvent(new AddShelfEvent(shelfName));
		for (IDocument doc : library) {
			doc.registerListener(newShelf);
		}
		library.registerListener(newShelf);
		return true;
	}


	/**
	 * Removes the specified document from the shelf identified by shelfName.
	 *
	 * @param shelfName the name of the shelf; must not be null.
	 * @param document the document to remove; must not be null.
	 * @throws OperationNotSupportedException if the operation is not supported by the shelf.
	 */
	@Override
	public void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
		if (shelfName == null || document == null) {
			throw new IllegalArgumentException("Shelf name and document cannot be null");
		}
		IShelf shelf = shelfMap.get(shelfName);
		if (shelf == null) {
			return;
		}
		boolean removed = shelf.removeDocument(document);
		if (removed) {
			emitEvent(new RemoveDocumentShelfEvent(shelfName, document));
		}
	}

	/**
	 * Adds the specified document to the shelf identified by shelfName.
	 *
	 * @param shelfName the name of the shelf; must not be null.
	 * @param document the document to add; must not be null.
	 * @return true if the document was added successfully, false otherwise.
	 * @throws OperationNotSupportedException if the operation is not supported by the shelf.
	 */
	@Override
	public boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
		if (shelfName == null || document == null) {
			throw new IllegalArgumentException("Shelf name and document cannot be null");
		}
		IShelf shelf = shelfMap.get(shelfName);
		if (shelf == null) {
			return false;
		}
		boolean added = shelf.addDocument(document);
		return added;
	}

	/**
	 * Returns an iterable collection of documents from the shelf with the specified name.
	 *
	 * @param shelfName the name of the shelf; must not be null.
	 * @return an iterable collection of documents in the shelf, or null if the shelf does not exist.
	 */
	@Override
	public Iterable<IDocument> getDocuments(String shelfName) {
		if (shelfName == null) {
			throw new IllegalArgumentException(SHELF_NAME_CANNOT_BE_NULL);
		}
		IShelf shelf = shelfMap.get(shelfName);
		if (shelf == null) {
			return null;
		}
		return shelf;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Shelves=\n");
		for (Map.Entry<String, IShelf> entry : shelfMap.entrySet()) {
			sb.append(entry.getKey()).append(" = [");
			Iterator<IDocument> it = entry.getValue().iterator();
			while (it.hasNext()) {
				sb.append(it.next().getFile());
				if (it.hasNext()) {
					sb.append(", ");
				}
			}
			sb.append("]\n");
		}
		return sb.toString();
	}

}