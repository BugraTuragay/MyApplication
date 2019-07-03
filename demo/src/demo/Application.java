package demo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Application {

	public static final Random RANDOM = new Random();

	private int numberToFind; // number to find

	private int numberGuesses; // number guesses before finding

	private boolean guessed;

	// UI made with Swing API

	private JTextPane textPane;

	private JTextField textField;

	private void createNumber() { // method to create the number to find

		do {
			numberToFind = RANDOM.nextInt(9000) + 1000; // between 1000-9999, 4-digits number
		} while (hasDuplicates(numberToFind)); // need 4-digits numbers with no duplicates

		// System.out.println(numberToFind);  // if we want to see answer show in console
	}

	private boolean hasDuplicates(int number) {

		boolean[] digits = new boolean[10];

		while (number > 0) {
			int last = number % 10;

			if (digits[last])
				return true;

			digits[last] = true;
			number = number / 10;
		}
		return false;
	}

	private int[] plusAndMinus(int entered) { // method to create plus and minus values for the entered number comparing
												// to the number to guess

		int plusValues = 0; // plusValues: good digits well placed
		int minusValues = 0; // minusValues: good digits bad placed

		String enteredStr = String.valueOf(entered); // string conversion
		String numberStr = String.valueOf(numberToFind);

		for (int i = 0; i < numberStr.length(); i++) {

			char a = enteredStr.charAt(i);

			if (a == numberStr.charAt(i)) {
				plusValues++;
			} else if (numberStr.contains(String.valueOf(a))) {
				minusValues++;
			}
		}
		return new int[] { plusValues, minusValues };

	}

	private void playGame() { // method to manage the game play with the build of the UI with Swing

		JFrame frame = new JFrame("Application on CS-TECH by BugraTuragay");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();

		// add buttons
		JPanel buttonsPanel = new JPanel();
		JLabel inputLabel = new JLabel("Input: ");
		buttonsPanel.add(inputLabel, BorderLayout.LINE_START);

		textField = new JTextField(15);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okClick();
			}
		});

		buttonsPanel.add(textField, BorderLayout.LINE_START);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okClick();
			}
		});
		
		buttonsPanel.add(okButton, BorderLayout.LINE_START);
		
		JButton newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				init();
			}
		});
		
		buttonsPanel.add(newGameButton, BorderLayout.LINE_END);
		contentPane.add(buttonsPanel, BorderLayout.PAGE_END);

		//add a text area for display user's numbers and other messages
		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		
		//customize rendering in the JTextPane
		SimpleAttributeSet bSet = new SimpleAttributeSet();
		StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontSize(bSet, 20);
		StyledDocument doc = textPane.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), bSet, false);
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		frame.setMinimumSize(new Dimension(800,600));
		
		//center JFrame on the screen		
		Dimension objDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int X = (objDimension.width - frame.getWidth()) / 2;
		int Y = (objDimension.height - frame.getHeight()) / 2;
		frame.setLocation(X, Y);
		
		//display the wiindow
		frame.pack();
		frame.setVisible(true);
		
		init();
			
	}
    //manage user click on OK
	private void okClick() {
		// TODO Auto-generated method stub
		//we get user input
		String userInput = textField.getText();
		int entered = -1;
		
		try {
			
			entered = Integer.parseInt(userInput);
			
		}catch (Exception e) {
			
			textField.setText("");
			JOptionPane.showMessageDialog(null, "You must enter an integer", "ERROR!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (hasDuplicates(entered) || entered < 1000 || entered > 9999) {
			
			textField.setText("");
			JOptionPane.showMessageDialog(null, "You must enter an integer with no duplicates digits and with 4-digits", "ERROR!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		numberGuesses++;
		
		int[] plusAndMinus = plusAndMinus(entered); // count plus and minus values
		
		if (plusAndMinus[0] == 4) {
			
			guessed = true;
		
		}else {
			
			updateText (entered + "\t\t\t\t" + "+" + plusAndMinus[0] + " and " + "-" + plusAndMinus[1] );
		}
		
		if (guessed) {
			
			updateText("\n" + entered + "\n\n You won after " + numberGuesses + " guesses!");
		}
		
		textField.setText("");
	
	}
	
	private void updateText(String message) {
		
		textPane.setText(textPane.getText() + "\n" + message);
	}
	
	private void init() {
		
		createNumber();
		numberGuesses = 0;
		guessed = false;
		textPane.setText("You must guess a 4-digits number with no duplicate digits");
		textField.setText("");
	}
	
	public static void main(String[] args) {
		
		Application plusAndMinus = new Application();
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				plusAndMinus.playGame();
			}
		});
	}

}
