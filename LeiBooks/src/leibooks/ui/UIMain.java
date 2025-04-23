package leibooks.ui;

import java.awt.EventQueue;

import leibooks.ui.delegates.BookshelfUIDelegate;
import leibooks.ui.delegates.DocumentMetadataUIDelegate;
import leibooks.ui.delegates.DocumentUIDelegate;
import leibooks.ui.swing.BookshelfUI;

/**
 * The main UI classe
 * 
 * @author fmartins
 *
 */
public class UIMain {

	/**
	 * Launch user interface.
	 */
	public static void run(final BookshelfUIDelegate bookshelfDelegate, 
			final DocumentUIDelegate documentViewerDelegate, 
			final DocumentMetadataUIDelegate documentMetadataUIDelegate) {
		EventQueue.invokeLater( () -> {
				try {
					BookshelfUI frame = new BookshelfUI(bookshelfDelegate, 
							documentViewerDelegate, documentMetadataUIDelegate);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
		});
	}

}
