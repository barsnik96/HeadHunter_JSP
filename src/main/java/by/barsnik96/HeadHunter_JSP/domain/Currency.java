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
@Table(name = "currencies")
public class Currency
{
    @Id
    @Column(name = "curr_code")
    private String code;

    @Column(name = "curr_abbr")
    private String abbr;

    @Column(name = "curr_name")
    private String name;

    @Column(name = "curr_rate_to_rub")
    private double rate_to_rub;

    public Currency() {}

    public Currency(String code, String abbr, String name, double rate_to_rub)
    {
        this.code = code;
        this.abbr = abbr;
        this.name = name;
        this.rate_to_rub = rate_to_rub;
    }
}