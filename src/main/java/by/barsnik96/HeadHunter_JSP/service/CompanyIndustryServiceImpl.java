package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.CompanyIndustryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyIndustryServiceImpl implements CompanyIndustryService
{
    public final CompanyIndustryRepository companyIndustryRepository;

    @Autowired
    public CompanyIndustryServiceImpl(CompanyIndustryRepository companyIndustryRepository) {
        this.companyIndustryRepository = companyIndustryRepository;
    }
}