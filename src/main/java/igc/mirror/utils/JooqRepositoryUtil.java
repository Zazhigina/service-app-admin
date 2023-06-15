package igc.mirror.utils;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqRepositoryUtil {
    @Autowired
    private DSLContext dsl;

    public long findRecordTotal(Select<? extends Record> query) {
        return dsl.fetchCount(query);
    }

    public long findRecordTotal(Table<? extends Record> query) {
        return dsl.fetchCount(query);
    }
}
