package by.barsnik96.HeadHunter_JSP.repository;

import by.barsnik96.HeadHunter_JSP.domain.ProfArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfAreaRepository extends JpaRepository<ProfArea, Integer>
{

}
