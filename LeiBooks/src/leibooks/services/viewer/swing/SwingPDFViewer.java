package leibooks.services.viewer.swing;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import leibooks.services.viewer.AViewer;
import leibooks.services.viewer.NoSuchPageException;

/**
 * SwingPDFViewer is a class that extends AViewer to provide PDF viewing capabilities
 * using the Swing framework.
 * 
 * This class uses the com.sun.pdfview library to read and render PDF files.
 * 
 * <p>Usage:</p>
 * <pre>
 * SwingPDFViewer viewer = new SwingPDFViewer();
 * viewer.setDocument(new File("path/to/pdf"));
 * Image pageImage = viewer.getPage(1, 800, 600);
 * </pre>
 * 
 * <p>Note: This class is not thread-safe.</p>
 */
public class SwingPDFViewer extends AViewer {

	/**
	 * Serial version UID for serialization.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7010322497796689226L;

	/**
	 * The PDFFile object representing the loaded PDF document.
	 */
	private PDFFile pdffile;

	/**
	 * Constructs a new SwingPDFViewer with the viewer type set to "swing".
	 */
	public SwingPDFViewer() {
		super("swing");
	}

	/**
	 * Sets the PDF document to be viewed.
	 * 
	 * @param file the PDF file to be loaded
	 * @throws IOException if an I/O error occurs while reading the file
	 */
	@Override
	public void setDocument(File file) throws IOException {
		super.setDocument(file);
		try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			pdffile = new PDFFile(buf);
		}
	}

	/**
	 * Loads the supported MIME types for this viewer.
	 */
	@Override
	protected void loadMimeTypes() {
		supportedViewerMimeTypes.add("application/pdf");
	}

	/**
	 * Retrieves an image of the specified page from the PDF document.
	 * 
	 * @param pageNum the page number to retrieve (1-based index)
	 * @param width the desired width of the resulting image
	 * @param height the desired height of the resulting image
	 * @return an Image object representing the specified page
	 * @throws NoSuchPageException if the specified page number is out of range
	 */
	@Override
	public Image getPage(int pageNum, int width, int height) throws NoSuchPageException {
		if (pageNum < 1 || pageNum > pdffile.getNumPages())
			throw new NoSuchPageException();

		PDFPage page = pdffile.getPage(pageNum);

		int sourceWidth = (int) page.getBBox().getWidth();
		int sourceHeight = (int) page.getBBox().getHeight();
		int thumbWidth;
		int thumbHeight;

		if (sourceWidth > sourceHeight) {
			thumbWidth = width;
			thumbHeight = thumbWidth * sourceHeight / sourceWidth;
		} else {
			thumbHeight = height;
			thumbWidth = thumbHeight * sourceWidth / sourceHeight;
		}

		if (thumbWidth > width) {
			thumbWidth = width;
			thumbHeight = thumbWidth * sourceHeight / sourceWidth;
		} else if (thumbHeight > height) {
			thumbHeight = height;
			thumbWidth = thumbHeight * sourceWidth / sourceHeight;
		}

		final Rectangle2D rect = page.getBBox();

		return page.getImage(
				thumbWidth, thumbHeight, // width & height
				rect, // clip rect
				null, // null for the ImageObserver
				true, // fill background with white
				true  // block until drawing is done
		);
	}
}
