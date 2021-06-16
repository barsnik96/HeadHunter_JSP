package by.barsnik96.HeadHunter_JSP.repository;

import by.barsnik96.HeadHunter_JSP.domain.Area;
import by.barsnik96.HeadHunter_JSP.domain.MetroLine;
import by.barsnik96.HeadHunter_JSP.domain.MetroStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetroLineRepository extends JpaRepository<MetroLine, Integer>
{
    List<MetroLine> findAllByArea(Area area);

    MetroLine findOneByName(String name);
}