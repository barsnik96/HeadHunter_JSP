package by.barsnik96.HeadHunter_JSP.repository;

import by.barsnik96.HeadHunter_JSP.domain.CompanyIndustry;
import by.barsnik96.HeadHunter_JSP.domain.CompanyScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyScopeRepository extends JpaRepository<CompanyScope, Integer>
{
    List<CompanyScope> findAllByCompanyIndustry(CompanyIndustry company_industry);
}