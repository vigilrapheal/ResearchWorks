import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.PreparedStatement;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JTextArea;

public class Hiddenframe extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblId;
	private JLabel lblClass;
	private JTextArea textArea;
	private JTextArea textArea_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hiddenframe frame = new Hiddenframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Hiddenframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(-21, 12, 640, 458);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblUrl = new JLabel("URL");
		lblUrl.setBounds(93, 65, 70, 15);
		panel.add(lblUrl);

		textField = new JTextField();
		textField.setBounds(215, 57, 322, 31);
		panel.add(textField);
		textField.setColumns(10);

		lblId = new JLabel("ID");
		lblId.setBounds(148, 142, 70, 15);
		panel.add(lblId);

		lblClass = new JLabel("Class");
		lblClass.setBounds(434, 142, 70, 15);
		panel.add(lblClass);

		textArea = new JTextArea();
		textArea.setBounds(75, 184, 212, 262);
		panel.add(textArea);

		textArea_1 = new JTextArea();
		textArea_1.setBounds(388, 184, 212, 262);
		panel.add(textArea_1);
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				String[] str = {};
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					String rs = dbConnection(textField.getText());

					if (rs.contains("****")) {
						System.out.println(rs);
						rs = rs.replace("****", "\n");
						System.out.println(rs);
						str = rs.split("\n");
						System.out.println(rs);
						String classN = "";
						String id = "";
						for (int i = 0; i < str.length; i++) {
							// System.out.println(str[i]);
							if (str[i].contains("#"))
								id += str[i] + "\n";
							else if (str[i].contains("."))
								classN += str[i] + "\n";
						}
						textArea.setText(id);
						textArea_1.setText(classN);
					}

				}
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					
					textField.setText("");
					textArea.setText("");
					textArea_1.setText("");
				}

			}
		});

	}

	private static String dbConnection(String url) {
		ResultSet rs = null;
		String returnValue = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hiddenData", "root",
					"careersnow@123");
			String sql = "select * from hiddenData.hidden where url=?";
			PreparedStatement pr = (PreparedStatement) con.prepareStatement(sql);
			pr.setString(1, url.trim());
			rs = pr.executeQuery();
			if (rs.next())
				returnValue = rs.getString(4);
			System.out.println(returnValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
}
