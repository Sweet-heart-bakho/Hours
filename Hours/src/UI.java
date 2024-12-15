import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UI {
    Main main;
    List<Worker> workers;
    Worker worker;
    DefaultComboBoxModel<String> personList = new DefaultComboBoxModel<>();
    DefaultListModel<String> listMessages = new DefaultListModel<>();

    List<String> result;
    public UI () {

        JFrame mainFrame = new JFrame("Hours Count");
        mainFrame.setSize(1150, 780);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel characters = new JLabel("Person");
        characters.setBounds(40, 20, 50, 20);
        mainFrame.add(characters);

        JComboBox<String> jComboBox = new JComboBox<>(personList);
        jComboBox.setBounds(40, 60, 500, 30);
        mainFrame.add(jComboBox);

        JList<String> jlistMessages = new JList<>(listMessages);
        JScrollPane scrollPane = new JScrollPane(jlistMessages);
        scrollPane.setBounds(40, 120, 500, 500);
        mainFrame.add(scrollPane);

        JButton calculateHours = new JButton("Calculate");
        calculateHours.setBounds(40, 680, 150, 40);
        mainFrame.add(calculateHours);

        JLabel countedHours = new JLabel();
        countedHours.setBounds(560, 120, 33, 500);
        mainFrame.add(countedHours);
        countedHours.setVerticalAlignment(SwingConstants.TOP);

        JTextField correct = new JTextField();
        correct.setBounds(800, 120, 310, 30);
        mainFrame.add(correct);

        JLabel breakTime = new JLabel();
        breakTime.setBounds(800, 180, 100, 30);
        breakTime.setText("Break time: ");
        mainFrame.add(breakTime);

        JTextField textBreak = new JTextField();
        textBreak.setBounds(960, 180, 150, 30);
        mainFrame.add(textBreak);

        JButton confirmCorrect = new JButton("Confirm");
        confirmCorrect.setBounds(800, 240, 150, 40);
        mainFrame.add(confirmCorrect);

        JButton delete = new JButton("Delete");
        delete.setBounds(960, 240, 150, 40);
        mainFrame.add(delete);

        JButton copy = new JButton("Copy");
        copy.setBounds(800, 680, 150, 40);
        mainFrame.add(copy);

        JButton file = new JButton("Choose File");
        file.setBounds(960, 680, 150, 40);
        mainFrame.add(file);

        mainFrame.repaint();

        jComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listMessages.removeAllElements();
                worker = workers.get(jComboBox.getSelectedIndex());

                for (WorkedHours hour : worker.getHours()) {
                    listMessages.addElement(hourToString(hour));
                }

                if (worker.countedHours != null) {
                    String toLabel = worker.countedHours.replace("\n", "<br>");
                    countedHours.setText("<html>" + toLabel + "</html>");
                }
            }
        });

        jlistMessages.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (jlistMessages.getSelectedIndex() > -1) {
                    correct.setText(worker.getHours().get(jlistMessages.getSelectedIndex()).getTextHours());
                    textBreak.setText(worker.getHours().get(jlistMessages.getSelectedIndex()).getBreakHour());
                }
            }
        });

        confirmCorrect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                worker.getHours().get(jlistMessages.getSelectedIndex()).setTextHours(correct.getText(), textBreak.getText());
                listMessages.setElementAt(hourToString(worker.getHours().get(jlistMessages.getSelectedIndex())), jlistMessages.getSelectedIndex());
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                worker.getHours().remove(jlistMessages.getSelectedIndex());
                listMessages.remove(jlistMessages.getSelectedIndex());
            }
        });

        calculateHours.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (Worker worker1 : workers)
                    worker1.countHours();
                String toLabel = worker.countedHours.replace("\n", "<br>");
                countedHours.setText("<html>" + toLabel + "</html>");
            }
        });

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(worker.countedHoursToCopy);
                clipboard.setContents(stringSelection, null);
            }
        });

        file.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                // Установка начальной директории в папку "Downloads"
                String userHome = System.getProperty("user.home");
                fileChooser.setCurrentDirectory(new File(userHome + "/Downloads"));

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Выбранный файл: " + selectedFile.getAbsolutePath());
                    main.getFile(selectedFile);
                }
            }
        });
    }

    public String hourToString(WorkedHours hour) {
        return "<html>" + hour.getTextHours() +
                "<br>Start: " + hour.getStartHour() +
                "    End: " + hour.getEndHour() +
                "    Break:" + hour.getBreakHour() +
                "    Work" + hour.getWorkedHour() +
                "</html>";
    }

    public void getWorkers(List<Worker> workers) {
        personList.removeAllElements();
        this.workers = workers;
        for (Worker worker : workers) {
            personList.addElement(worker.name);
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
