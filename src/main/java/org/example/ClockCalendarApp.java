package org.example;
import javax.swing.*;
import java.awt.*;

public class ClockCalendarApp extends JFrame {

    public ClockCalendarApp() {
        setTitle("时钟日历应用");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize UI
        initUI();
    }

    private void initUI() {
        // Create and add each functional panel
        ClockPanel clockPanel = new ClockPanel();
        TomatoPanel tomatoPanel = new TomatoPanel();
        CalendarPanel calendarPanel = new CalendarPanel();
        TodoAndHabitPanel todoAndHabitPanel = new TodoAndHabitPanel(calendarPanel);

        // Add panels to the main frame
        add(clockPanel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.WEST);
        add(todoAndHabitPanel, BorderLayout.CENTER);
        add(tomatoPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClockCalendarApp().setVisible(true);
            }
        });
    }
}