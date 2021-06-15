package by.barsnik96.HeadHunter_JSP.view;

import by.barsnik96.HeadHunter_JSP.domain.CompanyIndustry;
import by.barsnik96.HeadHunter_JSP.domain.CompanyScope;
import by.barsnik96.HeadHunter_JSP.service.CompanyIndustryServiceImpl;
import by.barsnik96.HeadHunter_JSP.service.CompanyScopeServiceImpl;
import by.barsnik96.HeadHunter_JSP.utils.BeanProvider;
import by.barsnik96.HeadHunter_JSP.utils.LoadingParameters;
import by.barsnik96.HeadHunter_JSP.utils.RequestParameters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyIndustrySettingsFrame extends JDialog
{
    private JScrollPane scroll_pane_company_industries = new JScrollPane();
    private JPanel panel_company_industries = new JPanel();
    private JPanel panel_buttons = new JPanel();
    ///////
    private ArrayList<JCheckBox> check_boxes_company_industries = new ArrayList<JCheckBox>();
    private ArrayList<JList<CompanyScope>> lists_company_scopes = new ArrayList<JList<CompanyScope>>();
    ///////
    private JButton button_select = new JButton("Выбрать");
    private JButton button_cancel = new JButton("Отменить");
    ///////
    private CompanyIndustryServiceImpl companyIndustryService;
    private CompanyScopeServiceImpl companyScopeService;
    ///////
    private ArrayList<CompanyIndustry> company_industries = new ArrayList<CompanyIndustry>();
    ///////
    // Fonts settings
    private Font arial = new Font("Arial", Font.PLAIN, 16);


    public void stateChanged_CompanyIndustry_JCheckBox(ActionEvent e)
    {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        //
        for (int i = 0; i < check_boxes_company_industries.size(); i++)
        {
            // Находим checkbox который был нажат
            if (check_boxes_company_industries.get(i).equals(checkBox))
            {
                if (checkBox.isSelected()) // check_boxes_company_industries.get(i)
                {
                    lists_company_scopes.get(i).addSelectionInterval(0, lists_company_scopes.get(i).getModel().getSize()-1);
                }
                else
                {
                    lists_company_scopes.get(i).clearSelection();
                }
            }
        }
    }

    public void valueChanged_CompanyScope_JList(ListSelectionEvent e)
    {
        JList<CompanyScope> list = (JList<CompanyScope>) e.getSource();
        ListSelectionModel list_selection_model = list.getSelectionModel();
        if(!e.getValueIsAdjusting())
        {
            // Находим какому JList в списке соответствует тот JList, из которого пришло событие
            for (int i = 0; i < lists_company_scopes.size(); i++)
            {
                if (lists_company_scopes.get(i).equals(list))
                {
                    // Если размер выбранных индексов не совпадает с размеров всех индексов
                    // Значит с какого-то элемента был снят выбор
                    if (list_selection_model.getSelectedIndices().length != list.getModel().getSize())
                    {
                        // Убираем выбор с соответствующего JCheckBox
                        check_boxes_company_industries.get(i).setSelected(false);
                    }
                    else // Если все компоненты были выбраны (имеется в виду вручную)
                    {
                        // Выбираем соответствующий JCheckBox
                        check_boxes_company_industries.get(i).setSelected(true);
                    }
                }
            }
        }
    }

    private static ListCellRenderer<? super CompanyScope> createListRenderer() {
        return new DefaultListCellRenderer() {
            //private Color background = new Color(0, 100, 255, 15);
            //private Color defaultBackground = (Color) UIManager.get("List.background");

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel)
                {
                    JLabel label = (JLabel) c;
                    CompanyScope company_scope = (CompanyScope) value;
                    label.setText(company_scope.getName());
                    //if (!isSelected) {
                    //    label.setBackground(index % 2 == 0 ? background : defaultBackground);
                    //}
                }
                return c;
            }
        };
    }

    private void CreateGUI() throws SQLException
    {
        // Получаем все CompanyIndustries из БД
        company_industries = (ArrayList<CompanyIndustry>) companyIndustryService.companyIndustryRepository.findAll();
        //
        for (int i = 0; i < company_industries.size(); i++)
        {
            // Получаем объект CompanyIndustry из списка
            CompanyIndustry company_industry = company_industries.get(i);
            // Получаем для него все CompanyScopes из БД
            ArrayList<CompanyScope> company_scopes = (ArrayList<CompanyScope>) companyScopeService.companyScopeRepository.findAllByCompanyIndustry(company_industry);
            // Создаём новый JCheckBox
            check_boxes_company_industries.add(i, new JCheckBox(company_industry.getName()));
            // Настраиваем параметры JCheckBox
            check_boxes_company_industries.get(i).setFont(arial);
            check_boxes_company_industries.get(i).addActionListener(this::stateChanged_CompanyIndustry_JCheckBox);
            // Настраиваем параметры GridBagConstraints для JCheckBox
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.ipady = 0;
            constraints.gridx = 0;
            constraints.gridy = (i * 2);
            constraints.insets = new Insets(0, 0, 10, 0);
            // Размещаем JCheckBox на панели
            panel_company_industries.add(check_boxes_company_industries.get(i), constraints);
            // Создаём новый JList
            lists_company_scopes.add(i, new JList<>(company_scopes.toArray(new CompanyScope[company_scopes.size()])));
            // Настраиваем параметры JList<CompanyScope>
            lists_company_scopes.get(i).setFont(arial);
            lists_company_scopes.get(i).setCellRenderer(createListRenderer());
            lists_company_scopes.get(i).setSelectedIndex(-1);
            lists_company_scopes.get(i).setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            lists_company_scopes.get(i).addListSelectionListener(this::valueChanged_CompanyScope_JList);
            // Настраиваем параметры GridBagConstraints для JList<CompanyScope>
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.ipady = 0;
            constraints.gridx = 0;
            constraints.gridy = ((i * 2) + 1);
            constraints.insets = new Insets(0, 20, 10, 0);
            // Размещаем JList<CompanyScope> на панели
            panel_company_industries.add(lists_company_scopes.get(i), constraints);
        }
        ///////
        // Обновление выборов при загрузке на основе параметров из статического глобального класса LoadingParameters
        //
        // Сумма размеров всех предыдущих JList
        int sum_all_prev_lists_sizes = 0;
        //
        for (int i = 0; i < company_industries.size(); i++)
        {
            // Если этот JCheckBox уже был выбран в прошлый раз
            if (LoadingParameters.company_industries_names.get(i) != null)
            {
                // Выбираем его снова
                check_boxes_company_industries.get(i).doClick();
                // Связанный с ним JList выделится автоматически ???????
            }
            else // Если он не был выбран, выбираем строки связанного с ним JList
            {
                for (int j = sum_all_prev_lists_sizes; j < sum_all_prev_lists_sizes + lists_company_scopes.get(i).getModel().getSize(); j++)
                {
                    // Если эта строка была выбрана в прошлый раз
                    if (LoadingParameters.company_scopes_names.get(j) != null)
                    {
                        // Конвертируем глобальный индекс в локальный
                        int converted_to_local_index = (j - sum_all_prev_lists_sizes);
                        // Выбираем её снова
                        lists_company_scopes.get(i).addSelectionInterval(converted_to_local_index, converted_to_local_index);;
                    }
                }
            }
            // Увеличиваем счётчик
            sum_all_prev_lists_sizes += lists_company_scopes.get(i).getModel().getSize();
        }
    }

    private void UpdateParameters()
    {
        for (int i = 0; i < check_boxes_company_industries.size(); i++)
        {
            if (check_boxes_company_industries.get(i).isSelected())
            {
                // обновляем глобальные параметры окна
                LoadingParameters.company_industries_names.set(i, company_industries.get(i).getName());
                // обновляем глобальные параметры запроса
                RequestParameters.company_industries_ids.set(i, (double) company_industries.get(i).getId());
            }
            else
            {
                // обновляем в null глобальные параметры окна
                LoadingParameters.company_industries_names.set(i, null);
                // обновляем в null глобальные параметры запроса
                RequestParameters.company_industries_ids.set(i, null);
            }
        }
        //
        int counter_for_loading_parameters = 0;
        int counter_for_request_parameters = check_boxes_company_industries.size();
        // Для каждого JList из списка
        for (int i = 0; i < lists_company_scopes.size(); i++)
        {
            // Если не выбран JCheckBox, привязанный к этому JList
            if(!check_boxes_company_industries.get(i).isSelected())
            {
                JList<CompanyScope> list = lists_company_scopes.get(i);
                // Получаем выбранные индексы (соответствующие реальному порядку строк)
                int[] indices = list.getSelectedIndices();
                // Для каждой выбранной строки в конкретном JList (по индексу)
                for (int j = 0; j < indices.length; j++)
                {
                    // indices[j] - номер строки в конкретном списке, а при добавлении нужно указать номер глобальный
                    // new index = counter + indices[j]
                    //
                    // обновляем глобальные параметры окна
                    LoadingParameters.company_scopes_names.set(counter_for_loading_parameters + indices[j],
                            list.getModel().getElementAt(indices[j]).getName());
                    // обновляем глобальные параметры запроса
                    RequestParameters.company_industries_ids.set(counter_for_request_parameters + indices[j],
                            Double.valueOf(company_industries.get(i).getId() + "." + list.getModel().getElementAt(indices[j]).getId()));
                }
                // Получаем невыбранные индексы и обновляем их в null
                for (int k = 0; k < list.getModel().getSize(); k++)
                {
                    if (!list.isSelectedIndex(k))
                    {
                        // добавляем null в глобальные параметры окна
                        LoadingParameters.company_scopes_names.set(counter_for_loading_parameters + k, null);
                        // добавляем null в глобальные параметры запроса
                        RequestParameters.company_industries_ids.set(counter_for_request_parameters + k, null);
                    }
                }
            }
            // Если выбран JCheckBox, привязанный к этому JList
            // То нужно сбросить все строки связанного JList в null
            else
            {
                for (int j = 0; j < lists_company_scopes.get(i).getModel().getSize(); j++)
                {
                    // обновляем в null глобальные параметры окна
                    LoadingParameters.company_scopes_names.set(counter_for_loading_parameters + j, null);
                    // обновляем в null глобальные параметры запроса
                    RequestParameters.company_industries_ids.set(counter_for_request_parameters + j, null);
                }
            }
            // Увеличиваем счётчики на размер пройденного списка
            counter_for_loading_parameters += lists_company_scopes.get(i).getModel().getSize();
            counter_for_request_parameters += lists_company_scopes.get(i).getModel().getSize();
        }
    }



    public CompanyIndustrySettingsFrame()
    {
        setTitle("Select CompanyIndustries");
        setSize(1200, 1080);
        setLocationRelativeTo(null); // должно стоять после setSize
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        ///////
        // Creating components
        panel_company_industries.setLayout(new GridBagLayout());
        //
        scroll_pane_company_industries.setViewportView(panel_company_industries);
        scroll_pane_company_industries.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_company_industries.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_company_industries.getVerticalScrollBar().setUnitIncrement(16);
        ///////
        // Adding panels and set default borders
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(scroll_pane_company_industries, BorderLayout.CENTER);
        getContentPane().add(panel_buttons, BorderLayout.SOUTH);
        ///////
        // Services autowiring
        BeanProvider.autowire(this);
        this.companyIndustryService = BeanProvider.applicationContext.getBean(CompanyIndustryServiceImpl.class);
        this.companyScopeService = BeanProvider.applicationContext.getBean(CompanyScopeServiceImpl.class);
        ///////
        // Create GUI
        try
        {
            CreateGUI();
        }
        catch (SQLException sql_ex) {}
        //
        button_cancel.setFont(arial);
        button_cancel.setMargin(new Insets(10, 13, 10, 13));
        button_cancel.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        button_select.setFont(arial);
        button_select.setMargin(new Insets(10, 13, 10, 13));
        button_select.setForeground(Color.WHITE);
        button_select.setBackground(new Color(30, 136, 229));
        button_select.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                UpdateParameters();
                dispose();
            }
        });
        FlowLayout fl_layout = new FlowLayout();
        fl_layout.setHgap(20);
        panel_buttons.setLayout(fl_layout);
        panel_buttons.setBorder(BorderFactory.createEmptyBorder(10, 900, 0, 0));
        panel_buttons.add(button_cancel);
        panel_buttons.add(button_select);
    }
}