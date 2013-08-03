package game.utility;

import javax.swing.*;
import static javax.swing.JOptionPane.*;

public class Dialog {
   private Dialog() {}

   /**
    * Asks the user to input a string with the default message.
    * @return a non-empty string.
    */
   public static String inputString() {
      return inputString("Enter a sequence of characters: ");
   }

   /**
    * Asks the user to input a string.
    * @param message the desired message to be displayed.
    * @return a non-empty string.
    */
   public static String inputString(String message) {
      while (true) {
         try {
            String currentString = showInputDialog(null, message,
                   "Input", QUESTION_MESSAGE);
            if (!currentString.isEmpty()) {
               return currentString;
            }
            errorMessage("Your input was invalid!");
         }
         catch (NullPointerException ex) {
            return null;
         }
      }
   }

   /**
    * Asks the user to input a string with a maximum length.
    * @param message the desired message to be displayed.
    * @param maxLength the maximum number of characters in the string.
    * @return a string with length less than or equal to the maxLength.
    */
   public static String inputString(String message, int maxLength) {
      while (true) {
         String currentString = inputString(message + " (" + maxLength
                + " letters max)");

         if (currentString == null)
            return null;
         else if (currentString.length() <= maxLength) {
            return currentString;
         }
         else
            errorMessage("Length exceeded " + maxLength + " letters!");
      }
   }

   /**
    * Prompts the user with a yes / no option.
    * @param message the message to be displayed.
    * @return true if yes, false if no.
    */
   public static boolean confirmYesNo(String message) {
      return showConfirmDialog(null, message, "Confirm", YES_NO_OPTION, WARNING_MESSAGE) == YES_OPTION;
   }

   /**
    * Prompts the user with a yes / no option.
    * @param message the message to be displayed.
    * @param icon the icon to be displayed.
    * @return true if yes, false if no.
    */
   public static boolean confirmYesNo(String message, Icon icon) {
      return showConfirmDialog(null, message, "Confirm", YES_NO_OPTION, PLAIN_MESSAGE, icon) == YES_OPTION;
   }

   /**
    * Displays a plain message dialog.
    * @param message the message to be displayed.
    */
   public static void message(String message) {
      showMessageDialog(null, message, "Message", PLAIN_MESSAGE);
   }

   /**
    * Displays a plain message dialog.
    * @param message the message to be displayed.
    * @param icon the icon to be displayed.
    */
   public static void message(String message, ImageIcon icon) {
      showMessageDialog(null, message, "Message", PLAIN_MESSAGE, icon);
   }

   /**
    * Displays an information dialog.
    * @param message the message to be displayed.
    */
   public static void infoMessage(String message) {
      showMessageDialog(null, message, "Info", INFORMATION_MESSAGE);
   }

   /**
    * Displays an error dialog.
    * @param message the message to be displayed.
    */
   public static void errorMessage(String message) {
      showMessageDialog(null, message, "Error", ERROR_MESSAGE);
   }
}
