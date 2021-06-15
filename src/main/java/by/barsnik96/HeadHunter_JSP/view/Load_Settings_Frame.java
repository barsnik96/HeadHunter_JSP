package by.barsnik96.HeadHunter_JSP.view;

import by.barsnik96.HeadHunter_JSP.utils.DatabaseListModel;
import by.barsnik96.HeadHunter_JSP.utils.LoadingParameters;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;

import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

public class Load_Settings_Frame extends JDialog
{
    private JPanel panel_west = new JPanel();
    private JPanel panel_center = new JPanel();
    private JPanel panel_east = new JPanel();
    private JPanel panel_south = new JPanel();
    //
    private JPanel panel_prof_area = new JPanel();
    private JPanel panel_company_industries_n_scope = new JPanel();
    private JPanel panel_area = new JPanel();
    private JPanel panel_salary = new JPanel();
    private JPanel panel_metro = new JPanel();
    private JPanel panel_time = new JPanel();
    //
    private JPanel panel_radio_buttons_experience = new JPanel(new GridLayout(0, 1));
    private JPanel panel_checkboxes_employment = new JPanel(new GridLayout(0, 1));
    private JPanel panel_checkboxes_schedule = new JPanel(new GridLayout(0, 1));
    private JPanel panel_radio_buttons_time = new JPanel(new GridLayout(0, 1));
    ///////
    private JLabel label_key_words = new JLabel("Ключевые слова");
    private JLabel label_prof_area = new JLabel("Профессиональная область");
    private JLabel label_company_industries_n_scope = new JLabel("Отрасль компании");
    private JLabel label_area = new JLabel("Регион");
    private JLabel label_salary = new JLabel("Уровень дохода");
    private JLabel label_search_only_with_salary = new JLabel("Показывать только вакансии с указанным уровнем дохода");
    private JLabel label_metro = new JLabel("Метро");
    private JLabel label_experience = new JLabel("Требуемый опыт работы");
    private JLabel label_employment = new JLabel("Тип занятости");
    private JLabel label_schedule = new JLabel("График работы");
    private JLabel label_time = new JLabel("Выводить");
    ///////
    private JTextField text_field_key_words = new JTextField();
    private JTextField text_field_salary = new JTextField();
    private JTextField text_field_metro = new JTextField();
    private JTextField text_field_time = new JTextField();
    ///////
    private String[] combo_box_currency_code_items = {"руб.", "USD", "EUR"};
    private JComboBox combo_box_currency_code = new JComboBox(combo_box_currency_code_items);
    private JCheckBox check_box_only_with_salary = new JCheckBox("Только с указанным уровнем дохода");
    ///////
    private JRadioButton radio_btn_exprience_irrelvant = new JRadioButton("Не имеет значения");
    private JRadioButton radio_btn_exprience_null = new JRadioButton("Нет опыта");
    private JRadioButton radio_btn_exprience_1_3 = new JRadioButton("От 1 года до 3 лет");
    private JRadioButton radio_btn_exprience_3_6 = new JRadioButton("От 3 до 6 лет");
    private JRadioButton radio_btn_exprience_6_more = new JRadioButton("Более 6 лет");
    //
    private JCheckBox check_box_employment_full = new JCheckBox("Полная занятость");
    private JCheckBox check_box_employment_part = new JCheckBox("Частичная занятость");
    private JCheckBox check_box_employment_project = new JCheckBox("Проектная работа");
    private JCheckBox check_box_employment_volunteer = new JCheckBox("Волонтерство");
    private JCheckBox check_box_employment_probation = new JCheckBox("Стажировка");
    //
    private JCheckBox check_box_schedule_full_day = new JCheckBox("Полный день");
    private JCheckBox check_box_schedule_shift = new JCheckBox("Сменный график");
    private JCheckBox check_box_schedule_flexible = new JCheckBox("Гибкий график");
    private JCheckBox check_box_schedule_remote = new JCheckBox("Удаленная работа");
    private JCheckBox check_box_schedule_fly_in_fly_out = new JCheckBox("Вахтовый метод");
    //
    private JRadioButton radio_btn_time_all_time = new JRadioButton("За всё время");
    private JRadioButton radio_btn_time_month = new JRadioButton("За месяц");
    private JRadioButton radio_btn_time_week = new JRadioButton("За неделю");
    private JRadioButton radio_btn_time_three_days = new JRadioButton("За последние 3 дня");
    private JRadioButton radio_btn_time_one_day = new JRadioButton("За сутки");
    ///////
    private JButton button_select_prof_area = new JButton("Указать профобласти");
    private JButton button_select_company_industries_n_scope = new JButton("Указать отрасли компании");
    private JButton button_select_area = new JButton("Указать регион");
    private JButton button_select_metro = new JButton("Указать станцию метро");
    //
    private JButton button_save_settings = new JButton("Сохранить настройки загрузки");
    ///////
    private JScrollPane scroll_pane_prof_area = new JScrollPane();
    private JScrollPane scroll_pane_company_industries_n_scope = new JScrollPane();
    private JScrollPane scroll_pane_area = new JScrollPane();
    private JScrollPane scroll_pane_metro = new JScrollPane();
    //
    //
    //
    //String[] array = {"111222333444555666777888999000xxxyyyzzz82155490000000001111111333444555666777888", "aaabbbcccaaabbbccc", "bbbaaacccbbbaaaccc", "111", "222", "333", "444"};
    String[] array = {"aaabbbcccaaabbbccc", "aaabbbcccaaabbbccc", "bbbaaacccbbbaaaccc"};
    private JList<String> list_prof_area = new JList<>(array);
    private JList<String> list_company_industries_n_scope = new JList<>(array);
    private JList<String> list_area = new JList<>(array);
    private JList<String> list_metro = new JList<>(array);


