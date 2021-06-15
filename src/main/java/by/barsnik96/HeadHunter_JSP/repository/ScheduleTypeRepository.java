package by.barsnik96.HeadHunter_JSP.repository;

import by.barsnik96.HeadHunter_JSP.domain.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleTypeRepository extends JpaRepository<ScheduleType, String>
{

}
