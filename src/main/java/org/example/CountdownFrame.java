package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownFrame extends JFrame {
    private Timer countdownTimer;
    private JLabel timeLabel;
    private ClockCalendarApp mainAppFrame;
    private int totalTime; // 倒计时的总时间，单位为秒
    private int elapsedTime; // 已经过去的时间，单位为秒

    public CountdownFrame(ClockCalendarApp mainAppFrame, int timeInSeconds) {
        super("倒计时");
        this.mainAppFrame = mainAppFrame;
        this.totalTime = timeInSeconds;
        this.elapsedTime = 0; // 初始化已过去的时间
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;

        this.setLocation(x, y);
        this.setVisible(true);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        add(timeLabel, BorderLayout.CENTER);

        updateTimeLabel();

        // 关闭按钮
        JButton closeButton = new JButton("提前结束");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                countdownTimer.stop();
                finishCountdown();
            }
        });
        add(closeButton, BorderLayout.SOUTH);

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++; // 增加已过去的时间
                updateTimeLabel();
                if (elapsedTime >= totalTime) {
                    countdownTimer.stop();
                    finishCountdown();
                }
            }
        });
        countdownTimer.start();
    }

    private void updateTimeLabel() {
        timeLabel.setText("剩余时间: " + formatTime(totalTime - elapsedTime));
    }

    private String formatTime(int seconds) {
        int hours = seconds / 3600; // 计算小时
        int minutes = (seconds % 3600) / 60; // 计算分钟
        int secondsLeft = seconds % 60; // 计算剩余秒数

        return String.format("%02d:%02d:%02d", hours, minutes, secondsLeft);
    }

    // 倒计时结束时调用的方法
    private void finishCountdown() {
        // 更新 TomatoPanel 的 totalWorkTime
        TomatoPanel tomatoPanel = mainAppFrame.getTomatoPanel(); // 假设这是获取 TomatoPanel 实例的方法
        tomatoPanel.safeAddToTotalWorkTime(elapsedTime);

        // 显示主界面
        mainAppFrame.setVisible(true);
        this.dispose();
    }

    // 倒计时界面的入口方法
    public static void startCountdown(ClockCalendarApp mainAppFrame, int minutes) {
        mainAppFrame.setVisible(false); // 隐藏主界面
        new CountdownFrame(mainAppFrame, minutes * 60).setVisible(true); // 倒计时
    }
}