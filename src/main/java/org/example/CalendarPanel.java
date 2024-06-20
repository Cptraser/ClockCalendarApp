package org.example;

import org.example.Item.TodoItem;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CalendarPanel extends JPanel {

    private JLabel monthYearLabel;
    private JPanel daysPanel;
    private Calendar calendar;
    private Map<String, List<TodoItem>> todoMap;

    public CalendarPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("日历"));

        calendar = Calendar.getInstance();
        todoMap = new HashMap<>();

        // Initialize components
        monthYearLabel = new JLabel();
        daysPanel = new JPanel(new GridLayout(0, 7));

        // Create header panel for month and year
        JPanel headerPanel = new JPanel();
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        headerPanel.add(prevButton);
        headerPanel.add(monthYearLabel);
        headerPanel.add(nextButton);

        // Add components to the main panel
        add(headerPanel, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);

        // Add action listeners
        prevButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        nextButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        // Initial calendar update
        updateCalendar();
    }

    private void updateCalendar() {
        monthYearLabel.setText(String.format("%1$tY年 %1$tm月", calendar));

        // Clear previous days
        daysPanel.removeAll();

        // Add day names
        String[] dayNames = {"日", "一", "二", "三", "四", "五", "六"};
        for (String dayName : dayNames) {
            daysPanel.add(new JLabel(dayName, SwingConstants.CENTER));
        }

        // Set calendar to the first day of the month
        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = tempCal.get(Calendar.DAY_OF_WEEK) - 1;

        // Add empty labels for days of the previous month
        for (int i = 0; i < startDay; i++) {
            daysPanel.add(new JLabel(""));
        }

        // Add day buttons for the current month
        int maxDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 添加当前月份的天数按钮，并检查是否为周末或有待办事项
        for (int day = 1; day <= maxDay; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setBackground(Color.WHITE);
            String dateKey = String.format("%1$tY-%1$tm-%1$td", tempCal);
            // 检测是否为周末或节假日
            if (isWeekendOrHoliday(tempCal)) {
                applyHighlight(dayButton, "weekend");
            }
            // 检测是否有待办事项
            if (!getTodoItems(dateKey).isEmpty()) {
                applyHighlight(dayButton, "todo");
            }
            dayButton.addActionListener(e -> showTodoDialog(dateKey));
            daysPanel.add(dayButton);
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Refresh the panel
        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private void showTodoDialog(String dateKey) {
        List<TodoItem> todoItems = todoMap.getOrDefault(dateKey, new ArrayList<>());
        JDialog dialog = new JDialog((Frame) null, "待办事项 - " + dateKey, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);

        JPanel todoPanel = new JPanel();
        todoPanel.setLayout(new BoxLayout(todoPanel, BoxLayout.Y_AXIS));
        for (TodoItem item : todoItems) {
            JCheckBox checkBox = new JCheckBox(item.getText(), item.isCompleted());
            checkBox.addActionListener(e -> item.setCompleted(checkBox.isSelected()));
            todoPanel.add(checkBox);
        }

        dialog.add(new JScrollPane(todoPanel), BorderLayout.CENTER);

        // Set dialog location to the right side of the main frame
        Point location = getLocationOnScreen();
        dialog.setLocation(location.x - getWidth(), location.y);

        dialog.setVisible(true);
    }

    public void addTodoItem(String dateKey, TodoItem todoItem) {
        todoMap.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(todoItem);
    }

    public List<TodoItem> getTodoItems(String dateKey) {
        return todoMap.getOrDefault(dateKey, new ArrayList<>());
    }

    public void highlightDateButton(String dateKey) {
        for (Component component : daysPanel.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(dateKey.substring(8))) {
                applyHighlight((JButton) component, "todo");
                break; // 假设每个日期都是唯一的，找到后即可退出循环
            }
        }
    }

    private void applyHighlight(JButton dayButton, String type) {
        Color backgroundColor = switch (type) {
            case "weekend" -> Color.cyan; // 周末使用蓝色
            case "todo" -> Color.YELLOW; // 待办使用淡黄色，RGB(255, 255, 102)
            default -> Color.WHITE; // 默认背景色为白色
        };
        dayButton.setBackground(backgroundColor); // 设置背景色
        dayButton.setOpaque(true); // 设置按钮有背景色
    }

    private boolean isWeekendOrHoliday(Calendar cal) {
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // 周末是星期六和星期日
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
        // 如果需要检测法定节假日，可以在这里添加逻辑
    }
}