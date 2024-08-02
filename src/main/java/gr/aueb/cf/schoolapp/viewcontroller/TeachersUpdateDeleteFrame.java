package gr.aueb.cf.schoolapp.viewcontroller;


import java.io.Serial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import gr.aueb.cf.schoolapp.Main;
import gr.aueb.cf.schoolapp.service.util.DBUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Toolkit;

public class TeachersUpdateDeleteFrame extends JFrame {

	@Serial
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable teachersTable;
	private DefaultTableModel model = new DefaultTableModel();
	private JTextField lastnameSearchText;
	private JLabel idLabel;
	private JTextField idText;
	private JLabel firstnameLabel;
	private JTextField firstnameText;
	private JLabel lastnameLabel;
	private JTextField lastnameText;
	private JLabel errorFirstname;
	private JLabel errorLastname;

	public TeachersUpdateDeleteFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Thread.currentThread().getContextClassLoader().getResource("eduv2.png")));
		setTitle("Ενημέρωση / Διαγραφή Εκπαιδευτή");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				buildTable();	// initial rendering
				idText.setText("");
				firstnameText.setText("");
				lastnameText.setText("");
			}
			@Override
			public void windowActivated(WindowEvent e) {
				buildTable();	// refresh after update / delete
				idText.setText("");
				firstnameText.setText("");
				lastnameText.setText("");
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 821, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		teachersTable = new JTable();
		teachersTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				idText.setText((String) model.getValueAt(teachersTable.getSelectedRow(), 0));
				firstnameText.setText((String) model.getValueAt(teachersTable.getSelectedRow(), 1));
				lastnameText.setText((String) model.getValueAt(teachersTable.getSelectedRow(), 2));
			}
		});
		teachersTable.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {"Κωδικός", "Όνομα", "Επώνυμο"}
		));
		
		model = (DefaultTableModel) teachersTable.getModel();
		
		teachersTable.setBounds(33, 97, 313, 320);
		contentPane.add(teachersTable);
		
		JScrollPane scrollPane = new JScrollPane(teachersTable);
		scrollPane.setBounds(33, 66, 371, 392);
		contentPane.add(scrollPane);
		
		JLabel lastnameSearchLabel = new JLabel("Επώνυμο");
		lastnameSearchLabel.setForeground(new Color(189, 0, 0));
		lastnameSearchLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lastnameSearchLabel.setBounds(33, 25, 56, 25);
		contentPane.add(lastnameSearchLabel);
		
		lastnameSearchText = new JTextField();
		lastnameSearchText.setBounds(99, 25, 199, 25);
		contentPane.add(lastnameSearchText);
		lastnameSearchText.setColumns(10);
		
		JButton btnSearch = new JButton("Αναζήτηση");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildTable();
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearch.setForeground(new Color(0, 0, 128));
		btnSearch.setBounds(319, 27, 100, 21);
		contentPane.add(btnSearch);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(424, 66, 339, 176);
		contentPane.add(panel);
		panel.setLayout(null);
		
		idLabel = new JLabel("Κωδικός");
		idLabel.setBounds(35, 23, 45, 13);
		panel.add(idLabel);
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		idLabel.setForeground(new Color(0, 0, 128));
		
		idText = new JTextField();
		idText.setBounds(92, 20, 64, 19);
		panel.add(idText);
		idText.setEditable(false);
		idText.setColumns(10);
		
		firstnameText = new JTextField();
		firstnameText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String inputFirstname;
				inputFirstname = firstnameText.getText().trim();
				validateFirstname(inputFirstname);				
			}
		});
		firstnameText.setBounds(92, 46, 170, 19);
		panel.add(firstnameText);
		firstnameText.setColumns(10);
		
		firstnameLabel = new JLabel("Όνομα");
		firstnameLabel.setBounds(45, 49, 36, 13);
		panel.add(firstnameLabel);
		firstnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		firstnameLabel.setForeground(new Color(0, 0, 128));
		
		lastnameLabel = new JLabel("Επώνυμο");
		lastnameLabel.setBounds(26, 110, 55, 13);
		panel.add(lastnameLabel);
		lastnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lastnameLabel.setForeground(new Color(0, 0, 128));
		
		lastnameText = new JTextField();
		lastnameText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String inputLastname;
				inputLastname = lastnameText.getText().trim();
				validateLastname(inputLastname);
			}
		});
		lastnameText.setBounds(92, 107, 170, 19);
		panel.add(lastnameText);
		lastnameText.setColumns(10);
		
		errorLastname = new JLabel("");
		errorLastname.setForeground(new Color(255, 0, 0));
		errorLastname.setBounds(92, 136, 201, 19);
		panel.add(errorLastname);
		
		errorFirstname = new JLabel("");
		errorFirstname.setForeground(new Color(255, 0, 0));
		errorFirstname.setBounds(92, 75, 170, 19);
		panel.add(errorFirstname);
		
		JButton updateBtn = new JButton("Ενημέρωση");
		updateBtn.setForeground(new Color(0, 0, 128));
		updateBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Data Binding
				int inputId = Integer.parseInt(idText.getText().trim());
				String inputFirstname = firstnameText.getText().trim();
				String inputLastname = lastnameText.getText().trim();
				
				// Validation
				validateFirstname(inputFirstname);
				validateLastname(inputLastname);
				
				if (inputFirstname.isEmpty() || inputLastname.isEmpty()) {
					return;
				}
				
				String sql = "UPDATE teachers SET firstname = ?, lastname = ? WHERE id = ?";
				try (Connection conn = DBUtil.getConnection();
						PreparedStatement ps = conn.prepareStatement(sql);) {
					
					ps.setString(1, inputFirstname);
					ps.setString(2, inputLastname);
					ps.setInt(3, inputId);
					
					int answer = JOptionPane.showConfirmDialog(null, "Είστε σίγουρη/ος", "Ενημέρωση", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						int rowsAffected = ps.executeUpdate();
						JOptionPane.showMessageDialog(null, rowsAffected + " γραμμές ενημερώθηκαν", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
					} else {
						return;
					}
				} catch (SQLException e1) {
					
					// e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Insertion error", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		updateBtn.setBounds(468, 292, 134, 56);
		contentPane.add(updateBtn);
		
		JButton deleteBtn = new JButton("Διαγραφή");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "DELETE FROM teachers WHERE id = ?";
				
				try (Connection conn = DBUtil.getConnection();
						PreparedStatement ps = conn.prepareStatement(sql);) {
					
					int inputId = Integer.parseInt(idText.getText().trim());					
					ps.setInt(1, inputId);
					
					int answer = JOptionPane.showConfirmDialog(null, "Είστε σίγουρη/ος", "Διαγραγή", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						int rowsAffected = ps.executeUpdate();
						JOptionPane.showMessageDialog(null, rowsAffected + " γραμμές διαγράφηκαν", "Διαγραγή", JOptionPane.INFORMATION_MESSAGE);
					} else {
						return;
					}
					
				} catch (SQLException ex) {
					// ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Insertion error", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		deleteBtn.setForeground(new Color(0, 0, 128));
		deleteBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		deleteBtn.setBounds(612, 292, 134, 56);
		contentPane.add(deleteBtn);
		
		JButton closeBtn = new JButton("Κλείσιμο");
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getTeachersMenuFrame().setEnabled(true);
				Main.getTeachersUpdateDeleteframe().setVisible(false);
			}
		});
		closeBtn.setForeground(new Color(0, 0, 128));
		closeBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		closeBtn.setBounds(629, 402, 134, 56);
		contentPane.add(closeBtn);
	}
	
	private void buildTable() {
		
		Vector<String> vector;
		
		String sql = "SELECT id, firstname, lastname FROM teachers WHERE lastname LIKE ?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);) {


			ps.setString(1,  lastnameSearchText.getText().trim() + "%");
			
			ResultSet rs = ps.executeQuery();
			
			// Clear model -> clear table - MVVM
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
			
			while (rs.next()) {
				vector = new Vector<>(3);
				vector.add(rs.getString("id"));
				vector.add(rs.getString("firstname"));
				vector.add(rs.getString("lastname"));
				
				model.addRow(vector);
			}			
		} catch (SQLException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Insertion error", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void validateFirstname(String inputFirstname) {
		if (inputFirstname.equals("")) {
			errorFirstname.setText("Το όνομα είναι υποχρεώτικό");
		};
		
		if (!inputFirstname.equals("")) {
			errorFirstname.setText("");
		};
	}
	
	private void validateLastname(String inputLastname) {
		if (inputLastname.equals("")) {
			errorLastname.setText("Το επώνυμο είναι υποχρεώτικό");
		};
		
		if (!inputLastname.equals("")) {
			errorLastname.setText("");
		};
	}
}
