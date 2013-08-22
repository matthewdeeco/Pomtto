package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HelpPanel extends JPanel {

	private GameWindow gameWindow;

	private JButton backButton, nextButton;
	private JTextArea textArea;
	private String[] sentences;
	private int nextSentence;
	
	public HelpPanel(GameWindow gameWindow) {
		this.gameWindow = gameWindow;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel buttonPanel = new JPanel(new BorderLayout());
		backButton = new JButton("Back to Menu");
		backButton.addActionListener(new BackListener());
		buttonPanel.add(backButton, BorderLayout.LINE_START);

		nextButton = new JButton("Next");
		nextButton.addActionListener(new NextListener());
		nextButton.setAlignmentX(RIGHT_ALIGNMENT);
		buttonPanel.add(nextButton, BorderLayout.LINE_END);
		
		add(buttonPanel);
		
		textArea = createTextArea();
		
		sentences = new String[8];
		sentences[0] = " A puzzle game where you control the poms falling from the top, as the poms gradually rise up from below.";
		sentences[1] = " There are 4 pom colors, and a special \"Shining\" pom.";
		sentences[2] = " Press Up to exhange the position.";
		sentences[3] = " As time passes, poms below rise up.";
		sentences[4] = " Chaining 3 poms of the same color (excluding Shining poms) makes them vanish. In this case, the score is 1 CP (Combo Points) per pom in the chain. Time your combos to get bonus CP.";
		sentences[5] = " If the chain is adjacent to a Shining pom, it will be linked at the same time. In this case, the score is 100 CP per pom in the chain.";
		sentences[6] = " If you build up more than 100 CP, you can release 1 of 2 special powers.\n   Press Q to Defend, reducing the number of poms on your field.\n   Press W to Attack, increasing the number of poms on your opponent's field.";
		sentences[7] = " The more CP built up, the stronger the power will be. When a pom reaches the top of the middle line, the game is finished.";
		
		textArea.setText(sentences[0]);
		nextSentence = 1;
	}
	
	private JTextArea createTextArea() {
		JTextArea textArea = new JTextArea(5, 25);
		textArea.setEditable(false);
		textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
		textArea.setOpaque(false);
		textArea.setCaretPosition(0);
		textArea.setCursor(null);
		textArea.setFocusable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(335, 437));
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		verticalScrollBar.setUnitIncrement(17);

		add(scrollPane);
		return textArea;
	}

	class BackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AudioHandler.playClickEffect();
			gameWindow.showMainMenu();
		}
	}
	
	class NextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setText(textArea.getText() + "\n\n" + sentences[nextSentence]);
			if (++nextSentence >= sentences.length)
				nextButton.setVisible(false);
		}
		
	}
}
