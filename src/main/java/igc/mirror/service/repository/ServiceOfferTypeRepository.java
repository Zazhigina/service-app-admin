package igc.mirror.service.repository;

import igc.mirror.service.dto.ServiceOfferTypeDto;
import igc.mirror.service.filter.ServiceOfferTypeSearchCriteria;
import igc.mirror.service.model.ServiceOfferType;
import igc.mirror.utils.JooqRepositoryUtil;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.qfilter.QueryBuilder;
import igc.mirror.utils.qfilter.QueryFilter;
import jooqdata.tables.TServiceOfferType;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static org.jooq.impl.DSL.select;

@Repository
public class ServiceOfferTypeRepository {
    @Autowired
    DSLContext dsl;

    @Autowired
    JooqRepositoryUtil jooqRepositoryUtil;


    /**
     * Возвращает начальный запрос отбора маппинга услуга - вид КП
     *
     * @param criteria критерий поиска
     * @return запрос к БД
     */
    private Select<? extends Record> buildServiceOfferTypeQuery(ServiceOfferTypeSearchCriteria criteria) {
        Condition condition = DSL.noCondition();
        if (criteria != null && criteria.getServiceCode() != null) {
            condition = condition.and(TServiceOfferType.T_SERVICE_OFFER_TYPE.SERVICE_CODE.equal(criteria.getServiceCode()));
        }
        Select<? extends Record> initQuery =
                select(TServiceOfferType.T_SERVICE_OFFER_TYPE.ID,
                        TServiceOfferType.T_SERVICE_OFFER_TYPE.SERVICE_CODE,
                        TServiceOfferType.T_SERVICE_OFFER_TYPE.OFFER_TYPE)
                        .from(TServiceOfferType.T_SERVICE_OFFER_TYPE)
                        .where(condition);
        return initQuery;
    }

    /**
     * Возвращает начальный запрос отбора маппинга услуга - вид КП
     *
     * @param dataFilter критерий поиска
     * @param pageable   настройки пэджинации
     * @return список строк таблицы маппинга
     */
    public List<ServiceOfferTypeDto> findServiceOfferTypeByFilter(DataFilter<ServiceOfferTypeSearchCriteria> dataFilter,
                                                                  Pageable pageable) {
        ServiceOfferTypeSearchCriteria criteria = (dataFilter != null ? dataFilter.getSearchCriteria() : null);
        QueryFilter subFilter = (dataFilter != null ? dataFilter.getSubFilter() : null);
        return dsl.fetch(QueryBuilder
                        .buildQuery(buildServiceOfferTypeQuery(criteria), subFilter, pageable))
                .into(ServiceOfferTypeDto.class);
    }

    /**
     * Возвращает количество поисковых запросов, удовлетворяющих переданным критриям без сортировки и пэджинации
     *
     * @param dataFilter основные критерии запроса и дополнительный фильтр
     * @return количество поисковых запросов, удовлетворяющих переданному фильтру
     */
    public Long getServiceOfferTypesCount(DataFilter<ServiceOfferTypeSearchCriteria> dataFilter) {
        ServiceOfferTypeSearchCriteria criteria = (dataFilter != null ? dataFilter.getSearchCriteria() : null);
        QueryFilter subFilter = (dataFilter != null ? dataFilter.getSubFilter() : null);

        return jooqRepositoryUtil.findRecordTotal(QueryBuilder.buildQuery(buildServiceOfferTypeQuery(criteria), subFilter));
    }

    /**
     * Возвращает вид КП по коду услуги
     *
     * @param services код услуги
     * @return вид КП
     */
    public List<ServiceOfferType> getOfferTypeByServiceCodes(List<String> services) {
        return dsl.select(TServiceOfferType.T_SERVICE_OFFER_TYPE.fields())
                .from(TServiceOfferType.T_SERVICE_OFFER_TYPE)
                .where(TServiceOfferType.T_SERVICE_OFFER_TYPE.SERVICE_CODE.in(services))
                .fetchInto(ServiceOfferType.class);
    }

    /**
     * Возвращает все записи маппинга услуга - вид КП
     *
     * @return записи справочника
     */
    public List<ServiceOfferType> findAll() {
        return dsl.select(TServiceOfferType.T_SERVICE_OFFER_TYPE.fields())
                .from(TServiceOfferType.T_SERVICE_OFFER_TYPE)
                .fetchInto(ServiceOfferType.class);
    }

    /**
     * Удаляет записи маппинга услуга - вид КП
     *
     * @param ids идентификаторы удаляемых записей
     */
    public void deleteServicesOfferTypes(Set<Long> ids) {
        dsl.deleteFrom(TServiceOfferType.T_SERVICE_OFFER_TYPE)
                .where(TServiceOfferType.T_SERVICE_OFFER_TYPE.ID.in(ids))
                .execute();
    }

    /**
     * Сохраняет записи маппинга услуга - вид КП
     *
     * @param serviceOfferTypes записи таблицы маппинга
     */
    public void saveServicesOfferTypes(List<ServiceOfferType> serviceOfferTypes) {
        for (ServiceOfferType record :
                serviceOfferTypes.stream().filter(r -> r.getId() == null).toList()) {
            dsl.insertInto(TServiceOfferType.T_SERVICE_OFFER_TYPE,
                            TServiceOfferType.T_SERVICE_OFFER_TYPE.SERVICE_CODE,
                            TServiceOfferType.T_SERVICE_OFFER_TYPE.OFFER_TYPE,
                            TServiceOfferType.T_SERVICE_OFFER_TYPE.CREATE_DATE,
                            TServiceOfferType.T_SERVICE_OFFER_TYPE.CREATE_USER,
                            TServiceOfferType.T_SERVICE_OFFER_TYPE.LAST_UPDATE_DATE,
                            TServiceOfferType.T_SERVICE_OFFER_TYPE.LAST_UPDATE_USER)
                    .values(record.getServiceCode(),
                            record.getOfferType().toString(),
                            record.getCreateDate(),
                            record.getCreateUser(),
                            record.getLastUpdateDate(),
                            record.getLastUpdateUser())
                    .onConflict()
                    .doUpdate()
                    .set(TServiceOfferType.T_SERVICE_OFFER_TYPE.OFFER_TYPE, record.getOfferType().toString())
                    .set(TServiceOfferType.T_SERVICE_OFFER_TYPE.LAST_UPDATE_DATE, record.getLastUpdateDate())
                    .set(TServiceOfferType.T_SERVICE_OFFER_TYPE.LAST_UPDATE_USER, record.getLastUpdateUser())
                    .where(TServiceOfferType.T_SERVICE_OFFER_TYPE.SERVICE_CODE.equal(record.getServiceCode()))
                    .execute();
        }

        for (ServiceOfferType record :
                serviceOfferTypes.stream().filter(r -> r.getId() != null).toList()) {
            dsl.update(TServiceOfferType.T_SERVICE_OFFER_TYPE)
                    .set(TServiceOfferType.T_SERVICE_OFFER_TYPE.SERVICE_CODE, record.getServiceCode())
                    .set(TServiceOfferType.T_SERVICE_OFFER_TYPE.OFFER_TYPE, record.getOfferType().toString())
                    .set(TServiceOfferType.T_SERVICE_OFFER_TYPE.LAST_UPDATE_DATE, record.getLastUpdateDate())
                    .set(TServiceOfferType.T_SERVICE_OFFER_TYPE.LAST_UPDATE_USER, record.getLastUpdateUser())
                    .where(TServiceOfferType.T_SERVICE_OFFER_TYPE.ID.equal(record.getId()))
                    .execute();
        }
    }
}
