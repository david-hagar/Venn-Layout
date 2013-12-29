package com.purediscovery.vennlayout.ui;

import com.purediscovery.vennlayout.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 */


public class MainViewWindow extends JFrame {

    public MainViewWindow() throws HeadlessException, VennException {

        ArrayList<Set> setList = new ArrayList<Set>();
        for (int i = 0; i < 4; i++) {
            setList.add(new Set(((char)('A' + i)) + ""));
        }

        SetMeasure measure = new ProportionalMeasure(setList);
        GALayout gaLayout = new GALayout(setList, measure);
        MainView renderPanel = new MainView(gaLayout);

        this.getContentPane().add(renderPanel, BorderLayout.CENTER);
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth()) - 200;
        int ySize = ((int) tk.getScreenSize().getHeight()) - 200;
        this.setSize(xSize, ySize);
        //this.pack();
        this.setLocationRelativeTo(getRootPane());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String args[]) {
        try {
            MainViewWindow c = new MainViewWindow();
            c.setVisible(true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
