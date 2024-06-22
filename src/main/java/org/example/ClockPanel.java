package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.TimeZone;

public class ClockPanel extends JPanel {

    private JLabel clockLabel;
    private JLabel dateLabel;
    private Timer clockTimer;
    private JComboBox<String> timeZoneComboBox; // 时区下拉列表
    private TimeZone nowTimeZone;

    public ClockPanel() {
        nowTimeZone = Calendar.getInstance().getTimeZone();

        // 使用 BoxLayout 垂直排列组件
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("时间和日期"));

        // 创建时区下拉列表并设置为顶部组件
        timeZoneComboBox = new JComboBox<>(TimeZone.getAvailableIDs());
        timeZoneComboBox.setSelectedItem(TimeZone.getDefault().getID());
        timeZoneComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 当时区改变时更新时间
                updateTimeZone((String) timeZoneComboBox.getSelectedItem());
            }
        });
        JPanel timeZonePanel = new JPanel(new BorderLayout());
        timeZonePanel.add(timeZoneComboBox, BorderLayout.NORTH);
        timeZonePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(timeZonePanel);

        // 创建时钟标签并设置为居中对齐
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Serif", Font.BOLD, 36));
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(clockLabel);

        // 创建日期标签并设置为居中对齐
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Serif", Font.BOLD, 24));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(dateLabel);

        // 启动时钟更新
        startClock();
    }

    private void updateTimeZone(String timeZoneId) {
        nowTimeZone = TimeZone.getTimeZone(timeZoneId);
    }

    private void startClock() {
        clockTimer = new Timer(1000, new ActionListener() { // 每秒更新时间
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        clockTimer.start();
    }

    private void updateClock() {
        Calendar now = Calendar.getInstance(nowTimeZone); // 获取当前时间
        clockLabel.setText(String.format("%1$tH:%1$tM:%1$tS", now));
        dateLabel.setText(String.format("%1$tY年%1$tm月%1$td日 %1$tA", now));
    }
}