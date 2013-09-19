package game;

import game.audio.AudioHandler;
import game.utility.ImageFactory;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/** Dialog prompting the user to choose his/her avatar. */
public class AvatarChoiceDialog extends JDialog implements ActionListener
{
   private static final int AVATAR_COUNT = 35;
   private int selectedAvatar;
   private ArrayList<JButton> avatarButtons;

   public AvatarChoiceDialog() {
      avatarButtons = new ArrayList<JButton>();

      setTitle("Choose your avatar");
      setModal(true);
      setResizable(false);
      setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

      setLayout(new GridLayout(5, 7));
      addAvatarButtons();
      pack();
      setLocationRelativeTo(null);

      setVisible(true);
   }

   private void addAvatarButtons() {
      for (int i = 1; i <= AVATAR_COUNT; i++)
         addAvatarButton(i);
   }

   private void addAvatarButton(int avatarIndex) {
      JButton avatarButton = createAvatarButton(avatarIndex);
      add(avatarButton);
      avatarButtons.add(avatarButton);
   }

   private JButton createAvatarButton(int avatarIndex) {
      final int BUTTON_LENGTH = 66;
      final int BUTTON_WIDTH = 66;

      final Dimension BUTTON_DIMENSION =
             new Dimension(BUTTON_LENGTH, BUTTON_WIDTH);

      JButton avatarButton = new JButton(ImageFactory.createAvatarImage("selection", avatarIndex));
      avatarButton.addActionListener(this);
      avatarButton.setPreferredSize(BUTTON_DIMENSION);

      return avatarButton;
   }

   public int getSelectedAvatar() {
	  // return 1 + (int)(Math.random() * AVATAR_COUNT);
      return selectedAvatar;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JButton avatarButtonPressed = (JButton) e.getSource();
      selectedAvatar = avatarButtons.indexOf(avatarButtonPressed) + 1;
      AudioHandler.playClickEffect();
      dispose();
   }
}
