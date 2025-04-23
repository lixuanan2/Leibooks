package leibooks.ui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import leibooks.domain.facade.DocumentProperties;

@SuppressWarnings("serial")
public class DocumentPropertiesUI extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private JTextField titleTextField;
	private JTextField authorTextField;	
	private JLabel docTypeLabel;

	private DocumentProperties result;

	private DefaultTableModel tableModel;

	private JLabel lblPath;

	/**
	 * Create the dialog.
	 * @param f 
	 * @param targetPanel 
	 */
	public DocumentPropertiesUI(JFrame parent, DocumentProperties result) {
		super (parent, true);
		this.result = result;
		setBounds(100, 100, 450, 300);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
						FormFactory.PARAGRAPH_GAP_ROWSPEC,
						RowSpec.decode("28px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("28px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("28px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("16px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("16px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("83px"),}));

		JLabel lblTitle = new JLabel("Title:");
		contentPanel.add(lblTitle, "2, 2, right, center");

		titleTextField = new JTextField();
		contentPanel.add(titleTextField, "4, 2, fill, top");
		titleTextField.setColumns(10);

		JLabel lblAuthor = new JLabel("Author:");
		contentPanel.add(lblAuthor, "2, 4, right, center");

		authorTextField = new JTextField();
		contentPanel.add(authorTextField, "4, 4, fill, top");
		authorTextField.setColumns(10);

		JLabel lblDocumentType = new JLabel("Type:");
		contentPanel.add(lblDocumentType, "2, 8, right, center");

		docTypeLabel = new JLabel(result.mimeType());
		contentPanel.add(docTypeLabel, "4, 8, fill, top");

		JLabel lblFile = new JLabel("File:");
		contentPanel.add(lblFile, "2, 10, right, top");

		lblPath = new JLabel(result.path());
		contentPanel.add(lblPath, "4, 10, fill, top");

		tableModel = new DefaultTableModel();

		//load data
		titleTextField.setText(result.title());
		authorTextField.setText(result.author());
		docTypeLabel.setText(result.mimeType());


		if (tableModel.getRowCount() == 0) {
			String [] newTableRowData = {"double click to add a tag..."};
			tableModel.addRow(newTableRowData);
		} else {
			String [] newTableRowData = {""};
			tableModel.addRow(newTableRowData);
		}

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{	
			ActionListener al = e -> {
				if (e.getActionCommand().equals("Create")) {
					DocumentPropertiesUI.this.result.setTitle(titleTextField.getText());
					DocumentPropertiesUI.this.result.setAuthor(authorTextField.getText());			
				} 
				DocumentPropertiesUI.this.dispose();
			};

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton createButton = new JButton("Update");
				createButton.setActionCommand("Create");
				createButton.addActionListener(al);
				buttonPane.add(createButton);
				getRootPane().setDefaultButton(createButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(al);
				buttonPane.add(cancelButton);
			}
		}

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
