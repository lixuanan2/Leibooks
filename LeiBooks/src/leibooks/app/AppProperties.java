package leibooks.app;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Singleton enum that loads application properties from a properties file.
 * Provides default values if the properties file is not found or if specific properties are missing.
 * 
 * Properties loaded:
 * - app_root_name: The root name of the application (default: "LEIBooks").
 * - library_name: The name of the library (default: "Library").
 * - shelf_group: The name of the shelf group (default: "Shelves").
 * - shelf_recent: The name of the most recent shelf (default: "Recent").
 * - shelf_bookmarked: The name of the bookmarked shelf (default: "Bookmarked").
 * - months_recent_shelf: The number of months for the recent shelf (default: 12).
 * - app_window_start_x: The starting X position of the application window (default: 100).
 * - app_window_start_y: The starting Y position of the application window (default: 100).
 * - app_window_start_width: The starting width of the application window (default: 850).
 * - app_window_start_height: The starting height of the application window (default: 600).
 * - extra_classes_folder: The folder for extra viewers and readers (default: "viewers_readers").
 * 
 * Usage:
 * <pre>
 * {@code
 * String appName = AppProperties.INSTANCE.APP_ROOT_NAME;
 * int startX = AppProperties.INSTANCE.APP_START_X;
 * }
 * </pre>
 */
public enum AppProperties {
	INSTANCE;
	
	public final String APP_ROOT_NAME;
	public final String LIBRARY_NAME;
	public final String  SHELF_GROUP_NAME;
	
	public final int APP_START_X;
	public final int APP_START_Y;
	public final int APP_START_WIDTH;
	public final int APP_START_HEIGHT;
	
	public final String MOST_RECENT_NAME;
	public final String BOOKMARKED_NAME; 
	public final int MONTHS_RECENT;
	
	public final String FOLDER_EXTRA_VIEWERS_AND_READERS;
	public final String FOLDER_DOCUMENT_FILES;

	private Properties appProperties;
	
	AppProperties() {
		String propertiesFileName = "app.properties";
		appProperties = new Properties();
		try (FileInputStream f = new FileInputStream(propertiesFileName)) {
			appProperties.load(f);
		} catch (Exception e) {
			// It was not able to load properties file.
			// Bad luck, proceed with the default values
		}
		APP_ROOT_NAME = parseString("app_root_name", "LEIBooks");
		LIBRARY_NAME = parseString("library_name", "Library");
		SHELF_GROUP_NAME = parseString("shelf_group", "Shelves");

		MOST_RECENT_NAME = parseString("shelf_recent", "Recent");
		BOOKMARKED_NAME = parseString("shelf_bookmarked", "Bookmarked");
		MONTHS_RECENT = parseInt("months_recent_shelf", 12);
		
		APP_START_X = parseInt("app_window_start_x", 100);
		APP_START_Y = parseInt("app_window_start_y", 100);
		APP_START_WIDTH = parseInt("app_window_start_width", 850);
		APP_START_HEIGHT = parseInt("app_window_start_height", 600);
		
		FOLDER_EXTRA_VIEWERS_AND_READERS = parseString("extra_classes_folder", "viewers_readers");
		FOLDER_DOCUMENT_FILES = parseString("docs_files_folder", "doc_files");
	}

	private int parseInt(String property, int defaultValue) {
		try {
			return Integer.parseInt(appProperties.getProperty(property));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	private String parseString(String property, String defaultValue) {
		if (appProperties.getProperty(property) != null)
			return appProperties.getProperty(property);
		return defaultValue;
	}

}
