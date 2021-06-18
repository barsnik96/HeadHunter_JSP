package by.barsnik96.HeadHunter_JSP.service;

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
}