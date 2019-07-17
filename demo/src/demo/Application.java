package demo;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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

    private int numberGuesses; // number guesses before finding for user
    
    private int computerNumberGuesses; // number guesses before finding for computer

    private boolean guessed;
    
    private List<Integer> possibleNumberList = new ArrayList<>(); // list of possible number

    private int computerNumber; // number to the computer tries to guess

    private String computerNumberHistory="";

    private String clueHistory="";

    // UI made with Swing API

    private JTextPane textPane;

    private JTextField textField;

    private JTextField textFieldClue;

    private JTextField textFieldClueLabelGuess;


    private void createNumber() { // method to create the number to find

        do {
            numberToFind = RANDOM.nextInt(9000) + 1000; // between 1000-9999, 4-digits number
            
            computerNumber = RANDOM.nextInt(9000) + 1000; 
           
        } while (hasDuplicates(numberToFind)); // need 4-digits numbers with no duplicates

        System.out.println(numberToFind);  // if we want to see answer show in console
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
        return new int[]{plusValues, minusValues};

    }

    private void playGame() { // method to manage the game play with the build of the UI with Swing

        JFrame frame = new JFrame("Application on CS-TECH by BugraTuragay");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        // add buttons
        JPanel buttonsPanel = new JPanel();
        JLabel inputLabel = new JLabel("Input: ");
        JLabel clueLabel = new JLabel("Clue: ");
        JLabel clueLabelGuess = new JLabel("My Guess: ");

        buttonsPanel.add(inputLabel, BorderLayout.LINE_START);

        textField = new JTextField(15);
        textField.addActionListener(e -> okClick());

        textFieldClue = new JTextField(15);
        textFieldClue.addActionListener(e -> okClick());

        textFieldClueLabelGuess = new JTextField(15);
        textFieldClueLabelGuess.setEditable(false);

        buttonsPanel.add(textField, BorderLayout.LINE_START);
        buttonsPanel.add(clueLabel, BorderLayout.LINE_START);
        buttonsPanel.add(textFieldClue, BorderLayout.LINE_START);
        buttonsPanel.add(clueLabelGuess, BorderLayout.LINE_START);
        buttonsPanel.add(textFieldClueLabelGuess, BorderLayout.LINE_START);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> okClick());
        buttonsPanel.add(okButton, BorderLayout.LINE_START);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> init());

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

        frame.setMinimumSize(new Dimension(800, 600));

        //center JFrame on the screen
        Dimension objDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int X = (objDimension.width - frame.getWidth()) / 2;
        int Y = (objDimension.height - frame.getHeight()) / 2;
        frame.setLocation(X, Y);

        //display the window
        frame.pack();
        frame.setVisible(true);

        init();

    }

    private void updateTextFieldClueLabelGuess(String str) {
        textFieldClueLabelGuess.setText(str);
    }

    //manage user click on OK
    private void okClick() {
        // TODO Auto-generated method stub
    	
        //we get user input or clue input
        String userInput = textField.getText();
        
        String userClueInput = textFieldClue.getText();

        int entered = -1;

        try {

            if (checkUserInputIsClue(userClueInput)) {
            	
                parseUserInput(userClueInput);
                
                clueHistory = userClueInput;
                
                updateClueText("");
                
                return;
            }

            entered = Integer.parseInt(userInput);

        } catch (Exception e) {

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

        } else {

            updateText(entered + "\t\t\t\t" + "+" + plusAndMinus[0] + " and " + "-" + plusAndMinus[1]);
        }

        if (guessed) {

            updateText("\n" + entered + "\n\n You won after " + numberGuesses + " guesses!");
        }

        textField.setText("");

    }

    private void parseUserInput(String userInput) {  // take clue from user

        int indexOfPlus = userInput.indexOf('+');
        
        int indexOfMinus = userInput.indexOf('-');

        String plusClue = userInput.substring(indexOfPlus + 1, indexOfMinus);
        
        String minusClue = userInput.substring(indexOfMinus + 1);

        guessUserNumber(Integer.parseInt(plusClue), Integer.parseInt(minusClue), userInput);

    }


    private void guessUserNumber(int plusClue, int minusClue, String userInput) { // method to try find user number

    	if (plusClue == 4 ) {
        	
        	updateText("\n\n I won after " + computerNumberGuesses + " guesses!");
        	
        }
    	
    	else if (plusClue == 0 && minusClue == 0) {
        	
            if(clueHistory.equals(userInput)){
            	
                JOptionPane.showMessageDialog(null, "No possible answer fits the scores you gave!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                
                return;
            }
            removeFromPossibleNumbers(computerNumber, false, false);

        }else if(minusClue ==0 && plusClue!=0){

            removeFromPossibleNumbers(computerNumber, false, true);

        }else if(minusClue!=0&& plusClue==0){

            removeFromPossibleNumbers(computerNumber, true, false);
        }
        else if (minusClue != 0 && plusClue != 0) {

            removeFromPossibleNumbers(computerNumber, true, false);
            
            removeFromPossibleNumbers(computerNumber, false, true);
        }
        

        computerNumber = possibleNumberList.get(new Random().nextInt(possibleNumberList.size() - 1));
        
        updateTextFieldClueLabelGuess(String.valueOf(computerNumber));

    }


    private void removeFromPossibleNumbers(int computerNumber, boolean computeForMinus, boolean computeForPlus) { // elimination numbers from the list

        ArrayList<Integer> toBeRemovedList = new ArrayList<>();

        ArrayList<Integer> toBeStayedList = new ArrayList<>();


        for (int i = 0; i < 4; i++) {
        	
            int last = computerNumber % 10;
            
            for (Integer num : possibleNumberList) {
            	
                if (String.valueOf(num).contains(last + "")) {
                	
                    toBeRemovedList.add(num);
                }

                if (computeForPlus && getDigit(last, i, num)) {
                	
                    toBeStayedList.add(num);
                }
            }
            computerNumber = computerNumber / 10;
        }

        if (!computeForMinus && !computeForPlus) {
        	
            possibleNumberList.removeAll(toBeRemovedList);
        }


        if (computeForMinus && !computeForPlus) {
        	
            possibleNumberList = toBeRemovedList;
        }


        if (!computeForMinus && computeForPlus) {
        	
            possibleNumberList = toBeStayedList;
        }


    }

    private boolean getDigit(int compNumberLast, int index, int number) {

        char c = String.valueOf(number).charAt(Math.abs(index - 3));
        
        return String.valueOf(c).equals(String.valueOf(compNumberLast));

    }

    private boolean checkUserInputIsClue(String userInput) {
    	
        if (userInput.startsWith("+")) {
            return true;
        }
        return false;

    }

    private void updateClueText(String message) {

        textFieldClue.setText(message);
    }

    private void updateText(String message) {

        textPane.setText(textPane.getText() + "\n" + message);
    }

    private void init() {

        createNumber();
        
        numberGuesses = 0;
        
        computerNumberGuesses = 0;
        
        guessed = false;
        
        textPane.setText("You must guess a 4-digits number with no duplicate digits or \n Clue (in the form of like '+2-2')");
        
        textField.setText("");
        
        fillPossibleNumber();

        while (hasDuplicates(computerNumber)) {
        	
            computerNumber = RANDOM.nextInt(9000) + 1000;
        }

        updateTextFieldClueLabelGuess(String.valueOf(computerNumber));
        
        computerNumberGuesses++;
    }

    private void fillPossibleNumber() {

        for (int i = 1234; i <= 9876; i++) {
        	
            if (!hasDuplicates(i)) {
            	
                possibleNumberList.add(i);
            }
        }
    }

    public static void main(String[] args) {

        Application plusAndMinus = new Application();

        SwingUtilities.invokeLater(() -> plusAndMinus.playGame());
    }

}