    // Listener for RadioButton's
    public void actionPerformed(ActionEvent e)
    {
        e.getSource();
    }

    public void itemStateChangedEmployment(ItemEvent e)
    {
        Object source = e.getItemSelectable();
        // Определяем какой именно из checkbox'ов был выбран
        if (check_box_employment_full.equals(source))
        {
            ;
        } else if (check_box_employment_part.equals(source))
        {
            ;
        } else if (check_box_employment_project.equals(source))
        {
            ;
        } else if (check_box_employment_volunteer.equals(source))
        {
            ;
        } else if (check_box_employment_probation.equals(source))
        {
            ;
        }
        // Если выбор снят, то убираем из параметров
        if (e.getStateChange() == ItemEvent.DESELECTED)
        {
            //c = '-';
        }
    }

    public void itemStateChangedSchedule(ItemEvent e)
    {
        Object source = e.getItemSelectable();
        // Определяем какой именно из checkbox'ов был выбран
        if (check_box_schedule_full_day.equals(source))
        {
            ;
        } else if (check_box_schedule_shift.equals(source))
        {
            ;
        } else if (check_box_schedule_flexible.equals(source))
        {
            ;
        } else if (check_box_schedule_remote.equals(source))
        {
            ;
        } else if (check_box_schedule_fly_in_fly_out.equals(source))
        {
            ;
        }
        // Если выбор снят, то убираем из параметров
        if (e.getStateChange() == ItemEvent.DESELECTED)
        {
            //c = '-';
        }
    }

    private String[] DisplayRelatedProfAreasInList()
    {
        ArrayList<String> prof_areas_n_specializations = new ArrayList<String>();
        for (int i = 0; i < LoadingParameters.prof_areas_names.size(); i++)
        {
            // Отфильтровываем нулевые строки
            if (LoadingParameters.prof_areas_names.get(i) != null)
            {
                // Добавляем ненулевые в конец ArrayList
                prof_areas_n_specializations.add(LoadingParameters.prof_areas_names.get(i) + " :: " + i);
            }
        }
        for (int i = 0; i < LoadingParameters.specializations_names.size(); i++)
        {
            // Отфильтровываем нулевые строки
            if (LoadingParameters.specializations_names.get(i) != null)
            {
                // Добавляем ненулевые в конец ArrayList
                prof_areas_n_specializations.add(LoadingParameters.specializations_names.get(i) + " :: " + i);
            }
        }
        return prof_areas_n_specializations.toArray(new String[prof_areas_n_specializations.size()]);
    }

