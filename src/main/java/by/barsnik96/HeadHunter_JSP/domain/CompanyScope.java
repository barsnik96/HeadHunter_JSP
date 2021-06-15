package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "scopes")
public class CompanyScope
{

    @Id
    @Column(name = "scope_ID")
    private int id;

    @Column(name = "scope_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_ID")
    private CompanyIndustry companyIndustry;

    public CompanyScope() {}

    public CompanyScope(int id, String name, CompanyIndustry companyIndustry)
    {
        this.id = id;
        this.name = name;
        this.companyIndustry = companyIndustry;
    }
}