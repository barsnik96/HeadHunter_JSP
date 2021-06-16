package by.barsnik96.HeadHunter_JSP.view;

import by.barsnik96.HeadHunter_JSP.domain.*;
import by.barsnik96.HeadHunter_JSP.service.MetroLineServiceImpl;
import by.barsnik96.HeadHunter_JSP.service.MetroStationServiceImpl;
import by.barsnik96.HeadHunter_JSP.utils.BeanProvider;
import by.barsnik96.HeadHunter_JSP.utils.LoadingParameters;
import by.barsnik96.HeadHunter_JSP.utils.RequestParameters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class MetroSettingsFrame extends JDialog
{
    private JScrollPane scroll_pane_metro = new JScrollPane();
    private JPanel panel_metro = new JPanel();
    private JPanel panel_buttons = new JPanel();
    ///////
    private ArrayList<JCheckBox> check_boxes_metro_lines = new ArrayList<JCheckBox>();
    private ArrayList<JList<MetroStation>> lists_metro_stations = new ArrayList<JList<MetroStation>>();
    ///////
    private JButton button_select = new JButton("Выбрать");
    private JButton button_cancel = new JButton("Отменить");
    ///////
    private MetroLineServiceImpl metroLineService;
    private MetroStationServiceImpl metroStationService;
    ///////
    private ArrayList<MetroLine> metro_lines = new ArrayList<MetroLine>();
    ///////
    private Area metro_area;
    ///////
    // Fonts settings
    private Font arial = new Font("Arial", Font.PLAIN, 16);

    public void stateChanged_MetroLine_JCheckBox(ActionEvent e)
    {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        //
        for (int i = 0; i < check_boxes_metro_lines.size(); i++)
        {
            // Находим checkbox который был нажат
            if (check_boxes_metro_lines.get(i).equals(checkBox))
            {
                if (checkBox.isSelected())
                {
                    lists_metro_stations.get(i).addSelectionInterval(0, lists_metro_stations.get(i).getModel().getSize()-1);
                }
                else
                {
                    lists_metro_stations.get(i).clearSelection();
                }
            }
        }
    }

    public void valueChanged_MetroStation_JList(ListSelectionEvent e)
    {
        /////// Это для случая, если сначала выбрали JCheckBox (соот-но выбрались все строки JList),
        /////// а потом сняли выбор с 1 из строк JList
        /////// В таком случае выбор с JCheckBox должен убираться, но без клика, чтобы не убрать все остальные строки JList - так не получится
        JList<MetroStation> list = (JList<MetroStation>) e.getSource();
        ListSelectionModel list_selection_model = list.getSelectionModel();
        if(!e.getValueIsAdjusting())
        {
            // Находим какому JList в списке соответствует тот JList, из которого пришло событие
            for (int i = 0; i < lists_metro_stations.size(); i++)
            {
                if (lists_metro_stations.get(i).equals(list))
                {
                    // Если размер выбранных индексов не совпадает с размеров всех индексов
                    // Значит с какого-то элемента был снят выбор
                    if (list_selection_model.getSelectedIndices().length != list.getModel().getSize())
                    {
                        // Убираем выбор с соответствующего JCheckBox
                        check_boxes_metro_lines.get(i).setSelected(false);
                    }
                    else // Если все компоненты были выбраны (имеется в виду вручную)
                    {
                        // Выбираем соответствующий JCheckBox
                        check_boxes_metro_lines.get(i).setSelected(true);
                    }
                }
            }
        }
    }

    private static ListCellRenderer<? super MetroStation> createListRenderer(Color metro_line_color) {
        return new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel)
                {
                    JLabel label = (JLabel) c;
                    MetroStation metro_station = (MetroStation) value;
                    label.setText(metro_station.getName());
                    //
                    Icon icon_metro_line_color = new Icon() {
                        @Override
                        public void paintIcon(Component c, Graphics g, int x, int y) {
                            Graphics2D g2d = ( Graphics2D ) g;
                            //g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
                            java.awt.geom.Area circle = new java.awt.geom.Area(new Ellipse2D.Double ( 10, 4, 10, 10 ));
                            g2d.setPaint(metro_line_color);
                            g2d.fill(circle);
                        }

                        @Override
                        public int getIconWidth() {
                            return 0;
                        }

                        @Override
                        public int getIconHeight() {
                            return 0;
                        }
                    };
                    label.setIcon(icon_metro_line_color);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 5));
                }
                return c;
            }
        };
    }


    private void CreateGUI()
    {
        // Получаем все MetroLine's из БД
        metro_lines = (ArrayList<MetroLine>) metroLineService.metroLineRepository.findAllByArea(metro_area);
        //
        for (int i = 0; i < metro_lines.size(); i++)
        {
            // Получаем объект MetroLine из списка
            MetroLine metro_line = metro_lines.get(i);
            // Получаем для него все MetroStation's из БД
            ArrayList<MetroStation> metro_stations = (ArrayList<MetroStation>) metroStationService.metroStationRepository.findAllByLine(metro_line);
            // Создаём новый JCheckBox
            check_boxes_metro_lines.add(i, new JCheckBox(metro_line.getName()));
            // Настраиваем параметры JCheckBox
            check_boxes_metro_lines.get(i).setFont(arial);
            check_boxes_metro_lines.get(i).addActionListener(this::stateChanged_MetroLine_JCheckBox);
            // Настраиваем параметры GridBagConstraints для JCheckBox
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.ipady = 0;
            constraints.gridx = 0;
            constraints.gridy = (i * 2);
            constraints.insets = new Insets(0, 0, 10, 0);
            // Размещаем JCheckBox на панели
            panel_metro.add(check_boxes_metro_lines.get(i), constraints);
            // Создаём новый JList
            lists_metro_stations.add(i, new JList<>(metro_stations.toArray(new MetroStation[metro_stations.size()])));
            // Настраиваем параметры JList<MetroStation>
            lists_metro_stations.get(i).setFont(arial);
            // Цвет получаем из MetroLine, затем декодируем из HEX в RGB
            lists_metro_stations.get(i).setCellRenderer(createListRenderer(Color.decode(metro_line.getColor())));
            lists_metro_stations.get(i).setSelectedIndex(-1);
            lists_metro_stations.get(i).setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            lists_metro_stations.get(i).addListSelectionListener(this::valueChanged_MetroStation_JList);
            // Настраиваем параметры GridBagConstraints для JList<MetroStation>
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.ipady = 0;
            constraints.gridx = 0;
            constraints.gridy = ((i * 2) + 1);
            constraints.insets = new Insets(0, 20, 10, 0);
            // Размещаем JList<MetroStation> на панели
            panel_metro.add(lists_metro_stations.get(i), constraints);
        }
        ///////
        // Обновление выборов при загрузке на основе параметров из статического глобального класса LoadingParameters
        //
        // Сумма размеров всех предыдущих JList
        int sum_all_prev_lists_sizes = 0;
        //
        for (int i = 0; i < metro_lines.size(); i++)
        {
            // Если этот JCheckBox уже был выбран в прошлый раз
            if (LoadingParameters.metro_lines_names.get(i) != null)
            {
                // Выбираем его снова
                check_boxes_metro_lines.get(i).doClick();
                // Связанный с ним JList выделится автоматически
            }
            else // Если он не был выбран, выбираем строки связанного с ним JList
            {
                for (int j = sum_all_prev_lists_sizes; j < sum_all_prev_lists_sizes + lists_metro_stations.get(i).getModel().getSize(); j++)
                {
                    // Если эта строка была выбрана в прошлый раз
                    if (LoadingParameters.metro_stations_names.get(j) != null)
                    {
                        // Конвертируем глобальный индекс в локальный
                        int converted_to_local_index = (j - sum_all_prev_lists_sizes);
                        // Выбираем её снова
                        lists_metro_stations.get(i).addSelectionInterval(converted_to_local_index, converted_to_local_index);;
                    }
                }
            }
            // Увеличиваем счётчик
            sum_all_prev_lists_sizes += lists_metro_stations.get(i).getModel().getSize();
        }
    }

    private void UpdateParameters()
    {
        for (int i = 0; i < check_boxes_metro_lines.size(); i++)
        {
            if (check_boxes_metro_lines.get(i).isSelected())
            {
                // обновляем глобальные параметры окна
                LoadingParameters.metro_lines_names.set(i, metro_lines.get(i).getName() + "_" + metro_lines.get(i).getColor());
                // обновляем глобальные параметры запроса
                RequestParameters.metro_ids.set(i, (double) metro_lines.get(i).getId());
            }
            else
            {
                // обновляем в null глобальные параметры окна
                LoadingParameters.metro_lines_names.set(i, null);
                // обновляем в null глобальные параметры запроса
                RequestParameters.metro_ids.set(i, null);
            }
        }
        //
        int counter_for_loading_parameters = 0;
        int counter_for_request_parameters = check_boxes_metro_lines.size();
        // Для каждого JList из списка
        for (int i = 0; i < lists_metro_stations.size(); i++)
        {
            // Если не выбран JCheckBox, привязанный к этому JList
            if(!check_boxes_metro_lines.get(i).isSelected())
            {
                JList<MetroStation> list = lists_metro_stations.get(i);
                // Получаем выбранные индексы (соответствующие реальному порядку строк)
                int[] indices = list.getSelectedIndices();
                // Для каждой выбранной строки в конкретном JList (по индексу)
                for (int j = 0; j < indices.length; j++)
                {
                    // indices[j] - номер строки в конкретном списке, а при добавлении нужно указать номер глобальный
                    // new index = counter + indices[j]
                    //
                    // обновляем глобальные параметры окна
                    LoadingParameters.metro_stations_names.set(counter_for_loading_parameters + indices[j],
                            list.getModel().getElementAt(indices[j]).getName()
                                    + "_" + list.getModel().getElementAt(indices[j]).getLine().getColor());
                    // обновляем глобальные параметры запроса
                    RequestParameters.metro_ids.set(counter_for_request_parameters + indices[j],
                            Double.valueOf(metro_lines.get(i).getId() + "." + list.getModel().getElementAt(indices[j]).getId()));
                }
                // Получаем невыбранные индексы и обновляем их в null
                for (int k = 0; k < list.getModel().getSize(); k++)
                {
                    if (!list.isSelectedIndex(k))
                    {
                        // обновляем в null глобальные параметры окна
                        LoadingParameters.metro_stations_names.set(counter_for_loading_parameters + k, null);
                        // обновляем в null глобальные параметры запроса
                        RequestParameters.metro_ids.set(counter_for_request_parameters + k, null);
                    }
                }
            }
            // Если выбран JCheckBox, привязанный к этому JList
            // То нужно сбросить все строки связанного JList в null
            else
            {
                for (int j = 0; j < lists_metro_stations.get(i).getModel().getSize(); j++)
                {
                    // обновляем в null глобальные параметры окна
                    LoadingParameters.metro_stations_names.set(counter_for_loading_parameters + j, null);
                    // обновляем в null глобальные параметры запроса
                    RequestParameters.metro_ids.set(counter_for_request_parameters + j, null);
                }
            }
            // Увеличиваем счётчики на размер пройденного списка
            counter_for_loading_parameters += lists_metro_stations.get(i).getModel().getSize();
            counter_for_request_parameters += lists_metro_stations.get(i).getModel().getSize();
        }
    }


    public MetroSettingsFrame(Area metro_area)
    {
        setTitle("Select Metro Lines and Stations");
        setSize(1200, 1080);
        setLocationRelativeTo(null); // должно стоять после setSize
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        ///////
        // Creating components
        panel_metro.setLayout(new GridBagLayout());
        //
        scroll_pane_metro.setViewportView(panel_metro);
        scroll_pane_metro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_metro.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_metro.getVerticalScrollBar().setUnitIncrement(16);
        //scroll_pane_prof_areas.setPreferredSize(new Dimension(500, 123));
        ///////
        // Adding panels and set default borders
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(scroll_pane_metro, BorderLayout.CENTER);
        getContentPane().add(panel_buttons, BorderLayout.SOUTH);
        ///////
        // Services autowiring
        BeanProvider.autowire(this);
        this.metroLineService = BeanProvider.applicationContext.getBean(MetroLineServiceImpl.class);
        this.metroStationService = BeanProvider.applicationContext.getBean(MetroStationServiceImpl.class);
        //
        this.metro_area = metro_area;
        ///////
        // Create GUI
        try
        {
            CreateGUI();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
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