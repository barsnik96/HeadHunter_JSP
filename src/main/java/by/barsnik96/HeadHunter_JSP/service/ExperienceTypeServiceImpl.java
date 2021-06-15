package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.ExperienceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypeServiceImpl implements ExperienceTypeService
{
    public final ExperienceTypeRepository experienceTypeRepository;

    @Autowired
    public ExperienceTypeServiceImpl(ExperienceTypeRepository experienceTypeRepository) {
        this.experienceTypeRepository = experienceTypeRepository;
    }
}