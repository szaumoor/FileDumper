package com.szaumoor.program.view;

import javax.swing.JOptionPane;

/**
 * Defines what a dialog type will be for Swing.
 * Exists only to avoid error-prone use of magic constants and improve clarity.
 */
public enum DialogType {
    ERROR(JOptionPane.ERROR_MESSAGE),
    INFO (JOptionPane.INFORMATION_MESSAGE),
    WARNING(JOptionPane.WARNING_MESSAGE);

    private final int code;

    DialogType(int code){
        this.code = code;
    }

    public int getDialogType() {
        return code;
    }

    /**
     * Returns by default the name of the constant with the first letter upper-cased.
     *
     * @return The resulting string.
     */
    @Override
    public String toString() {
        final String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
