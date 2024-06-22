package org.example;

import org.example.Item.HabitItem;
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

    private List<HabitItem> habitItemList = new ArrayList<>();

    public TodoAndHabitPanel(CalendarPanel calendarPanel) {

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

        // 创建查看习惯按钮和习惯显示面板
        JButton viewHabitButton = new JButton("查看习惯");
        viewHabitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建一个面板来容纳习惯信息
                JDialog habitsDialog = new JDialog((Frame) null, "习惯信息", true);
                habitsDialog.setLayout(new BorderLayout());
                habitsDialog.setSize(400, 700);
                JPanel habitsPanel = new JPanel();
                habitsPanel.setLayout(new BoxLayout(habitsPanel, BoxLayout.Y_AXIS));

                // 加载所有习惯信息到 habitsPanel
                loadHabitsToPanel(habitsPanel);

                habitsDialog.add(new JScrollPane(habitsPanel));

                // 显示窗口
                habitsDialog.setLocationRelativeTo(TodoAndHabitPanel.this); // 相对于当前面板定位
                habitsDialog.setVisible(true);
            }
        });

        // Add components to the main panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(todoInputPanel);
        inputPanel.add(habitInputPanel);
        inputPanel.add(viewHabitButton);


        add(inputPanel);

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
        Date timeSpinnerValue = (Date) timeSpinner.getValue();

        // 设置 nextTime 为当前时间加上用户设置的时间
        nextTime.set(Calendar.HOUR_OF_DAY, timeSpinnerValue.getHours());
        nextTime.set(Calendar.MINUTE, timeSpinnerValue.getMinutes());
        nextTime.set(Calendar.SECOND, 0);

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

        int year = Calendar.getInstance().get(Calendar.YEAR); // 获取年
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1; // 获取月
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1; // 获取日
        String startTime = String.format("%d-%02d-%02d", year, month, day);
        int hour = timeSpinnerValue.getHours();
        int minute = timeSpinnerValue.getMinutes();
        String time = String.format("%02d:%02d", hour, minute);

        habitItemList.add(new HabitItem(startTime, frequency, time, habitText, timer));

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

    private void loadHabitsToPanel(JPanel panel) {
        List<HabitItem> habitItems = getHabitItems();
        for (HabitItem habit : habitItems) {
            JPanel habitInfoPanel = new JPanel();
            habitInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // 创建开始时间标签
            JLabel startTimeLabel = new JLabel(habit.getStartTime());
            // 创建频率标签
            JLabel frequencyLabel = new JLabel(habit.getFrequency());
            // 创建时间标签
            JLabel timeLabel = new JLabel(habit.getTime());
            // 创建内容标签
            JLabel contentLabel = new JLabel(habit.getContent());

            // 创建删除按钮
            JButton deleteButton = new JButton("删除");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    habit.cancelTimer();
                    // 从 habitItemList 中移除习惯项
                    habitItemList.remove(habit);
                    // 从面板中移除 habitInfoPanel
                    panel.remove(habitInfoPanel);
                    // 重新验证面板以更新UI
                    panel.revalidate();
                    panel.repaint();
                    // 如果需要，可以在这里添加其他删除后的逻辑
                }
            });

            habitInfoPanel.add(deleteButton); // 添加删除按钮到习惯信息面板
            habitInfoPanel.add(startTimeLabel);
            habitInfoPanel.add(frequencyLabel);
            habitInfoPanel.add(timeLabel);
            habitInfoPanel.add(contentLabel);

            panel.add(habitInfoPanel); // 将习惯信息面板添加到习惯显示面板
        }
    }

    private List<HabitItem> getHabitItems() {
        return habitItemList;
    }
}
