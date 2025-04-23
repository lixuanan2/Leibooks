package leibooks.domain.metadatareader;

import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * GenericMetadataReader extends AMetadataReader and uses the default values
 * for the number of pages (Optional.empty()) and authors ("n/a").
 */
public class GenericMetadataReader extends AMetadataReader {

    /**
     * Constructs a GenericMetadataReader for the given document file.
     *
     * @param pathToDocFile the file system path to the document.
     * @throws FileNotFoundException if the file cannot be accessed.
     */
    public GenericMetadataReader(String pathToDocFile) throws FileNotFoundException {
        super(pathToDocFile);
        this.numPages = Optional.of(1);
    }
}
