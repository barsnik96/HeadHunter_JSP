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
@Table(name = "schedule_types")
public class ScheduleType
{
    @Id
    @Column(name = "schedule_ID")
    private String id;

    @Column(name = "schedule_name")
    private String name;

    public ScheduleType() {}

    public ScheduleType(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
}