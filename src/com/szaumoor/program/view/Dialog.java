package com.szaumoor.program.view;

import javax.swing.JOptionPane;

/**
 * Utility class to show dialogs easily.
 */
public enum Dialog {
    ;
    /**
     * Shows a popup window informing of the outcome of the program.
     *
     * @param message Message to send
     * @param type type of dialog that should be constructed
     */
    public static void show(final String message, final DialogType type) {
        JOptionPane.showMessageDialog(null, message, type.toString(), type.getDialogType());
    }
}
