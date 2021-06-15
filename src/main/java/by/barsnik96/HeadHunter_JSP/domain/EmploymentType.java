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
@Table(name = "employment_types")
public class EmploymentType
{
    @Id
    @Column(name = "employment_ID")
    private String id;

    @Column(name = "employment_name")
    private String name;

    public EmploymentType() {}

    public EmploymentType(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
}