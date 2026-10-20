package cr.tec.revisatec.mscore.course.application.port.in;

import cr.tec.revisatec.mscore.course.domain.Course;
import cr.tec.revisatec.mscore.course.domain.PageResult;

public interface CourseUseCase {

    PageResult<Course> listCourses(int page, int pageSize, String search);

    Course createCourse(String name);

    Course getCourse(Long id);

    Course updateCourse(Long id, String name);

    void deleteCourse(Long id);
}
