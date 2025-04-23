package leibooks.domain.metadatareader;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents a metadata reader for documents stored on disk,
 * providing methods to retrieve authors, MIME type, last modified date,
 * and the number of pages.
 *
 * <p>All methods must never return null. If some piece of information is
 * unavailable, return an empty string (for authors or MIME type) or
 * {@link Optional#empty()} (for page count).</p>
 */
public interface IMetadataReader {

    /**
     * Returns the authors of the document.
     * <p>Must not return null. If no authors can be determined, return an empty string.</p>
     *
     * @return the authors of the document
     */
    String getAuthors();

    /**
     * Returns the MIME type of the document.
     * <p>Must not return null. If the MIME type cannot be determined,
     * return an empty string.</p>
     *
     * @return the MIME type of the document
     */
    String getMimeType();

    /**
     * Returns the last modified date of the document.
     * <p>Must not return null. If the date cannot be determined,
     * you can default to a fallback (e.g., {@code LocalDate.now()}).</p>
     *
     * @return the last modified date
     */
    LocalDate getModifiedDate();

    /**
     * Returns an optional integer representing the total number of pages in the document.
     * <p>Must never return null. If no page count is available, return
     * {@link Optional#empty()}.</p>
     *
     * @return an optional page count
     */
    Optional<Integer> getNumPages();
}
