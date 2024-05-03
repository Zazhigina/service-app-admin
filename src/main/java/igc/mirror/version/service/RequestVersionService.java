package igc.mirror.version.service;

import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.question.dto.StandardAnswerVersion;
import igc.mirror.question.dto.StandardQuestion;
import igc.mirror.question.ref.AnswerType;
import igc.mirror.question.ref.QuestionOwner;
import igc.mirror.question.service.QuestionService;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.version.dto.PreparedRequestVersionRatingDto;
import igc.mirror.version.dto.RequestVersionProcessingStatus;
import igc.mirror.version.dto.RequestVersionRating;
import igc.mirror.version.filter.IncomingRequestVersionRatingSearchCriteria;
import igc.mirror.version.filter.RequestVersionRatingSearchCriteria;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestVersionService {
    private static final String XLSX_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String REQUEST_CONTRACTOR_VERSION_FILENAME = "RequestContractorVersion.xlsx";
    private final RemoteRequestVersionService remoteRequestVersionService;
    private final QuestionService questionService;

    public RequestVersionService(RemoteRequestVersionService remoteRequestVersionService,
                                 QuestionService questionService) {
        this.remoteRequestVersionService = remoteRequestVersionService;
        this.questionService = questionService;
    }

    /**
     * Получить перечень оценок версий поиска по переданным критериям с учетом пагинации
     *
     * @param filter   критерии поиска
     * @param pageable настройки пагинации
     * @return страница с данными
     */
    @PreAuthorize("hasAuthority('APP_ADMIN.EXEC')")
    public Page<PreparedRequestVersionRatingDto> findRequestVersionRatingsByFilter(DataFilter<IncomingRequestVersionRatingSearchCriteria> filter, Pageable pageable) {
        //определение списка идентификаторов ответов по указанным типам ответов в исходных критериях поиска
        Map<Long, AnswerType> answerTypesByIds = findRequestVersionRatingAnswerIdsByTypes(filter.getSearchCriteria().getAnswerTypes());

        if (CollectionUtils.isEmpty(answerTypesByIds))
            throw new EntityNotFoundException("Ошибка обработки критериев отбора оценок версий поиска: не найдены ответы по переданным типам", null, StandardAnswerVersion.class);

        //формирование новых критериев поиска с учетом полученного списка идентификаторов ответов
        RequestVersionRatingSearchCriteria criteria = new RequestVersionRatingSearchCriteria(filter.getSearchCriteria(), answerTypesByIds);
        DataFilter<RequestVersionRatingSearchCriteria> dataFilter = new DataFilter<>(criteria, filter.getSubFilter());

        Page<RequestVersionRating> requestVersionRatingPage = remoteRequestVersionService.findRequestVersionRatingByFilters(dataFilter, pageable);

        return
                new PageImpl<>(
                        requestVersionRatingPage.getContent().stream()
                                .map(requestVersionRating -> new PreparedRequestVersionRatingDto(requestVersionRating, answerTypesByIds))
                                .collect(Collectors.toList()),
                        pageable,
                        requestVersionRatingPage.getTotalElements()
                );
    }

    /**
     * Сформировать карту соотвестсвия идентификатора ответа типу ответа по переданныму списку типов
     *
     * @param answerTypes список типов ответов
     * @return карта
     */
    private Map<Long, AnswerType> findRequestVersionRatingAnswerIdsByTypes(List<AnswerType> answerTypes) {
        List<StandardQuestion> questions = questionService.findAllStandardQuestions(QuestionOwner.MA_SEARCH_RESULT);

        StandardQuestion question = questions.stream().findFirst().orElse(null);

        if (question == null)
            throw new EntityNotFoundException("Ошибка обработки критериев отбора оценок версий поиска: не найден стандартный вопрос", null, StandardQuestion.class);

        return !CollectionUtils.isEmpty(answerTypes)
                ? question.getAnswerVersions().stream().filter(answer -> answerTypes.contains(answer.getAnswerType())).collect(Collectors.toMap(StandardAnswerVersion::getId, StandardAnswerVersion::getAnswerType))
                : question.getAnswerVersions().stream().collect(Collectors.toMap(StandardAnswerVersion::getId, StandardAnswerVersion::getAnswerType));
    }

    /**
     * Изменить статус обработки версии поиска
     *
     * @param versionProcessingStatus данные для изменения статуса версии поиска
     * @return информация об успешном изменении
     */
    @PreAuthorize("hasAuthority('APP_ADMIN.EXEC')")
    public SuccessInfo changeRequestVersionProcessingStatus(RequestVersionProcessingStatus versionProcessingStatus) {
        return remoteRequestVersionService.changeRequestVersionProcessingStatus(versionProcessingStatus);
    }

    /**
     * Выгрузка отчета "Перечень найденных поставщиков" для версии поиска в excel
     *
     * @param requestVersionId, идентификатор версии поиска
     * @return файл excel "Перечень найденных поставщиков"
     */
    @PreAuthorize("hasAuthority('APP_ADMIN.EXEC')")
    public ResponseEntity<Resource> getExcelReportRequestContractorVersion(Long requestVersionId) {
        return remoteRequestVersionService.getExcelReportRequestContractorVersion(requestVersionId);
    }
}
