package leibooks.domain.metadatareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Abstract class AMetadataReader provides a skeleton implementation of the IMetadataReader interface.
 *
 * <p>
 * It defines a constructor that accepts the path to a document file, and retrieves the file's
 * last modification date and MIME type directly from the file, throwing a FileNotFoundException
 * if there is any problem accessing the file.
 * </p>
 *
 * <p>
 * It also declares two protected attributes:
 * <ul>
 *   <li>{@code numPages} - the number of pages, defaulting to {@link Optional#empty()}</li>
 *   <li>{@code authors} - a String representing the document's authors, defaulting to "n/a"</li>
 * </ul>
 * </p>
 */
public abstract class AMetadataReader implements IMetadataReader {

    protected Optional<Integer> numPages = Optional.empty();
    protected String authors = "n/a";
    private LocalDate dateModified;
    private String mimeType;

    /**
     * Constructs an AMetadataReader by reading metadata from the specified file.
     * It obtains the file's last modification date and MIME type.
     *
     * @param pathToDocFile the file system path to the document.
     * @throws FileNotFoundException if the file cannot be accessed.
     */
    public AMetadataReader(String pathToDocFile) throws FileNotFoundException {
        File file = new File(pathToDocFile);
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException("Cannot access file: " + pathToDocFile);
        }

        long lastMod = file.lastModified();
        this.dateModified = Instant.ofEpochMilli(lastMod)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        try {
            String type = Files.probeContentType(file.toPath());
            this.mimeType = (type != null) ? type : "";
        } catch (IOException e) {
            this.mimeType = "";
        }
    }

    /**
     * Returns the authors of the document.
     * <p>
     * Guaranteed to never return null. If authors cannot be determined, returns "n/a".
     * </p>
     *
     * @return the document's authors.
     */
    @Override
    public String getAuthors() {
        return this.authors;
    }

    /**
     * Returns the MIME type of the document.
     * <p>
     * Guaranteed to never return null. If the MIME type cannot be determined, returns an empty string.
     * </p>
     *
     * @return the MIME type.
     */
    @Override
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * Returns the last modified date of the document.
     * <p>
     * Guaranteed to never return null.
     * </p>
     *
     * @return the last modified date.
     */
    @Override
    public LocalDate getModifiedDate() {
        return this.dateModified;
    }

    /**
     * Returns an optional integer representing the number of pages in the document.
     * <p>
     * Guaranteed to never return null. If no page count is available, returns {@link Optional#empty()}.
     * </p>
     *
     * @return an Optional containing the number of pages, or empty if unavailable.
     */
    @Override
    public Optional<Integer> getNumPages() {
        return this.numPages;
    }
}
