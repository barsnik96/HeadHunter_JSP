package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.MetroStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetroStationServiceImpl implements MetroStationService
{
    public final MetroStationRepository metroStationRepository;

    @Autowired
    public MetroStationServiceImpl(MetroStationRepository metroStationRepository) {
        this.metroStationRepository = metroStationRepository;
    }
}