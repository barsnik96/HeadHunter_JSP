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
@Table(name = "employer_types")
public class EmployerType
{
    @Id
    @Column(name = "employer_type_ID")
    private String id;

    @Column(name = "employer_type_name")
    private String name;

    public EmployerType() {}

    public EmployerType(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
}