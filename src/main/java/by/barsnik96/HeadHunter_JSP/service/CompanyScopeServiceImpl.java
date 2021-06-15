package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.CompanyScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyScopeServiceImpl implements CompanyScopeService
{
    public final CompanyScopeRepository companyScopeRepository;

    @Autowired
    public CompanyScopeServiceImpl(CompanyScopeRepository companyScopeRepository) {
        this.companyScopeRepository = companyScopeRepository;
    }
}