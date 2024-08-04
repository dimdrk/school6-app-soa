package gr.aueb.cf.schoolapp.viewcontroller;


import java.io.Serial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import gr.aueb.cf.schoolapp.Main;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.TeacherDAOIml;
import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.TeacherServiceImp;
import gr.aueb.cf.schoolapp.service.exceptions.TeacherNotFoundException;
import gr.aueb.cf.schoolapp.service.util.DBUtil;
import gr.aueb.cf.schoolapp.validator.TeacherValidator;

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

	// Wiring
	private final ITeacherDAO teacherDAO = new TeacherDAOIml();
	private final ITeacherService teacherService = new TeacherServiceImp(teacherDAO);

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
	private JButton updateBtn;

	public TeachersUpdateDeleteFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Thread.currentThread().getContextClassLoader().getResource("eduv2.png")));
		setTitle("Ενημέρωση / Διαγραφή Εκπαιδευτή");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				lastnameSearchText.setText("");
				buildTable();	// initial rendering
				idText.setText("");
				firstnameText.setText("");
				lastnameText.setText("");
			}
			@Override
			public void windowActivated(WindowEvent e) {
				lastnameSearchText.setText("");
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
		
		updateBtn = new JButton("Ενημέρωση");
		updateBtn.setForeground(new Color(0, 0, 128));
		updateBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, String> errors;
				String firstnameMessage;
				String lastnameMessage;
				Teacher teacher;

				if (idText.getText().trim().isEmpty()) return;

				try {
					// Data Binding
					TeacherUpdateDTO updateDTO = new TeacherUpdateDTO();
					updateDTO.setId(Integer.parseInt(idText.getText().trim()));
					updateDTO.setFirstname(firstnameText.getText().trim());
					updateDTO.setLastname(lastnameText.getText().trim());

					// Validate
					errors = TeacherValidator.validate(updateDTO);

					// If errors assign messages to UI
					if (!errors.isEmpty()) {
						firstnameMessage = errors.getOrDefault("firstname", "");
						lastnameMessage = errors.getOrDefault("lastname", "");
						errorFirstname.setText(firstnameMessage);
						errorLastname.setText(lastnameMessage);

						return;
					}

					// On validation success, call the update service
					teacher = teacherService.updateTeacher(updateDTO);

					// Results mapped to ReadOnly DTO
					TeacherReadOnlyDTO readOnlyDTO = mapToReadOnlyDTO(teacher);

					// Feedback
					JOptionPane.showMessageDialog(
							null,
							"Teacher with id: " + readOnlyDTO.getId() + " was updated.",
							"Update",
							JOptionPane.INFORMATION_MESSAGE
							);
				} catch (TeacherDAOException | TeacherNotFoundException e1) {
					// e1.printStackTrace();
					// On failure, show message
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		updateBtn.setBounds(468, 292, 134, 56);
		contentPane.add(updateBtn);
		
		JButton deleteBtn = new JButton("Διαγραφή");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response;

				try	{
					if (idText.getText().trim().isEmpty()) return;
					int inputId = Integer.parseInt(idText.getText().trim());

					response = JOptionPane.showConfirmDialog(null, "Είστε σίγουρος;", "Warning", JOptionPane.YES_NO_OPTION);
					if (response == JOptionPane.YES_OPTION) {
						teacherService.deleteTeacher(inputId);
						JOptionPane.showMessageDialog(null, "Teacher was deleted successfully", "Delete", JOptionPane.INFORMATION_MESSAGE);
					}

				} catch (TeacherDAOException | TeacherNotFoundException ex) {
					// ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
		List<TeacherReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
		TeacherReadOnlyDTO readOnlyDTO;
		
		try {
			String searchStr = lastnameSearchText.getText().trim();

			List<Teacher> teachers = teacherService.getTeachersByLastname(searchStr);

			for (Teacher teacher : teachers) {
				readOnlyDTO = mapToReadOnlyDTO(teacher);
				readOnlyDTOS.add(readOnlyDTO);
			}

			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}

			for (TeacherReadOnlyDTO teacherReadOnlyDTO : readOnlyDTOS) {
				vector = new Vector<>(3);
				vector.add(String.valueOf(teacherReadOnlyDTO.getId()));
				vector.add(teacherReadOnlyDTO.getFirstname());
				vector.add(teacherReadOnlyDTO.getLastname());
				model.addRow(vector);
			}
		} catch (TeacherDAOException ex) {
			// ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

	private TeacherReadOnlyDTO mapToReadOnlyDTO(Teacher teacher) {
		return new TeacherReadOnlyDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname());
	}
}
