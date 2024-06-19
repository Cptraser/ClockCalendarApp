package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class ClockPanel extends JPanel {

    private JLabel clockLabel;
    private JLabel dateLabel;
    private Timer clockTimer;

    public ClockPanel() {
        setLayout(new GridLayout(2, 1));
        setBorder(BorderFactory.createTitledBorder("时间和日期"));

        clockLabel = new JLabel("", SwingConstants.CENTER);
        dateLabel = new JLabel("", SwingConstants.CENTER);

        // 设置字体大小
        clockLabel.setFont(new Font("Serif", Font.BOLD, 36));
        dateLabel.setFont(new Font("Serif", Font.BOLD, 24));

        add(clockLabel);
        add(dateLabel);

        startClock();
    }

    private void startClock() {
        clockTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar now = Calendar.getInstance();
                clockLabel.setText(String.format("%1$tH:%1$tM:%1$tS", now));
                dateLabel.setText(String.format("%1$tY年 %1$tm月 %1$td日 %1$tA", now));
            }
        });
        clockTimer.start();
    }
}