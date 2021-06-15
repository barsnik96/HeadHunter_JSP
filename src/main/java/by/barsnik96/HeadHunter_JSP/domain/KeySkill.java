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
@Table(name = "key_skills")
public class KeySkill
{
    @Id
    @Column(name = "key_skill_ID")
    private int id;

    @Column(name = "key_skill_name")
    private String name;

    public KeySkill() {}

    public KeySkill(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
}