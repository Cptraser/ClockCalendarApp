package org.example;
import javax.swing.*;
import java.awt.*;

public class ClockCalendarApp extends JFrame {

    private ClockPanel clockPanel;
    private TomatoPanel tomatoPanel;
    private CalendarPanel calendarPanel;
    private TodoAndHabitPanel todoAndHabitPanel;

    public ClockCalendarApp() {
        setTitle("时钟日历应用");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize UI
        initUI();
    }

    private void initUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;

        this.setLocation(x, y);
        this.setVisible(true);

        // Create and add each functional panel
        clockPanel = new ClockPanel();
        tomatoPanel = new TomatoPanel(this);
        calendarPanel = new CalendarPanel();
        todoAndHabitPanel = new TodoAndHabitPanel(calendarPanel);

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

    public TomatoPanel getTomatoPanel() {
        return tomatoPanel;
    }

    public CalendarPanel getCalendarPanel() {
        return calendarPanel;
    }
}