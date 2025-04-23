package leibooks.domain.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.metadatareader.IMetadataReader;
import leibooks.domain.metadatareader.MetadataReaderFactory;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;
/**
 * A factory for creating Document objects.
 *
 * This enum is a singleton, providing a single INSTANCE that can
 * create IDocument objects by reading metadata from a file.
 */
public enum DocumentFactory {

    INSTANCE;

    /**
     * Creates a new IDocument by reading metadata from the given file path.
     *
     * @param title the title to assign to the Document
     * @param pathToPhotoFile the path to the file from which to read metadata
     * @requires title != null && pathToPhotoFile != null
     * @return an IDocument representing the file
     * @throws FileNotFoundException if the file cannot be accessed
     */

    public IDocument createDocument(String title, String pathToPhotoFile)
            throws FileNotFoundException {

        File file = new File(pathToPhotoFile);
        if (!file.exists()) {
            System.out.println("File " + pathToPhotoFile + " not found or could not be open");
            throw new FileNotFoundException("File " + pathToPhotoFile + " not found.");
        }


        IMetadataReader reader = MetadataReaderFactory.INSTANCE
                .createMetadataReader(pathToPhotoFile);

        String author = reader.getAuthors();
        //LocalDate dateModified = reader.getModifiedDate();
        //if (dateModified == null) {
          //  dateModified = LocalDate.now();
        //}
        LocalDate dateModified = LocalDate.now();


        String mimeType = reader.getMimeType();
        Optional<Integer> numPages = reader.getNumPages();

        return new Document(
                title,
                author,
                dateModified,
                mimeType,
                pathToPhotoFile,
                numPages
        );
    }
}
