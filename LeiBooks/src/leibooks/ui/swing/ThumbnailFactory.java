package leibooks.ui.swing;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.ImageIcon;

import leibooks.app.AppProperties;
import leibooks.domain.facade.IDocument;
import leibooks.services.viewer.IViewer;
import leibooks.services.viewer.NoSuchPageException;
import leibooks.ui.delegates.DocumentUIDelegate;

final class ThumbnailFactory {

	/**
	 * Thumbnail size
	 */
	public static final int THUMBNAIL_SIZE = 215;
	
	private static final ImageIcon NO_VIEWER_IMAGE = new ImageIcon ("images/no_viewer_available.png");

	private static ThumbnailFactory instance;

	public static ThumbnailFactory getInstance () {
		if (instance == null)
			instance = new ThumbnailFactory();
		return instance;
	}

	private Map<IDocument, Thumbnail> thumbnails;

	private ThumbnailFactory () {
		thumbnails = new HashMap<> ();
	}

	public Thumbnail getThumbnail (DocumentUIDelegate uiDelegate) throws IOException, NoSuchPageException {
		File dFile = uiDelegate.getDocumentFile();
		File tbFile = new File(System.getProperty("user.dir") + File.separator + AppProperties.INSTANCE.FOLDER_DOCUMENT_FILES + File.separator + "thumbnails" + 
				File.separator + "thumb_" + dFile.getName());
		Optional<IViewer> v = uiDelegate.getViewer(uiDelegate.getDocumentType(), "swing");
		if (!tbFile.exists() || dFile.lastModified() > tbFile.lastModified()) {
			// criar thumbnail se nao existe ou se o ficheiro eh mais recente do que o thumbnail
			Thumbnail tb;
			if (v.isPresent()) {
				v.get().setDocument(dFile);
				tb = new Thumbnail (tbFile, new ImageIcon((Image) v.get().getPage(1, THUMBNAIL_SIZE, THUMBNAIL_SIZE)), v.get());
			} 
			else 
				tb = new Thumbnail (tbFile, NO_VIEWER_IMAGE, null);
			tb.save();
			thumbnails.put(uiDelegate.getDocument(), tb);
		} 

		if (thumbnails.get(uiDelegate.getDocument()) == null) {
			Thumbnail tb = Thumbnail.load(tbFile);
			if (v.isPresent())
				tb.setViewer(v.get());
			else 
				tb.setViewer(null);
			thumbnails.put(uiDelegate.getDocument(), tb);
		}

		return thumbnails.get(uiDelegate.getDocument());
	}
}
