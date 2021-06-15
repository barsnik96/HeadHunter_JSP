package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.VacancyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VacancyTypeServiceImpl implements VacancyTypeService
{
    public final VacancyTypeRepository vacancyTypeRepository;

    @Autowired
    public VacancyTypeServiceImpl(VacancyTypeRepository vacancyTypeRepository) {
        this.vacancyTypeRepository = vacancyTypeRepository;
    }
}