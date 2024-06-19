package org.example;

import javax.swing.*;
import java.awt.*;

public class HabitPanel extends JPanel {

    private JTextArea habitList;

    public HabitPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("习惯打卡"));

        habitList = new JTextArea(10, 20);
        JScrollPane habitScrollPane = new JScrollPane(habitList);
        add(habitScrollPane, BorderLayout.CENTER);

        JButton addHabitButton = new JButton("添加");
        add(addHabitButton, BorderLayout.SOUTH);
    }
}