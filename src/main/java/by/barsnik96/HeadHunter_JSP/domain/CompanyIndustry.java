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
@Table(name = "industries")
public class CompanyIndustry
{
    @Id
    @Column(name = "industry_ID")
    private int id;

    @Column(name = "industry_name")
    private String name;

    public CompanyIndustry() {}

    public CompanyIndustry(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
}