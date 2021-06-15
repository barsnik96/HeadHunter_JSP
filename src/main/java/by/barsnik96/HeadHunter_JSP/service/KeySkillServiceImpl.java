package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.domain.KeySkill;
import by.barsnik96.HeadHunter_JSP.repository.KeySkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeySkillServiceImpl implements KeySkillService
{
    public final KeySkillRepository keySkillRepository;

    @Autowired
    public KeySkillServiceImpl(KeySkillRepository keySkillRepository) {
        this.keySkillRepository = keySkillRepository;
    }

    @Override
    public KeySkill findOneByName(String name) {
        return this.findOneByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        if (this.findOneByName(name) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}