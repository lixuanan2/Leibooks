package leibooks.services.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;

/**
 * The PDFReader class provides functionality to read PDF files and extract metadata such as the number of pages and authors.
 * It uses the com.sun.pdfview.PDFFile class to read and parse the PDF file.
 * 
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * File file = new File("example.pdf");
 * PDFReader reader = new PDFReader(file);
 * int pages = reader.getPages();
 * String authors = reader.getAuthors();
 * }
 * </pre>
 * 
 * @author malopes
 */


public class PDFReader {

	private int pages;
	private String authors;

	/**
	 * Constructs a new PDFReader object to read the specified PDF file.
	 * 
	 * @param file the PDF file to read
	 * @throws IOException 	if an I/O error occurs while reading the file
	 */
	public PDFReader(File file) throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(file, "r")){
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,
					0, channel.size());
			PDFFile pdffile = new PDFFile(buf);
			pages = pdffile.getNumPages();
			authors = pdffile.getStringMetadata("Author");
		}
	}

	/**
	 * Retrieves the number of pages in the PDF document.
	 *
	 * @return the total number of pages in the PDF document.
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * Retrieves the authors of the PDF document.
	 * @return the authors of the PDF document.
	 */
	public String getAuthors() {
		return authors;
	}

}
