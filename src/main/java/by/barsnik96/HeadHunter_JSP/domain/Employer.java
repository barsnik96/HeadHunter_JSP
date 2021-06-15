package by.barsnik96.HeadHunter_JSP.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "employers")
public class Employer
{
    @Id
    @Column(name = "employer_ID")
    private int id;

    @Column(name = "employer_name")
    private String name;

    @Column(name = "employer_description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_type")
    private EmployerType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employers_n_scopes",
            joinColumns = @JoinColumn(name = "employer_ID"),
            inverseJoinColumns = @JoinColumn(name = "scope_ID"))
    private Set<CompanyScope> scopes;

    public Employer() {}

    public Employer(int id, String name, String description, EmployerType type)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }
}