package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.MetroLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetroLineServiceImpl implements MetroLineService
{
    public final MetroLineRepository metroLineRepository;

    @Autowired
    public MetroLineServiceImpl(MetroLineRepository metroLineRepository) {
        this.metroLineRepository = metroLineRepository;
    }
}