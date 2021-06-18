package by.barsnik96.HeadHunter_JSP.domain;

import by.barsnik96.HeadHunter_JSP.utils.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "vacancies")
public class Vacancy
{
    @Id
    @Column(name = "vacancy_ID")
    private int id;

    @Column(name = "vacancy_name")
    private String name;

    @Column(name = "vacancy_description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_type")
    private VacancyType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_employer")
    private Employer employer;

    @Column(name = "vacancy_salary_from")
    private int salary_from;

    @Column(name = "vacancy_salary_to")
    private int salary_to;

    @Column(name = "vacancy_salary_gross")
    private boolean salary_gross;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_salary_currency")
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_area")
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_address")
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_experience")
    private ExperienceType experience;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_employment")
    private EmploymentType employment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacancy_schedule")
    private ScheduleType schedule;

    @Column(name = "vacancy_archived")
    private boolean archived;

    @Column(name = "vacancy_published_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime published_at;

    @Column(name = "vacancy_accept_handicapped")
    private boolean accept_handicapped;

    @Column(name = "vacancy_accept_kids")
    private boolean accept_kids;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vacancies_n_specializations",
            joinColumns = @JoinColumn(name = "vacancy_ID"),
            inverseJoinColumns = @JoinColumn(name = "specialization_ID"))
    private Set<Specialization> specializations;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vacancies_n_key_skills",
            joinColumns = @JoinColumn(name = "vacancy_ID"),
            inverseJoinColumns = @JoinColumn(name = "key_skill_ID"))
    private Set<KeySkill> key_skills;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vacancies_n_metro_stations",
            joinColumns = @JoinColumn(name = "vacancy_ID"),
            inverseJoinColumns = @JoinColumn(name = "station_ID"))
    private Set<MetroStation> metro_stations;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vacancies_n_driver_license_types",
            joinColumns = @JoinColumn(name = "vacancy_ID"),
            inverseJoinColumns = @JoinColumn(name = "driver_license_ID"))
    private Set<DriverLicense> driver_licenses;

    public Vacancy() {}

    public Vacancy(int id, String name, String description, VacancyType type, Employer employer,
                   int salary_from, int salary_to, boolean salary_gross, Currency currency,
                   Area area, Address address,
                   ExperienceType experience, EmploymentType employment, ScheduleType schedule,
                   boolean archived, LocalDateTime published_at, boolean accept_handicapped, boolean accept_kids,
                   Set<Specialization> specializations, Set<KeySkill> key_skills,
                   Set<MetroStation> metro_stations, Set<DriverLicense> driver_licenses)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.employer = employer;
        this.salary_from = salary_from;
        this.salary_to = salary_to;
        this.salary_gross = salary_gross;
        this.currency = currency;
        this.area = area;
        this.address = address;
        this.experience = experience;
        this.employment = employment;
        this.schedule = schedule;
        this.archived = archived;
        this.published_at = published_at;
        this.accept_handicapped = accept_handicapped;
        this.accept_kids = accept_kids;
        this.specializations = specializations;
        this.key_skills = key_skills;
        this.metro_stations = metro_stations;
        this.driver_licenses = driver_licenses;
    }
}