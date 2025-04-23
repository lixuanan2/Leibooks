import java.awt.Image;

import javax.swing.ImageIcon;

import leibooks.services.viewer.AViewer;
import leibooks.services.viewer.NoSuchPageException;

public class SwingImageViewer extends AViewer {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7010322497796689226L;
	
	public SwingImageViewer() {
		super("swing");
	}
	
	@Override
	protected void loadMimeTypes() {
		supportedViewerMimeTypes.add("image/jpg");
		supportedViewerMimeTypes.add("image/jpeg");
		supportedViewerMimeTypes.add("image/png");
		supportedViewerMimeTypes.add("image/gif");
	}
	

	@Override
	public Object getPage(int pageNum, int width, int height) throws NoSuchPageException {
		if (pageNum != 1)
			throw new NoSuchPageException();
		
		Image image = new ImageIcon (file.getAbsolutePath()).getImage();
		
	    double sourceWidth = image.getWidth(null);
	    double sourceHeight = image.getHeight(null);

	    double maxRatio = Math.min(Math.min (width/sourceWidth, height/sourceHeight), 1);

	    int thumbWidth = (int) (sourceWidth * maxRatio);
	    int thumbHeight = (int) (sourceHeight * maxRatio);
	    			
	    return image.getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_SMOOTH);
	}
}
