package by.barsnik96.HeadHunter_JSP.view;

import by.barsnik96.HeadHunter_JSP.domain.ProfArea;
import by.barsnik96.HeadHunter_JSP.domain.Specialization;
import by.barsnik96.HeadHunter_JSP.service.ProfAreaServiceImpl;
import by.barsnik96.HeadHunter_JSP.service.SpecializationServiceImpl;
import by.barsnik96.HeadHunter_JSP.utils.BeanProvider;
import by.barsnik96.HeadHunter_JSP.utils.LoadingParameters;
import by.barsnik96.HeadHunter_JSP.utils.RequestParameters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.*;
import java.util.ArrayList;

public class ProfAreaSettingsFrame extends JDialog
{
    private JScrollPane scroll_pane_prof_areas = new JScrollPane();
    private JPanel panel_prof_areas = new JPanel();
    private JPanel panel_buttons = new JPanel();
    ///////
    private ArrayList<JCheckBox> check_boxes_prof_areas = new ArrayList<JCheckBox>();
    private ArrayList<JList<Specialization>> lists_specializations = new ArrayList<JList<Specialization>>();
    ///////
    private JButton button_select = new JButton("Выбрать");
    private JButton button_cancel = new JButton("Отменить");
    ///////
    private ProfAreaServiceImpl profAreaService;
    private SpecializationServiceImpl specializationService;
    ///////
    private ArrayList<ProfArea> prof_areas = new ArrayList<ProfArea>();
    ///////
    // Fonts settings
    private Font arial = new Font("Arial", Font.PLAIN, 16);




    public void stateChanged_ProfArea_JCheckBox(ActionEvent e)
    {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        //
        for (int i = 0; i < check_boxes_prof_areas.size(); i++)
        {
            // Находим checkbox который был нажат
            if (check_boxes_prof_areas.get(i).equals(checkBox))
            {
                if (checkBox.isSelected()) // check_boxes_prof_areas.get(i)
                {
                    lists_specializations.get(i).addSelectionInterval(0, lists_specializations.get(i).getModel().getSize()-1);
                }
                else
                {
                    lists_specializations.get(i).clearSelection();
                }
            }
        }
    }

    public void valueChanged_Specialization_JList(ListSelectionEvent e)
    {
        /////// Это для случая, если сначала выбрали JCheckBox (соот-но выбрались все строки JList),
        /////// а потом сняли выбор с 1 из строк JList
        /////// В таком случае выбор с JCheckBox должен убираться, но без клика, чтобы не убрать все остальные строки JList - так не получится
        JList<Specialization> list = (JList<Specialization>) e.getSource();
        ListSelectionModel list_selection_model = list.getSelectionModel();
        if(!e.getValueIsAdjusting())
        {
            // Находим какому JList в списке соответствует тот JList, из которого пришло событие
            for (int i = 0; i < lists_specializations.size(); i++)
            {
                if (lists_specializations.get(i).equals(list))
                {
                    // Если размер выбранных индексов не совпадает с размеров всех индексов
                    // Значит с какого-то элемента был снят выбор
                    if (list_selection_model.getSelectedIndices().length != list.getModel().getSize())
                    {
                        // Убираем выбор с соответствующего JCheckBox
                        check_boxes_prof_areas.get(i).setSelected(false);
                    }
                    else // Если все компоненты были выбраны (имеется в виду вручную)
                    {
                        // Выбираем соответствующий JCheckBox
                        check_boxes_prof_areas.get(i).setSelected(true);
                    }
                }
            }
        }
    }

