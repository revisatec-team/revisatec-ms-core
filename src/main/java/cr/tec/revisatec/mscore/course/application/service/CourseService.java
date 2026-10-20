package cr.tec.revisatec.mscore.course.application.service;

import cr.tec.revisatec.mscore.course.application.port.in.CourseUseCase;
import cr.tec.revisatec.mscore.course.application.port.out.CourseRepositoryPort;
import cr.tec.revisatec.mscore.course.domain.Course;
import cr.tec.revisatec.mscore.course.domain.CourseNotFoundException;
import cr.tec.revisatec.mscore.course.domain.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseService implements CourseUseCase {

    private static final int MAX_PAGE_SIZE = 100;

    private final CourseRepositoryPort courseRepository;

    public CourseService(CourseRepositoryPort courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Course> listCourses(int page, int pageSize, String search) {
        if (page < 1) {
            throw new IllegalArgumentException("page debe ser mayor o igual a 1");
        }
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize debe estar entre 1 y 100");
        }
        return courseRepository.findAll(page, pageSize, search == null ? "" : search.trim());
    }

    @Override
    public Course createCourse(String name) {
        return courseRepository.save(new Course(null, name));
    }

    @Override
    @Transactional(readOnly = true)
    public Course getCourse(Long id) {
        return findCourse(id);
    }

    @Override
    public Course updateCourse(Long id, String name) {
        Course existingCourse = findCourse(id);
        return courseRepository.save(new Course(existingCourse.id(), name));
    }

    @Override
    public void deleteCourse(Long id) {
        findCourse(id);
        courseRepository.deleteById(id);
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }
}
