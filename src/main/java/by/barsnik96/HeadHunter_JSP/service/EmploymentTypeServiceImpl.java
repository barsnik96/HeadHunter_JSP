package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.EmploymentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmploymentTypeServiceImpl implements EmploymentTypeService
{
    public final EmploymentTypeRepository employmentTypeRepository;

    @Autowired
    public EmploymentTypeServiceImpl(EmploymentTypeRepository employmentTypeRepository) {
        this.employmentTypeRepository = employmentTypeRepository;
    }
}