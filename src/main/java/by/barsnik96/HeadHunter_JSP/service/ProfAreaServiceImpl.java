package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.ProfAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfAreaServiceImpl implements ProfAreaService
{
    public final ProfAreaRepository profAreaRepository;

    @Autowired
    public ProfAreaServiceImpl(ProfAreaRepository profAreaRepository) {
        this.profAreaRepository = profAreaRepository;
    }
}