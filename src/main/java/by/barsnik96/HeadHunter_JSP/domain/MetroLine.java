package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "metro_lines")
public class MetroLine
{
    @Id
    @Column(name = "line_ID")
    private int id;

    @Column(name = "line_name")
    private String name;

    @Column(name = "line_hex_color")
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_ID")
    private Area area;

    public MetroLine() {}

    public MetroLine(int id, String name, String color, Area area)
    {
        this.id = id;
        this.name = name;
        this.color = color;
        this.area = area;
    }
}