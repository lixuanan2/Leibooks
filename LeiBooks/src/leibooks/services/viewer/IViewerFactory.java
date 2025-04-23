package leibooks.services.viewer;

import java.util.Optional;

/**
 * Factory interface for creating viewer instances based on document MIME type and widget toolkit.
 */
public interface IViewerFactory {
	 /**
	 * Get a viewer instance for the given document MIME type and widget toolkit,
	 * if available.
	 * 
	 * @param documentMime MIME type of the document
	 * @param widgetToolkit Toolkit to use for rendering the document
	 * @return Viewer instance for the given document MIME type and widget toolkit if available
	 */
	Optional<IViewer> getViewer (String documentMime, String widgetToolkit);
}