    private String[] DisplayRelatedCompanyIndustriesInList()
    {
        ArrayList<String> company_industries_n_company_scopes = new ArrayList<String>();
        for (int i = 0; i < LoadingParameters.company_industries_names.size(); i++)
        {
            // Отфильтровываем нулевые строки
            if (LoadingParameters.company_industries_names.get(i) != null)
            {
                // Добавляем ненулевые в конец ArrayList
                company_industries_n_company_scopes.add(LoadingParameters.company_industries_names.get(i) + " :: " + i);
            }
        }
        for (int i = 0; i < LoadingParameters.company_scopes_names.size(); i++)
        {
            // Отфильтровываем нулевые строки
            if (LoadingParameters.company_scopes_names.get(i) != null)
            {
                // Добавляем ненулевые в конец ArrayList
                company_industries_n_company_scopes.add(LoadingParameters.company_scopes_names.get(i) + " :: " + i);
            }
        }
        return company_industries_n_company_scopes.toArray(new String[company_industries_n_company_scopes.size()]);
    }

    private String[] DisplayRelatedAreasInList()
    {
        ArrayList<String> areas = new ArrayList<String>();
        for (int i = 0; i < LoadingParameters.areas_names.size(); i++)
        {
            // Отфильтровываем нулевые строки
            if (LoadingParameters.areas_names.get(i) != null)
            {
                // Добавляем ненулевые в конец ArrayList
                areas.add(LoadingParameters.areas_names.get(i) + " :: " + i);
            }
        }
        return areas.toArray(new String[areas.size()]);
    }


