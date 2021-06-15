package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "vacancies_types")
public class VacancyType
{
    @Id
    @Column(name = "type_ID")
    private String id;

    @Column(name = "type_name")
    private String name;

    public VacancyType() {}

    public VacancyType(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
}