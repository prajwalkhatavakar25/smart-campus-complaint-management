package com.smartcampus.cms;

import javax.swing.SwingUtilities;

import com.smartcampus.cms.ui.LoginFrame;

public class GuiMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}