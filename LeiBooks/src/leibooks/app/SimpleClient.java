package leibooks.app;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.ILibraryController;
import leibooks.domain.facade.IShelvesController;
import leibooks.domain.facade.LEIBooks;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.LBEvent;
import leibooks.domain.metadatareader.MetadataReaderFactory;
import leibooks.services.viewer.swing.SwingViewerFactory;
import leibooks.utils.Listener;

/**
 * A simple application client that uses the different services provided by the 
 * two application controllers.
 *	
 * @author malopes
 */
public class SimpleClient {

	private static final String OPEN_MSG = "\n----------------------------------------------- ";
	private static final String CLOSE_MSG = "-----------------------------------------------\n";

	/**
	 * An utility class should not have public constructors
	 */
	private SimpleClient() {
	}

	public static void main (String [] args) {

		LEIBooks leibooks = new  LEIBooks();
		ILibraryController lc = leibooks.getLibraryController();
		IShelvesController sc = leibooks.getShelvesController();

		//for testing that the dynamic loading of extra classes is working 
		showDynamicLoadingState();

		//for testing that the right events about shelves and the library are being emitted
        lc.registerListener(new InfoListener<>("Library"));
        sc.registerListener(new InfoListener<>("Shelves"));
         
		//setUp
		List<IDocument> docs = loadSomeDocuments(lc); 
		showState(lc,sc);
		
		//for testing that the right events about documents are being emitted
		Map<IDocument,InfoListener<DocumentEvent>> listeners = new HashMap<>();
		for(IDocument d: docs) {
			InfoListener<DocumentEvent> l = new InfoListener<>("Document");
			listeners.put(d, l);
			d.registerListener(l);
		}
	
		//core operations with docs
		addBookmarksToSomeDocuments(docs.get(0), docs.get(1));
		showState(lc,sc);

		addAnnotationsToSomeDocuments("CHECK MOODLE ALSO",docs.get(0));
		showState(lc,sc);
		
		deleteSomeDocuments(listeners, lc, docs.get(2));
		showState(lc,sc);
		
		searchDocuments(lc, ".*Lopes.*");
	
		//operations with shelves
		createAndPopulateSomeNormalShelves(sc, docs);
		showState(lc,sc);

		createSmartShelf(sc);
		showState(lc,sc);
		
		addBookmarksToSomeDocuments(docs.get(5));
		showState(lc,sc);

		//operations over the library that interfere with shelves
		deleteSomeDocuments(listeners, lc, docs.get(0));
		showState(lc,sc);

		//delete document from shelf
		deleteSomeDocumentFromShelf(sc, "Photos", docs.get(3));
		showState(lc,sc);
		
		//delete document from shelfs
		deleteSomeDocumentFromShelf(sc, "Recent", docs.get(1));
		showState(lc,sc);
		
		//delete shelfs
		deleteShelf(sc, "Photos");
		showState(lc,sc);
	
		deleteShelf(sc, "Recent");
		showState(lc,sc);
	}


	private static void showDynamicLoadingState() {
		System.out.println(OPEN_MSG);		
		System.out.print("Swing Viewers available\n");
		System.out.println(CLOSE_MSG);
		System.out.println(SwingViewerFactory.INSTANCE);
		
		System.out.println(OPEN_MSG);		
		System.out.print("Metadata readers available\n");
		System.out.println(CLOSE_MSG);
		System.out.println(MetadataReaderFactory.INSTANCE);

	}

	private static void deleteSomeDocumentFromShelf(IShelvesController sc, String nameShelf, IDocument doc) {
		System.out.println(OPEN_MSG);		
		System.out.print("Delete "+ idsToString(Arrays.asList(doc)) + "from shelf " + nameShelf + "\n");
		System.out.println(CLOSE_MSG);
		try {
			sc.removeDocument(nameShelf, doc);
		} catch (OperationNotSupportedException e) {
			System.out.println("Raised OperationNotSupportedException \n");
		}

		
	}

	private static void deleteShelf(IShelvesController sc, String nameShelf) {
		System.out.println(OPEN_MSG);	
		System.out.print("Delete shelf with name " + nameShelf + "\n");
		System.out.println(CLOSE_MSG);
		try {
			sc.remove(nameShelf);
		} catch (OperationNotSupportedException e) {
			System.out.println("OperationNotSupportedException \n");
		}
	}

	private static void createSmartShelf(IShelvesController sc) {
		System.out.println(OPEN_MSG);		
		System.out.println("Create a smart shelf  with docs modified today");
		System.out.println(CLOSE_MSG);
		sc.addSmartShelf("Today", d -> (d.getLastModifiedDate()).equals(LocalDate.now()));
	}

	private static void createAndPopulateSomeNormalShelves(IShelvesController sc, List<IDocument> docs) {
		System.out.println(OPEN_MSG);		
		System.out.println("Create two normal shelves and populate them    ");
		System.out.println(CLOSE_MSG);
		LoaderDocuments.createAndPopulateShelves(sc, docs);
	}

	private static void showState(Object... objects) {
		for(Object o: objects)
			System.out.println(o);
	}

	private static List<IDocument> loadSomeDocuments(ILibraryController lc) {
		System.out.println(OPEN_MSG);		
		System.out.println("Load photos and add them to library    ");
		System.out.println(CLOSE_MSG);
		return LoaderDocuments.loadDocs(lc);
	}

	private static void addBookmarksToSomeDocuments(IDocument... docs) {
		System.out.println(OPEN_MSG);		
		System.out.print("Adding bookmarks to \n" +  idsToString(Arrays.asList(docs)) );
		System.out.println(CLOSE_MSG);
		for (IDocument doc: docs) {
			doc.toggleBookmark(1);
		}
	}
	
	private static void addAnnotationsToSomeDocuments(String text, IDocument... docs) {
		System.out.println(OPEN_MSG);		
		System.out.print("Adding annotations to \n" +  idsToString(Arrays.asList(docs)) );
		System.out.println(CLOSE_MSG);
		for (IDocument doc: docs) {
			doc.addAnnotation(8, text);
		}
	}

	private static void deleteSomeDocuments(
			Map<IDocument,InfoListener<DocumentEvent>> listeners, ILibraryController lc, IDocument... docs) {
		System.out.println(OPEN_MSG);		
		System.out.print("Delete \n" + idsToString(Arrays.asList(docs)));
		System.out.println(CLOSE_MSG);
		for(IDocument d: docs) {
				lc.removeDocument(d);
				InfoListener<DocumentEvent> l = listeners.get(d);
				d.unregisterListener(l);
		}
	}

	private static List<IDocument> searchDocuments(ILibraryController lc, String regex) {
		System.out.println(OPEN_MSG);		
		System.out.println("Search docs in library matching " + regex);
		System.out.println(CLOSE_MSG);
		List<IDocument> list = lc.getMatches(regex);
		System.out.println(idsToString(list));
		return list;
	}

	private static String idsToString(Iterable<IDocument> docs) {
		StringBuilder sb = new StringBuilder();
		for (IDocument document: docs) {
			sb.append(document.getFile()+"\n");
		}
		return sb.toString();
	}
	
	public static class InfoListener<E extends LBEvent> implements Listener<E> {
		private String contextInfo;
		
		public InfoListener(String info) {
			this.contextInfo = info;
		}
        public void processEvent(LBEvent evt) {
            System.out.println("-------->> " + contextInfo + ": " + evt +"<<--------\n");
        }
    }
}
