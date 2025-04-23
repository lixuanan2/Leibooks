package leibooks.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import leibooks.services.viewer.IViewer;
import leibooks.services.viewer.NoSuchPageException;
import leibooks.ui.delegates.DocumentMetadataUIDelegate;
import leibooks.ui.delegates.DocumentUIDelegate;

public class DocumentUI {

	private static final ImageIcon BOOKMARK_ICON_TRUE = new ImageIcon("images/bookmarkTrue.png");
	private static final ImageIcon BOOKMARK_ICON_FALSE = new ImageIcon("images/bookmarkFalse.png");
	private static final ImageIcon PAGEINFO_ICON_TRUE = new ImageIcon("images/pageInfoTrue.png");
	private static final ImageIcon PAGEINFO_ICON_FALSE = new ImageIcon("images/pageInfoFalse.png");

	private JFrame frame; 
	private DocumentLabel documentLabel;
	private DocumentUIDelegate documentUIDelegate;
	private DocumentMetadataUIDelegate documentMetadataUIDelegate;
	private int pageNum;
	private JLabel documentJLabel;
	private int documentsPanelWidth;
	private int documentsPanelHeight;
	private Component oldRightComponent;
	private JLabel pageActionLabel;
	private JLabel bookmarkLabel;
	
	public DocumentUI (JFrame frame, DocumentLabel documentLabel, 
			DocumentUIDelegate documentViewerDelegate, 
			DocumentMetadataUIDelegate metadataUIDelegate) {
	
		this.documentLabel = documentLabel;
		this.frame = frame;
		this.documentUIDelegate = documentViewerDelegate;
		this.documentMetadataUIDelegate = metadataUIDelegate;

		// register the UI with its delegate
		documentViewerDelegate.setDocumentViewerUI(this);
		
		createGUIComponents ();
		activateDocumentViewer();
		
		pageNum = Math.max(documentViewerDelegate.getLastPageVisited() - 1, 0); 
		pageDown();
	}


	private void activateDocumentViewer() {
		try {
			documentLabel.getDocumentViewer().setDocument(documentUIDelegate.getDocumentFile());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Cannot read document", 
					"IO Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	private void createGUIComponents() {
		JSplitPane contentPane = (JSplitPane) frame.getContentPane();
		oldRightComponent = contentPane.getRightComponent();
		Rectangle paneSize = oldRightComponent.getBounds();
		documentsPanelWidth = paneSize.width - BookshelfUI.GAP_SIZE;
		documentsPanelHeight = paneSize.height - BookshelfUI.GAP_SIZE * 2;

		JPanel documentPanel = createDocumentPanel();
		contentPane.setRightComponent(documentPanel);
		
		createActionsPanel(documentPanel);
	}


	private JPanel createDocumentPanel() {
		JPanel documentPanel = new JPanel(new BorderLayout(0, 0));
		documentPanel.setBackground(Color.black);
		
		documentJLabel = new JLabel ();
		documentJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		documentJLabel.addMouseListener(documentJLabelController());

		documentPanel.add(documentJLabel, BorderLayout.CENTER);

		return documentPanel;
	}

	private void createActionsPanel(JPanel documentPanel) {		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setBackground(Color.black);
		actionsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		FlowLayout flPanel = new FlowLayout(FlowLayout.RIGHT, 0, 0);
		actionsPanel.setLayout(flPanel);
		documentPanel.add(actionsPanel, BorderLayout.NORTH);
		
		bookmarkLabel = new JLabel();
		updateBookmarkLabel(pageNum, documentUIDelegate.isBookmarked(pageNum));
		bookmarkLabel.addMouseListener(bookmarkLabelController());
		actionsPanel.add(bookmarkLabel);

		JLabel fullScreenLabel = new JLabel(new ImageIcon("images/fullscreen.png"));
		fullScreenLabel.addMouseListener(fullScreenLabelController());
		actionsPanel.add(fullScreenLabel);
		
		pageActionLabel = new JLabel();
		updatePageActionLabel(pageNum, documentUIDelegate.hasAnnotations(pageNum));
		pageActionLabel.addMouseListener(pageActionLabelController());
		actionsPanel.add(pageActionLabel);			
		
		JLabel closeLabel = new JLabel(new ImageIcon("images/close.png"));
		closeLabel.addMouseListener(closeLabelController());
		actionsPanel.add(closeLabel);
	}

	
	public void showPage (int pageNum) throws NoSuchPageException {
		this.pageNum = pageNum;
		IViewer viewer = documentLabel.getDocumentViewer();
   		documentJLabel.setIcon(new ImageIcon((Image) viewer.getPage(pageNum, documentsPanelWidth, documentsPanelHeight)));
		updateBookmarkLabel(pageNum, documentUIDelegate.isBookmarked(pageNum));
		updatePageActionLabel(pageNum, documentUIDelegate.hasAnnotations(pageNum));
	}
	
	private void pageDown() {
    	try {
			showPage(++pageNum);
		} catch (NoSuchPageException e) {
			JOptionPane.showMessageDialog(frame, "End of document", "Error reading page", JOptionPane.ERROR_MESSAGE);
			pageNum--;
		}
	}
	
	public void pageUp() {
		try {
			showPage (--pageNum);
		} catch (NoSuchPageException e) {
			JOptionPane.showMessageDialog(frame, "Begin of document", "Error reading page", JOptionPane.ERROR_MESSAGE);
			pageNum++;
		}
	}
		
	public int getCurrentPage() {
		return pageNum;
	}

	public void updatePageActionLabel(int pageN, boolean active) {
		if (pageNum == pageN)
			pageActionLabel.setIcon(active ? PAGEINFO_ICON_TRUE : PAGEINFO_ICON_FALSE);
	}

	public void updateBookmarkLabel(int pageN, boolean active) {
		if (pageNum == pageN)
			bookmarkLabel.setIcon (active ? BOOKMARK_ICON_TRUE : BOOKMARK_ICON_FALSE);
	}
	
	public void aboutToExitFull(int pageNum) {
		this.pageNum = pageNum - 1;
		pageDown();
		frame.setVisible(true);
	}

	
	// Controllers
	
	public MouseAdapter bookmarkLabelController() {
		return new MouseAdapter () {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				documentUIDelegate.toggleBookmark (pageNum);
			}

		};
	}
	
	private MouseAdapter documentJLabelController() {
		return new MouseAdapter () {
		
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1) 
        			pageDown();
        		else if(event.getButton() == MouseEvent.BUTTON3) 
        			pageUp();
			}
			
		};
	}
	

	private MouseAdapter closeLabelController() {
		return new MouseAdapter () {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				((JSplitPane) DocumentUI.this.frame.getContentPane()).setRightComponent(oldRightComponent);
				documentUIDelegate.deleteListeners();
				documentUIDelegate.setLastPageVisited(pageNum);
    	        SwingUtilities.updateComponentTreeUI(DocumentUI.this.frame.getContentPane());
			}

		};
	}


	private MouseAdapter pageActionLabelController() {
		return new MouseAdapter () {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				documentMetadataUIDelegate.setDocument(documentLabel.getDocument());
				new DocumentMetadataUI (frame, getCurrentPage(), documentMetadataUIDelegate, DocumentUI.this);
				documentMetadataUIDelegate.deleteObservers();
			}

		};
	}


	private MouseAdapter fullScreenLabelController() {
		return new MouseAdapter () {

			@Override
			public void mouseClicked(MouseEvent arg0) {
		    	frame.setVisible(false);
		    	new DocumentUIFullscreen(documentLabel, DocumentUI.this);
			}

		};
	}
}
