package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "specializations")
public class Specialization
{
    @Id
    @Column(name = "specialization_ID")
    private int id;

    @Column(name = "specialization_name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProfArea.class)
    @JoinColumn(name = "prof_area_ID")
    private ProfArea profArea;

    public Specialization() {}

    public Specialization(int id, String name, ProfArea profArea)
    {
        this.id = id;
        this.name = name;
        this.profArea = profArea;
    }
}