package gr.aueb.cf.schoolapp.viewcontroller;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gr.aueb.cf.schoolapp.Main;
import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.TeacherDAOIml;
import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.TeacherServiceImp;
import gr.aueb.cf.schoolapp.service.util.DBUtil;
import gr.aueb.cf.schoolapp.validator.TeacherValidator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class TeachersInsertFrame extends JFrame {

	// Wiring
	private final ITeacherDAO teacherDAO = new TeacherDAOIml();
	private final ITeacherService teacherService = new TeacherServiceImp(teacherDAO);

	@Serial
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField firstnameText;
	private JTextField lastnameText;
	private JLabel errorFirstname;
	private JLabel errorLastname;

	
	public TeachersInsertFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				firstnameText.setText("");
				lastnameText.setText("");
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(Thread.currentThread().getContextClassLoader().getResource("eduv2.png")));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 336);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(28, 37, 368, 199);
		contentPane.add(panel);
		panel.setLayout(null);
		
		errorFirstname = new JLabel("");
		errorFirstname.setForeground(new Color(255, 0, 0));
		errorFirstname.setBounds(91, 83, 243, 26);
		panel.add(errorFirstname);
		
		errorLastname = new JLabel("");
		errorLastname.setForeground(Color.RED);
		errorLastname.setBounds(91, 155, 243, 26);
		panel.add(errorLastname);
		
		JLabel firstnameLabel = new JLabel("Όνομα");
		firstnameLabel.setBounds(39, 46, 41, 26);
		panel.add(firstnameLabel);
		firstnameLabel.setForeground(new Color(0, 0, 255));
		firstnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		firstnameText = new JTextField();
		firstnameText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String inputFirstname;
				inputFirstname = firstnameText.getText().trim();
				
				if (inputFirstname.equals("")) {
					errorFirstname.setText("Το όνομα είναι υποχρεώτικό");
				};
				
				if (!inputFirstname.equals("")) {
					errorFirstname.setText("");
				};
			}
		});
		firstnameText.setBounds(91, 47, 243, 26);
		panel.add(firstnameText);
		firstnameText.setFont(new Font("Tahoma", Font.PLAIN, 12));
		firstnameText.setColumns(10);
		
		lastnameText = new JTextField();
		lastnameText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String inputLastname;
				inputLastname = lastnameText.getText().trim();
				
				if (inputLastname.equals("")) {
					errorLastname.setText("Το επώνυμο είναι υποχρεώτικό");
				};
				
				if (!inputLastname.equals("")) {
					errorLastname.setText("");
				};				
			}
		});
		lastnameText.setBounds(91, 119, 243, 26);
		panel.add(lastnameText);
		lastnameText.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lastnameText.setColumns(10);
		
		JLabel lastnameLabel = new JLabel("Επώνυμο");
		lastnameLabel.setBounds(28, 118, 52, 26);
		panel.add(lastnameLabel);
		lastnameLabel.setForeground(Color.BLUE);
		lastnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton insertBtn = new JButton("Εισαγωγή");
		insertBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Map<String, String > errors;
				TeacherInsertDTO insertDTO = new TeacherInsertDTO();
				String firstnameMessage;
				String lastnameMessage;
				Teacher teacher;

				try	{
					// Data binding
					insertDTO.setFirstname(firstnameText.getText().trim());
					insertDTO.setLastname(lastnameText.getText().trim());

					// Validation
					errors = TeacherValidator.validate(insertDTO);

					if (!errors.isEmpty()) {
						firstnameMessage = errors.getOrDefault("firstname", "");
						lastnameMessage = errors.getOrDefault("lastname", "");
						// lastnameMessage = errors.containsKey("lastname") ? errors.get("lastname") : "";

						errorFirstname.setText(firstnameMessage);
//						if (!firstnameMessage.isEmpty()) {
//							errorFirstname.setText(firstnameMessage);
//						}
//
//						if (firstnameMessage.isEmpty()) {
//							errorFirstname.setText("");
//						}

						if (!lastnameMessage.isEmpty()) {
							errorLastname.setText(lastnameMessage);
						}

						if (lastnameMessage.isEmpty()) {
							errorLastname.setText("");
						}

						return;
					}

					teacher = teacherService.insertTeacher(insertDTO);
					TeacherReadOnlyDTO readOnlyDTO = mapToReadOnly(teacher);

					JOptionPane.showMessageDialog(
							null,
							"Teacher with lastname: " + readOnlyDTO.getLastname(),
							"Insert Teacher", JOptionPane.INFORMATION_MESSAGE
					);

				} catch (TeacherDAOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(
							null,
							"Insertion",
							"Error",
							JOptionPane.ERROR_MESSAGE
					);
				}
			}
		});
		insertBtn.setForeground(new Color(0, 0, 255));
		insertBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insertBtn.setBounds(190, 246, 97, 32);
		contentPane.add(insertBtn);
		
		JButton closeBtn = new JButton("Κλείσιμο");
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getTeachersMenuFrame().setEnabled(true);
				Main.getTeachersInsertFrame().setVisible(false);
			}
		});
		closeBtn.setForeground(Color.BLUE);
		closeBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBtn.setBounds(297, 246, 97, 32);
		contentPane.add(closeBtn);
	}

	private TeacherReadOnlyDTO mapToReadOnly(Teacher teacher) {
		return new TeacherReadOnlyDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname());
	}
}
