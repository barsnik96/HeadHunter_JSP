package by.barsnik96.HeadHunter_JSP.repository;

import by.barsnik96.HeadHunter_JSP.domain.CompanyIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyIndustryRepository extends JpaRepository<CompanyIndustry, Integer>
{

}