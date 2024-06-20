package org.example;

import org.example.Item.TodoItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TodoAndHabitPanel extends JPanel {

    private JTextField todoInput;
    private JButton addTodoButton;
    private JSpinner todoDateSpinner;

    private DefaultListModel<String> combinedListModel;

    private JTextField habitInput;
    private JButton addHabitButton;
    private JComboBox<String> habitFrequencyComboBox;

    private CalendarPanel calendarPanel; // Reference to CalendarPanel for communication

    public TodoAndHabitPanel(CalendarPanel calendarPanel) {
        this.calendarPanel = calendarPanel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("待办事项与习惯打卡"));

        combinedListModel = new DefaultListModel<>();
        JList<String> combinedList = new JList<>(combinedListModel);

        // Create components for Todo
        todoInput = new JTextField(50); // Reduced size
        addTodoButton = new JButton("添加");

        // Create date spinner for Todo
        SpinnerDateModel todoSpinnerDateModel = new SpinnerDateModel();
        todoDateSpinner = new JSpinner(todoSpinnerDateModel);
        JSpinner.DateEditor todoDateEditor = new JSpinner.DateEditor(todoDateSpinner, "yyyy-MM-dd");
        todoDateSpinner.setEditor(todoDateEditor);
        todoDateSpinner.setValue(new Date()); // Default to today's date

        // Create and organize Todo input panel
        JPanel todoInputPanel = new JPanel();
        todoInputPanel.add(new JLabel("待办事项:"));
        todoInputPanel.add(todoInput);
        todoInputPanel.add(new JLabel("日期:"));
        todoInputPanel.add(todoDateSpinner);
        todoInputPanel.add(Box.createHorizontalStrut(10));
        todoInputPanel.add(addTodoButton);

        JSpinner habitDaySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1)); // Default Day 1
        JSpinner habitWeekSpinner = new JSpinner(new SpinnerNumberModel(Calendar.MONDAY, Calendar.SUNDAY, Calendar.SATURDAY, 1)); // Default Monday
        JLabel habitDayLabel = new JLabel("日期:");
        JLabel habitWeekLabel = new JLabel("星期:");


        habitWeekSpinner.setVisible(false);
        habitDaySpinner.setVisible(false);
        habitDayLabel.setVisible(false);
        habitWeekLabel.setVisible(false);

        ActionListener habitFrequencyListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFrequency = (String) habitFrequencyComboBox.getSelectedItem();
                if ("每周".equals(selectedFrequency)) {
                    // 显示星期选择器，隐藏日期选择器
                    habitWeekSpinner.setVisible(true);
                    habitDaySpinner.setVisible(false);
                    habitWeekLabel.setVisible(true);
                    habitDayLabel.setVisible(false);
                } else if ("每月".equals(selectedFrequency)) {
                    // 显示日期选择器，隐藏星期选择器
                    habitDaySpinner.setVisible(true);
                    habitWeekSpinner.setVisible(false);
                    habitDayLabel.setVisible(true);
                    habitWeekLabel.setVisible(false);
                } else {
                    // 其他频率，隐藏两个选择器
                    habitWeekSpinner.setVisible(false);
                    habitDaySpinner.setVisible(false);
                    habitWeekLabel.setVisible(false);
                    habitDayLabel.setVisible(false);
                }
            }
        };

        // Create components for Habit
        habitInput = new JTextField(50); // Reduced size
        addHabitButton = new JButton("添加");
        habitFrequencyComboBox = new JComboBox<>(new String[]{"每日", "每周", "每月"});
        habitFrequencyComboBox.addActionListener(habitFrequencyListener);

        // Create time chooser
        SpinnerDateModel timeSpinnerDateModel = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(timeSpinnerDateModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm"); // 24h/day
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date()); // Default to current time

        // Create and organize Habit input panel
        JPanel habitInputPanel = new JPanel();