    private static ListCellRenderer<? super Specialization> createListRenderer() {
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
                    Specialization specialization = (Specialization) value;
                    label.setText(specialization.getName());
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
        // Получаем все ProfAreas из БД
        prof_areas = (ArrayList<ProfArea>) profAreaService.profAreaRepository.findAll();
        //
        for (int i = 0; i < prof_areas.size(); i++)
        {
            // Получаем объект ProfArea из списка
            ProfArea prof_area = prof_areas.get(i);
            // Получаем для него все Specialization's из БД
            ArrayList<Specialization> specializations = (ArrayList<Specialization>) specializationService.specializationRepository.findAllByProfArea(prof_area);
            // Создаём новый JCheckBox
            check_boxes_prof_areas.add(i, new JCheckBox(prof_area.getName()));
            // Настраиваем параметры JCheckBox
            check_boxes_prof_areas.get(i).setFont(arial);
            check_boxes_prof_areas.get(i).addActionListener(this::stateChanged_ProfArea_JCheckBox);
            // Настраиваем параметры GridBagConstraints для JCheckBox
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.ipady = 0;
            constraints.gridx = 0;
            constraints.gridy = (i * 2);
            constraints.insets = new Insets(0, 0, 10, 0);
            // Размещаем JCheckBox на панели
            panel_prof_areas.add(check_boxes_prof_areas.get(i), constraints);
            // Создаём новый JList
            lists_specializations.add(i, new JList<>(specializations.toArray(new Specialization[specializations.size()])));
            // Настраиваем параметры JList<Specialization>
            lists_specializations.get(i).setFont(arial);
            lists_specializations.get(i).setCellRenderer(createListRenderer());
            lists_specializations.get(i).setSelectedIndex(-1);
            lists_specializations.get(i).setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            //lists_specializations.get(i).setVisibleRowCount(5); /// ??? не работает,т.к. не добавлен на отдельную панель
            lists_specializations.get(i).addListSelectionListener(this::valueChanged_Specialization_JList);
            // Настраиваем параметры GridBagConstraints для JList<Specialization>
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.ipady = 0;
            constraints.gridx = 0;
            constraints.gridy = ((i * 2) + 1);
            constraints.insets = new Insets(0, 20, 10, 0);
            // Размещаем JList<Specialization> на панели
            panel_prof_areas.add(lists_specializations.get(i), constraints);
        }
        ///////
        // Обновление выборов при загрузке на основе параметров из статического глобального класса LoadingParameters
        //
        // Сумма размеров всех предыдущих JList
        int sum_all_prev_lists_sizes = 0;
        //
        for (int i = 0; i < prof_areas.size(); i++)
        {
            // Если этот JCheckBox уже был выбран в прошлый раз
            if (LoadingParameters.prof_areas_names.get(i) != null)
            {
                // Выбираем его снова
                check_boxes_prof_areas.get(i).doClick();
                // Связанный с ним JList выделится автоматически
            }
            else // Если он не был выбран, выбираем строки связанного с ним JList
            {
                for (int j = sum_all_prev_lists_sizes; j < sum_all_prev_lists_sizes + lists_specializations.get(i).getModel().getSize(); j++)
                {
                    // Если эта строка была выбрана в прошлый раз
                    if (LoadingParameters.specializations_names.get(j) != null)
                    {
                        // Конвертируем глобальный индекс в локальный
                        int converted_to_local_index = (j - sum_all_prev_lists_sizes);
                        // Выбираем её снова
                        lists_specializations.get(i).addSelectionInterval(converted_to_local_index, converted_to_local_index);;
                    }
                }
            }
            // Увеличиваем счётчик
            sum_all_prev_lists_sizes += lists_specializations.get(i).getModel().getSize();
        }
    }

