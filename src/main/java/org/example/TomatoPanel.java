package org.example;

import javax.swing.*;
import java.awt.*;

public class TomatoPanel extends JPanel {

    private JTextArea tomatoCount;

    public TomatoPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("番茄钟"));

        tomatoCount = new JTextArea(5, 20);
        JScrollPane tomatoScrollPane = new JScrollPane(tomatoCount);
        add(tomatoScrollPane, BorderLayout.CENTER);

        JPanel tomatoControlPanel = new JPanel();
        JTextField tomatoTimeField = new JTextField(5);
        JButton startTomatoButton = new JButton("开始");
        tomatoControlPanel.add(new JLabel("时间（分钟）："));
        tomatoControlPanel.add(tomatoTimeField);
        tomatoControlPanel.add(startTomatoButton);
        add(tomatoControlPanel, BorderLayout.SOUTH);
    }
}