//        habitInputPanel.setLayout(new BoxLayout(habitInputPanel, BoxLayout.X_AXIS));
        habitInputPanel.add(new JLabel("习惯打卡:"));
        habitInputPanel.add(habitInput);
        habitInputPanel.add(Box.createHorizontalStrut(10));
        habitInputPanel.add(new JLabel("周期:"));
        habitInputPanel.add(habitFrequencyComboBox);
        habitInputPanel.add(Box.createHorizontalStrut(10));
        habitInputPanel.add(habitDayLabel);
        habitInputPanel.add(habitDaySpinner);
        habitInputPanel.add(Box.createHorizontalStrut(10));
        habitInputPanel.add(habitWeekLabel);
        habitInputPanel.add(habitWeekSpinner);
        habitInputPanel.add(Box.createHorizontalStrut(10));
        habitInputPanel.add(new JLabel("时间:"));
        habitInputPanel.add(timeSpinner); // 添加时间选择器
        habitInputPanel.add(Box.createHorizontalStrut(10));
        habitInputPanel.add(addHabitButton);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add components to the main panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(todoInputPanel);
        inputPanel.add(habitInputPanel);
        inputPanel.setBorder(BorderFactory.createTitledBorder("输入"));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.add(new JScrollPane(combinedList));
        listPanel.setBorder(BorderFactory.createTitledBorder("日志"));

        add(inputPanel);
        add(listPanel);

        // Add action listener to the addTodoButton
        addTodoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String todoText = todoInput.getText();
                Date selectedDate = (Date) todoDateSpinner.getValue();
                String dateKey = String.format("%1$tY-%1$tm-%1$td", selectedDate);

                if (!todoText.isEmpty()) {
                    combinedListModel.addElement(String.format("%s - %s", dateKey, todoText));
                    calendarPanel.addTodoItem(dateKey, new TodoItem(todoText));
                    todoInput.setText("");

                    // 调用 highlightDateButton 方法高亮显示按钮
                    calendarPanel.highlightDateButton(dateKey);
                }
            }
        });

        // Add action listener to the addHabitButton
        addHabitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String habitText = habitInput.getText();
                String frequency = (String) habitFrequencyComboBox.getSelectedItem();
                int dayOfWeekOrDayOfMonth = -1; // 初始化为无效值

                if ("每周".equals(frequency)) {
                    dayOfWeekOrDayOfMonth = (Integer) habitWeekSpinner.getValue(); // 获取星期几
                } else if ("每月".equals(frequency)) {
                    dayOfWeekOrDayOfMonth = (Integer) habitDaySpinner.getValue(); // 获取日期
                }

                if (!habitText.isEmpty() && (dayOfWeekOrDayOfMonth != -1 || "每日".equals(frequency))) {
                    // 添加习惯到列表和提醒
                    if ("每日".equals(frequency)) {
                        combinedListModel.addElement(String.format("%s - %s", frequency, habitText));
                    } else {
                        combinedListModel.addElement(String.format("%s - %d - %s", frequency, dayOfWeekOrDayOfMonth, habitText));
                    }
                    addHabitReminder(habitText, frequency, dayOfWeekOrDayOfMonth, timeSpinner);
                    habitInput.setText("");
                }
            }
        });
    }

    private void addHabitReminder(String habitText, String frequency, int dayOfWeekOrDayOfMonth, JSpinner timeSpinner) {
        Timer timer = new Timer();
        Calendar now = Calendar.getInstance(); // 当前时间
        Calendar nextTime = Calendar.getInstance(); // 下一次提醒的时间
        Date next = (Date) timeSpinner.getValue();

        // 设置 nextTime 为当前时间加上用户设置的时间
        nextTime.set(Calendar.HOUR_OF_DAY, next.getHours());
        nextTime.set(Calendar.MINUTE, next.getMinutes());
        nextTime.set(Calendar.SECOND, next.getSeconds());

        // 根据频率设置下一次提醒的时间
        switch (frequency) {
            case "每日":
                break;
            case "每周":
                // 如果是每周，找到这个星期的指定星期几的相同时间
                while (nextTime.get(Calendar.DAY_OF_WEEK) != dayOfWeekOrDayOfMonth) {
                    nextTime.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case "每月":
                // 如果是每月，找到这个月的指定日期的相同时间
                while (nextTime.get(Calendar.DAY_OF_MONTH) != dayOfWeekOrDayOfMonth) {
                    nextTime.add(Calendar.MONTH, 1);
                }
                break;
            default:
                return; // 如果频率无效，直接返回
        }

        // 计算 delay，即下一次提醒时间与当前时间的差值
        long delay = Math.max(nextTime.getTimeInMillis() - now.getTimeInMillis(), 0);

        // 创建并调度 TimerTask
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Notify user about the habit
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "该打卡习惯了: " + habitText));
            }
        }, delay, getPeriodInMillis(frequency));
    }

    // 辅助方法，用于获取周期性任务的间隔时间（毫秒）
    private long getPeriodInMillis(String frequency) {
        return switch (frequency) {
            case "每日" -> 1000L * 60 * 60 * 24;
            case "每周" -> 1000L * 60 * 60 * 24 * 7;
            case "每月" -> 1000L * 60 * 60 * 24 * 30;
            default -> 0;
        };
    }


    private void showTodoDialog(String dateKey) {
        List<TodoItem> todoItems = calendarPanel.getTodoItems(dateKey);
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
        dialog.setLocation(location.x + getWidth(), location.y);

        dialog.setVisible(true);
    }
}