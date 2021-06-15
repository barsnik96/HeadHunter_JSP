package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.ScheduleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleTypeServiceImpl implements ScheduleTypeService
{
    public final ScheduleTypeRepository scheduleTypeRepository;

    @Autowired
    public ScheduleTypeServiceImpl(ScheduleTypeRepository scheduleTypeRepository) {
        this.scheduleTypeRepository = scheduleTypeRepository;
    }
}