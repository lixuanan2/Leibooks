package leibooks.domain.shelves;

import java.util.function.Predicate;
import leibooks.domain.facade.IDocument;
import leibooks.utils.UnRemovable;
import leibooks.domain.core.ILibrary;

/**
 * Represents an unremovable smart shelf.
 *
 * <p>This smart shelf is created with a name, an associated library, and a predicate
 * to determine which documents from the library belong to it. It extends SmartShelf,
 * inheriting its behavior of dynamically filtering documents based on the given criteria,
 * and implements Unremovable to mark that this shelf cannot be removed from the collection
 * of shelves.</p>
 */
public class UnremovableSmartShelf extends SmartShelf implements UnRemovable {

    /**
     * Constructs an UnremovableSmartShelf with the specified name, library, and criteria.
     *
     * @param name the unique name of the shelf
     * @param library the library to monitor for documents
     * @param criteria the predicate that determines which documents from the library belong to this shelf
     * @throws IllegalArgumentException if library or criteria is null
     */
    public UnremovableSmartShelf(String name, ILibrary library, Predicate<IDocument> criteria) {
        super(name, library, criteria);
    }

}
