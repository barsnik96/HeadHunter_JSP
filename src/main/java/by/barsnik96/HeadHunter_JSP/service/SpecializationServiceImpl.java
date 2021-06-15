package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.domain.Specialization;
import by.barsnik96.HeadHunter_JSP.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationServiceImpl implements SpecializationService
{
    public final SpecializationRepository specializationRepository;

    @Autowired
    public SpecializationServiceImpl(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }
}