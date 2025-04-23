package leibooks.services.viewer.swing;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import leibooks.app.AppProperties;
import leibooks.services.viewer.IViewer;
import leibooks.services.viewer.IViewerFactory;

/**
 * SwingViewerFactory is an enumeration that implements the IViewerFactory interface.
 * It provides a singleton instance to manage and create viewer instances for different MIME types.
 * 
 * The factory loads viewers during initialization, including a default PDF viewer and additional viewers
 * from a specified folder. It supports retrieving viewers based on document MIME type and widget toolkit.
 * 
 * <p>Usage example:</p>
 * <pre>{@code
 * Optional<IViewer> viewer = SwingViewerFactory.INSTANCE.getViewer("application/pdf", "Swing");
 * </pre>
 * 
 */
public enum SwingViewerFactory implements IViewerFactory{
	/**
	 * The singleton instance of the SwingViewerFactory.
	 */
	INSTANCE;
	
	/*
	 * A map that associates viewer names with their corresponding viewer classes.
	 * The keys are strings representing the names of the viewers, and the values
	 * are classes that extend the IViewer interface.
	 */
	private Map<String, Class<? extends IViewer>> viewers; 

	/*
	 * Constructs a SwingViewerFactory and loads viewers.
	*/
	private SwingViewerFactory () {
		viewers = new HashMap<> ();
		
		//load pdf viewer
		SwingPDFViewer viewer = new SwingPDFViewer(); 
		for (String mimeType : viewer.getSupportedViewerMimeTypes())
			viewers.put(mimeType + viewer.getWidgetToolkit(), SwingPDFViewer.class);
		
		//load other viewers
		loadViewers();
	}

	/*
	 * Loads viewers from a predefined folder.
	*/
	private void loadViewers() {	

		// add filters in the folder
		File folder = new File(AppProperties.INSTANCE.FOLDER_EXTRA_VIEWERS_AND_READERS); 
		File [] classes = folder.listFiles( (dir, name) -> {
				return name.endsWith(".class");
		});

		// try to load the classes that implement IViewer
		for (File className : classes) {
			try {
				String s = className.getName();
				@SuppressWarnings("unchecked")
				Class<IViewer> viewerClass = (Class<IViewer>) Class.forName(s.substring(0, s.lastIndexOf('.')));
				IViewer viewer = viewerClass.getConstructor().newInstance();
				for (String mimeType : viewer.getSupportedViewerMimeTypes())
					viewers.put(mimeType + viewer.getWidgetToolkit(), viewerClass);
			} 
			catch (ClassNotFoundException|SecurityException | InstantiationException | 
					IllegalAccessException | IllegalArgumentException | InvocationTargetException | 
					NoSuchMethodException e) {
						// Just ignore the class as it is not a valid viewer
			}	
		}
	}
	/**
	 * Returns a viewer instance for the specified document MIME type and widget toolkit.
	 * 
	 * @param documentMime the MIME type of the document to be viewed
	 * @param widgetToolkit the widget toolkit to be used by the viewer
	 * @return an Optional containing the viewer instance if it exists, or an empty Optional otherwise
	 */
	public Optional<IViewer> getViewer (String documentMime, String widgetToolkit) {
		try {
			Class<? extends IViewer> viewerClass = viewers.get(documentMime+widgetToolkit);
			if (viewerClass != null)
				return Optional.of(viewerClass.getConstructor().newInstance());
		}
		catch (SecurityException | InstantiationException | 
				IllegalAccessException | IllegalArgumentException | InvocationTargetException | 
				NoSuchMethodException e) {
				//Can't happen (checked before)
		}	
		return Optional.empty();
	}
	
	public String toString() {
		return viewers.toString();
	}
}
