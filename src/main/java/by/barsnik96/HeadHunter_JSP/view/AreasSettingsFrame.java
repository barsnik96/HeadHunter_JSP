package by.barsnik96.HeadHunter_JSP.view;

import by.barsnik96.HeadHunter_JSP.domain.Area;
import by.barsnik96.HeadHunter_JSP.domain.CompanyIndustry;
import by.barsnik96.HeadHunter_JSP.domain.CompanyScope;
import by.barsnik96.HeadHunter_JSP.domain.Specialization;
import by.barsnik96.HeadHunter_JSP.service.AreaServiceImpl;
import by.barsnik96.HeadHunter_JSP.service.CompanyIndustryServiceImpl;
import by.barsnik96.HeadHunter_JSP.service.CompanyScopeServiceImpl;
import by.barsnik96.HeadHunter_JSP.utils.BeanProvider;
import by.barsnik96.HeadHunter_JSP.utils.LoadingParameters;
import by.barsnik96.HeadHunter_JSP.utils.RequestParameters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.springframework.util.ResourceUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AreasSettingsFrame extends JDialog
{
    private JScrollPane scroll_pane_areas = new JScrollPane();
    private JPanel panel_areas = new JPanel();
    private JPanel panel_buttons = new JPanel();
    ///////
    // Страны
    private ArrayList<JCheckBox> check_boxes_countries = new ArrayList<JCheckBox>();
    // Регионы
    private ArrayList<ArrayList<JCheckBox>> check_boxes_regions = new ArrayList<ArrayList<JCheckBox>>();
    // Города
    private ArrayList<ArrayList<JList<Area>>> lists_cities = new ArrayList<ArrayList<JList<Area>>>();
    ///////
    private JButton button_select = new JButton("Выбрать");
    private JButton button_cancel = new JButton("Отменить");
    ///////
    private AreaServiceImpl areaService;
    ///////
    private ArrayList<Area> countries = new ArrayList<Area>();
    private ArrayList<ArrayList<Area>> regions = new ArrayList<ArrayList<Area>>();
    ///////
    // Fonts settings
    private Font arial = new Font("Arial", Font.PLAIN, 16);
    // File
    File areas;



    public void on_actionPerformedClick_Country_JCheckBox(ActionEvent e)
    {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        //
        for (int i = 0; i < check_boxes_countries.size(); i++)
        {
            // Находим checkbox который был нажат
            if (check_boxes_countries.get(i).equals(checkBox))
            {
                if (checkBox.isSelected())
                {
                    // Тогда выбираем все связанные JCheckBox Region
                    for (int j = 0; j < check_boxes_regions.get(i).size(); j++)
                    {
                        check_boxes_regions.get(i).get(j).setSelected(true);
                        // Связанные с этим JCheckBox Region JList's City обновятся автоматически - да
                    }
                }
                else
                {
                    // Иначе снимаем выделение со всех связанных JCheckBox Region
                    for (int j = 0; j < check_boxes_regions.get(i).size(); j++)
                    {
                        check_boxes_regions.get(i).get(j).setSelected(false);
                        // Связанные с этим JCheckBox Region JList's City обновятся автоматически - нет
                        // Потому что если обновятся - будут очищаться полностью каждый раз, когда выделены не все элементы
                        lists_cities.get(i).get(j).clearSelection();
                    }
                }
            }
        }
    }



    public void on_actionPerformedClick_Region_JCheckBox(ActionEvent e)
    {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        //
        // Нужно как-то узнать, к какой Country принадлежал нажатый JCheckBox Region
        for (int i = 0; i < check_boxes_regions.size(); i++)
        {
            for (int j = 0; j < check_boxes_regions.get(i).size(); j++)
            {
                if (check_boxes_regions.get(i).get(j).equals(checkBox))
                {
                    if (!checkBox.isSelected())
                    {
                        // Снимаем выделение со всех строк
                        lists_cities.get(i).get(j).clearSelection();
                    }
                }
            }
        }
    }


    // На клик Region будем добавлять/снимать выбор со всех элементов JList City

    // На stateChanged Region isSelected == false снимаем выбор со связанного JCheckBox Country
    // (здесь даже вычислять ничего больше не нужно, т.к. он по любому должен быть снят)

    // На stateChanged Region isSelected == true
    //
    // (для случаев когда выбор приходит от JCheckBox Country) выбираем все строки в JList City - это обработка в Country value changed
    // (если выбраны все Region для данной Country) - то выбираем Country

    // И нужно разобраться с этим -1 при добавлении элементов JList
    // Возможно, мы неправильно считаем все размеры ArrayList, оттого и все баги


    private void on_itemStateChanged_Region_JCheckBox(ItemEvent e)
    {
        for (int i = 0; i < check_boxes_regions.size(); i++)
        {
            for (int j = 0; j < check_boxes_regions.get(i).size(); j++)
            {
                // Находим e.getItemSelectable(); // checkbox который был нажат
                // Это по сути нужно только для точного позиционирования
                if (check_boxes_regions.get(i).get(j).equals(e.getItemSelectable()))
                {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                    {
                        // Счётчик сколько Region выделено
                        int is_all_check_boxes_region_for_related_check_box_country_selected = 0;
                        // Проверяем, выделены ли теперь все JCheckBox Region
                        for (int k = 0; k < check_boxes_regions.get(i).size(); k++)
                        {
                            if (check_boxes_regions.get(i).get(k).isSelected())
                            {
                                is_all_check_boxes_region_for_related_check_box_country_selected++;
                            }
                        }
                        // Если выбраны все Region, то выбираем соот-ую Country
                        if (check_boxes_regions.get(i).size() == is_all_check_boxes_region_for_related_check_box_country_selected)
                        {
                            check_boxes_countries.get(i).setSelected(true);
                        }
                        // И просто выбираем все строки в List
                        // Оставить на кнопке ActionListener только очистку
                        // Тогда выделяем все строки связанного JList City
                        lists_cities.get(i).get(j).addSelectionInterval(0, lists_cities.get(i).get(j).getModel().getSize()-1);

                    }
                    else if (e.getStateChange() == ItemEvent.DESELECTED) // и в jlist тоже выбраны не все элементы
                    {
                        // На stateChanged Region ItemEvent.DESELECTED снимаем выбор со связанного JCheckBox Country
                        check_boxes_countries.get(i).setSelected(false);
                    }
                }
            }
        }

    }



    public void on_valueChanged_City_JList(ListSelectionEvent e)
    {
        JList<Area> list = (JList<Area>) e.getSource();
        ListSelectionModel list_selection_model = list.getSelectionModel();
        if(!e.getValueIsAdjusting())
        {
            // Находим какому JList в списке соответствует тот JList, из которого пришло событие
            for (int i = 0; i < lists_cities.size(); i++)
            {
                for (int j = 0; j < lists_cities.get(i).size(); j++)
                {
                    if (lists_cities.get(i).get(j).equals(list))
                    {
                        // Если размер выбранных индексов не совпадает с размеров всех индексов
                        // Значит с какого-то элемента был снят выбор
                        if (list_selection_model.getSelectedIndices().length != list.getModel().getSize())
                        {
                            // Убираем выбор с соответствующего JCheckBox
                            check_boxes_regions.get(i).get(j).setSelected(false);
                        }
                        else // Если все строки были выбраны (имеется в виду вручную)
                        {
                            // Выбираем соответствующий JCheckBox
                            check_boxes_regions.get(i).get(j).setSelected(true);
                        }
                    }
                }
            }
        }
    }



    private static ListCellRenderer<? super Area> createListRenderer() {
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
                    Area city = (Area) value;
                    label.setText(city.getName());
                    //if (!isSelected) {
                    //    label.setBackground(index % 2 == 0 ? background : defaultBackground);
                    //}
                }
                return c;
            }
        };
    }

    private JsonArray GetAreasFromJsonFile()
    {
        try {
            areas = ResourceUtils.getFile("D:\\nikita\\nikita_projects\\Projects\\Java\\HeadHunter_JSP\\src\\main\\resources\\areas.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (areas.exists())
        {
            try
            {
                Reader reader = new FileReader(areas);
                JsonReader json_reader = new JsonReader(reader);
                //
                Gson gson = new Gson();
                JsonArray areas_array = gson.fromJson(json_reader, JsonArray.class);
                //
                return areas_array;
            } catch (Exception exception)
            { System.out.println("Ошибка при загрузке JsonArray с регионами из файла " + areas.getAbsolutePath());}
        }
        // Default
        return null;
    }

    private void CreateGUI() throws SQLException
    {
        // Загружать все Area нужно из JSON, полученного из файла
        JsonArray areas_array = GetAreasFromJsonFile();
        if (areas_array != null)
        {
            // Порядковый номер gridy
            int gridy = 0;
            //
            for (int i = 0; i < areas_array.size(); i++)
            {
                // Countries level
                JsonObject country_json = areas_array.get(i).getAsJsonObject();
                // Создаём новую Area - country
                Area country = new Area();
                country.setId(country_json
                        .get("id")
                        .getAsInt());
                country.setName(country_json
                        .get("name")
                        .getAsString());
                countries.add(country);
                // Создаём новый JCheckBox
                check_boxes_countries.add(i, new JCheckBox(country.getName()));
                // Настраиваем параметры JCheckBox
                check_boxes_countries.get(i).setFont(arial);
                check_boxes_countries.get(i).addActionListener(this::on_actionPerformedClick_Country_JCheckBox);
                // Настраиваем параметры GridBagConstraints для JCheckBox - Country
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.ipady = 0;
                constraints.gridx = 0;
                constraints.gridy = gridy;
                gridy++;
                constraints.insets = new Insets(0, 0, 10, 0);
                // Размещаем JCheckBox на панели
                panel_areas.add(check_boxes_countries.get(i), constraints);
                // Regions level
                //
                //
                // Создаём новый список JCheckBox Region для связанной страны
                ArrayList<JCheckBox> check_boxes_regions_for_related_country = new ArrayList<JCheckBox>();
                // Создаём новый список Region для связанной страны
                ArrayList<Area> regions_for_related_country = new ArrayList<Area>();
                // Города для определённой страны
                ArrayList<JList<Area>> lists_cities_for_related_country = new ArrayList<JList<Area>>();
                //
                //
                JsonArray region_array = country_json.get("areas").getAsJsonArray();
                for (int j = 0; j < region_array.size(); j++)
                {
                    JsonObject region_json = region_array.get(j).getAsJsonObject();
                    // Создаём новую Area - region
                    Area region = new Area();
                    region.setId(region_json
                            .get("id")
                            .getAsInt());
                    region.setName(region_json
                            .get("name")
                            .getAsString());
                    regions_for_related_country.add(region);
                    // Создаём новый JCheckBox
                    check_boxes_regions_for_related_country.add(j, new JCheckBox(region.getName()));
                    // Настраиваем параметры JCheckBox
                    check_boxes_regions_for_related_country.get(j).setFont(arial);
                    check_boxes_regions_for_related_country.get(j).addActionListener(this::on_actionPerformedClick_Region_JCheckBox);
                    check_boxes_regions_for_related_country.get(j).addItemListener(this::on_itemStateChanged_Region_JCheckBox);
                    // Настраиваем параметры GridBagConstraints для JCheckBox - Region
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.ipady = 0;
                    constraints.gridx = 0;
                    constraints.gridy = gridy;
                    gridy++;
                    constraints.insets = new Insets(0, 0, 10, 0);
                    // Размещаем JCheckBox на панели
                    panel_areas.add(check_boxes_regions_for_related_country.get(j), constraints);
                    // Для заполнения JList<Area>
                    ArrayList<Area> cities_for_related_region = new ArrayList<Area>();
                    //
                    //
                    JsonArray city_array = region_json.get("areas").getAsJsonArray();
                    for (int k = 0; k < city_array.size(); k++)
                    {
                        JsonObject city_json = city_array.get(k).getAsJsonObject();
                        // Создаём новую Area - city
                        Area city = new Area();
                        city.setId(city_json
                                .get("id")
                                .getAsInt());
                        city.setName(city_json
                                .get("name")
                                .getAsString());
                        cities_for_related_region.add(city);
                    }
                    // Создаём новый JList
                    lists_cities_for_related_country.add(j, new JList<>(cities_for_related_region.toArray(new Area[cities_for_related_region.size()])));
                    // Настраиваем параметры JList<Area> Cities
                    lists_cities_for_related_country.get(j).setFont(arial);
                    lists_cities_for_related_country.get(j).setCellRenderer(createListRenderer());
                    lists_cities_for_related_country.get(j).setSelectedIndex(-1);
                    lists_cities_for_related_country.get(j).setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    lists_cities_for_related_country.get(j).addListSelectionListener(this::on_valueChanged_City_JList);
                    // Настраиваем параметры GridBagConstraints для JList<Area> - City
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.ipady = 0;
                    constraints.gridx = 0;
                    constraints.gridy = gridy;
                    gridy++;
                    constraints.insets = new Insets(0, 20, 10, 0);
                    // Размещаем JList<Area> на панели
                    panel_areas.add(lists_cities_for_related_country.get(j), constraints);
                }
                check_boxes_regions.add(i, check_boxes_regions_for_related_country);
                regions.add(i, regions_for_related_country);
                lists_cities.add(i, lists_cities_for_related_country);
            }
        }
        else
        {
            System.out.println("Не удалось получить файл areas.json, или файл пуст!");
        }




        ///////
        /////// Повторный выбор записанных в параметрах компонентов
        ///////
        //
        // Посчитаем сколько всего регионов, т.к. теперь они разбиты на группы по странам, и нельзя просто сделать size()
        //
        int regions_count = 0;
        // Для каждой страны
        for (int i = 0; i < regions.size(); i++)
        {
            // Получаем размер списка регионов для этой страны - количество регионов для этой страны
            regions_count += regions.get(i).size();
            System.out.println("Количество регионов (на самом деле): " + regions_count); // 639
        }
        //
        // Сумма размеров всех предыдущих (проверенных) JList City
        int sum_all_prev_lists_sizes = 0;
        // Сумма размеров всех предыдущих (проверенных) JCheckBox Region
        int sum_all_prev_regions_sizes = 0;
        // Сумма размеров всех JCheckBox Country
        //
        for (int i = 0; i < countries.size(); i++)
        {
            // Если этот JCheckBox Country уже был выбран в прошлый раз
            if (LoadingParameters.areas_names.get(i) != null)
            {
                // Выбираем его снова
                check_boxes_countries.get(i).doClick(); // set selected
                // Связанные с ним JCheckBox Region и JList City выделятся автоматически
                //
                // Увеличиваем счётчик на количество проверенных JCheckBox's Region
                // +Все связанные с данной страной Region's
                sum_all_prev_regions_sizes += regions.get(i).size();
                // Увеличиваем счётчик на количество проверенных строк JList's City
                // +Все строки связанных с данной страной City's
                for (int j = 0; j < lists_cities.get(i).size(); j++)
                {
                    sum_all_prev_lists_sizes += lists_cities.get(i).get(j).getModel().getSize();
                }

            }
            else // Если JCheckBox Country не был выбран, проверяем связанные с ним JCheckBox Region
            {
                for (int j = 0; j < regions.get(i).size(); j++)
                {
                    // !!!
                    System.out.println(i + " :: " + j + " :: " + sum_all_prev_regions_sizes + " :: " + countries.size() + " :: " + regions.get(i).size());





                    // Если этот JCheckBox Region уже был выбран в прошлый раз
                    if (LoadingParameters.areas_names.get((sum_all_prev_regions_sizes + countries.size())) != null) // j +
                    {
                        // Выбираем его снова
                        check_boxes_regions.get(i).get(j).doClick();
                        //
                        // Увеличиваем счётчик на количество проверенных строк JList Cities
                        // +Все строки связанного с данным регионом JList
                        //sum_all_prev_lists_sizes += lists_cities.get(i).get(j).getModel().getSize();
                        // Связанным с ним JList выделится автоматически
                    }
                    else // Если JCheckBox Region не был выбран, проверяем связанный с ним JList City
                    {
                        for (int k = 0; k < lists_cities.get(i).get(j).getModel().getSize(); k++)
                        {

                            // Если эта строка была выбрана в прошлый раз
                            if (LoadingParameters.areas_names.get(k + (sum_all_prev_lists_sizes + countries.size() + regions_count)) != null) // k +
                            {
                                // Выбираем её снова
                                lists_cities.get(i).get(j).addSelectionInterval(k, k);;
                            }

                            // И в любом случае увеличиваем количество пройденных строк на 1
                            //sum_all_prev_lists_sizes++;
                        }
                    }
                    // Увеличиваем счётчик на количество проверенных строк JList Cities
                    // +Все строки связанного с данным регионом JList
                    sum_all_prev_lists_sizes += lists_cities.get(i).get(j).getModel().getSize();
                    // Увеличиваем счётчик на количество проверенных JCheckBox Regions
                    // +1 регион
                    sum_all_prev_regions_sizes++;
                }
            }
        }
    }





    private void UpdateParameters()
    {
        // Посчитаем сколько всего регионов, т.к. теперь они разбиты на группы по странам, и нельзя просто сделать size()
        //
        int regions_count = 0;
        // Для каждой страны
        for (int r = 0; r < regions.size(); r++)
        {
            // Получаем размер списка регионов для этой страны - количество регионов для этой страны
            regions_count += regions.get(r).size();
        }
        //
        // Сумма размеров всех предыдущих (проверенных) JList City
        int sum_all_prev_lists_sizes = 0;
        // Сумма размеров всех предыдущих (проверенных) JCheckBox Region
        int sum_all_prev_regions_sizes = 0;
        //
        // Проверяем все JCheckBox Country на наличие выбранных
        for (int i = 0; i < check_boxes_countries.size(); i++)
        {
            // Если JCheckBox Country выбран
            if (check_boxes_countries.get(i).isSelected())
            {
                // обновляем глобальные параметры окна
                LoadingParameters.areas_names.set(i, countries.get(i).getName());
                // обновляем глобальные параметры запроса
                RequestParameters.areas_ids.set(i, (double) countries.get(i).getId());
                //
                // И нужно сбросить в null все связанные элементы - JCheckBox's Region, JList's City
                //
                // JCheckBox's Region
                for (int j = 0; j < regions.get(i).size(); j++)
                {
                    // обновляем в null глобальные параметры окна
                    LoadingParameters.areas_names.set(j + (sum_all_prev_regions_sizes + countries.size()), null); // j +
                    // обновляем в null глобальные параметры запроса
                    RequestParameters.areas_ids.set(j + (sum_all_prev_regions_sizes + countries.size()), null); // j +

                    // JList's City
                    for (int c = 0; c < lists_cities.get(i).get(j).getModel().getSize(); c++)
                    {
                        // обновляем в null глобальные параметры окна
                        LoadingParameters.areas_names.set(c + (sum_all_prev_lists_sizes + countries.size() + regions_count), null); // c +
                        // обновляем в null глобальные параметры запроса
                        RequestParameters.areas_ids.set(c + (sum_all_prev_lists_sizes + countries.size() + regions_count), null); // c +
                    }
                    // Увеличиваем счётчик на количество проверенных строк JList Cities
                    // +Все строки связанного с данным регионом JList
                    sum_all_prev_lists_sizes += lists_cities.get(i).get(j).getModel().getSize();
                }
                // Увеличиваем счётчик на количество проверенных JCheckBox Regions
                // +проверенные регионы
                sum_all_prev_regions_sizes += regions.get(i).size();
            }
            else // Если JCheckBox Country не выбран
            {
                // обновляем в null глобальные параметры окна
                LoadingParameters.areas_names.set(i, null);
                // обновляем в null глобальные параметры запроса
                RequestParameters.areas_ids.set(i, null);
                //
                //
                // То нужно проверить все JCheckBox Region привязанные к нему
                for (int j = 0; j < check_boxes_regions.get(i).size(); j++)
                {
                    // Если JCheckBox Region выбран
                    if (check_boxes_regions.get(i).get(j).isSelected())
                    {
                        // JCheckBox Region
                        // обновляем глобальные параметры окна
                        LoadingParameters.areas_names.set(j + (sum_all_prev_regions_sizes + countries.size()),
                                regions.get(i).get(j).getName());
                        // обновляем глобальные параметры запроса
                        RequestParameters.areas_ids.set(j + (sum_all_prev_regions_sizes + countries.size()),
                                (double) regions.get(i).get(j).getId());
                        // И нужно сбросить в null все связанные элементы
                        // JList's City
                        for (int k = 0; k < lists_cities.get(i).get(j).getModel().getSize(); k++)
                        {
                            // обновляем в null глобальные параметры окна
                            LoadingParameters.areas_names.set(k + (sum_all_prev_lists_sizes + countries.size() + regions_count), null);
                            // обновляем в null глобальные параметры запроса
                            RequestParameters.areas_ids.set(k + (sum_all_prev_lists_sizes + countries.size() + regions_count), null);
                        }
                    }
                    else // Если JCheckBox Region не выбран
                    {
                        // То нужно сбросить в null JCheckBox Region
                        // обновляем в null глобальные параметры окна
                        LoadingParameters.areas_names.set(j + (sum_all_prev_regions_sizes + countries.size()), null);
                        // обновляем в null глобальные параметры запроса
                        RequestParameters.areas_ids.set(j + (sum_all_prev_regions_sizes + countries.size()), null);
                        //
                        // И нужно проверить все JList City привязанные к нему
                        //
                        JList<Area> list = lists_cities.get(i).get(j);
                        // Получаем выбранные индексы (соответствующие реальному порядку строк)
                        int[] indices = list.getSelectedIndices();
                        // Для каждой выбранной строки в конкретном JList (по индексу)
                        for (int k = 0; k < indices.length; k++)
                        {
                            // обновляем глобальные параметры окна
                            LoadingParameters.areas_names.set((sum_all_prev_lists_sizes + countries.size() + regions_count)
                                            + indices[k], list.getModel().getElementAt(indices[k]).getName());
                            // обновляем глобальные параметры запроса // было от j
                            RequestParameters.areas_ids.set((sum_all_prev_lists_sizes + countries.size() + regions_count)
                                            + indices[k], Double.valueOf(list.getModel().getElementAt(indices[k]).getId()));
                        }
                        // Получаем невыбранные индексы и обновляем их в null
                        for (int k = 0; k < list.getModel().getSize(); k++)
                        {
                            if (!list.isSelectedIndex(k))
                            {
                                // обновляем в null глобальные параметры окна
                                LoadingParameters.areas_names.set(k + (sum_all_prev_lists_sizes + countries.size() + regions_count), null);
                                // обновляем в null глобальные параметры запроса
                                RequestParameters.areas_ids.set(k + (sum_all_prev_lists_sizes + countries.size() + regions_count), null);
                            }
                        }
                    }
                    // Увеличиваем счётчик на количество проверенных строк JList Cities
                    // +Все строки связанного с данным регионом JList
                    sum_all_prev_lists_sizes += lists_cities.get(i).get(j).getModel().getSize();
                }
                // Увеличиваем счётчик на количество проверенных JCheckBox Regions
                // + регионы
                sum_all_prev_regions_sizes += regions.get(i).size();
            }
        }
    }





    public AreasSettingsFrame()
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
        panel_areas.setLayout(new GridBagLayout());
        //
        scroll_pane_areas.setViewportView(panel_areas);
        scroll_pane_areas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_areas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_areas.getVerticalScrollBar().setUnitIncrement(16);
        ///////
        // Adding panels and set default borders
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(scroll_pane_areas, BorderLayout.CENTER);
        getContentPane().add(panel_buttons, BorderLayout.SOUTH);
        ///////
        // Services autowiring
        BeanProvider.autowire(this);
        this.areaService = BeanProvider.applicationContext.getBean(AreaServiceImpl.class);
        ///////
        // Create GUI
        try
        {
            CreateGUI();
            //SQLException ex = new SQLException();
            //throw ex;
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

/// Deprecated ///
/*
    public void on_actionPerformedClick_Region_JCheckBox(ActionEvent e)
    {
        JCheckBox checkBox = (JCheckBox) e.getSource();
        //
        // Нужно как-то узнать, к какой Country принадлежал нажатый JCheckBox Region
        for (int i = 0; i < check_boxes_regions.size(); i++)
        {
            int is_all_check_boxes_region_for_related_check_box_country_selected = 0;
            //
            for (int j = 0; j < check_boxes_regions.get(i).size(); j++)
            {
                if (check_boxes_regions.get(i).get(j).equals(checkBox))
                {
                    if (checkBox.isSelected())
                    {
                        // Тогда выделяем все строки связанного JList City
                        lists_cities.get(i).get(j).addSelectionInterval(0, lists_cities.get(i).get(j).getModel().getSize()-1);
                    }
                    else
                    {
                        // Иначе снимаем выделение со всех строк
                        lists_cities.get(i).get(j).clearSelection();
                    }
                }
                // Затем проверка, если выделены все JCheckBox Region связанные с JCheckBox Country
                // То нужно выбрать этот JCheckBox Country
                if (check_boxes_regions.get(i).get(j).isSelected())
                {
                    is_all_check_boxes_region_for_related_check_box_country_selected++;
                }
            }
            // Получаем количество Region для определённой Country
            if (check_boxes_regions.get(i).size() == is_all_check_boxes_region_for_related_check_box_country_selected)
            {
                // Если все JCheckBox Region's выбраны
                // Выбираем соответствующий JCheckBox Country
                check_boxes_countries.get(i).setSelected(true);
            }
            else
            {
                // Если выбраны не все JCheckBox Region's
                // Отменяем выбор
                check_boxes_countries.get(i).setSelected(false);
            }
        }
    }


            // Увеличиваем счётчик на количество проверенных JCheckBox's Region
            // +Все связанные с данной страной Region's
            sum_all_prev_regions_sizes += regions.get(i).size();
            // Увеличиваем счётчик на количество проверенных строк JList's City
            // +Все строки связанных с данной страной City's
            for (int j = 0; j < lists_cities.get(i).size(); j++)
            {
                sum_all_prev_lists_sizes += lists_cities.get(i).get(j).getModel().getSize();
            }
 */