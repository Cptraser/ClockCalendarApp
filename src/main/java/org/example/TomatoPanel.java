package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Objects;

import static org.example.CountdownFrame.startCountdown;

public class TomatoPanel extends JPanel {
    // 记录总工作时间的变量，初始值为0
    private int totalWorkTime = 0;
    private int totalTomato = 0;

    private JPanel tomatoIconsPanel; // 用于放置小番茄图标的面板
    private ClockCalendarApp mainAppFrame;

    public TomatoPanel(ClockCalendarApp mainAppFrame) {
        this.mainAppFrame = mainAppFrame;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("番茄钟"));

        // 创建用于放置小番茄图标的面板，并设置 FlowLayout
        tomatoIconsPanel = new JPanel();
        tomatoIconsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tomatoIconsPanel.setBorder(BorderFactory.createTitledBorder("你的番茄"));
        add(tomatoIconsPanel, BorderLayout.CENTER);

        JPanel tomatoControlPanel = new JPanel();
        JTextField tomatoTimeField = new JTextField(5);
        JButton startTomatoButton = new JButton("开始");
        tomatoControlPanel.add(new JLabel("时间（分钟）："));
        tomatoControlPanel.add(tomatoTimeField);
        tomatoControlPanel.add(startTomatoButton);

        // 添加按钮的事件监听器，以处理开始工作逻辑
        startTomatoButton.addActionListener(e -> {
            String timeText = tomatoTimeField.getText();
            if (!timeText.isEmpty()) {
                int minutes = Integer.parseInt(timeText);
                startCountdown(mainAppFrame, minutes);
            }
        });

        add(tomatoControlPanel, BorderLayout.SOUTH);
    }

    public void addToTotalWorkTime(int timeToAdd) {
        totalWorkTime += timeToAdd;
        System.out.println(totalWorkTime);
        int nowTotalTomato = totalWorkTime / (25 * 60);
        if (nowTotalTomato > totalTomato) {
            for (int i = totalTomato; i <= nowTotalTomato; ++i) {
                JLabel tomatoIconLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/tomato.jpg"))));
                tomatoIconsPanel.add(tomatoIconLabel); // 将小番茄图片添加到面板上
                String dateKey = String.format("%1$tY-%1$tm-%1$td", Calendar.getInstance());
                mainAppFrame.getCalendarPanel().addTomato(dateKey);
            }
            revalidate(); // 重新验证面板以更新UI
            repaint(); // 重绘面板
            totalTomato = nowTotalTomato;
        }
    }

    // 确保在事件调度线程上更新UI
    private void updateUIInEDT(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) {
            action.run();
        } else {
            SwingUtilities.invokeLater(action);
        }
    }

    // 可以在外部调用此方法来安全地更新UI
    public void safeAddToTotalWorkTime(int timeToAdd) {
        updateUIInEDT(() -> addToTotalWorkTime(timeToAdd));
    }

}