package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.EmployerTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployerTypeServiceImpl implements EmployerTypeService
{
    public final EmployerTypeRepository employerTypeRepository;

    @Autowired
    public EmployerTypeServiceImpl(EmployerTypeRepository employerTypeRepository) {
        this.employerTypeRepository = employerTypeRepository;
    }
}