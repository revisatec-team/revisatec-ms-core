package cr.tec.revisatec.mscore.course.infrastructure.web;

import cr.tec.revisatec.mscore.course.application.port.in.CourseUseCase;
import cr.tec.revisatec.mscore.course.domain.Course;
import cr.tec.revisatec.mscore.course.domain.PageResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseUseCase courseUseCase;

    public CourseController(CourseUseCase courseUseCase) {
        this.courseUseCase = courseUseCase;
    }

    @GetMapping
    public PageResult<CourseDto> listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search) {
        PageResult<Course> result = courseUseCase.listCourses(page, pageSize, search);
        List<CourseDto> items = result.items().stream().map(CourseDto::from).toList();
        return new PageResult<>(result.totalRecords(), result.page(), result.pageSize(), items);
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto request) {
        Course course = courseUseCase.createCourse(request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(CourseDto.from(course));
    }

    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable Long id) {
        return CourseDto.from(courseUseCase.getCourse(id));
    }

    @PutMapping("/{id}")
    public CourseDto updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto request) {
        return CourseDto.from(courseUseCase.updateCourse(id, request.name()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseUseCase.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
}
