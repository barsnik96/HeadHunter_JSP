package by.barsnik96.HeadHunter_JSP.view;

import by.barsnik96.HeadHunter_JSP.domain.*;
import by.barsnik96.HeadHunter_JSP.domain.Currency;
import by.barsnik96.HeadHunter_JSP.service.*;
import by.barsnik96.HeadHunter_JSP.utils.BeanProvider;

import by.barsnik96.HeadHunter_JSP.utils.LoadingParameters;
import by.barsnik96.HeadHunter_JSP.utils.RequestParameters;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import by.barsnik96.HeadHunter_JSP.api.NetworkService;
import okhttp3.ResponseBody;
import org.springframework.util.ResourceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HeadHunterJSP_App_MainFrame extends JFrame
{
    private JPanel panel_north = new JPanel(); // Верхняя панель
    private JPanel panel_north_west = new JPanel(); // Левая верхняя панель для название приложения
    private JPanel panel_north_east = new JPanel(); // Правая верхняя панель для кнопоу настроек
    //
    private JPanel panel_center = new JPanel(); // Центральная панель
    private JPanel panel_center_west = new JPanel(); // Левая центральная панель для вакансий
    private JPanel panel_center_east = new JPanel(); // Правая центральная панель для данных
    //
    private JPanel panel_south = new JPanel(); // Нижняя панель
    private JPanel panel_south_west = new JPanel(); // Панель с кнопками "Загрузить" и "Просмотреть"
    private JPanel panel_south_east = new JPanel(); // Панель с кнопкой "Выход"
    // Панель с прокруткой для списка вакансий
    private JScrollPane vacanciesListScrollPane = new JScrollPane(panel_center_west);
    // Панель с прокруткой для данных
    private JScrollPane vacanciesInfoScrollPane = new JScrollPane(panel_center_east);
    // Раздельная центральная панель для отображения вакансий и данных
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vacanciesListScrollPane, vacanciesInfoScrollPane);
    //
    private JLabel label_app_name = new JLabel("HeadHunterJSP App"); // Название приложения
    //
    //private JTextField text_field_message = new JTextField(); //
    //
    private JButton btn_load_settings = new JButton("Настройки параметров загрузки вакансий");
    private JButton btn_view_settings = new JButton("Настройки параметров просмотра вакансий");
    private JButton btn_load = new JButton("Загрузка вакансий");
    private JButton btn_view = new JButton("Просмотр вакансий");
    private JButton btn_exit = new JButton("Выход");
    //
    private Dimension min_size_list;
    ///            ///
    /// REPOSITORY ///
    ///            ///
    private AddressServiceImpl addressService;
    private AreaServiceImpl areaService;
    private CompanyIndustryServiceImpl companyIndustryService;
    private CompanyScopeServiceImpl companyScopeService;
    private CurrencyServiceImpl currencyService;
    private DriverLicenseServiceImpl driverLicenseService;
    private EmployerServiceImpl employerService;
    private EmployerTypeServiceImpl employerTypeService;
    private EmploymentTypeServiceImpl employmentTypeService;
    private ExperienceTypeServiceImpl experienceTypeService;
    private KeySkillServiceImpl keySkillService;
    private MetroLineServiceImpl metroLineService;
    private MetroStationServiceImpl metroStationService;
    private ProfAreaServiceImpl profAreaService;
    private ScheduleTypeServiceImpl scheduleTypeService;
    private SpecializationServiceImpl specializationService;
    private VacancyServiceImpl vacancyService;
    private VacancyTypeServiceImpl vacancyTypeService;
    ///           ///
    /// FUNCTIONS ///
    ///           ///
    File areas = null;

    {
        try {
            areas = ResourceUtils.getFile("D:\\nikita\\nikita_projects\\Projects\\Java\\HeadHunter_JSP\\src\\main\\resources\\areas.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> GetLinksToVacancies()
    {
        // Потом вынесем добавление параметров через GUI
        ArrayList<Double> data = new ArrayList<Double>();
        data.add(8.77);
        data.add(27.498);
        //
        //
        //
        ArrayList<String> links_to_vacancies = new ArrayList<String>();
        //
        NetworkService.getInstance().getHeadHunterApi().getVacancies("HR", null, null, data, null,
                null, null, null, null, null, null)
                .enqueue(new Callback<>()
                {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response)
                    {
                        if (response.isSuccessful())
                        {
                            if(response.body() != null)
                            {
                                JsonArray items = response.body().getAsJsonArray("items");
                                //
                                for (int i = 0; i < items.size(); i++)
                                {
                                    links_to_vacancies.add(i, items
                                            .get(i)
                                            .getAsJsonObject()
                                            .get("url")
                                            .getAsString());
                                }
                            }
                            System.out.println("Список ссылок на вакансии сформирован!");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t)
                    {
                        System.out.println("Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
        return links_to_vacancies;
    }

    private JsonObject GetVacancyJsonByUrl(String url)
    {
        final JsonObject[] result = {new JsonObject()};
        //
        NetworkService.getInstance().getHeadHunterApi().getVacancyByUrl(url).enqueue(new Callback<>()
        {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response)
            {
                if (response.isSuccessful())
                {
                    if(!response.body().isJsonNull())
                    {
                        // Проверка что в ответе именно данные, а не Json ошибки
                        // И что такой вакансии ещё нет в БД
                        if (!response.body().has("id") ||
                                vacancyService.vacancyRepository.existsById(response.body().get("id").getAsInt())
                        )
                        {
                            result[0] = null;
                        }
                        else
                        {
                            result[0] = response.body();
                        }
                    }
                    System.out.println("Vacancy by url:" + url + "loaded!");
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
        //
        return result[0];
    }

    private void ParseAndSaveVacancyFromJson(JsonObject vacancy_json)
    {
        // Init all domain entity
        VacancyType vacancy_type = new VacancyType();
        Employer employer = new Employer();
        Currency currency = new Currency();
        Area area = new Area();
        Address address = new Address();
        MetroStation metro_station = new MetroStation();
        MetroLine metro_line = new MetroLine();
        ExperienceType experience_type = new ExperienceType();
        EmploymentType employment_type = new EmploymentType();
        ScheduleType schedule_type = new ScheduleType();
        ProfArea prof_area = new ProfArea();
        Set<Specialization> specializations = new HashSet<Specialization>();
        Set<KeySkill> key_skills = new HashSet<KeySkill>();
        Set<DriverLicense> driver_licenses = new HashSet<DriverLicense>();
        Set<CompanyScope> company_scopes = new HashSet<CompanyScope>();
        //
        Vacancy vacancy = new Vacancy();
        // Basic
        vacancy.setId(vacancy_json
                .get("id")
                .getAsInt());
        vacancy.setName(vacancy_json
                .get("name")
                .getAsString());
        vacancy.setDescription(vacancy_json
                .get("description")
                .getAsString());
        // Boolean
        vacancy.setArchived(vacancy_json
                .get("archived")
                .getAsBoolean());
        vacancy.setAccept_handicapped(vacancy_json
                .get("accept_handicapped")
                .getAsBoolean());
        vacancy.setAccept_kids(vacancy_json
                .get("accept_kids")
                .getAsBoolean());
        // Time
        String published_at = vacancy_json.get("published_at").getAsString();
        published_at = published_at.substring(0, 22) + ":" + published_at.substring(22, 24);
        // UTC zone
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        // Parsing
        // ISO_LOCAL_DATE_TIME to null offset
        // ISO_OFFSET_DATE_TIME to +hh:mm offset
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(published_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        // Convert from local timezone to UTC
        offsetDateTime = offsetDateTime.withOffsetSameInstant(zoneOffset);
        // Convert to LocalDateTime for compatibility with MySQL RDBMS
        LocalDateTime local_date_time = offsetDateTime.toLocalDateTime();
        // set
        vacancy.setPublished_at(local_date_time);
        //
        // VacancyType
        //
        vacancyTypeService.vacancyTypeRepository
                .findById(vacancy_json
                        .get("type")
                        .getAsJsonObject()
                        .get("id")
                        .getAsString())
                .ifPresent(vacancy::setType);
        //
        // Employer
        //
        if (!vacancy_json.get("employer").getAsJsonObject().isJsonNull())
        {
            JsonObject employer_json = vacancy_json.get("employer").getAsJsonObject();
            String link = employer_json
                    .get("url")
                    .getAsString();
            JsonObject employer_full_json = GetEmployerByUrl(link);
            if (employerService.employerRepository
                    .existsById(employer_full_json
                            .get("id")
                            .getAsInt())
            )
            {
                employerService.employerRepository
                        .findById(employer_full_json
                                .get("id")
                                .getAsInt())
                        .ifPresent(vacancy::setEmployer);
            }
            else
            {
                employer.setId(employer_full_json
                        .get("id")
                        .getAsInt());
                employer.setName(employer_full_json
                        .get("name")
                        .getAsString());
                employerTypeService.employerTypeRepository
                        .findById(employer_full_json
                                .get("type")
                                .getAsString())
                        .ifPresent(employer::setType);
                employer.setDescription(employer_full_json
                        .get("description")
                        .getAsString());

                JsonArray scopes_json = employer_full_json.get("industries").getAsJsonArray();
                for (int i = 0; i < scopes_json.size(); i++)
                {
                    JsonObject array_element = scopes_json.get(i).getAsJsonObject();
                    companyScopeService.companyScopeRepository
                            .findById(Integer.parseInt(array_element
                                    .get("id")
                                    .getAsString()
                                    .split("\\.")[1]))
                            .ifPresent(company_scopes::add);
                }
                // set
                employer.setScopes(company_scopes);
                // save
                employerService.employerRepository.save(employer);
                // set
                vacancy.setEmployer(employer);
            }
        }
        //
        // Area
        //
        JsonObject area_json = vacancy_json.get("area").getAsJsonObject();
        areaService.areaRepository
                .findById(area_json
                        .get("id")
                        .getAsInt())
                .ifPresent(vacancy::setArea);
        //
        // Salary
        //
        if (!vacancy_json.get("salary").isJsonNull())
        {
            JsonObject salary_json = vacancy_json.get("salary").getAsJsonObject();
            vacancy.setSalary_from(salary_json
                    .get("from")
                    .getAsInt());
            vacancy.setSalary_to(salary_json
                    .get("to")
                    .getAsInt());
            vacancy.setSalary_gross(salary_json
                    .get("gross")
                    .getAsBoolean());
            //
            // Currency
            //
            currencyService.currencyRepository
                    .findById(salary_json
                            .get("currency")
                            .getAsString())
                    .ifPresent(vacancy::setCurrency);
        }
        //
        // Address
        //
        JsonObject address_json = vacancy_json.get("address").getAsJsonObject();
        address.setCity(address_json
                .get("city")
                .getAsString());
        address.setStreet(address_json
                .get("street")
                .getAsString());
        address.setBuilding(address_json
                .get("building")
                .getAsString());
        address.setDescription(address_json
                .get("description")
                .getAsString());
        address.setLat(address_json
                .get("lat")
                .getAsDouble());
        address.setLng(address_json
                .get("lng")
                .getAsDouble());
        // save and set
        vacancy.setAddress(addressService.addressRepository.save(address));
        //
        // Metro
        //
        if (!vacancy_json.get("address").getAsJsonObject().get("metro").isJsonNull())
        {
            JsonObject metro_json = vacancy_json
                    .get("address")
                    .getAsJsonObject()
                    .get("metro")
                    .getAsJsonObject();
            metroStationService.metroStationRepository
                    .findById(Integer.parseInt(metro_json
                            .get("station_id")
                            .getAsString()
                            .split("\\.")[1]))
                    .ifPresent(vacancy::setMetro_station);
        }
        //
        // ExperienceType
        //
        JsonObject experience_type_json = vacancy_json
                .get("experience")
                .getAsJsonObject();
        experienceTypeService.experienceTypeRepository
                .findById(experience_type_json
                        .get("id")
                        .getAsString())
                .ifPresent(vacancy::setExperience);
        //
        // EmploymentType
        //
        JsonObject employment_type_json = vacancy_json.get("employment").getAsJsonObject();
        employmentTypeService.employmentTypeRepository
                .findById(employment_type_json
                        .get("id")
                        .getAsString())
                .ifPresent(vacancy::setEmployment);
        //
        // ScheduleType
        //
        JsonObject schedule_type_json = vacancy_json.get("schedule").getAsJsonObject();
        scheduleTypeService.scheduleTypeRepository
                .findById(schedule_type_json
                        .get("id")
                        .getAsString())
                .ifPresent(vacancy::setSchedule);
        //
        // Specialization's with ProfArea's
        //
        JsonArray specializations_json = vacancy_json.get("specializations").getAsJsonArray();
        for (int i = 0; i < specializations_json.size(); i++)
        {
            JsonObject array_element = specializations_json.get(i).getAsJsonObject();
            // ProfArea - вернётся автоматически через связь, если конечно такая специализация есть в БД
            specializationService.specializationRepository
                    .findById(Integer.parseInt(array_element
                            .get("id")
                            .getAsString()
                            .split("\\.")[1]))
                    .ifPresent(specializations::add);
        }
        // set
        vacancy.setSpecializations(specializations);
        //
        // KeySkill
        //
        JsonArray key_skills_json = vacancy_json.get("key_skills").getAsJsonArray();
        for (int i = 0; i < key_skills_json.size(); i++)
        {
            JsonObject array_element = key_skills_json.get(i).getAsJsonObject();
            // вот тут нужна проверка, т.к. данные не со справочников, а введённые вручную
            if (keySkillService.existsByName(array_element
                    .get("name")
                    .getAsString())
            )
            {
                // add
                key_skills.add(keySkillService
                        .findOneByName(array_element
                                .get("name")
                                .getAsString()));
            }
            else
            {
                KeySkill key_skill = new KeySkill();
                key_skill.setName(array_element
                        .get("name")
                        .getAsString());
                // add and save
                key_skills.add(keySkillService.keySkillRepository
                        .save(key_skill));
            }
        }
        // set
        vacancy.setKey_skills(key_skills);
        //
        // DriverLicense
        //
        JsonArray driver_license_types_array = vacancy_json
                .get("driver_license_types")
                .getAsJsonArray();
        if (!driver_license_types_array.isJsonNull())
        {
            for (int i = 0; i < driver_license_types_array.size(); i++)
            {
                JsonObject array_element = driver_license_types_array
                        .get(i)
                        .getAsJsonObject();
                driverLicenseService.driverLicenseRepository
                        .findById(array_element
                                .get("id")
                                .getAsString())
                        .ifPresent(driver_licenses::add);
            }
            // set
            vacancy.setDriver_licenses(driver_licenses);
        }
        vacancyService.vacancyRepository.save(vacancy);
        // !!! где-то тут ещё нужно сохранить саму вакансию
    }

    private JsonObject GetEmployerByUrl(String url)
    {
        final JsonObject[] employer_json = {new JsonObject()};
        //
        NetworkService.getInstance().getHeadHunterApi().getEmployerByUrl(url).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response)
            {
                if (response.isSuccessful())
                {
                    if(response.body() != null)
                    {
                        employer_json[0] = response.body().getAsJsonObject();
                    }
                    else
                    {
                        employer_json[0] = null;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
        return employer_json[0];
    }

    private void GetDictionaries()
    {
        NetworkService.getInstance().getHeadHunterApi().getDictionaries().enqueue(new Callback<>()
        {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response)
            {
                if (response.isSuccessful())
                {
                    if(!response.body().isJsonNull())
                    {
                        JsonObject dictionaries_json = response.body().getAsJsonObject();
                        //
                        // VacancyType
                        //
                        JsonArray vacancy_type_array = dictionaries_json.get("vacancy_type").getAsJsonArray();
                        for (int i = 0; i < vacancy_type_array.size(); i++)
                        {
                            JsonObject vacancy_type_json = vacancy_type_array.get(i).getAsJsonObject();
                            if (!vacancyTypeService.vacancyTypeRepository
                                    .existsById(vacancy_type_json
                                            .get("id")
                                            .getAsString())
                            )
                            {
                                VacancyType vacancy_type = new VacancyType();
                                //
                                vacancy_type.setId(vacancy_type_json
                                        .get("id")
                                        .getAsString());
                                vacancy_type.setName(vacancy_type_json
                                        .get("name")
                                        .getAsString());
                                vacancyTypeService.vacancyTypeRepository.save(vacancy_type);
                            }
                        }
                        //
                        // Currency
                        //
                        JsonArray currency_array = dictionaries_json.get("currency").getAsJsonArray();
                        for (int i = 0; i < currency_array.size(); i++)
                        {
                            JsonObject array_element = currency_array.get(i).getAsJsonObject();
                            if (!currencyService.currencyRepository
                                    .existsById(array_element
                                            .get("code")
                                            .getAsString())
                            )
                            {
                                Currency currency = new Currency();
                                //
                                currency.setCode(array_element
                                        .get("code")
                                        .getAsString());
                                currency.setAbbr(array_element
                                        .get("abbr")
                                        .getAsString());
                                currency.setName(array_element
                                        .get("name")
                                        .getAsString());
                                currency.setRate_to_rub(array_element
                                        .get("rate")
                                        .getAsDouble());
                                currencyService.currencyRepository.save(currency);
                            }
                        }
                        //
                        // ExperienceType
                        //
                        JsonArray experience_array = dictionaries_json.get("experience").getAsJsonArray();
                        for (int i = 0; i < experience_array.size(); i++)
                        {
                            JsonObject array_element = experience_array.get(i).getAsJsonObject();
                            if (!experienceTypeService.experienceTypeRepository
                                    .existsById(array_element
                                            .get("id")
                                            .getAsString())
                            )
                            {
                                ExperienceType experience_type = new ExperienceType();
                                //
                                experience_type.setId(array_element
                                        .get("id")
                                        .getAsString());
                                experience_type.setName(array_element
                                        .get("name")
                                        .getAsString());
                                experienceTypeService.experienceTypeRepository.save(experience_type);
                            }
                        }
                        //
                        // EmploymentType
                        //
                        JsonArray employment_array = dictionaries_json.get("employment").getAsJsonArray();
                        for (int i = 0; i < employment_array.size(); i++)
                        {
                            JsonObject array_element = employment_array.get(i).getAsJsonObject();
                            if (!employmentTypeService.employmentTypeRepository
                                    .existsById(array_element
                                            .get("id")
                                            .getAsString())
                            )
                            {
                                EmploymentType employment_type = new EmploymentType();
                                //
                                employment_type.setId(array_element
                                        .get("id")
                                        .getAsString());
                                employment_type.setName(array_element
                                        .get("name")
                                        .getAsString());
                                employmentTypeService.employmentTypeRepository.save(employment_type);
                            }
                        }
                        //
                        // ScheduleType
                        //
                        JsonArray schedule_array = dictionaries_json.get("schedule").getAsJsonArray();
                        for (int i = 0; i < schedule_array.size(); i++)
                        {
                            JsonObject array_element = schedule_array.get(i).getAsJsonObject();
                            if (!scheduleTypeService.scheduleTypeRepository
                                    .existsById(array_element
                                            .get("id")
                                            .getAsString())
                            )
                            {
                                ScheduleType schedule_type = new ScheduleType();
                                //
                                schedule_type.setId(array_element
                                        .get("id")
                                        .getAsString());
                                schedule_type.setName(array_element
                                        .get("name")
                                        .getAsString());
                                scheduleTypeService.scheduleTypeRepository.save(schedule_type);
                            }
                        }
                        //
                        // DriverLicense
                        //
                        JsonArray driver_license_types_array = dictionaries_json.get("driver_license_types").getAsJsonArray();
                        for (int i = 0; i < driver_license_types_array.size(); i++)
                        {
                            JsonObject array_element = driver_license_types_array.get(i).getAsJsonObject();
                            if (!driverLicenseService.driverLicenseRepository
                                    .existsById(array_element
                                            .get("id")
                                            .getAsString())
                            )
                            {
                                DriverLicense driver_license = new DriverLicense();
                                //
                                driver_license.setId(array_element
                                        .get("id")
                                        .getAsString());
                                driverLicenseService.driverLicenseRepository.save(driver_license);
                            }
                        }
                    }
                    System.out.println("Dictionaries from https://api.hh.ru/specializations loaded!");
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
    }

    private void GetProfAreasAndSpecializations()
    {
        NetworkService.getInstance().getHeadHunterApi().getSpecializations().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if (response.isSuccessful())
                {
                    if (!response.body().isJsonNull())
                    {
                        JsonArray prof_areas_and_specializations_json = response.body().getAsJsonArray();
                        for (int i = 0; i < prof_areas_and_specializations_json.size(); i++)
                        {
                            JsonObject prof_area_json = prof_areas_and_specializations_json.get(i).getAsJsonObject();
                            //
                            ProfArea prof_area = new ProfArea();
                            //
                            if (!profAreaService.profAreaRepository
                                    .existsById(prof_area_json
                                            .get("id")
                                            .getAsInt())
                            )
                            {
                                prof_area.setId(prof_area_json
                                        .get("id")
                                        .getAsInt());
                                prof_area.setName(prof_area_json
                                        .get("name")
                                        .getAsString());
                                // save
                                profAreaService.profAreaRepository.save(prof_area);
                            }
                            else
                            {
                                prof_area = profAreaService.profAreaRepository
                                        .getOne(prof_area_json
                                                .get("id")
                                                .getAsInt());
                            }
                            //
                            JsonArray specializations_json = prof_area_json.get("specializations").getAsJsonArray();
                            for (int j = 0; j < specializations_json.size(); j++)
                            {
                                Specialization specialization = new Specialization();
                                //
                                JsonObject spec_json = specializations_json.get(j).getAsJsonObject();
                                if (!specializationService.specializationRepository
                                        .existsById(Integer.parseInt(spec_json
                                                .get("id")
                                                .getAsString()
                                                .split("\\.")[1]))
                                )
                                {
                                    specialization.setId(Integer.parseInt(spec_json
                                            .get("id")
                                            .getAsString()
                                            .split("\\.")[1]));
                                    specialization.setName(spec_json
                                            .get("name")
                                            .getAsString());
                                    // set
                                    specialization.setProfArea(prof_area);
                                    // save
                                    specializationService.specializationRepository.save(specialization);
                                }
                            }
                        }
                        System.out.println("ProfAreas and Specializations from https://api.hh.ru/specializations loaded!");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
    }

    private void GetAreas()
    {
        NetworkService.getInstance().getHeadHunterApi().getAreas().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response)
            {
                if (response.isSuccessful())
                {
                    if(!response.body().isJsonNull())
                    {
                        JsonArray areas_array = response.body().getAsJsonArray();
                        // Сохраняем Areas в файл areas.json
                        //File areas = null;

                        try
                        {
                            Writer writer = new FileWriter(areas);
                            JsonWriter json_writer = new JsonWriter(writer);
                            //
                            Gson gson = new GsonBuilder().create();
                            gson.toJson(areas_array, json_writer);
                            json_writer.close();
                        } catch (IOException exception) {
                            System.out.println("Ошибка при сохранении JsonArray с регионами в файл " + areas.getAbsolutePath());
                            exception.printStackTrace();
                        }
                        System.out.println("JsonArray с регионами сохранён в файл " + areas.getAbsolutePath());
                        //
                        for (int i = 0; i < areas_array.size(); i++)
                        {
                            // Countries level
                            JsonObject country_json = areas_array.get(i).getAsJsonObject();
                            if (!areaService.areaRepository
                                    .existsById(country_json
                                            .get("id")
                                            .getAsInt())
                            )
                            {
                                Area country = new Area();
                                country.setId(country_json
                                        .get("id")
                                        .getAsInt());
                                country.setName(country_json
                                        .get("name")
                                        .getAsString());
                                // save
                                areaService.areaRepository.save(country);
                            }
                            // Regions level
                            JsonArray region_array = country_json.get("areas").getAsJsonArray();
                            for (int j = 0; j < region_array.size(); j++)
                            {
                                JsonObject region_json = region_array.get(j).getAsJsonObject();
                                if (!areaService.areaRepository
                                        .existsById(region_json
                                                .get("id")
                                                .getAsInt())
                                )
                                {
                                    Area region = new Area();
                                    region.setId(region_json
                                            .get("id")
                                            .getAsInt());
                                    region.setName(region_json
                                            .get("name")
                                            .getAsString());
                                    // save
                                    areaService.areaRepository.save(region);
                                }
                                // Cities level
                                JsonArray city_array = region_json.get("areas").getAsJsonArray();
                                for (int k = 0; k < city_array.size(); k++)
                                {
                                    JsonObject city_json = city_array.get(k).getAsJsonObject();
                                    if (!areaService.areaRepository
                                            .existsById(city_json
                                                    .get("id")
                                                    .getAsInt())
                                    )
                                    {
                                        Area city = new Area();
                                        city.setId(city_json
                                                .get("id")
                                                .getAsInt());
                                        city.setName(city_json
                                                .get("name")
                                                .getAsString());
                                        areaService.areaRepository.save(city);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
    }



    private void GetMetroStationsAndLines()
    {
        NetworkService.getInstance().getHeadHunterApi().getMetro().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response)
            {
                if (response.isSuccessful())
                {
                    if(!response.body().isJsonNull())
                    {
                        JsonArray metro_array = response.body().getAsJsonArray();
                        for (int i = 0; i < metro_array.size(); i++)
                        {
                            // Cities level
                            JsonObject city_json = metro_array.get(i).getAsJsonObject();
                            Area city = areaService.areaRepository
                                    .getOne(city_json
                                            .get("id")
                                            .getAsInt());
                            // Lines level
                            JsonArray lines_array = city_json.get("lines").getAsJsonArray();
                            for (int j = 0; j < lines_array.size(); j++)
                            {
                                JsonObject line_json = lines_array.get(j).getAsJsonObject();
                                MetroLine metro_line = new MetroLine();
                                if (!metroLineService.metroLineRepository
                                        .existsById(line_json
                                                .get("id")
                                                .getAsInt())
                                )
                                {
                                    metro_line.setId(line_json
                                            .get("id")
                                            .getAsInt());
                                    metro_line.setName(line_json
                                            .get("name")
                                            .getAsString());
                                    metro_line.setArea(city);
                                    // save
                                    metroLineService.metroLineRepository.save(metro_line);
                                }
                                else
                                {
                                    metro_line = metroLineService.metroLineRepository
                                            .getOne(line_json
                                                    .get("id")
                                                    .getAsInt());
                                }
                                // Stations level
                                JsonArray stations_array = line_json.get("stations").getAsJsonArray();
                                for (int k = 0; k < stations_array.size(); k++)
                                {
                                    JsonObject station_json = stations_array.get(k).getAsJsonObject();
                                    MetroStation metro_station = new MetroStation();
                                    if (!metroStationService.metroStationRepository
                                            .existsById(Integer.parseInt(station_json
                                                    .get("id")
                                                    .getAsString()
                                                    .split("\\.")[1]))
                                    )
                                    {
                                        metro_station.setId(Integer.parseInt(station_json
                                                .get("id")
                                                .getAsString()
                                                .split("\\.")[1]));
                                        metro_station.setName(station_json
                                                .get("name")
                                                .getAsString());
                                        metro_station.setLine(metro_line);
                                        // save
                                        metroStationService.metroStationRepository.save(metro_station);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
    }

    private void GetCompanyIndustriesAndCompanyScopes()
    {
        NetworkService.getInstance().getHeadHunterApi().getIndustries().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response)
            {
                if (response.isSuccessful())
                {
                    if(!response.body().isJsonNull())
                    {
                        JsonArray industries_array = response.body().getAsJsonArray();
                        //
                        for (int i = 0; i < industries_array.size(); i++)
                        {
                            // CompanyIndustries level
                            JsonObject company_industry_json = industries_array.get(i).getAsJsonObject();
                            CompanyIndustry company_industry = new CompanyIndustry();
                            //
                            if (!companyIndustryService.companyIndustryRepository
                                    .existsById(company_industry_json
                                            .get("id")
                                            .getAsInt())
                            )
                            {
                                company_industry.setId(company_industry_json
                                        .get("id")
                                        .getAsInt());
                                company_industry.setName(company_industry_json
                                        .get("name")
                                        .getAsString());
                                // save
                                companyIndustryService.companyIndustryRepository.save(company_industry);
                            }
                            else
                            {
                                company_industry = companyIndustryService.companyIndustryRepository
                                        .getOne(company_industry_json
                                                .get("id")
                                                .getAsInt());
                            }
                            //
                            JsonArray company_scopes_array = company_industry_json.get("industries").getAsJsonArray();
                            for (int j = 0; j < company_scopes_array.size(); j++)
                            {
                                CompanyScope company_scope = new CompanyScope();
                                //
                                JsonObject company_scope_json = company_scopes_array.get(j).getAsJsonObject();
                                if (!companyScopeService.companyScopeRepository
                                        .existsById(Integer.parseInt(company_scope_json
                                                .get("id")
                                                .getAsString()
                                                .split("\\.")[1]))
                                )
                                {
                                    company_scope.setId(Integer.parseInt(company_scope_json
                                            .get("id")
                                            .getAsString()
                                            .split("\\.")[1]));
                                    company_scope.setName(company_scope_json
                                            .get("name")
                                            .getAsString());
                                    // set
                                    company_scope.setCompanyIndustry(company_industry);
                                    // save
                                    companyScopeService.companyScopeRepository.save(company_scope);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
    }



    private void GetTestVacancy(int id)
    {
        NetworkService.getInstance().getHeadHunterApi().getVacancyById(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response)
            {
                if (response.isSuccessful())
                {
                    if(!response.body().isJsonNull())
                    {
                        JsonObject vacancy_json = response.body().getAsJsonObject();
                        JsonElement salary_json = vacancy_json.get("salary");
                        System.out.println(salary_json.isJsonNull());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t)
            {
                System.out.println("Error occurred while getting request!");
                t.printStackTrace();
            }
        });
    }

    private void TestSave(Address address)
    {
        addressService.addressRepository.save(address);
    }

    private void SetStaticClassesParametersToNull()
    {
        int prof_areas_count = profAreaService.profAreaRepository.findAll().size();
        int specializations_count = specializationService.specializationRepository.findAll().size();
        //
        int company_industries_count = companyIndustryService.companyIndustryRepository.findAll().size();
        int company_scopes_count = companyScopeService.companyScopeRepository.findAll().size();
        //
        int areas_count = areaService.areaRepository.findAll().size();
        System.out.println(areas_count);
        //
        int sum_specializations_count = prof_areas_count + specializations_count;
        int sum_company_industries_count = company_industries_count + company_scopes_count;
        //
        LoadingParameters.SetLoadingParametersArrayListsToNull(
                prof_areas_count, specializations_count,
                company_industries_count, company_scopes_count,
                areas_count);
        RequestParameters.SetRequestParametersArrayListsToNull(
                sum_specializations_count,
                sum_company_industries_count,
                areas_count);
    }



    // Проблема. Не обнуляются элементы (в частности города), если был выбран вышестоящий элемент


    /////// Диплом отчёт ///////
    // 1. Перерисовать картинку в Анализе объекта, добавив добавленные позже поля Scopes and Industries
    // 2. Добавить требования пользователей в таблицу.
    //    Добавить требования к интерфейсу.
    //    Добавить системные требования.
    //    Требования к оборудованию.
    // 3. Добавить к картинке с архитектурой отображение UI через Swing и Retrofit API если ещё нет
    //    А лучше создать ещё одну - архитектура клиентской части.
    //    Получение данных с API HH, через Retrofit API.
    //    Сохранение в MySQL RDBMS через Spring JPA или как там его - Spring Data JPA
    //    Ну и вывод на экран через Swing
    // 4. Добавить хоть немного текста в проектирование структур хранения данных
    //    Что данные хранятся в БД в виде таблиц, а в самом приложение в виде классов Entity - уже есть
    //    Отдельно описать о методах хранения параметров запроса
    // 5. Для вариантов использования добавить теории, что такое актор, какие типы связей бывают
    //    А потом уже то что есть - как реализуется вариант использования, и какими методами
    // 6. Реализация - примеры кода и методов, и что они делают. В принципе, у нас уже есть комментарии



    ///////   CODE   ///////
    // (+/-) Создать статический класс для хранения параметров запроса
    // (+/-) Создать статический класс для хранения параметров окна
    // Добавить действия по добавлению параметров в статический класс слушателям RadioButton и Checkbox в окне настроек
    // Добавить действия на кнопку Сохранение параметров в окне настроек
    //    нужно будет сохранять только выборы RadioButton'ов и CheckBox'ов окна настроек
    // (+/-) Заполнение списков листов данными выбранными в других окнах из класса с параметрами окна
    // Заполнение параметров запроса из статического класса с параметрами
    // (+) Метод для загрузки JSON Areas, но уже для меню
    //    наверное, стоит попробовать сохранять этот JSON в файл на диске, чтобы можно было загружать менюшки без Интернета
    // (+/-) Автогенерация окошек с меню из БД
    // Заполнение списка вакансий на основном меню - ListModel, ListRenderer
    // Вывод информации об отдельных вакансиях на панель фрейма - GridBagLayout
    // (+) Перерисовка окон с использованием данных, полученных из глобальных параметров из стат. класса






    ///    HELP    ///
    // getOne вовзращается лениво (только id), для операций удаления, например
    // findById возвращается жадно, весь объект

    // isJsonNull возвращает true если например, salary: null;
    // А вот == null возвращает false, т.к. почему-то JsonElement хранит null как "null"

    // bool спокойно сохраняется в MySQL в виде null

    // Вовсе не обязательно добавлять к response.body() ещё и getAsJsonArray или getAsJsonObject
    // т.к. тип вовращаемого значения уже прописан в методе



    public HeadHunterJSP_App_MainFrame() // Constructor
    {
        BeanProvider.autowire(this);
        //
        this.addressService = BeanProvider.applicationContext.getBean(AddressServiceImpl.class);
        this.areaService = BeanProvider.applicationContext.getBean(AreaServiceImpl.class);
        this.companyIndustryService = BeanProvider.applicationContext.getBean(CompanyIndustryServiceImpl.class);
        this.companyScopeService = BeanProvider.applicationContext.getBean(CompanyScopeServiceImpl.class);
        this.currencyService = BeanProvider.applicationContext.getBean(CurrencyServiceImpl.class);
        this.driverLicenseService = BeanProvider.applicationContext.getBean(DriverLicenseServiceImpl.class);
        this.employerService = BeanProvider.applicationContext.getBean(EmployerServiceImpl.class);
        this.employerTypeService = BeanProvider.applicationContext.getBean(EmployerTypeServiceImpl.class);
        this.employmentTypeService = BeanProvider.applicationContext.getBean(EmploymentTypeServiceImpl.class);
        this.experienceTypeService = BeanProvider.applicationContext.getBean(ExperienceTypeServiceImpl.class);
        this.keySkillService = BeanProvider.applicationContext.getBean(KeySkillServiceImpl.class);
        this.metroLineService = BeanProvider.applicationContext.getBean(MetroLineServiceImpl.class);
        this.metroStationService = BeanProvider.applicationContext.getBean(MetroStationServiceImpl.class);
        this.profAreaService = BeanProvider.applicationContext.getBean(ProfAreaServiceImpl.class);
        this.scheduleTypeService = BeanProvider.applicationContext.getBean(ScheduleTypeServiceImpl.class);
        this.specializationService = BeanProvider.applicationContext.getBean(SpecializationServiceImpl.class);
        this.vacancyService = BeanProvider.applicationContext.getBean(VacancyServiceImpl.class);
        this.vacancyTypeService = BeanProvider.applicationContext.getBean(VacancyTypeServiceImpl.class);
        //
        setTitle("HeadHunter Job Search Project App");
        setSize(1920, 1080); // 860, 560
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        //
        //setExtendedState(MAXIMIZED_BOTH);
        ///              ///
        /// Init actions ///
        ///              ///
        SetStaticClassesParametersToNull();
        ///             ///
        /// North panel ///
        ///             ///
        panel_north.setLayout((new BorderLayout()));
        panel_north.add(panel_north_west, BorderLayout.WEST);
        panel_north.add(panel_north_east, BorderLayout.EAST);
        ///                  ///
        /// North west panel ///
        ///                  ///
        label_app_name.setFont(new Font("Serif", Font.BOLD, 20));
        label_app_name.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        panel_north_west.add(label_app_name, BorderLayout.WEST);
        ///                  ///
        /// North east panel ///
        ///                  ///
        FlowLayout flow_lt_north_east = new FlowLayout();
        flow_lt_north_east.setHgap(20);
        flow_lt_north_east.setVgap(20);
        panel_north_east.setLayout(flow_lt_north_east);
        //
        // Кнопка "Настройки параметров загрузки вакансий"
        btn_load_settings.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Load_Settings_Frame load_settings_frame = new Load_Settings_Frame();
                load_settings_frame.setVisible(true);
                /*
                try
                {
                }
                catch (SQLException sqlException) {}
                */
                //dispose(); // Выход
            }
        });
        //
        // Кнопка "Настройки параметров просмотра вакансий"
        btn_view_settings.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //dispose(); // Выход
            }
        });
        //
        panel_north_east.add(btn_load_settings);
        panel_north_east.add(btn_view_settings);
        add(panel_north, BorderLayout.NORTH);
        ///              ///
        /// Center panel ///
        ///              ///
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(400);
        //
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent pce)
            {
                min_size_list = vacanciesListScrollPane.getPreferredSize();
            }
        });
        // Provide minimum sizes for the two components in the split pane.
        vacanciesListScrollPane.setMinimumSize(new Dimension(400, 50));
        vacanciesInfoScrollPane.setMinimumSize(new Dimension(400, 50));
        // Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(1850, 900)); // 1200, 520
        ///                   ///
        /// Center west panel ///
        ///                   ///
        // Vacancies List
        vacanciesListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        vacanciesListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        vacanciesListScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        /*
        list = new JList();
        SetModelToJList(list);
        list.setCellRenderer(createListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(-1);
        list.setFont(new Font("Serif", Font.BOLD, 20));
        list.setPreferredSize(min_size_list);
        // Выбор чата из списка
        list.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if (list.isSelectionEmpty())
                {
                    //btn_AddChat.setText("Добавить чат");
                    //selected_chat = null;
                }
                else
                {
                    /*
                    btn_AddChat.setText("Изменить чат");
                    //
                    selected_chat = list.getSelectedValue();
                    // другой вариант - list.getModel().getElementAt(list.getSelectedIndex());
                    // (+) Сюда нужен код создания списка members чтобы потом загрузить чат через Post
                    //members = (ArrayList<User>) ((Chat) list.getSelectedValue()).getMembers();
                    GetAllMembers(token, Integer.parseInt(selected_chat.getId()));
                    GetAllMessages(token, Integer.parseInt(selected_chat.getId()));
                    RevalidateBasisForAllMessages();
                    PrintAllMessages();

                    // Нужно доставать вакансии и инфу по ним через методы репозиториев
                }
            }
        });
        */
        panel_center_west.setLayout(new BorderLayout());
        //panel_center_west_list.add(list, BorderLayout.CENTER);
        //panel_center_west_list.add(btn_AddChat, BorderLayout.NORTH);
        //panel_center_west_list.add(btn_DeleteChat, BorderLayout.SOUTH);
        ///                   ///
        /// Center east panel ///
        ///                   ///
        // Messages
        panel_center_east.setBackground(new Color(255, 255, 255, 255));
        vacanciesInfoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        vacanciesInfoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        vacanciesInfoScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        //
        //group_layout = new GroupLayout(panel_center_east_messages);
        //panel_center_east_messages.setLayout(group_layout);
        ///                          ///
        /// Adding all center panels ///
        ///                          ///
        panel_center.add(splitPane);
        add(panel_center, BorderLayout.CENTER);
        ///             ///
        /// South panel ///
        ///             ///
        panel_south.setLayout((new BorderLayout()));
        panel_south.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
        panel_south.add(panel_south_west, BorderLayout.WEST);
        panel_south.add(panel_south_east, BorderLayout.EAST);
        ///                  ///
        /// South west panel ///
        ///                  ///
        FlowLayout flow_lt_south_buttons = new FlowLayout();
        flow_lt_south_buttons.setHgap(20);
        flow_lt_south_buttons.setVgap(20);
        panel_south_west.setLayout(flow_lt_south_buttons);
        // Кнопка "Загрузка вакансий"
        btn_load.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                /*
                ArrayList<String> links = new ArrayList<>();
                links = GetLinksToVacancies();
                for (int i = 0; i < links.size(); i++)
                {
                    GetVacancyJsonByUrl(links.get(i));
                }
                */
                try
                {
                    TestLoadingFromJList testLoadingFromJList = new TestLoadingFromJList();
                }
                catch (SQLException sqlException) {}
            }
        });
        // Кнопка "Просмотр вакансий"
        btn_view.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Address test_address = new Address();
                //test_address.setId(3);
                //test_address.setCity("Пекин");
                //test_address.setStreet("ул.Чкалова");
                //test_address.setBuilding("25");
                //
                //TestSave(test_address);
                //dispose(); // Выход
                //GetProfAreasAndSpecializations();
                //GetTestVacancy(44552119);
                GetAreas();
                //GetCompanyIndustriesAndCompanyScopes();
            }
        });
        panel_south_west.add(btn_load);
        panel_south_west.add(btn_view);
        ///                  ///
        /// South east panel ///
        ///                  ///
        // Кнопка "Выход"
        btn_exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
                //dispose(); // Выход
            }
        });
        panel_south_east.add(btn_exit);
        //
        add(panel_south, BorderLayout.SOUTH);
        //
    }
}

///              ///
/// GARBAGE CODE ///
///              ///


///////
// Найдена ещё 1 фича, которую нужно пофиксить.
// Если в класс глоб. параметров уже были добавлены отдельные элементы списка
// А затем, при следущем выборе, не снимая предыдущего, был выбран чекбокс
// То отмечаются, все элементы, добавляется корневой элемент (из чекбокса)
// + !!! с какого-то хрена тот самый отдельно выбранный
// Точно такая же фича работает и наоборот. Если выбрать некоторые элементы, добавить их
// А потом через CheckBox добавить все, то чек бокс просто добавится к остальным в списке
// НО НИКАК НЕ ЗАМЕНЯТ ИХ
// Решено. Если JCheckBox выбран, то все параметры соот-го JList сетятся в null
//
// JList для CompanyIndustries в окне настроек почему-то отображает одну строку 2 раза
// Решено. 1 цикл был скопирован, и поэтому повторялся 2 раза. Решено заменой параметров
///////
///    PLAN    ///
// С методами загрузки и БД вроде всё, нужно городить интерфейс и методы его заполнения + методы анализа
// (+) Нужно добавить поле Area в Metro_Station, чтобы можно было различать станции с одинаковыми названиями, но из разных регионов
// (+) - херня, нужно чтобы MetroStation принадлежала MetroLine, а уже сама MetroLine будет принадлежать к определённому городу (Area)
// (+) Возможно, придётся слегка модифицировать БД, добавить отрасли и подотрасли работы компаний
// (+) Добавить сохранение вакансии в БД, в методе ParseAndSaveVacancyFromJson, (+) если там ещё нет такой вакансии
// Место отмечено в самом методе
// (+) Добавить загрузку Metro из справочников
// (+) Добавить загрузку Company_Industry из справочников
// (+) Specialty - лучше Company_Scope
// (+) Нужно модифицировать загрузку Employer - у него появился список Scope's, который нужно загружать из БД
// (+) Нужно сделать репозитории и сервисы для 2 новых Entity - Industry и Scope
// (+) Добавить эти сервисы в проект
// (+) Сделать метод для загрузки Key_Skills - не нужен, видимо скилы записываются в базу самими людьми,
// пока проверял сколько их, дошёл до 10.000.000, и причём встречается откровенный бред вроде:
//"items": [
//{
//    "id": "6000",
//        "text": "жизнь нада цынить"
//}
//]
// Проще уж грузить их как раньше, постепенно, сохраняя в базу по мере поступления
///////
///    GUI    ///
// для создания адекватного отображения иерархического выпадающего списка Areas
// можно использовать setVisibleRowCount(int visibleRowCount) для настройки количества отображаемых без полосы прокрутки элементов
// можно использовать JMenuBar с JMenu, или через netbeans-outline (но там нужно ещё дерево после парсинга собирать)
///////
//@Query("SELECT specialization FROM Specialization specialization WHERE specialization.prof_area.id = :id")
///////
// Снова какой-то баг с отображением
// Ломается вроде только если выбирать всё в первом поле, и некрирпые в остальных
// Ломается только отображение на окне выбора ProfArea's, с данными и окном настроек всё норм
//
// Сначала был выбран интернет маркетинг, потом управление маркетингом на 3 меню
// В итоге в основном окне всё норм, а в окне ProfArea при повторной загрузке выбирается Ценные бумаги меню 2
//
//Радио реклама меню 3- учёт счетов и платежей меню 2
// 50 индекс показывает - это действительно учёт
// порядковый номер индекс у радио     37 первое меню    радио на 22 месте в своём меню
// 0 - 36 индексы 1 меню (37 всего)
// 0 - 28 индексы 2 меню (29 всего)
// учёт 0 - 50 индекс с начала (0 - 13 в меню 2)
// 0 - 21 радио 3 меню (0 - 87)
//
// Значит отмечаем элемент с индексом 87, текст добавляется в параметры, но на 50 индекс
// Значит загружает он нормально (т.к. грузит те объекты которые не null, и по индексам на которых они записаны)
// А вот при сохранении зачем то смещает на размер предыдущего листа
//
// Решено. Проблема была в том, что счётчик менялся только если JCheckBox не был отмечен
// А нам то нужно считать порядок для всех, а не только неотмеченных
///////
// Итак, с дизайном интерфейса вроде бы определились
// Будем использовать JList, в сочетании с JCheckBox для высшего уровня
// JCheckBox - используется для выбора сразу всех элементов в JList (нужен отдельный метод)
// JList - с мультивыбором, позволяет выбрать сразу несколько элементов
// Нужно только потом как-то передавать их в виде списка в основное окно настроек

// Передавать данные между формами можно с помощью статического класса со статическими полями
// Создавать несколько объектов с разными именами во время выполнения программы нельзя
// Но можно сделать массив с этими объектами, например

// Для адекватной работы setVisibleRowCount нужно чтобы JScrollPane был добавлен на JPanel
// И не нжуно создавать на JPanel никаких Layout, иначе все ломается
// setPrefferedSize() тоже частично мешает работе
// 1. Создаём JPanel, JList и JScrollPane
// 2. Добавляем JList на JScrollPane, а JScrollPane на JPanel
///////
// нужно как-то сортировать specializations по нужной prof_area
// как вариант, создать отдельный ResultSet и выбрать в него id prof_area,
// а потом через параметр ID делать запрос через WHERE

/*
                        JsonArray industries_array = response.body().getAsJsonArray();
                        //
                        String max_longer_name_industry;
                        int max_longer_industry = 0;
                        //
                        String max_longer_name_scope;
                        int max_longer_scope = 0;
                        //
                        for (int i = 0; i < industries_array.size(); i++)
                        {
                            JsonObject industry_json = industries_array.get(i).getAsJsonObject();
                            //
                            max_longer_name_industry = industry_json.get("name").getAsString();
                            //
                            if (max_longer_industry < max_longer_name_industry.length())
                            {
                                max_longer_industry = max_longer_name_industry.length();
                            }
                            //
                            JsonArray scopes_array = industry_json.get("industries").getAsJsonArray();
                            //
                            for (int j = 0; j < scopes_array.size(); j++)
                            {
                                JsonObject scope_json = scopes_array.get(j).getAsJsonObject();
                                //
                                max_longer_name_scope = scope_json.get("name").getAsString();
                                //
                                if (max_longer_scope < max_longer_name_scope.length())
                                {
                                    max_longer_scope = max_longer_name_scope.length();
                                }
                            }
                        }
                        System.out.println("Максимальная длина Industry - " + max_longer_industry + "символов");
                        System.out.println("Максимальная длина Scope - " + max_longer_scope + "символов");
*/

// Пустыми могут быть:
// (+) Массив станций метро в адресе - metro_stations
// (+) Работодатель, если вакансий анон. - employer
// (+) Все строчки о ЗП - salary (но salary.currency всегда будет, даже если невыставлена - в RUB)

// (+) specializations
//ProfArea prof_area = new ProfArea();
//Set<Specialization> specializations = new HashSet<Specialization>();
//
// (+) areas
//Area area = new Area();
// metro
//MetroStation metro_station = new MetroStation();
//MetroLine metro_line = new MetroLine();
//
// (+) Чтобы получить бины сервисов, придётся или присваивать их через ApplicationContext в запускающем классе
// в функции main(), либо через ту же функцию в самом конструкторе JFrame

// (+) Нужно добавить, что Employer и Metro при получении могут быть пустыми
// (+) Также нужна проверка через ИЛИ || , что вакансии с таким ID ещё нет в БД
// Иначе в цикле пропускаем эту вакансию через continue

// 1. (+) Загрузка специализация и профобластей из https://api.hh.ru/specializations
// 4. Возможно, придётся слегка модифицировать БД, добавить отрасли и подотрасли работы компаний,
//     загрузка также из справочников
// 2. (+) Нужно убрать лишние части методов, где создаются заново дочерние сущности,
//     эти части нужны только для инициирующей загрузки из dictionaries
// 3. (+) Нужно также откалибровать время по установленному формату, есть скрины и видео от Андрея letscode
// 1. (+) И нужен метод загрузка словарей из URL dictionaries - DictionariesInit
//     с проверкой, появились ли новые значения, и только тогда сохранять новые
//
//
//
// Здесь нужно:
// распарсить данные из JSON
// создать вакансию по полученным данным
// !!! сохранить её в БД, если там ещё нет такой вакансии
// Значит, нужна проверка, есть ли уже вакансий с таким id
//
// что-то типа
// JsonObject vacancy = response.body();
// JsonObject salary = vacancy.get("salary").getAsJsonObject();
// String currency_code = salary.get("currency").getAsString();
// обращаемся в нужный репозиторий, типо
// Currency currency = GetCurrencyById(currency_code);
// а затем уже присваиваем полученный объект в корневой объект Vacancy
// Vacancy vacancy = new Vacancy();
// и потом либо в конструкторе, либо с помощью set'ов присваиваем значения
// и сохраняем в БД через нужный репозиторий
//

// сохранить area? или сначала присвоить в vacancy и сохранить всё сразу?
// в принципе, можно и так, и так
// просто придётся переделать сервисы, в частности добавить репозиториев в сервис vacancy
//
// Возможно, лучше сохранить данные со справочников "dictionaries" 1 раз
// и использовать многократно, просто возвращая целиком объект по полученному id
// и затем присваивая его в vacancy
//
// Response body // Хз, нужно ли дополнительное приведение к JsonObject
//JsonObject vacancy_json = vacancy_json;
// Проверка есть ли уже такая вакансия в БД - не нужна тут, была вынесена в вышележащий метод
//vacancyService.vacancyRepository.existsById(vacancy_json.get("id").getAsInt());

// Короче, оставляем сервисный слой для каких-либо сложных методов,
// либо для методов, требущих несколько репозиториев.
// Можно сделать доступ сразу к репозиториям, но тогда нельзя будет никак добавить новые функции,
// т.к. для них нужна реализация интерфейса репозитория, что по сути и является сервисом

// Короче, будем использовать LocalDateTime - не сохраняется автоматически в БД, нужен конвертер
// несовместим без конвертера с DATETIME, но совместим с TIMESTAMP
// DATETIME не хранит timezone, TIMESTAMP хранит
// Скорее всего, будет нужен SimpleDate что то там, чтобы время из запроса в переводить 0 UTC,
// а нужное время будет получаться само, приплюсовывая ±hhmm из местного часового пояса,
// но нужно тестить
        /*
        @Converter(autoApply = true)
        public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
            @Override
            public java.sql.Timestamp convertToDatabaseColumn(LocalDateTime entity_value) {
                return java.sql.Timestamp.valueOf(entity_value);
            }

            @Override
            public LocalDate convertToEntityAttribute(java.sql.Timestamp database_value) {
                return database_value.toLocalDateTime();
            }
        }

        Есть два варианта определения использования конвертера.
        Первый - установить autoapply = true в аннотации @Converter класса Converter.
        В этом случае поставщик JPA будет использовать этот конвертер
        для преобразования всех атрибутов сущности данного типа.
        Если для autoapply установлено значение false, вам необходимо добавить аннотацию javax.persistence.Convert
        ко всем атрибутам, которые должны быть преобразованы, и указать класс Converter.

        @Convert(converter = LocalDateTimeConverter.class)
        privare LocalDateTime localdatetime

        // Будем использовать TIMESTAMP для БД MySQL и LocalDateTime для приложения
        // Т.к. TIMESTAMP будет хранить время в UTC 0, а при выборке данных плюсовать к ним часовой пояс
        // Это будет полезно для работы приложения в разных часовых поясах
        // А самому приложению знать часовой пояс необязательно, т.к. он автоматически получает его из БД

        // Для конвертации из Json в удобоваримый формат времени с 0 поясом UTC (нужно редачить)
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));   // This line converts the given date into UTC time zone
        final java.util.Date dateObj = sdf.parse("2013-10-22T01:37:56");
        //
        aRevisedDate = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").format(dateObj);
        System.out.println(aRevisedDate);
        */

//@DateTimeFormat - всего лишь позволяет настроить формат вывода данных, а не их хранения
// вешается на Date, Calendar, Long
//DateTimeFormat.ISO.DATE_TIME
//YYYY-MM-DDThh:mm:ss±hhmm (где hhmm - смещение относительно UTC, например 0300)