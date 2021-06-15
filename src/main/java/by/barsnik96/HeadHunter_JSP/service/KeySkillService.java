package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.domain.KeySkill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeySkillService
{
    @Query("SELECT key_skill FROM KeySkill key_skill WHERE key_skill.name = :name")
    public KeySkill findOneByName(@Param("name") String name);

    public boolean existsByName(String name);
}