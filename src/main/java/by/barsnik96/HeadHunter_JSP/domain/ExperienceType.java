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
@Table(name = "experience_types")
public class ExperienceType
{
    @Id
    @Column(name = "experience_ID")
    private String id;

    @Column(name = "experience_name")
    private String name;

    public ExperienceType() {}

    public ExperienceType(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
}