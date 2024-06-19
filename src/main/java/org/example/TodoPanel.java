package org.example;

import org.example.Item.TodoItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TodoPanel extends JPanel {

    private JTextField todoInput;
    private JButton addButton;
    private JList<String> todoList;
    private DefaultListModel<String> listModel;
    private JSpinner dateSpinner;
    private CalendarPanel calendarPanel; // Reference to CalendarPanel for communication

    public TodoPanel(CalendarPanel calendarPanel) {
        this.calendarPanel = calendarPanel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("待办事项"));

        // Create components
        todoInput = new JTextField(15);
        addButton = new JButton("添加");
        listModel = new DefaultListModel<>();
        todoList = new JList<>(listModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create date spinner
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(spinnerDateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date()); // Default to today's date

        // Create and organize input panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("待办事项:"));
        inputPanel.add(todoInput);
        inputPanel.add(new JLabel("日期:"));
        inputPanel.add(dateSpinner);
        inputPanel.add(addButton);

        // Add components to the main panel
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(todoList), BorderLayout.CENTER);

        // Add action listener to the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String todoText = todoInput.getText();
                Date selectedDate = (Date) dateSpinner.getValue();
                String dateKey = String.format("%1$tY-%1$tm-%1$td", selectedDate);

                if (!todoText.isEmpty()) {
                    listModel.addElement(String.format("%s - %s", dateKey, todoText));
                    calendarPanel.addTodoItem(dateKey, new TodoItem(todoText));
                    todoInput.setText("");
                }
            }
        });

        // Add a listener to the list for item selection
        todoList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = todoList.getSelectedIndex();
                    if (index != -1) {
                        String selectedValue = listModel.getElementAt(index);
                        String[] parts = selectedValue.split(" - ");
                        if (parts.length == 2) {
                            String dateKey = parts[0];
                            showTodoDialog(dateKey);
                        }
                    }
                }
            }
        });
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