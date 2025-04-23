package leibooks.domain.metadatareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import leibooks.services.reader.PDFReader;

/**
 * PDFMetadataReaderAdapter is a metadata reader for PDF documents.
 * It extends AMetadataReader and uses services provided by PDFReader to obtain metadata.
 */
public class PDFMetadataReaderAdapter extends AMetadataReader {

    /**
     * Constructs a PDFMetadataReaderAdapter for the given PDF file.
     *
     * @param pathToDocFile the file system path to the PDF document.
     * @throws FileNotFoundException if the file cannot be accessed.
     */
    public PDFMetadataReaderAdapter(String pathToDocFile) throws FileNotFoundException {
        super(pathToDocFile);
        try {
            File file = new File(pathToDocFile);
            PDFReader reader = new PDFReader(file);
            
            this.numPages = Optional.of(reader.getPages());
            String pdfAuthors = reader.getAuthors();
            this.authors = (pdfAuthors != null && !pdfAuthors.isEmpty()) ? pdfAuthors : "n/a";
        } catch (Exception e) {
            this.numPages = Optional.empty();
            this.authors = "n/a";
        }
    }    
}
