package leibooks.app;

import leibooks.app.delegates.LEIBooksBookshelfUIDelegate;
import leibooks.app.delegates.LEIBooksDocumentMetadataUIDelegate;
import leibooks.app.delegates.LEIBooksDocumentUIDelegate;
import leibooks.domain.facade.LEIBooks;
import leibooks.ui.UIMain;

/**
 * The main project class
 * 
 * @author fmartins
 *
 */
public class GUIClient {

	public static void main(String[] args) {
		LEIBooks leib = new LEIBooks();

		LoaderDocuments.loadState(leib.getLibraryController(), leib.getShelvesController());

		UIMain.run(new LEIBooksBookshelfUIDelegate(leib.getShelvesController(), leib.getLibraryController()), 
				new LEIBooksDocumentUIDelegate(), new LEIBooksDocumentMetadataUIDelegate());
	}
}
