package leibooks.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.ILibraryController;
import leibooks.domain.facade.IShelvesController;

public class LoaderDocuments {

	/**
	 * An utility class should not have public constructors
	 */
	private LoaderDocuments() {
	}

	/**
	 * Import some documents to a library through its controller, create and 
	 * populate shelves for testing purposes
	 * 
	 * @param lc the library controller
	 * @param lc the shelves controller
	 */
	public static void loadState(ILibraryController lc, IShelvesController sc) {
		List<IDocument> docs = loadDocs(lc);
		createAndPopulateShelves(sc, docs);
	}

	public static List<IDocument> loadDocs(ILibraryController lc) {
		List<IDocument> docs = new ArrayList<>();

		String folder = AppProperties.INSTANCE.FOLDER_DOCUMENT_FILES;
		
		loadDocument(lc, docs, "Aula01", buildPath(folder, "Apresentacao.pdf"));
		loadDocument(lc, docs, "Aula02", buildPath(folder, "Aula2.pdf"));
		loadDocument(lc, docs, "Scale", buildPath(folder, "TLXScale.pdf"));
		loadDocument(lc, docs, "Tulipas", buildPath(folder, "tulips2.jpg"));
		loadDocument(lc, docs, "Flor", buildPath(folder, "SummerFades.jpg"));
		loadDocument(lc, docs, "Lyrics Scarlet Town", buildPath(folder, "ScarletTown.txt"));
		loadDocument(lc, docs, "Does not exist", buildPath(folder, "MissingFile.jpg"));

		return Collections.unmodifiableList(docs);
	}


	private static void loadDocument(ILibraryController lc, List<IDocument> docs, String title, String path) {
		Optional<IDocument> doc = lc.importDocument(title, path);
		if (doc.isPresent()) 
			docs.add(doc.get());
	}

	public static void createAndPopulateShelves(IShelvesController sc, List<IDocument> docs) {
		sc.addNormalShelf("Dco");
		sc.addNormalShelf("Photos");

		try {
			sc.addDocument("Dco", docs.get(0));
			sc.addDocument("Dco", docs.get(1));

			sc.addDocument("Photos", docs.get(3));
			sc.addDocument("Photos", docs.get(4));
		} 
		catch (OperationNotSupportedException e) {
			//should not happen
		}
	}

	private static String buildPath(String... parts) {
		return String.join(File.separator, parts);
	}
}
