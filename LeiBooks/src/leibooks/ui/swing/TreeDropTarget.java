package leibooks.ui.swing;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.naming.OperationNotSupportedException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import leibooks.domain.facade.IDocument;
import leibooks.ui.delegates.BookshelfUIDelegate;

class TreeDropTarget implements DropTargetListener {

	private JTree targetTree;
	private JFrame frame;
	private BookshelfUIDelegate uiDelegate;

	public TreeDropTarget(JFrame frame, JTree tree, BookshelfUIDelegate uiDelegate) {
		targetTree = tree;
	    new DropTarget(targetTree, this);
	    this.uiDelegate = uiDelegate;
	    this.frame = frame;
	}

	  /*
	   * Drop Event Handlers
	   */
	  private TreeNode getNodeForEvent(DropTargetDragEvent dtde) {
	    Point p = dtde.getLocation();
	    DropTargetContext dtc = dtde.getDropTargetContext();
	    JTree tree = (JTree) dtc.getComponent();
	    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
	    return (TreeNode) path.getLastPathComponent();
	  }

	  public void dragEnter(DropTargetDragEvent dtde) {
	    TreeNode node = getNodeForEvent(dtde);
	    if (!node.isLeaf()) {
	      dtde.rejectDrag();
	    } else {
	      // start by supporting move operations
	      //dtde.acceptDrag(DnDConstants.ACTION_MOVE);
	      dtde.acceptDrag(dtde.getDropAction());
	    }
	  }

	  public void dragOver(DropTargetDragEvent dtde) {
	    TreeNode node = getNodeForEvent(dtde);
	    if (!node.isLeaf()) {
	      dtde.rejectDrag();
	    } else {
	      // start by supporting move operations
	      //dtde.acceptDrag(DnDConstants.ACTION_MOVE);
	      dtde.acceptDrag(dtde.getDropAction());
	    }
	  }

	  public void dragExit(DropTargetEvent dte) {
	  }

	  public void dropActionChanged(DropTargetDragEvent dtde) {
	  }

	  public void drop(DropTargetDropEvent dtde) {
	    Point pt = dtde.getLocation();
	    DropTargetContext dtc = dtde.getDropTargetContext();
	    JTree tree = (JTree) dtc.getComponent();
	    TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
	    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) parentpath
	        .getLastPathComponent();
	    if (!parent.isLeaf()) {
	      dtde.rejectDrop();
	      return;
	    }

	    //dtde.acceptDrop(dtde.getDropAction());
	    
	    try {
	      Transferable tr = dtde.getTransferable();
	      DataFlavor[] flavors = tr.getTransferDataFlavors();
	      for (int i = 0; i < flavors.length; i++) {
	        if (tr.isDataFlavorSupported(flavors[i])) {
	          dtde.acceptDrop(dtde.getDropAction());
	          IDocument source = (IDocument) tr.getTransferData(flavors[i]);
	          String shelfName = (String) parent.getUserObject();
	          if (!uiDelegate.addDocumentShelf(shelfName, source))
	        	  JOptionPane.showMessageDialog(frame, "Duplicated document!", 
			    			"Add document error", JOptionPane.ERROR_MESSAGE);
	          dtde.dropComplete(true);
	          return;
	        }
	      }
	      //dtde.rejectDrop();
	    } catch (OperationNotSupportedException e) {
	    	JOptionPane.showMessageDialog(frame, "Cannot add a document to this shelf", 
	    			"Add document error", JOptionPane.ERROR_MESSAGE);
	    } catch (Exception e) {
	    }
	  }
	}
