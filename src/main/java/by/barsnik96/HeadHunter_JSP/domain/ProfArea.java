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
@Table(name = "prof_areas")
public class ProfArea
{
    @Id
    @Column(name = "prof_area_ID")
    private int id;

    @Column(name = "prof_area_name")
    private String name;

    public ProfArea() {}

    public ProfArea(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
}