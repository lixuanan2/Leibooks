package leibooks.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.naming.OperationNotSupportedException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import leibooks.app.AppProperties;
import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.services.viewer.NoSuchPageException;
import leibooks.ui.delegates.BookshelfUIDelegate;
import leibooks.ui.delegates.DocumentMetadataUIDelegate;
import leibooks.ui.delegates.DocumentUIDelegate;

/**
 * @author fmartins
 *
 */
public class BookshelfUI extends JFrame {

	/**
	 * Gap size between thumnails 
	 */
	public static final int GAP_SIZE = 40;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5592203823306666309L;

	private JTextField searchField; // Search box for user input
	private JSplitPane contentPane;
	private JPanel documentsPanel;
	private DefaultMutableTreeNode selectedTreeNode;
	private DocumentLabel selectedDocumentLabel;
	private JPopupMenu treeContextMenu;
	private JPopupMenu treeNodeContextMenu;
	private DefaultMutableTreeNode shelvesNode;
	private JTree tree;
	private JPopupMenu documentContextMenu;
	private DefaultMutableTreeNode libraryNode;
	private BookshelfUIDelegate uiDelegate;
	private DocumentUIDelegate documentUIDelegate;
	private DocumentMetadataUIDelegate documentMetadataDelegate;

	/**
	 * Create the main frame given the shelves and the library controllers.
	 */
	public BookshelfUI(BookshelfUIDelegate uiDelegate, DocumentUIDelegate documentViewerDelegate, 
			DocumentMetadataUIDelegate documentMetadataDelegate) {

		this.uiDelegate = uiDelegate;
		this.documentUIDelegate = documentViewerDelegate;
		this.documentMetadataDelegate = documentMetadataDelegate;
		uiDelegate.setBookshelfUI(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(AppProperties.INSTANCE.APP_START_X, 
				AppProperties.INSTANCE.APP_START_Y, 
				AppProperties.INSTANCE.APP_START_WIDTH, 
				AppProperties.INSTANCE.APP_START_HEIGHT);


		// Main container panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

		// Add a search box at the top
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchField = new JTextField(20); // Set the size of the search box
		JButton searchButton = new JButton("Search");

		searchButton.addActionListener(e -> {
				String query = searchField.getText().trim();
				if (!query.isEmpty()) {
					query = ".*" + query + ".*";
					showDocuments(AppProperties.INSTANCE.LIBRARY_NAME, uiDelegate.searchDocuments(query));
				}
		});

		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		mainPanel.add(searchPanel, BorderLayout.NORTH);

		// Create the main split pane
		contentPane = new JSplitPane();

		// Set the right documents view
		documentsPanel = new JPanel(new ModifiedFlowLayout(FlowLayout.LEFT, GAP_SIZE, GAP_SIZE));
		documentsPanel.setBackground(new Color(103, 103, 103));
		JScrollPane scrollDocumentsPane = new JScrollPane(documentsPanel);
		scrollDocumentsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.setRightComponent(scrollDocumentsPane);

		// Set the left tree
		contentPane.setLeftComponent(new JScrollPane(createTree()));
		contentPane.setDividerLocation(150);

		mainPanel.add(contentPane, BorderLayout.CENTER);    	
	}

	@Override
	public JSplitPane getContentPane() {
		return contentPane;
	}

	/**
	 * Populates the main tree 
	 * 
	 * @return The application's tree
	 */
	private JTree createTree() {
		// the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(AppProperties.INSTANCE.APP_ROOT_NAME);

		// the library node
		libraryNode = new DefaultMutableTreeNode(AppProperties.INSTANCE.LIBRARY_NAME);
		root.add(libraryNode);

		// the shelves node
		shelvesNode = new DefaultMutableTreeNode(AppProperties.INSTANCE.SHELF_GROUP_NAME);
		root.add(shelvesNode);

		for(String s : uiDelegate.getShelves()) {
			internalAddShelfNode(s);
		}

		// creates the tree and attaches a controller
		tree = new JTree(root);
		tree.setBackground(Color.white);
		tree.addTreeSelectionListener(treeSelectionController());
		tree.expandPath(new TreePath(shelvesNode.getPath()));
		tree.setSelectionPath(new TreePath(libraryNode.getPath()));

		// context menu support
		createContextMenuTree();
		tree.addMouseListener(treeMouseController());

		// Drag and Drop Support
		new TreeDropTarget(this, tree, uiDelegate);

		return tree;
	}

	public void addShelfTreeNode(String shelfName) {
		internalAddShelfNode(shelfName);
		SwingUtilities.updateComponentTreeUI(tree);
	}

	public void removeSelectedShelfTreeNode() {
		// find the previous shelf node
		DefaultMutableTreeNode newSelection =(DefaultMutableTreeNode) shelvesNode.getChildBefore(selectedTreeNode);
		// if no such node, try to find the next shelf
		if (newSelection == null)
			newSelection =(DefaultMutableTreeNode) shelvesNode.getChildAfter(selectedTreeNode);
		// if no shelfs available, show the lib
		if (newSelection == null) 
			newSelection = libraryNode;
		shelvesNode.remove(selectedTreeNode);
		tree.setSelectionPath(new TreePath(newSelection.getPath()));
		SwingUtilities.updateComponentTreeUI(tree);
	}

	private void internalAddShelfNode(String s) {
		DefaultMutableTreeNode shelf = new DefaultMutableTreeNode(s);
		shelf.setUserObject(s);
		shelvesNode.add(shelf);
	}


	/**
	 * Show the library thumbnails 
	 */
	private void showDocuments(String target, Iterable<IDocument> iterable) {
		// remove previously added thumbnails
		documentsPanel.removeAll();
		createContextMenuDocuments(target);

		// load new thumbnails
		for(IDocument d : iterable) {
			internalAddToDocumentsPanel(d);
		}
		SwingUtilities.updateComponentTreeUI(documentsPanel);
	}

	public void removeSelectedDocumentFromPanel() {
		documentsPanel.remove(selectedDocumentLabel);
		SwingUtilities.updateComponentTreeUI(documentsPanel);
	}

	public void addToDocumentsPanel(IDocument document) {
		internalAddToDocumentsPanel(document);
		SwingUtilities.updateComponentTreeUI(documentsPanel);
	}

	private void internalAddToDocumentsPanel(IDocument d) {
		try {
			documentUIDelegate.setDocument(d);
			Thumbnail tb = ThumbnailFactory.getInstance().getThumbnail(documentUIDelegate);
			JLabel thumbnailLabel = new DocumentLabel(tb.image, d, tb.getViewer());
			thumbnailLabel.setToolTipText(uiDelegate.getDocumentTitle(d));
			thumbnailLabel.addMouseListener(thumbnailController());
			thumbnailLabel.addMouseMotionListener(thumbnailMouseMotionController());
			thumbnailLabel.setTransferHandler(new TransferHandler("document"));
			documentsPanel.add(thumbnailLabel);
		} catch(IOException e) {
			// Do not load the document thumbnail...
			//e.printStackTrace(); 
		} catch(NoSuchPageException e) {
			JOptionPane.showMessageDialog(this, "Empty document", "Error reading page", JOptionPane.ERROR_MESSAGE);
		}
	}


	private MouseAdapter thumbnailMouseMotionController() {
		return new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent event) {
				JComponent jc =(JComponent) event.getSource();
				TransferHandler th = jc.getTransferHandler();
				th.exportAsDrag(jc, event, TransferHandler.COPY);
			}
		};
	}


	/**
	 * @param libraryNode The library node
	 * @return The tree controller
	 */
	private TreeSelectionListener treeSelectionController() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent event) {
				selectedTreeNode = 
						(DefaultMutableTreeNode) event.getNewLeadSelectionPath().getLastPathComponent();

				if (selectedTreeNode == libraryNode) {
					// show lib
					showDocuments(AppProperties.INSTANCE.LIBRARY_NAME, uiDelegate.getLibraryDocuments());
				}
				else if (selectedTreeNode.getParent() == shelvesNode) {
					// show shelf
					String selectedShelf =(String) selectedTreeNode.getUserObject();
					showDocuments(selectedShelf, uiDelegate.getShelfDocuments(selectedShelf));
				}
			}
		};
	}



	/**
	 * @return The thumbnail controller
	 */
	private MouseAdapter thumbnailController() {
		return new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (selectedDocumentLabel != null)
					selectedDocumentLabel.setBorder(null);
				selectedDocumentLabel =(DocumentLabel) event.getSource();
				selectedDocumentLabel.setBorder(new MatteBorder(3, 3, 3, 3,(Color) Color.YELLOW));
				if (event.getButton() == MouseEvent.BUTTON1) {
					if (event.getClickCount() >= 2) {
						// on double click, view the document
						if (selectedDocumentLabel.getDocumentViewer() != null) {
							documentUIDelegate.setDocument(selectedDocumentLabel.getDocument());
							documentUIDelegate.setListeners();
							new DocumentUI(BookshelfUI.this, selectedDocumentLabel, documentUIDelegate, 
									documentMetadataDelegate);
						} 
						else
							JOptionPane.showMessageDialog(BookshelfUI.this, "Cannot obtain a viewer for this type of document", 
									"Error obtaining viewer for document", JOptionPane.ERROR_MESSAGE);
					}
				}
				//TODO FIND OUT WHY DOES NOT WORK WITH CTR CLICK in MACOS
				if (event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON3) {	 
					selectedDocumentLabel =(DocumentLabel) event.getSource();
					documentContextMenu.show(selectedDocumentLabel, event.getX(), event.getY());
				}
			}
		};
	}


	private MouseAdapter treeMouseController() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				//TODO FIND OUT WHY DOES NOT WORK WITH CTR CLICK in MACOS
				if (event.isPopupTrigger() || event.getButton() == 3) {	 
					JTree tree =(JTree) event.getSource();
					TreePath path = tree.getPathForLocation(event.getX(), event.getY());
					if (path == null)
						treeContextMenu.show(tree, event.getX(), event.getY());
					else {
						tree.setSelectionPath(path);
						if (selectedTreeNode.getUserObject() instanceof String)
							treeNodeContextMenu.show(tree, event.getX(), event.getY());
						else 
							treeContextMenu.show(tree, event.getX(), event.getY());
					}
				}
			}
		};
	}

	private void createContextMenuTree() {
		treeContextMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Add normal shelf...");
		menuItem.addActionListener(arg0 -> {
			String shelfName = JOptionPane.showInputDialog(BookshelfUI.this, "Shelf name: ", 
					"Add normal shelf", JOptionPane.QUESTION_MESSAGE);
			if (shelfName != null && !uiDelegate.addNormalShelf(shelfName))
				JOptionPane.showMessageDialog(BookshelfUI.this, "Cannot add shelf " + shelfName, 
						"Error adding shelf", JOptionPane.ERROR_MESSAGE);
		});
		treeContextMenu.add(menuItem);

		treeNodeContextMenu = new JPopupMenu();  
		menuItem = new JMenuItem("Delete");
		menuItem.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent event) {
				try {
					if (selectedTreeNode.getParent() == shelvesNode)
						uiDelegate.removeShelf(((String) selectedTreeNode.getUserObject()));
				}
				catch(OperationNotSupportedException e) {
					JOptionPane.showMessageDialog(BookshelfUI.this,
							"Shelf cannot be removed.",
							"Remove shelf error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		treeNodeContextMenu.add(menuItem);
	}

	private void createContextMenuDocuments(final String target) {
		documentContextMenu = new JPopupMenu();

		JMenuItem menuItem = new JMenuItem("Properties...");
		menuItem.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent event) {
				DocumentProperties documentProperties = 
						new DocumentProperties(selectedDocumentLabel.getDocument());
				new DocumentPropertiesUI(BookshelfUI.this, documentProperties);
				uiDelegate.updateDocument(selectedDocumentLabel.getDocument(), documentProperties);
			}
		});
		documentContextMenu.add(menuItem);

		menuItem = new JMenuItem("Delete");
		menuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				try {
					if (target.equals(AppProperties.INSTANCE.LIBRARY_NAME))
						uiDelegate.removeLibraryDocument(selectedDocumentLabel.getDocument());
					else
						uiDelegate.removeDocumentShelf(target, selectedDocumentLabel.getDocument());
					SwingUtilities.updateComponentTreeUI(documentsPanel);
				} 
				catch(OperationNotSupportedException e) {
					JOptionPane.showMessageDialog(BookshelfUI.this,
							"Document cannot be removed.",
							"Remove document error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		documentContextMenu.add(menuItem);
	}

}