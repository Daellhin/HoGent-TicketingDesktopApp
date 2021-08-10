package util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class FxUtil {

	public static void setErrorMessage(Label label, String message) {
		label.setText(message);
		label.setFont(Font.font(12));
		label.setStyle("-fx-text-fill: red");
	}

	public static void clearErrorMessage(Label label) {
		label.setText("");
		label.setFont(Font.font(0));
	}

	public static void setConfirmationMessage(Label label, String message) {
		label.setText(message);
		label.setFont(Font.font(12));
		label.setStyle("-fx-text-fill: green");
	}

	public static void setError(Label label, String error, Node input) {
		setErrorMessage(label, error);
		input.setStyle("-fx-border-color: red");
	}

	public static void clearError(Label label, Node input) {
		clearErrorMessage(label);
		input.setStyle("");
	}

	/**
	 * Source https://stackoverflow.com/a/38139005/8807613
	 * 
	 */
	public static boolean inHierarchy(Node parent, Node child) {
		while (child != null) {
			if (child == parent) {
				return true;
			}
			child = child.getParent();
		}
		return false;
	}

}
