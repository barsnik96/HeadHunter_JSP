package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "metro_stations")
public class MetroStation
{
    @Id
    @Column(name = "station_ID")
    private int id;

    @Column(name = "station_name")
    private String name;

    @Column(name = "station_order")
    private int order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "line_ID")
    private MetroLine line;

    public MetroStation() {}

    public MetroStation(int id, String name, int order, MetroLine line)
    {
        this.id = id;
        this.name = name;
        this.order = order;
        this.line = line;
    }
}