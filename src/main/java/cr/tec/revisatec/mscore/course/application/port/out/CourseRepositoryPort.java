package cr.tec.revisatec.mscore.course.application.port.out;

import cr.tec.revisatec.mscore.course.domain.Course;
import cr.tec.revisatec.mscore.course.domain.PageResult;

import java.util.Optional;

public interface CourseRepositoryPort {

    PageResult<Course> findAll(int page, int pageSize, String search);

    Optional<Course> findById(Long id);

    Course save(Course course);

    void deleteById(Long id);
}
