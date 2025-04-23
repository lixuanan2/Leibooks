package leibooks.services.viewer;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Abstract class AViewer that implements the IViewer interface.
 * This class provides a base implementation for viewers with common functionalities.
 * To use this class, extends it and implement loadMimeTypes and getPage.
 */
public abstract class AViewer implements IViewer {
	
	protected List<String> supportedViewerMimeTypes;
	private String widgetToolkit;
	protected File file;
	
	protected AViewer (String widgetToolkit) {
		this.widgetToolkit = widgetToolkit;
		this.supportedViewerMimeTypes = new LinkedList<> ();
		loadMimeTypes();  
	}

	/**
	 * Load the mime types supported by the viewer in
	 * the field supportedViewerMimeTypes. 
	 */
	protected abstract void loadMimeTypes();
	
	@Override
	public void setDocument (File file) throws IOException {
		this.file = file;
	}

	@Override
	public Iterable<String> getSupportedViewerMimeTypes() {
		return supportedViewerMimeTypes;
	}

	@Override
	public String getWidgetToolkit() {
		return widgetToolkit;
	}
		

}