    private void UpdateParameters()
    {
        for (int i = 0; i < check_boxes_prof_areas.size(); i++)
        {
            if (check_boxes_prof_areas.get(i).isSelected())
            {
                // обновляем глобальные параметры окна
                LoadingParameters.prof_areas_names.set(i, prof_areas.get(i).getName());
                // обновляем глобальные параметры запроса
                RequestParameters.specializations_ids.set(i, (double) prof_areas.get(i).getId());
            }
            else
            {
                // обновляем в null глобальные параметры окна
                LoadingParameters.prof_areas_names.set(i, null);
                // обновляем в null глобальные параметры запроса
                RequestParameters.specializations_ids.set(i, null);
            }
        }
        //
        int counter_for_loading_parameters = 0;
        int counter_for_request_parameters = check_boxes_prof_areas.size();
        // Для каждого JList из списка
        for (int i = 0; i < lists_specializations.size(); i++)
        {
            // Если не выбран JCheckBox, привязанный к этому JList
            if(!check_boxes_prof_areas.get(i).isSelected())
            {
                JList<Specialization> list = lists_specializations.get(i);
                // Получаем выбранные индексы (соответствующие реальному порядку строк)
                int[] indices = list.getSelectedIndices();
                // Для каждой выбранной строки в конкретном JList (по индексу)
                for (int j = 0; j < indices.length; j++)
                {
                    // indices[j] - номер строки в конкретном списке, а при добавлении нужно указать номер глобальный
                    // new index = counter + indices[j]
                    //
                    // обновляем глобальные параметры окна
                    LoadingParameters.specializations_names.set(counter_for_loading_parameters + indices[j],
                            list.getModel().getElementAt(indices[j]).getName());
                    // обновляем глобальные параметры запроса
                    RequestParameters.specializations_ids.set(counter_for_request_parameters + indices[j],
                            Double.valueOf(prof_areas.get(i).getId() + "." + list.getModel().getElementAt(indices[j]).getId()));
                }
                // Получаем невыбранные индексы и обновляем их в null
                for (int k = 0; k < list.getModel().getSize(); k++)
                {
                    if (!list.isSelectedIndex(k))
                    {
                        // обновляем в null глобальные параметры окна
                        LoadingParameters.specializations_names.set(counter_for_loading_parameters + k, null);
                        // обновляем в null глобальные параметры запроса
                        RequestParameters.specializations_ids.set(counter_for_request_parameters + k, null);
                    }
                }
            }
            // Если выбран JCheckBox, привязанный к этому JList
            // То нужно сбросить все строки связанного JList в null
            else
            {
                for (int j = 0; j < lists_specializations.get(i).getModel().getSize(); j++)
                {
                    // обновляем в null глобальные параметры окна
                    LoadingParameters.specializations_names.set(counter_for_loading_parameters + j, null);
                    // обновляем в null глобальные параметры запроса
                    RequestParameters.specializations_ids.set(counter_for_request_parameters + j, null);
                }
            }
            // Увеличиваем счётчики на размер пройденного списка
            counter_for_loading_parameters += lists_specializations.get(i).getModel().getSize();
            counter_for_request_parameters += lists_specializations.get(i).getModel().getSize();
        }
    }



