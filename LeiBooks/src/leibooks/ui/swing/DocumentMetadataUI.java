package leibooks.ui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import leibooks.services.viewer.NoSuchPageException;
import leibooks.ui.delegates.DocumentMetadataUIDelegate;

@SuppressWarnings("serial")
public class DocumentMetadataUI extends JDialog {

	private int pageNum;
	private JTree tree;
	private Map <Integer, DefaultMutableTreeNode> int2TreeNodes;
	private DefaultMutableTreeNode bookmarks;
	private JTextArea annotationTextArea;
	private DefaultMutableTreeNode annotations;
	private int selectedAnnotation;
	private DocumentMetadataUIDelegate delegate;
	private DocumentUI documentUI;
	protected int selectedBookmark;
	private JFrame frame;

	/**
	 * Create the dialog.
	 * @param d 
	 */
	public DocumentMetadataUI(JFrame frame, int pn, DocumentMetadataUIDelegate delegate, 
			DocumentUI documentUI) {
		super (frame,  true);
		this.frame = frame;
		this.pageNum = pn;
		this.delegate = delegate;
		delegate.setDocumentetadataUI(this);
		this.documentUI = documentUI; 
		int2TreeNodes = new HashMap <Integer, DefaultMutableTreeNode> ();
		
		createGUIComponents ();
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				DocumentMetadataUI.this.delegate.deleteObservers();
			}
			
		});
		
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void createGUIComponents() {
		setBounds(100, 100, 568, 304);
		getContentPane().setLayout(new BorderLayout());
		JSplitPane splitPanel = new JSplitPane ();
		getContentPane().add(splitPanel, BorderLayout.CENTER);
		
		splitPanel.setLeftComponent(new JScrollPane(createTree()));

		annotationTextArea = new JTextArea();
		annotationTextArea.setText("Type in an annotation...");
		annotationTextArea.addFocusListener(focusAnnotationTextAreaController());
		splitPanel.setRightComponent (new JScrollPane(annotationTextArea));

		getContentPane().add(createButtonsPane(), BorderLayout.SOUTH);
	}

	private JPanel createButtonsPane() {
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		buttonsPane.add(addButton("Toggle bookmark"));
		buttonsPane.add(addButton("Add annotation"));
		buttonsPane.add(addButton("Remove annotation"));
		buttonsPane.add(addButton("Close"));

		return buttonsPane;
	}

	private JButton addButton(String string) {
		JButton button = new JButton(string);
		button.setActionCommand(string);
		button.addActionListener(buttonsController());
		return button;
	}


	private JTree createTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(delegate.getDocumentTitle());
		bookmarks = new DefaultMutableTreeNode("Bookmarks");
		root.add(bookmarks);
			
		for (Integer p : delegate.getDocumentBookmarks()) 
			internalAddBookmarkTreeNode(p);
			
	    annotations = new DefaultMutableTreeNode("Annotations (page " + pageNum + ")");
		root.add(annotations);
			
		for (String s : delegate.getPageAnnotations(pageNum))
			internalAddAnnotationTreeNode(s);
			
		tree = new JTree (root);
		tree.addMouseListener(treeMouseController());
		tree.expandPath(new TreePath(bookmarks.getPath()));
		tree.expandPath(new TreePath(annotations.getPath()));
		tree.setSelectionPath(new TreePath(root.getPath()));

		return tree;
	}

	public void removeAnnotationTreeNode(int annotationNum) {
		annotations.remove(annotationNum);
		SwingUtilities.updateComponentTreeUI(tree);
	}
	
	public void addAnnotationTreeNode(String text) {
		internalAddAnnotationTreeNode(text);
		tree.expandPath(new TreePath(annotations.getPath()));
		SwingUtilities.updateComponentTreeUI(tree);
	}
	
	private void internalAddAnnotationTreeNode(String text) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(text.length() > 10 ? text.substring(0, 10) + " ..." : text);
		annotations.add(node);
	}

	public void removeBookmarkTreeNode(int pageNum) {
		DefaultMutableTreeNode node = int2TreeNodes.get(pageNum);
		bookmarks.remove(node);
		int2TreeNodes.remove(pageNum);
		SwingUtilities.updateComponentTreeUI(tree);
	}

	public void addBookmarkTreeNode(int pageNum) {
		internalAddBookmarkTreeNode(pageNum);
		tree.expandPath(new TreePath(bookmarks.getPath()));
		SwingUtilities.updateComponentTreeUI(tree);
	}

	private void internalAddBookmarkTreeNode(int pageNum) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(pageNum + "");
		bookmarks.add(node);
		int2TreeNodes.put(pageNum, node);
	}
	
	// Controllers
	
	private MouseAdapter treeMouseController() {
		return new MouseAdapter () {
			
			@Override
			public void mouseClicked(MouseEvent event) {
			
				JTree tree =  (JTree) event.getSource();
				TreePath selectionPath =  tree.getPathForLocation(event.getX(), event.getY());
				if (selectionPath != null) {
					selectedAnnotation = -1;
					
					DefaultMutableTreeNode selection = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
					if (selection.getParent() == annotations) { 
						selectedAnnotation = annotations.getIndex(selection);
						annotationTextArea.setText(DocumentMetadataUI.this.delegate.getAnnotationText(pageNum, selectedAnnotation));
					} 

					if (event.getClickCount() >= 2)
						if (selection.getParent() == bookmarks)
							try {
								documentUI.showPage(Integer.parseInt((String) selection.getUserObject()));
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(frame, "Invalid bookmark number", "Invalid bookmark", JOptionPane.ERROR_MESSAGE);
							} catch (NoSuchPageException e) {
								JOptionPane.showMessageDialog(frame, "No shuch page", "Error reading page", JOptionPane.ERROR_MESSAGE);
							}
				}
			}
		};
	}
	
	private ActionListener buttonsController() {
		return new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("Toggle bookmark"))
					DocumentMetadataUI.this.delegate.toggleBookmark (pageNum);
				else if (event.getActionCommand().equals("Add annotation")) 
					DocumentMetadataUI.this.delegate.addAnnotation (pageNum, annotationTextArea.getText());
				else if (event.getActionCommand().equals("Remove annotation")) 
					DocumentMetadataUI.this.delegate.removeAnnotation (pageNum, selectedAnnotation);
				else if (event.getActionCommand().equals("Close"))
					dispose();
			}
		};
	}

	private FocusListener focusAnnotationTextAreaController() {
		return new FocusAdapter () {

			@Override
			public void focusGained(FocusEvent event) {
				annotationTextArea.selectAll();
			}		
		};
	}
}
