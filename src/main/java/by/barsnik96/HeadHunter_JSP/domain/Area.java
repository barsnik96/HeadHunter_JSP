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
@Table(name = "areas")
public class Area
{
    @Id
    @Column(name = "area_ID")
    private int id;

    @Column(name = "area_name")
    private String name;

    public Area() {}

    public Area(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
}