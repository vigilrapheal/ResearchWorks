/**
 * 
 */
package com.hirrr.production.runninganalytics;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;


import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.UIManager;

import com.hirrr.dbconnection.DataBaseConnectivity;

import javax.swing.JRadioButton;

/**
 *
 * @author Padmajith
 */
public class UIGetParameterFromDBWhenCompanyCodePassedAsArgument {

	private JFrame frame;
	private String companyCode;
	private String pElement;
	private String formattedPElement;
	private JTextField companyCodeEnteringField;
	private final String pElementFetchingSQL = "SELECT pElement,company_name FROM scrapperdb.n_scrapper_info where company_id = ?";
	private final String urlFetchingSQL = "SELECT url FROM scrapperdb.n_scrapper_info where company_id = ?";
	private String fetchingQuery = "";

	private void formatThePElement() {

		formattedPElement = "";
		formattedPElement = pElement.replace("document", "").replace(".select", "").replace(";", "")
				.replaceAll("\\(", "").replaceAll("\\)", "  ").replaceAll("\"", "").trim();

	}

	private void fetchThePElement() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String companyname="";

		con = DataBaseConnectivity.getConnected("22", "scrapperdb");

		try {

			ps = con.prepareStatement(fetchingQuery);
			ps.setString(1, companyCode);
			rs = ps.executeQuery();
			if (rs.next())
			{
				pElement = rs.getString(1);
				companyname=rs.getString(2);
			}
			formatThePElement();

		} catch (Exception e) {
			e.printStackTrace();
		}

		DataBaseConnectivity.closingTheResultSet(rs);
		DataBaseConnectivity.closingThePreparedStatement(ps);
		DataBaseConnectivity.closingTheConnection(con);

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIGetParameterFromDBWhenCompanyCodePassedAsArgument window = new UIGetParameterFromDBWhenCompanyCodePassedAsArgument();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIGetParameterFromDBWhenCompanyCodePassedAsArgument() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 390);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		companyCodeEnteringField = new JTextField();
		companyCodeEnteringField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 12));
		companyCodeEnteringField.setBounds(12, 57, 426, 41);
		frame.getContentPane().add(companyCodeEnteringField);
		companyCodeEnteringField.setColumns(10);

		JLabel lblProvideTheCompany = new JLabel("Provide the Company Code");
		lblProvideTheCompany.setBounds(12, 26, 234, 31);
		frame.getContentPane().add(lblProvideTheCompany);

		JButton getButton = new JButton("GET");
		getButton.setBounds(309, 110, 117, 35);
		frame.getContentPane().add(getButton);

		JTextArea areaToPrintTheFormattedPElement = new JTextArea();
		areaToPrintTheFormattedPElement.setFont(UIManager.getFont("Label.font"));
		areaToPrintTheFormattedPElement.setBounds(12, 213, 426, 114);
		frame.getContentPane().add(areaToPrintTheFormattedPElement);

		JButton exitButton = new JButton("EXIT");
		exitButton.setBounds(321, 339, 117, 31);
		frame.getContentPane().add(exitButton);
		
		JRadioButton urlButton = new JRadioButton("URL");
		urlButton.setBounds(104, 182, 58, 23);
		frame.getContentPane().add(urlButton);
		
		JRadioButton parentElementButton = new JRadioButton("PARENT ELEMENT");
		parentElementButton.setBounds(166, 182, 149, 23);
		frame.getContentPane().add(parentElementButton);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(urlButton);
		buttonGroup.add(parentElementButton);
		
		urlButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange() ==  ItemEvent.SELECTED)
					fetchingQuery = urlFetchingSQL;	
			}
		});
		
		parentElementButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() ==  ItemEvent.SELECTED)
					fetchingQuery = pElementFetchingSQL;	
				
			}
		});
		
		getButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				companyCode = companyCodeEnteringField.getText().trim();
				areaToPrintTheFormattedPElement.setText("");
				fetchThePElement();
				areaToPrintTheFormattedPElement.setText(formattedPElement);
				
			}
		});

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}
}