package by.barsnik96.HeadHunter_JSP.view;

import by.barsnik96.HeadHunter_JSP.utils.DatabaseListModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.util.Arrays;

import static javax.swing.ListSelectionModel.*;
//import com.mysql.cj.jdbc.Driver;

public class TestLoadingFromJList extends JFrame
{
    private static String
        url = "jdbc:mysql://localhost:3306/headhunter",
        user = "root",
        password = "root",
        query = "select * from addresses";

    private JCheckBox checkBox_1;
    private JCheckBox checkBox_2;
    private int do_click_count = 0;

    public TestLoadingFromJList() throws SQLException {
        //
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet result_set = statement.executeQuery(query);
        // DatabaseListModel
        final DatabaseListModel databaseListModel = new DatabaseListModel();
        //
        databaseListModel.setDataSource(result_set, "address_city");
        result_set.close();
        //
        //
        //
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        //
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JList list = new JList(databaseListModel);
        list.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int[] indices = list.getSelectedIndices();
                    for (int i = 0; i < indices.length; i++)
                    {
                        //System.out.println(list.getModel().getElementAt(indices[i]) + "   " + indices[i]);
                    }
                    for (int i = 0; i < list.getSelectedValuesList().size(); i++)
                    {
                        //System.out.println(list.getSelectedValuesList().get(i) + "   " + list.getSelectedValuesList().indexOf(list.getSelectedValuesList().get(i)));
                    }

                    //list.setSelectedIndex();
                } catch (Exception exception) {}
            }
        });
        //list.setVisibleRowCount(2);
        JScrollPane scrollPane = new JScrollPane(list);
        panel.add(scrollPane, BorderLayout.NORTH);
        //
        Button abc = new Button("add");
        abc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(list.getSelectedValuesList());
                System.out.println(Arrays.toString(list.getSelectedIndices()));
                System.out.println(list.getModel().getSize());
                //int[] ints = {0, 2};
                //list.addSelectionInterval(0, 0);
                //list.addSelectionInterval(2, 2);
            }
        });
        checkBox_1 = new JCheckBox("Нажми на меня!  1");
        checkBox_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox che = (JCheckBox) e.getSource();
                //checkBox_2.doClick();
                //do_click_count++;
                //System.out.println(do_click_count);
                if (che.isSelected() == true)
                {
                    System.out.println("action_sel");
                }
                if (che.isSelected() == false)
                {
                    System.out.println("action_desel");
                }
            }
        });
        checkBox_1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    System.out.println("item_sel");
                }
                else if (e.getStateChange() == ItemEvent.DESELECTED)
                {
                    System.out.println("item_desel");
                }
            }
        });

        checkBox_1.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String property_name = evt.getPropertyName();
                if (property_name.equals("isSelected()"))
                {
                    System.out.println("Я РОДИЛСЯ");
                }
            }
        });



        checkBox_2 = new JCheckBox("Нажми на меня!  2");
        //
        FlowLayout fl = new FlowLayout();
        panel.setLayout(fl);
        panel.add(checkBox_1);
        panel.add(checkBox_2);
        panel.add(abc);
        //
        add(panel);
        setVisible(true);
    }
}