    public Load_Settings_Frame()
    {
        setTitle("Loading Settings");
        setSize(1200, 1080);
        setLocationRelativeTo(null); // должно стоять после setSize
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //
        setLayout(new BorderLayout());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 20 20 10 10
        setModalityType(ModalityType.APPLICATION_MODAL);
        //
        //
        getContentPane().add(panel_west, BorderLayout.WEST);
        getContentPane().add(panel_center, BorderLayout.CENTER);
        getContentPane().add(panel_east, BorderLayout.EAST);
        getContentPane().add(panel_south, BorderLayout.SOUTH);
        // Fonts settings
        Font arial = new Font("Arial", Font.PLAIN, 16);
        //
        // Components Settings Panel West
        //
        label_key_words.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_key_words.setFont(arial);
        label_key_words.setBorder(BorderFactory.createEmptyBorder(10, 20, 50, 0));
        //
        label_prof_area.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_prof_area.setFont(arial);
        label_prof_area.setBorder(BorderFactory.createEmptyBorder(0, 20, 123, 0));
        //
        label_company_industries_n_scope.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_company_industries_n_scope.setFont(arial);
        label_company_industries_n_scope.setBorder(BorderFactory.createEmptyBorder(0, 20, 126, 0));
        //
        label_area.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_area.setFont(arial);
        label_area.setBorder(BorderFactory.createEmptyBorder(0, 20, 53, 0));
        //
        label_salary.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_salary.setFont(arial);
        label_salary.setBorder(BorderFactory.createEmptyBorder(0, 20, 50, 0));
        //
        label_metro.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_metro.setFont(arial);
        label_metro.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));
        //
        label_experience.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_experience.setFont(arial);
        label_experience.setBorder(BorderFactory.createEmptyBorder(0, 20, 113, 0));
        //
        label_employment.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_employment.setFont(arial);
        label_employment.setBorder(BorderFactory.createEmptyBorder(0, 20, 110, 0));
        //
        label_schedule.setAlignmentX(Component.LEFT_ALIGNMENT);
        label_schedule.setFont(arial);
        label_schedule.setBorder(BorderFactory.createEmptyBorder(0, 20, 75, 0));
        //                //
        //   Panel West   //
        //                //
        BoxLayout box_layout_west = new BoxLayout(panel_west, BoxLayout.Y_AXIS);
        panel_west.setLayout(box_layout_west);
        //
        panel_west.add(label_key_words);
        panel_west.add(label_prof_area);
        panel_west.add(label_company_industries_n_scope);
        panel_west.add(label_area);
        panel_west.add(label_salary);
        panel_west.add(label_metro);
        panel_west.add(label_experience);
        panel_west.add(label_employment);
        panel_west.add(label_schedule);
        //
        // Components Settings Panel Center
        //
        text_field_key_words.setMaximumSize(new Dimension(600, 40));
        button_select_prof_area.setFont(arial);
        button_select_prof_area.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ProfAreaSettingsFrame prof_area_settings_frame = new ProfAreaSettingsFrame();
                prof_area_settings_frame.setVisible(true);
                list_prof_area.setListData(DisplayRelatedProfAreasInList());
            }
        });
        button_select_company_industries_n_scope.setFont(arial);
        button_select_company_industries_n_scope.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CompanyIndustrySettingsFrame company_industry_settings_frame = new CompanyIndustrySettingsFrame();
                company_industry_settings_frame.setVisible(true);
                list_company_industries_n_scope.setListData(DisplayRelatedCompanyIndustriesInList());
            }
        });
        button_select_area.setFont(arial);
        button_select_area.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                AreasSettingsFrame areas_settings_frame = new AreasSettingsFrame();
                areas_settings_frame.setVisible(true);
                list_area.setListData(DisplayRelatedAreasInList());
            }
        });
        //
        panel_salary.setLayout(new FlowLayout());
        text_field_salary.setMaximumSize(new Dimension(100, 30));
        text_field_salary.setPreferredSize(new Dimension(100, 30));
        text_field_salary.setFont(arial);
        check_box_only_with_salary.setFont(arial);
        check_box_only_with_salary.setSelected(false);
        check_box_only_with_salary.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (check_box_only_with_salary.isSelected())
                {
                    // тогда добавляем в статический список параметро only_with_salary = true;
                }
            }
        });
        //
        panel_salary.add(text_field_salary);
        panel_salary.add(combo_box_currency_code);
        panel_salary.setMaximumSize(new Dimension(290, 35));
        panel_salary.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_salary.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 128));
        //
        button_select_metro.setFont(arial);
        ///////
        ButtonGroup button_group_experience = new ButtonGroup();
        button_group_experience.add(radio_btn_exprience_irrelvant);
        button_group_experience.add(radio_btn_exprience_null);
        button_group_experience.add(radio_btn_exprience_1_3);
        button_group_experience.add(radio_btn_exprience_3_6);
        button_group_experience.add(radio_btn_exprience_6_more);
        //
        radio_btn_exprience_irrelvant.setFont(arial);
        radio_btn_exprience_irrelvant.addActionListener(this::actionPerformed);
        radio_btn_exprience_null.setFont(arial);
        radio_btn_exprience_null.addActionListener(this::actionPerformed);
        radio_btn_exprience_1_3.setFont(arial);
        radio_btn_exprience_1_3.addActionListener(this::actionPerformed);
        radio_btn_exprience_3_6.setFont(arial);
        radio_btn_exprience_3_6.addActionListener(this::actionPerformed);
        radio_btn_exprience_6_more.setFont(arial);
        radio_btn_exprience_6_more.addActionListener(this::actionPerformed);
        //
        panel_radio_buttons_experience.add(radio_btn_exprience_irrelvant);
        panel_radio_buttons_experience.add(radio_btn_exprience_null);
        panel_radio_buttons_experience.add(radio_btn_exprience_1_3);
        panel_radio_buttons_experience.add(radio_btn_exprience_3_6);
        panel_radio_buttons_experience.add(radio_btn_exprience_6_more);
        //
        panel_radio_buttons_experience.setMaximumSize(new Dimension(295, 110));
        panel_radio_buttons_experience.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_radio_buttons_experience.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 130));
        ///////
        check_box_employment_full.setFont(arial);
        check_box_employment_full.addItemListener(this::itemStateChangedEmployment);
        check_box_employment_part.setFont(arial);
        check_box_employment_part.addItemListener(this::itemStateChangedEmployment);
        check_box_employment_project.setFont(arial);
        check_box_employment_project.addItemListener(this::itemStateChangedEmployment);
        check_box_employment_volunteer.setFont(arial);
        check_box_employment_volunteer.addItemListener(this::itemStateChangedEmployment);
        check_box_employment_probation.setFont(arial);
        check_box_employment_probation.addItemListener(this::itemStateChangedEmployment);
        //
        panel_checkboxes_employment.add(check_box_employment_full);
        panel_checkboxes_employment.add(check_box_employment_part);
        panel_checkboxes_employment.add(check_box_employment_project);
        panel_checkboxes_employment.add(check_box_employment_volunteer);
        panel_checkboxes_employment.add(check_box_employment_probation);
        //
        panel_checkboxes_employment.setMaximumSize(new Dimension(310, 110));
        panel_checkboxes_employment.setPreferredSize(new Dimension(310, 110));
        panel_checkboxes_employment.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_checkboxes_employment.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 130));
        ///////
        check_box_schedule_full_day.setFont(arial);
        check_box_schedule_full_day.addItemListener(this::itemStateChangedSchedule);
        check_box_schedule_shift.setFont(arial);
        check_box_schedule_shift.addItemListener(this::itemStateChangedSchedule);
        check_box_schedule_flexible.setFont(arial);
        check_box_schedule_flexible.addItemListener(this::itemStateChangedSchedule);
        check_box_schedule_remote.setFont(arial);
        check_box_schedule_remote.addItemListener(this::itemStateChangedSchedule);
        check_box_schedule_fly_in_fly_out.setFont(arial);
        check_box_schedule_fly_in_fly_out.addItemListener(this::itemStateChangedSchedule);
        //
        panel_checkboxes_schedule.add(check_box_schedule_full_day);
        panel_checkboxes_schedule.add(check_box_schedule_shift);
        panel_checkboxes_schedule.add(check_box_schedule_flexible);
        panel_checkboxes_schedule.add(check_box_schedule_remote);
        panel_checkboxes_schedule.add(check_box_schedule_fly_in_fly_out);
        //
        panel_checkboxes_schedule.setMaximumSize(new Dimension(300, 110));
        panel_checkboxes_schedule.setPreferredSize(new Dimension(300, 110));
        panel_checkboxes_schedule.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_checkboxes_schedule.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 130));
        //                  //
        //   Panel Center   //
        //                  //
        BoxLayout box_layout_center = new BoxLayout(panel_center, BoxLayout.Y_AXIS);
        panel_center.setLayout(box_layout_center);
        panel_center.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        //
        panel_center.add(text_field_key_words);
        panel_center.add(Box.createVerticalStrut(35));
        panel_center.add(button_select_prof_area);
        panel_center.add(Box.createVerticalStrut(113));
        panel_center.add(button_select_company_industries_n_scope);
        panel_center.add(Box.createVerticalStrut(115));
        panel_center.add(button_select_area);
        panel_center.add(Box.createVerticalStrut(37));
        panel_center.add(panel_salary);
        panel_center.add(check_box_only_with_salary);
        panel_center.add(Box.createVerticalStrut(15));
        panel_center.add(button_select_metro);
        panel_center.add(Box.createVerticalStrut(20));
        panel_center.add(panel_radio_buttons_experience);
        panel_center.add(Box.createVerticalStrut(20));
        panel_center.add(panel_checkboxes_employment);
        panel_center.add(Box.createVerticalStrut(20));
        panel_center.add(panel_checkboxes_schedule);
        //
        // Components Settings Panel East
        //
        scroll_pane_prof_area.setViewportView(list_prof_area);
        scroll_pane_prof_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_prof_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_prof_area.setPreferredSize(new Dimension(500, 123));
        scroll_pane_prof_area.getVerticalScrollBar().setUnitIncrement(8);
        //
        scroll_pane_company_industries_n_scope.setViewportView(list_company_industries_n_scope);
        scroll_pane_company_industries_n_scope.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_company_industries_n_scope.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_company_industries_n_scope.setPreferredSize(new Dimension(500, 123));
        scroll_pane_company_industries_n_scope.getVerticalScrollBar().setUnitIncrement(8);
        //
        scroll_pane_area.setViewportView(list_area);
        scroll_pane_area.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_area.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_area.setPreferredSize(new Dimension(500, 123));
        scroll_pane_area.getVerticalScrollBar().setUnitIncrement(8);
        //
        scroll_pane_metro.setViewportView(list_metro);
        scroll_pane_metro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_metro.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_metro.setPreferredSize(new Dimension(500, 123));
        scroll_pane_metro.getVerticalScrollBar().setUnitIncrement(8);
        ///////
        list_prof_area.setFont(arial);
        list_prof_area.setVisibleRowCount(5);
        list_prof_area.setEnabled(false);
        list_prof_area.setListData(DisplayRelatedProfAreasInList());
        //
        list_company_industries_n_scope.setFont(arial);
        list_company_industries_n_scope.setVisibleRowCount(5);
        list_company_industries_n_scope.setEnabled(false);
        list_company_industries_n_scope.setListData(DisplayRelatedCompanyIndustriesInList());
        //
        list_area.setFont(arial);
        list_area.setVisibleRowCount(5);
        list_area.setEnabled(false);
        //list_area.setListData(DisplayRelatedAreasInList());
        //
        list_metro.setFont(arial);
        list_metro.setVisibleRowCount(5);
        list_metro.setEnabled(false);
        //list_metro.setListData(DisplayRelatedProfAreasInList());
        ///////
        label_time.setFont(arial);
        label_time.setBorder(BorderFactory.createEmptyBorder(0, 5, 85, 0));
        //
        ButtonGroup button_group_time = new ButtonGroup();
        button_group_time.add(radio_btn_time_all_time);
        button_group_time.add(radio_btn_time_month);
        button_group_time.add(radio_btn_time_week);
        button_group_time.add(radio_btn_time_three_days);
        button_group_time.add(radio_btn_time_one_day);
        //
        radio_btn_time_all_time.setFont(arial);
        radio_btn_time_all_time.addActionListener(this::actionPerformed);
        radio_btn_time_month.setFont(arial);
        radio_btn_time_month.addActionListener(this::actionPerformed);
        radio_btn_time_week.setFont(arial);
        radio_btn_time_week.addActionListener(this::actionPerformed);
        radio_btn_time_three_days.setFont(arial);
        radio_btn_time_three_days.addActionListener(this::actionPerformed);
        radio_btn_time_one_day.setFont(arial);
        radio_btn_time_one_day.addActionListener(this::actionPerformed);
        //
        panel_radio_buttons_time.add(radio_btn_time_all_time);
        panel_radio_buttons_time.add(radio_btn_time_month);
        panel_radio_buttons_time.add(radio_btn_time_week);
        panel_radio_buttons_time.add(radio_btn_time_three_days);
        panel_radio_buttons_time.add(radio_btn_time_one_day);
        //
        panel_radio_buttons_time.setPreferredSize(new Dimension(260, 110));
        panel_radio_buttons_time.setAlignmentX(Component.RIGHT_ALIGNMENT);
        ///////
        panel_prof_area.add(scroll_pane_prof_area);
        panel_company_industries_n_scope.add(scroll_pane_company_industries_n_scope);
        panel_area.add(scroll_pane_area);
        panel_metro.add(scroll_pane_metro);
        panel_time.setLayout(new BorderLayout());
        panel_time.add(label_time, BorderLayout.WEST);
        panel_time.add(panel_radio_buttons_time, BorderLayout.EAST);
        //                //
        //   Panel East   //
        //                //
        GridBagLayout grid_bag_layout = new GridBagLayout();
        panel_east.setLayout(grid_bag_layout);
        panel_east.setBorder(BorderFactory.createEmptyBorder(0, 0, 188, 10));
        GridBagConstraints c = new GridBagConstraints();
        //
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(70, 0, 10, 0); // 22
        panel_east.add(panel_prof_area, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 10, 0);
        panel_east.add(panel_company_industries_n_scope, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 10, 0);
        panel_east.add(panel_area, c);
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 50, 0);
        panel_east.add(panel_metro, c);
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        panel_east.add(panel_time, c);
        //
        // Components Settings Panel South
        //
        button_save_settings.setFont(arial);
        button_save_settings.setPreferredSize(new Dimension(this.getSize().width, button_save_settings.getPreferredSize().height));
        button_save_settings.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Do something
                // Сохраняем параметры с экрана в статический класс с настройками
            }
        });
        //
        // Panel South
        //
        panel_south.add(button_save_settings);
    }
}