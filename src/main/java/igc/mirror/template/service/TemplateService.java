package igc.mirror.template.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.doc.DocService;
import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.exception.common.IllegalEntityStateException;
import igc.mirror.template.dto.*;
import igc.mirror.template.filter.LetterTemplateSearchCriteria;
import igc.mirror.template.model.LetterTemplate;
import igc.mirror.template.model.LetterTemplateVariable;
import igc.mirror.template.ref.LetterTemplateAcceptableDocType;
import igc.mirror.template.ref.LetterTemplateType;
import igc.mirror.template.ref.TemplateStatus;
import igc.mirror.template.repository.LetterTemplateRepository;
import igc.mirror.template.repository.LetterTemplateVariableRepository;
import igc.mirror.utils.qfilter.DataFilter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class TemplateService {
    static final Logger logger = LoggerFactory.getLogger(TemplateService.class);
    private final LetterTemplateRepository letterTemplateRepository;
    private final UserDetails userDetails;
    private final DocService docService;
    private final LetterTemplateVariableRepository letterTemplateVariableRepository;

    @Autowired
    public TemplateService(LetterTemplateRepository letterTemplateRepository,
                           UserDetails userDetails,
                           DocService docService,
                           LetterTemplateVariableRepository letterTemplateVariableRepository) {
        this.letterTemplateRepository = letterTemplateRepository;
        this.userDetails = userDetails;
        this.docService = docService;
        this.letterTemplateVariableRepository = letterTemplateVariableRepository;
    }

    /**
     * Возвращает список найденных шаблонов по фильтру
     *
     * @param filter   набор критериев поиска
     * @param pageable настройки пэджинации и сортировки
     * @return список шаблонов
     */
    public Page<LetterTemplateBriefInfoDto> findLetterTemplateBriefs(DataFilter<LetterTemplateSearchCriteria> filter, Pageable pageable) {
        DataFilter<LetterTemplateSearchCriteria> dataFilter = Optional.ofNullable(filter).orElseGet(DataFilter::new);
        LetterTemplateSearchCriteria criteria = Optional.ofNullable(dataFilter.getSearchCriteria()).orElseGet(LetterTemplateSearchCriteria::new);

        dataFilter.setSearchCriteria(criteria);

        List<LetterTemplateBriefInfoDto> templates = letterTemplateRepository.findTemplates(filter, pageable);

        long total = (templates.size() >= pageable.getPageSize() ?
                letterTemplateRepository.getTemplatesCount(dataFilter) : templates.size());

        return new PageImpl<>(templates, pageable, total);
    }

    /**
     * Поиск информации о шаблоне по наименованию параметра
     *
     * @param letterType наименование параметра
     * @return шаблон
     */
    public LetterTemplateDto findByLetterType(String letterType) {
        LetterTemplate letterTemplate = letterTemplateRepository.findByLetterType(letterType);

        LetterTemplateDto letterTemplateDto = letterTemplate.getLetterSample() == null
                ? new LetterTemplateDto(letterTemplate)
                : new LetterTemplateDto(letterTemplate, new FileInfoDto(docService.retrieveDocumentInfo(letterTemplate.getLetterSample())));

        letterTemplateDto.setVariableIds(letterTemplateVariableRepository.getLetterTemplateVariableIds(letterTemplate.getId()));

        return letterTemplateDto;
    }

    /**
     * Изменяет шаблон
     *
     * @param id                    ID шаблона
     * @param letterTemplateRequest данные для изменения шаблона
     * @param file                  файл шаблона
     * @return данные измененного шаблона
     */
    @Transactional
    public LetterTemplateDto changeLetterTemplate(Long id, @Valid LetterTemplateDto letterTemplateRequest, MultipartFile file) {
        logger.info("Изменение шаблона с letter_type - {}, id - {}", letterTemplateRequest.getLetterType(), id);

        //проверить наличияе необходимых данных в случае утверждения шаблона
        if (letterTemplateRequest.getStatus() == TemplateStatus.ACTUAL) {
            if ((letterTemplateRequest.getTitle() == null) || (letterTemplateRequest.getTitle().trim().length() == 0))
                throw new EntityNotSavedException("Заголовок утверждаемого шаблона не заполнен", id, LetterTemplateDto.class);

            if (((letterTemplateRequest.getFileInfo().getFileId() == null) && (file.isEmpty())) ||
                    ((letterTemplateRequest.getFileInfo().getFileId() != null) && (letterTemplateRequest.getFileInfo().getFileSize() == null)))
                throw new EntityNotSavedException("Отсутствует файл утверждаемого шаблона", id, LetterTemplateDto.class);
        }

        //найти шаблон и заполнить данные
        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        if (file != null) {
            LetterTemplateAcceptableDocType acceptableDocType = Optional.ofNullable(LetterTemplateAcceptableDocType.getByExtension(StringUtils.getFilenameExtension(file.getOriginalFilename())))
                    .orElseThrow(() -> new EntityNotSavedException("Недопустимый тип файла", null, null));
            letterTemplate.setAcceptableDocumentFormat(acceptableDocType.getExtension());
        }

        letterTemplate.setTitle(letterTemplateRequest.getTitle());
        letterTemplate.setStatus(letterTemplateRequest.getStatus() != null ? letterTemplateRequest.getStatus().name() : TemplateStatus.DRAFT.name());
        letterTemplate.setLastUpdateUser(userDetails.getUsername());

        //добавить/изменить/удалить файл в привязке к шаблону
        DocumentDto document;

        if ((letterTemplateRequest.getFileInfo().getFileId() == null) && (file != null)) {
            document = docService.uploadDocument(file);
            letterTemplate.setLetterSample(document.getId());

            letterTemplateRepository.save(letterTemplate);
        }

        if ((letterTemplateRequest.getFileInfo().getFileId() != null) && (letterTemplateRequest.getFileInfo().getFileSize() != null) && (file != null)) {
            docService.changeUploadedDocument(letterTemplateRequest.getFileInfo().getFileId(), file);

            letterTemplateRepository.save(letterTemplate);
        }

        if ((letterTemplateRequest.getFileInfo().getFileId() != null) && (letterTemplateRequest.getFileInfo().getFileSize() == null)) {
            letterTemplate.setLetterSample(null);
            letterTemplateRepository.save(letterTemplate);

            docService.deleteUploadedDocument(letterTemplateRequest.getFileInfo().getFileId());
        }

        //синхронизировать переменные
        List<LetterTemplateVariable> variables = new ArrayList<>();

        if (letterTemplateRequest.getVariableIds() != null) {
            variables = letterTemplateRequest.getVariableIds()
                    .stream().map(variable -> new LetterTemplateVariable(id, variable)).toList();
        }

        letterTemplateVariableRepository.synchronizeLetterTemplateVariable(id, variables);

        LetterTemplateDto letterTemplateDto = new LetterTemplateDto(letterTemplate, letterTemplateRequest.getFileInfo());
        letterTemplateDto.setVariableIds(letterTemplateRequest.getVariableIds());

        return letterTemplateDto;
    }

    /**
     * Выгружает файл шаблона из сервиса doc
     *
     * @param id ID шаблона
     * @return данные файла
     */
    public DocumentDto downloadLetterTemplateDoc(Long id) {
        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        return docService.downloadDocument(letterTemplate.getLetterSample());
    }

    /**
     * Возвращает информацию о загруженном файле шаблона
     *
     * @param id ID шаблона
     * @return информация о загруженном файле шаблона
     */
    public DocumentDto getLetterTemplateDocInfo(Long id) {
        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        return docService.retrieveDocumentInfo(letterTemplate.getLetterSample());
    }

    /**
     * Возвращает информацию о типах шаблонов
     *
     * @return типы шаблонов
     */
    public List<LetterTemplateTypeDto> findLetterTemplateTypes() {
        return Arrays.stream(LetterTemplateType.values()).map(v -> new LetterTemplateTypeDto(v.name(), v.getName())).toList();
    }

    /**
     * Запрашивает шаблон по указанному параметру
     *
     * @param letterType параметр
     * @return шаблон
     */
    public TemplateDto retrieveTemplate(String letterType) {
        logger.info("Запрос шаблона с letter_type - {}", letterType);

        LetterTemplate letterTemplate = letterTemplateRepository.findByLetterType(letterType);

        if (TemplateStatus.valueOf(letterTemplate.getStatus()) != TemplateStatus.ACTUAL)
            throw new IllegalEntityStateException("Шаблон не утвержден", letterTemplate.getId(), LetterTemplate.class);

        TemplateDto template = new TemplateDto();
        template.setTitle(letterTemplate.getTitle());
        template.setVariables(letterTemplateVariableRepository.getLetterTemplateVariablesAsMap(letterTemplate.getId()));

        DocumentDto documentInfo = docService.retrieveDocumentInfo(letterTemplate.getLetterSample());
        template.setTemplateFileInfo(new FileInfoDto(documentInfo));

        return template;
    }
}
