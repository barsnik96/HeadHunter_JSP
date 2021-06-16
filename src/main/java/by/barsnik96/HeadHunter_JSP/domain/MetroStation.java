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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_ID")
    private MetroLine line;

    // Возможно, нужно добавить поле Order - порядковый номер станции на линии

    public MetroStation() {}

    public MetroStation(int id, String name, MetroLine line)
    {
        this.id = id;
        this.name = name;
        this.line = line;
    }
}