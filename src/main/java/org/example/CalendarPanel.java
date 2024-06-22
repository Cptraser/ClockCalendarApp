package org.example;

import org.example.Item.TodoItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CalendarPanel extends JPanel {

    private JLabel monthYearLabel;
    private JPanel daysPanel;
    private Calendar calendar;
    private Map<String, List<TodoItem>> todoMap;

    private boolean isWeekendHighlightVisible = true;

    // 存储日期和对应的番茄数量
    private Map<String, Integer> tomatoCountMap = new HashMap<>();

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
        JButton toggleWeekendHighlightButton = new JButton("隐藏/显示周末高亮");
        add(toggleWeekendHighlightButton, BorderLayout.SOUTH);
        // 添加按钮的事件监听器
        toggleWeekendHighlightButton.addActionListener(e -> {
            isWeekendHighlightVisible = !isWeekendHighlightVisible; // 切换状态
            toggleWeekendHighlight(isWeekendHighlightVisible); // 调用切换方法
        });

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

    private void toggleWeekendHighlight(boolean visible) {
        isWeekendHighlightVisible = visible;
        updateCalendar(); // 刷新日历以应用更改
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
            String dateKey = String.format("%1$tY-%1$tm-%1$td", tempCal);

            // 获取并显示该日期的番茄数量
            int tomatoCount = tomatoCountMap.getOrDefault(dateKey, 0);
            JButton dayButton = new JButton();
            dayButton.setText("<html>" + day + "<br>tomato: " + tomatoCount + "<html>");
            dayButton.setHorizontalTextPosition(SwingConstants.CENTER); // 设置文本水平居中
            dayButton.setVerticalTextPosition(SwingConstants.CENTER); // 设置文本垂直居中

            dayButton.setBackground(Color.WHITE);

            // 检测是否为周末或节假日
            if (isWeekendOrHoliday(tempCal, isWeekendHighlightVisible)) {
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
        dialog.setSize(400, 700);

        JPanel todoPanel = new JPanel();
        todoPanel.setLayout(new BoxLayout(todoPanel, BoxLayout.Y_AXIS));

        for (TodoItem item : todoItems) {
            JPanel itemPanel = new JPanel(); // 使用 BorderLayout 来放置待办事项和删除按钮
            itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // 添加删除按钮
            JButton deleteButton = new JButton("删除");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 从待办事项列表中移除当前项
                    todoItems.remove(item);
                    todoMap.put(dateKey, new ArrayList<>(todoItems)); // 更新 map 中的列表
                    // 从界面上移除待办事项项
                    todoPanel.remove(itemPanel);
                    todoPanel.revalidate(); // 重新验证面板以更新UI
                    todoPanel.repaint(); // 重绘面板
                }
            });
            itemPanel.add(deleteButton); // 将删除按钮放置在左侧

            JCheckBox checkBox = new JCheckBox(item.getText(), item.isCompleted());
            checkBox.addActionListener(e -> item.setCompleted(checkBox.isSelected()));
            itemPanel.add(checkBox); // 将复选框放置在右侧

            todoPanel.add(itemPanel); // 将包含复选框和删除按钮的面板添加到待办事项面板
        }

        dialog.add(new JScrollPane(todoPanel));

        // Set dialog location to the right side of the main frame
        Point location = getLocationOnScreen();
        dialog.setLocation(location.x - getWidth(), location.y);

        dialog.setVisible(true);
    }

    public void addTomato(String dateKey) {
        int currentCount = tomatoCountMap.getOrDefault(dateKey, 0);
        tomatoCountMap.put(dateKey, currentCount + 1);
        updateCalendar();
    }

    public void addTodoItem(String dateKey, TodoItem todoItem) {
        todoMap.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(todoItem);
    }

    public List<TodoItem> getTodoItems(String dateKey) {
        return todoMap.getOrDefault(dateKey, new ArrayList<>());
    }

    public void highlightDateButton(String dateKey) {
        Calendar currentCalendar = Calendar.getInstance();
        for (Component component : daysPanel.getComponents()) {
            if (component instanceof JButton dayButton) {
                int year = currentCalendar.get(Calendar.YEAR); // 获取年
                int month = currentCalendar.get(Calendar.MONTH) + 1; // 获取月，Calendar.MONTH 从 0 开始
                String day = dayButton.getText();

                // 使用 String.format() 格式化日期
                String buttonDateKey = String.format("%d-%02d-%02d", year, month, Integer.parseInt(day));

                // 检查按钮上的日期是否与待办事项的日期、月份和年份匹配
                if (dateKey.equals(buttonDateKey)) {
                    // 检查是否有待办事项
                    if (!getTodoItems(dateKey).isEmpty()) {
                        applyHighlight(dayButton, "todo");
                    }
                    break; // 找到匹配的日期后退出循环
                }
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

    private boolean isWeekendOrHoliday(Calendar cal, boolean isWeekendHighlightVisible) {
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // 周末是星期六和星期日
        if (isWeekendHighlightVisible && (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
            return true;
        }
        // 节假日

        return false;
    }
}