    public ProfAreaSettingsFrame()
    {
        setTitle("Select ProfAreas");
        setSize(1200, 1080);
        setLocationRelativeTo(null); // должно стоять после setSize
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        ///////
        // Creating components
        panel_prof_areas.setLayout(new GridBagLayout());
        //
        scroll_pane_prof_areas.setViewportView(panel_prof_areas);
        scroll_pane_prof_areas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_prof_areas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_prof_areas.getVerticalScrollBar().setUnitIncrement(16);
        //scroll_pane_prof_areas.setPreferredSize(new Dimension(500, 123));
        ///////
        // Adding panels and set default borders
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(scroll_pane_prof_areas, BorderLayout.CENTER);
        getContentPane().add(panel_buttons, BorderLayout.SOUTH);
        ///////
        // Services autowiring
        BeanProvider.autowire(this);
        this.profAreaService = BeanProvider.applicationContext.getBean(ProfAreaServiceImpl.class);
        this.specializationService = BeanProvider.applicationContext.getBean(SpecializationServiceImpl.class);
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





///////
/// Итак, нужно переделать методы инициализации стат. параметров с заполнением null
/// сразу на основе количество сущностей из базы данных, а не созданных из этих сущностей элементов интерфейса
/// Эти методы будут вызываться при открытии главного окна приложения, т.о. они будут запускаться только 1 раз,
/// т.к. если закрыть окно закроется сразу всё.
/// И нужно допилить логику работы связки JCheckBox и JList
/// При снятии выбора с какого-либо элемента JList снимается выбор с JCheckBox
/// Что автоматом вызывает снятие выбора сразу всех строк (это невозможно обойти)
/// И тогда нужно снова выбрать все строки в JList, кроме нажатой
/// Также добавить проверку случая, когда обезьяна с гранатой вручную выбирает все элементы JList,
/// С них всех должн слетать выделение, вместо них выбирается соот-ий JCheckBox
///////
// Баг - невозможно сбросить в null уже выбранные параметры стат. класса, можно только обновлять
// И какой-то бред с сохранением выделения не тех элементов, что были выбраны
// Возможно просто не хранится в том же порядке - неверно

// При добавлении элемента между или до уже добавленных, начинают смещаться индексы тех, что больше

// Пока что удалось выяснить, что при добавлении на определённую позицию,
// Какого-то хера увеличивается размер массив, с 621 до 622
// Оказывается, API add(index, object) сдвигает все элементы справа на 1
// То же самое и для удаления, только сдвиг влево
// Чинится заменой add на set
///////
// Save old values
// int[] temp = list_selection_model.getSelectedIndices();
// Выделяем заново
// lists_specializations.get(i).setSelectedIndices(temp);
///////
// Deprecated because cause problems with deselecting
    /*
    public void itemStateChangedProfArea(ItemEvent e)
    {
        for (int i = 0; i < check_boxes_prof_areas.size(); i++)
        {
            // Находим e.getItemSelectable(); // checkbox который был нажат
            if (check_boxes_prof_areas.get(i).equals(e.getItemSelectable()))
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    // отмечаем все строки в JList
                    lists_specializations.get(i).addSelectionInterval(0, lists_specializations.get(i).getModel().getSize()-1);
                }
                else if (e.getStateChange() == ItemEvent.DESELECTED)
                {
                    // очищаем выбор всех строк в JList
                    lists_specializations.get(i).clearSelection();
                }
            }
        }
    }*/
///////
/// OLD VERSION
// Если была выбрана хотя бы 1 строка
//if (!list_selection_model.isSelectionEmpty())
///////
/*
// Set All Parameters To Null
        if (!isAlreadySetParametersToNull)
                {
                //SetLoadingParametersArrayListsToNull();
                //SetRequestParametersArrayListsToNull();
                //isAlreadySetParametersToNull = true;
                }*/
///////
    /*
    private void SetLoadingParametersArrayListsToNull()
    {
        for (int i = 0; i < check_boxes_prof_areas.size(); i++)
        {
            LoadingParameters.prof_areas_names.add(i, null);
        }
        //
        int specializations_count = 0;
        //
        for (int i = 0; i < lists_specializations.size(); i++)
        {
            // Глобальный размер
            specializations_count += lists_specializations.get(i).getModel().getSize();
        }
        // Здесь не нужно прибавлять check_boxes_prof_areas.length, т.к. данные идут в разные массивы
        for (int i = 0; i < specializations_count; i++)
        {
            LoadingParameters.specializations_names.add(i, null);
        }
    }
    */
    /*
    private void SetRequestParametersArrayListsToNull()
    {
        int specializations_count = 0;
        //
        for (int i = 0; i < lists_specializations.size(); i++)
        {
            // Глобальный размер
            specializations_count += lists_specializations.get(i).getModel().getSize();
        }
        // Здесь нужно прибавлять check_boxes_prof_areas.length, т.к. данные идут в один массив
        for (int i = 0; i < (check_boxes_prof_areas.size() + specializations_count); i++)
        {
            RequestParameters.specializations_ids.add(i, null);
        }
    }*/
///////
// Доделать логику отметок
// (+) Если был отмечен JCheckBox, а затем пользователь отмечает строку JList под ним, то выбор JCheckBox отменяется
///////
// Как вариант, можно заранее подготовить список индексов не нулевых элементов
// Или просто добавлять интервал из 1 элемента - list.addSelectionInterval(0, 0);
// Это тоже работает)))
//LoadingParameters.specializations_names
//lists_specializations[i].getModel().getElementAt();
///////
// Итак, сначала загружаем параметры из стат. класса на экран
// Затем очищаем данные этих классов
// Теперь можно поменять выбор или оставить тот же и Сохранить.
// Но если пользователь нажмёт Отменить, то все данные пропадут
// Следовательно, просто не нужно очищать данные стат. классов
///////
// check_boxes_prof_areas[i].setSelected(); - бесполезный для нас метод, т.к. фактически
// просто устанавливает состояние кнопки, а не нажимает
// Лучше использовать check_boxes_prof_areas[i].doClick();
// Тогда автоматически отметятся все элементы в связанном JList
// Но нужно будет пропускать соответствующий диапазон при проверке строк JList'ов
///////
// get(index) - индексы идут только для выбранных элементов, без разрывов
// Если выбран 1 и 30 элемент - это будет массив из 2 элементов, соот-но 2 индекса, 0 и 1
// но можно попытаться запихнуть их в параметры через forEach
//
// Сначала для записи в стат. класс параметров используется specializations_count
// Который вначале равен check_boxes_prof_areas.length
// Затем он увеличивается на размер пройденного списка
//specializations_count += lists_specializations[i].getModel().getSize();
///////
/// Эти действия могут быть только здесь, т.к. если мы не уберём вручную отметки, то параметры сохранятся автоматически,
/// (не удалятся), даже при нажатии Отменить
/// Нужно либо отдельно проверять, что мы выбрали в этот раз, и что было выбрано в предыдущий, и искать разницу
/// Либо (что проще), передавать их в глоб. классы параметров только при нажатии Сохранить
/// И да, придётся дублировать код перебора всех элементов массивов JCheckBox[] и JList[]
///////
// добавляем в глобальные параметры окна
//LoadingParameters.prof_areas_names.add(i, prof_areas.get(i).getName());
// добавляем в глобальные параметры запроса
//RequestParameters.specializations_ids.add(i, (double) prof_areas.get(i).getId());
//
// убираем из глобальных параметров окна
//LoadingParameters.prof_areas_names.remove(i);
// убираем из параметров запроса
//RequestParameters.specializations_ids.remove(i);
///////
// Нет смысла использовать локальные параметры - они удалятся при reopen'е окна
// Можно сохранять сразу в глобальные, проверяя какие Checkbox'ы отмечены при нажатии на кнопку сохранения - херня,
// нужно будет снова делать такой же цикл, проще добавлять тут же
// А вот в параметры запроса только при сохранении
///////
// нужно определить, (+) какой checkbox из массива был нажат,
// (+) и активировать (+) добавить в параметры все строчки - все строчки добавлять не надо,
// если выбрана вся ProfArea
// (+) то передаются не все ID Specializations, а только ProfArea ID
//
// Короче, в параметры запроса добавляем ProfArea ID, а для отображения в окне настроек - ProfArea Name
//
// list с таким же номером индекса в массиве
///////
//private ArrayList<Specialization> specializations;
// список ProfArea и список JCheckBox будут одинакового размера, и будут соответствовать по ID,
// то есть, при выборе JCheckBox можно будет легко узнать, какой ID ему соответствует.
/////// Локальные параметры
//private ArrayList<String> prof_areas_names = new ArrayList<String>();
//private ArrayList<String> specializations_names = new ArrayList<String>();
///////
// сохранение в глобальные параметры окна
//LoadingParameters.prof_areas_names.add(i, prof_areas.get(i).getName());
/////// не требуется, т.к. сохранение вынесено в отдельный метод, чтобы не дергать туда-сюда данные для запроса,
/////// до которого как до Китая
// удаление из глобальных параметров окна
//LoadingParameters.prof_areas_names.remove(i);
///////
// При нажатии на элемент нужно сохранить в классе параметров ID этой ProfArea или Specialization
// А для передачи в окно настроек нужно только имя, для отображения
///
/// Осталось добавить
//  (+) слушатели действий на сгенерированные в цикле элементы,
//  (+/-) логику работы отметок - если выбран вышележащий checkbox, то все элементы в jlist ниже него отмечаются (добавляются в параметры)
//  (+) записи данных в стат. класс
//  (+) и панель с 2 кнопками
/// (+) Также нужен поиск ID ProfArea по соответствию порядка