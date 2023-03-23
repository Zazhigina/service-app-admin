package igc.mirror.repository.impl;

import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.model.Param;
import igc.mirror.repository.ParamRepository;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.qfilter.QueryBuilder;
import jooqdata.tables.AParam;
import jooqdata.tables.records.AParamRecord;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;

@Repository
public class ParamRepositoryImpl implements ParamRepository {
    @Autowired
    DSLContext dsl;

    @Override
    public Param find(String identifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Param> findByFilters(DataFilter<?> dataFilter, Pageable pageable) {
        Select<? extends Record> initQuery = select(AParam.A_PARAM.fields())
                .from(AParam.A_PARAM);

        List<Param> paramList =  dsl.fetch(QueryBuilder.buildQuery(initQuery, dataFilter.getSubFilter(), pageable))
                .into(Param.class);

        long total = paramList.size();

        if(total >= pageable.getPageSize()){
            total = dsl.fetchCount(QueryBuilder.buildQuery(initQuery, dataFilter.getSubFilter()));
        }

        return new PageImpl<>(paramList, pageable, total);
    }

    @Override
    public List<Param> findByFilters(DataFilter<?> dataFilter) {
        Select<? extends Record> initQuery = select(AParam.A_PARAM.fields())
                .from(AParam.A_PARAM);

        return dsl.fetch(QueryBuilder.buildQuery(initQuery, dataFilter.getSubFilter()))
                .into(Param.class);
    }

    @Override
    public Boolean checkExist(String identifier) {
        return dsl.fetchExists(
                selectOne()
                    .from(AParam.A_PARAM)
                    .where(AParam.A_PARAM.KEY.eq(identifier))
        );
    }

    @Override
    public Param save(Param data) {
        return dsl.insertInto(AParam.A_PARAM)
                    .set(dsl.newRecord(AParam.A_PARAM, data))
                .onDuplicateKeyUpdate()
                    .set(AParam.A_PARAM.VAL, data.getVal())
                    .set(AParam.A_PARAM.NAME, data.getName())
                    .set(AParam.A_PARAM.LAST_UPDATE_USER, data.getLastUpdateUser())
                .returningResult(AParam.A_PARAM.fields())
                .fetchOptional()
                .map(r -> r.into(Param.class))
                .orElseThrow(() -> new EntityNotFoundException(null, Param.class));
    }


    @Override
    public List<Param> saveList(List<Param> dataList) {
        List<InsertOnDuplicateSetMoreStep> recordInsertList = new ArrayList<>();

        for(Param paramData: dataList) {
            InsertOnDuplicateSetMoreStep<AParamRecord> recordInsert =
                    dsl.insertInto(AParam.A_PARAM)
                        .set( dsl.newRecord(AParam.A_PARAM, paramData) )
                    .onDuplicateKeyUpdate()
                            .set( dsl.newRecord(AParam.A_PARAM, paramData) );

            recordInsertList.add(recordInsert);
        }

        dsl.batch(recordInsertList).execute();

        return dsl.select(AParam.A_PARAM.fields())
                .from(AParam.A_PARAM)
                .where(AParam.A_PARAM.KEY.in(dataList.stream().map(param -> param.getKey()).collect(Collectors.toList())))
                .fetchInto(Param.class);
    }

    @Override
    public void delete(String identifier) {
        dsl.deleteFrom(AParam.A_PARAM)
                .where(AParam.A_PARAM.KEY.eq(identifier))
                .execute();
    }

    @Override
    public void deleteList(List<String> identifierList) {
        dsl.deleteFrom(AParam.A_PARAM)
                .where(AParam.A_PARAM.KEY.in(identifierList))
                .execute();
    }
}
