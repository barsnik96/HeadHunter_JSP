package by.barsnik96.HeadHunter_JSP.service;

import by.barsnik96.HeadHunter_JSP.repository.DriverLicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverLicenseServiceImpl implements DriverLicenseService
{
    public final DriverLicenseRepository driverLicenseRepository;

    @Autowired
    public DriverLicenseServiceImpl(DriverLicenseRepository driverLicenseRepository) {
        this.driverLicenseRepository = driverLicenseRepository;
    }
}