package leibooks.services.viewer;

import java.io.File;
import java.io.IOException;

/**
 * Interface representing a viewer for documents.
 */
public interface IViewer {

	/**
	 * Returns an iterable collection of supported MIME types for the viewer.
	 *
	 * @return an iterable collection of supported MIME types.
	 */
	Iterable<String> getSupportedViewerMimeTypes();

	/**
	 * Returns the widget toolkit used by the viewer.
	 *
	 * @return the widget toolkit used by the viewer.
	 */
	String getWidgetToolkit();

	/**
	 * Sets the document to be viewed.
	 *
	 * @param file the document file to be viewed.
	 * @throws IOException if an I/O error occurs while setting the document.
	 */
	void setDocument(File file) throws IOException;

	/**
	 * Retrieves a specific page of the document.
	 *
	 * @param pageNum the page number to retrieve.
	 * @param width the width of the page.
	 * @param height the height of the page.
	 * @return the page object.
	 * @throws NoSuchPageException if the specified page does not exist.
	 */
	Object getPage(int pageNum, int width, int height) throws NoSuchPageException;

}