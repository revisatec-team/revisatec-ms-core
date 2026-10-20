package cr.tec.revisatec.mscore.course.domain;

import java.util.List;

public record PageResult<T>(long totalRecords, int page, int pageSize, List<T> items) {

    public PageResult {
        items = List.copyOf(items);
    }
}
