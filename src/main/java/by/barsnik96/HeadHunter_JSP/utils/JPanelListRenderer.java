package by.barsnik96.HeadHunter_JSP.utils;

import by.barsnik96.HeadHunter_JSP.domain.MetroStation;
import by.barsnik96.HeadHunter_JSP.domain.Vacancy;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class JPanelListRenderer extends JPanel implements ListCellRenderer<Object>
{
    public JPanelListRenderer()
    {
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        Vacancy vacancy = (Vacancy) value;
        //
        JPanel center_panel = new JPanel();
        JPanel right_panel = new JPanel();
        ///////
        // Center Panel
        JLabel label_vacancy_name = new JLabel();
        JLabel label_company_name = new JLabel();
        JLabel label_address_n_metro = new JLabel();
        ///////
        // Right Panel
        JLabel label_salary = new JLabel();
        JLabel label_data = new JLabel();
        ///////
        Color color = new Color(30, 136, 229);
        JPanel all_panel = new JPanel();
        ///////
        // Fonts
        Font arial_16 = new Font("Arial", Font.BOLD, 16);
        Font arial_12 = new Font("Arial", Font.PLAIN, 14);
        ///////
        // Fonts settings
        label_vacancy_name.setFont(arial_16);
        label_company_name.setFont(arial_12);
        label_address_n_metro.setFont(arial_12);
        //
        label_salary.setFont(arial_16);
        label_data.setFont(arial_12);
        ///////
        // Center Panel Data
        label_vacancy_name.setText(vacancy.getName());
        label_company_name.setText(vacancy.getEmployer().getName());
        //
        String address = null;
        //
        if (vacancy.getAddress() != null)
        {
            if (vacancy.getAddress().getCity() != null &&
                    vacancy.getAddress().getStreet() != null &&
                    vacancy.getAddress().getBuilding() != null)
            {
                address = vacancy.getAddress().getCity() + ", " +
                        vacancy.getAddress().getStreet() + ", " +
                        vacancy.getAddress().getBuilding();
            }
            else if (vacancy.getAddress().getCity() != null &&
                    vacancy.getAddress().getStreet() != null)
            {
                address = vacancy.getAddress().getCity() + ", " +
                        vacancy.getAddress().getStreet();
            }
            else if (vacancy.getAddress().getCity() != null)
            {
                address = vacancy.getAddress().getCity();
            }
        }
        else
        {
            address = vacancy.getArea().getName();
        }
        //
        if (vacancy.getMetro_stations().size() != 0)
        {
            MetroStation[] metro_stations = vacancy.getMetro_stations().toArray(new MetroStation[vacancy.getMetro_stations().size()]);
            for (int i = 0; i < metro_stations.length; i++)
            {
                address += (", " + "ст. " + metro_stations[i].getName());
            }
        }
        label_address_n_metro.setText(address);
        //
        ///////
        // Layouts
        center_panel.setLayout(new GridBagLayout());
        right_panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        ///////
        // Center Panel Constraints
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 20, 0);
        center_panel.add(label_vacancy_name, constraints);
        //
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 14, 0);
        center_panel.add(label_company_name, constraints);
        //
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(0, 0, 0, 0);
        // bottom 18 if exists requirements
        center_panel.add(label_address_n_metro, constraints);
        ///////
        // Right Panel Data
        String salary = "";
        if (vacancy.getSalary_to() > 0)
        {
            salary += vacancy.getSalary_from() + " — " + vacancy.getSalary_to();
        }
        else if (vacancy.getSalary_from() > 0)
        {
            salary += vacancy.getSalary_from();
        }
        else
        {
            salary += "Не указано";
        }
        label_salary.setText(salary);
        //
        LocalDateTime localDateTime = vacancy.getPublished_at();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("ru"));
        label_data.setText(localDateTime.format(formatter));
        // Right Panel Constraints
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 50, 0);
        right_panel.add(label_salary, constraints);
        //
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        right_panel.add(label_data, constraints);
        //
        //this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //this.setLayout(new BorderLayout());
        all_panel.add(center_panel, BorderLayout.WEST);
        all_panel.add(right_panel, BorderLayout.EAST);
        //this.add(center_panel, BorderLayout.WEST);
        //this.add(right_panel, BorderLayout.EAST);
        this.add(all_panel);

        if (isSelected)
        {
            //this.setForeground(color);
            //this.setForeground((Color)UIManager.get("List.selectionForeground"));
            this.setBackground(color);
            //this.setBackground((Color)UIManager.get("List.selectionBackground"));
            //this.setBorder(BorderFactory.createLineBorder((Color)UIManager.get("List.selectionBorder")));
            //center_panel.setForeground((Color)UIManager.get("List.selectionForeground"));
            //center_panel.setBackground((Color)UIManager.get("List.selectionBackground"));
            //center_panel.setBorder(BorderFactory.createLineBorder((Color)UIManager.get("List.selectionBorder")));
        }
        else
        {
            this.setBackground(list.getBackground());
            //this.setForeground(list.getForeground());
            //this.setBorder(BorderFactory.createLineBorder((Color)UIManager.get("ContactItem.background")));
            //center_panel.setBackground(list.getBackground());
            //center_panel.setForeground(list.getForeground());
            //center_panel.setBorder(BorderFactory.createLineBorder((Color)UIManager.get("ContactItem.background")));
        }

        list.setBackground((Color)UIManager.get("ContactItem.background"));

        return this;
    }
}