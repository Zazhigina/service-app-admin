package igc.mirror.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.doc.DocService;
import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.dto.LetterTemplateDto;
import igc.mirror.dto.LetterTemplateTypeDto;
import igc.mirror.dto.LetterTemplateBriefInfoDto;
import igc.mirror.dto.TemplateDto;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.filter.LetterTemplateSearchCriteria;
import igc.mirror.model.LetterTemplate;
import igc.mirror.model.LetterTemplateVariable;
import igc.mirror.ref.LetterTemplateAcceptableDocType;
import igc.mirror.ref.LetterTemplateType;
import igc.mirror.repository.LetterTemplateRepository;
import igc.mirror.repository.LetterTemplateVariableRepository;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.validate.group.ChangeGroup;
import igc.mirror.utils.validate.group.CreateGroup;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<LetterTemplateBriefInfoDto> findLetterTemplatesByFilters(DataFilter<LetterTemplateSearchCriteria> filter, Pageable pageable) {
        Page<LetterTemplate> letterTemplatePage = letterTemplateRepository.findByFilters(filter, pageable);

        return new PageImpl<>(letterTemplatePage.getContent().stream().map(LetterTemplateBriefInfoDto::new).collect(Collectors.toList()),
                pageable, letterTemplatePage.getTotalElements());
    }

    /**
     * Поиск информации о шаблоне по наименованию параметра
     *
     * @param letterType наименование параметра
     * @return шаблон
     */
    public LetterTemplateDto findByLetterType(String letterType) {
        LetterTemplate letterTemplate = letterTemplateRepository.findByLetterType(letterType);
        letterTemplate.setVariables(letterTemplateVariableRepository.getLetterTemplateVariables(letterTemplate.getId()));

        DocumentDto documentDto = docService.retrieveDocumentInfo(letterTemplate.getLetterSample());
        LetterTemplateDto letterTemplateDto = new LetterTemplateDto(letterTemplate);
        letterTemplateDto.setSampleName(documentDto.getFilename());
        letterTemplateDto.setSampleSize(documentDto.getFileSize());
        letterTemplateDto.setSampleCreateDate(documentDto.getCreateDate());

        return letterTemplateDto;
    }

    /**
     * Выполняет сохранение нового шаблона и загрузку документа в сервис doc
     *
     * @param letterTemplateRequest данные шаблона
     * @param file                  файл для загрузки в сервис doc
     * @return данные сохраненного шаблона
     */
    @Validated(CreateGroup.class)
    @Transactional
    public LetterTemplateDto saveLetterTemplate(@Valid LetterTemplateDto letterTemplateRequest, MultipartFile file) {
        logger.info("Сохранение шаблона с letter_type - {}", letterTemplateRequest.getLetterType());

        LetterTemplateAcceptableDocType acceptableDocType = LetterTemplateAcceptableDocType.getByExtension(StringUtils.getFilenameExtension(file.getOriginalFilename()));
        if (acceptableDocType == null)
            throw new EntityNotSavedException("Недопустимый тип файла", null, null);

        DocumentDto documentUpload = docService.uploadDocument(file);

        LetterTemplate letterTemplate = new LetterTemplate();
        letterTemplate.setLetterType(letterTemplateRequest.getLetterType());
        letterTemplate.setTypeTemplate(letterTemplateRequest.getTypeTemplate().name());
        letterTemplate.setTitle(letterTemplateRequest.getTitle());
        letterTemplate.setCreateUser(userDetails.getUsername());
        letterTemplate.setAcceptableDocumentFormat(acceptableDocType.getExtension());
        letterTemplate.setLetterSample(documentUpload.getId());

        try {
            //return LetterTemplateDto.fromModel(letterTemplateRepository.save(letterTemplate));
            LetterTemplate savedLetterTemplate = letterTemplateRepository.save(letterTemplate);
            if (letterTemplateRequest.getVariables() != null) {
                List<LetterTemplateVariable> letterTemplateVariables = letterTemplateRequest.getVariables()
                        .entrySet().stream().map(entry -> new LetterTemplateVariable(savedLetterTemplate.getId(), entry.getKey(), entry.getValue())).toList();
                letterTemplateVariables.forEach(letterTemplateVariableRepository::createLetterTemplateVariable);
            }
            LetterTemplateDto letterTemplateDto = new LetterTemplateDto(savedLetterTemplate);
            letterTemplateDto.setVariables(letterTemplateRequest.getVariables());

            return letterTemplateDto;
        } catch (Exception e) {
            docService.deleteUploadedDocument(documentUpload.getId());
            throw e;
        }
    }

    /**
     * Заменяет загруженный файл шаблона
     *
     * @param id   ID шаблона
     * @param file файл для замены
     */
    @Transactional
    public void replaceLetterTemplate(Long id, MultipartFile file) {
        logger.info("Замена файла для шаблона с ID - {}", id);

        LetterTemplateAcceptableDocType acceptableDocType = LetterTemplateAcceptableDocType.getByExtension(StringUtils.getFilenameExtension(file.getOriginalFilename()));
        if (acceptableDocType == null)
            throw new EntityNotSavedException("Недопустимый тип файла", null, null);

        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        docService.changeUploadedDocument(letterTemplate.getLetterSample(), file);

        letterTemplate.setAcceptableDocumentFormat(acceptableDocType.getExtension());
        letterTemplate.setLastUpdateUser(userDetails.getUsername());
        letterTemplateRepository.save(letterTemplate);
    }

    /**
     * Изменяет информацию о шаблоне
     *
     * @param id                    ID шаблона
     * @param letterTemplateRequest данные для изменения шаблона
     * @return данные измененного шаблона
     */
    @Validated(ChangeGroup.class)
    @Transactional
    public LetterTemplateDto changeLetterTemplate(Long id, @Valid LetterTemplateDto letterTemplateRequest) {
        logger.info("Изменение шаблона с letter_type - {}, id - {}", letterTemplateRequest.getLetterType(), id);

        LetterTemplate letterTemplate = letterTemplateRepository.find(id);
        //letterTemplate.setTypeTemplate(letterTemplateRequest.getTypeTemplate().getName());
        letterTemplate.setTitle(letterTemplateRequest.getTitle());
        letterTemplate.setLastUpdateUser(userDetails.getUsername());

        if (letterTemplateRequest.getVariables() != null) {
            List<LetterTemplateVariable> variables = letterTemplateRequest.getVariables().entrySet().stream()
                    .map(entry -> new LetterTemplateVariable(id, entry.getKey(), entry.getValue())).toList();
            letterTemplate.setVariables(variables);
        }

        letterTemplateRepository.save(letterTemplate);
        letterTemplateVariableRepository.synchronizeLetterTemplateVariable(letterTemplate);

        return new LetterTemplateDto(letterTemplate);
    }

    /**
     * Удаляет шаблон
     *
     * @param id ID шаблона
     */
    @Transactional
    public void deleteLetterTemplate(Long id) {
        logger.info("Удаление шаблона с ID - {}", id);

        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        letterTemplateVariableRepository.deleteLetterTemplateVariable(id);
        letterTemplateRepository.delete(id);
        docService.deleteUploadedDocument(letterTemplate.getLetterSample());
    }

    /**
     * Выгружает файл шаблона из сервиса doc
     *
     * @param id ID шаблона
     * @return данные файла
     */
    public DocumentDto downloadLetterTemplate(Long id) {
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
        return Arrays.stream(LetterTemplateType.values()).map(v -> new LetterTemplateTypeDto(v.name(),v.getName())).toList();
    }

    /**
     * Запрашивает шаблон по указанному параметру
     *
     * @param letterType параметр
     * @return шаблон
     */
    public TemplateDto retrieveTemplate(String letterType) {
        LetterTemplateDto templateInfo = findByLetterType(letterType);

        DocumentDto document = docService.downloadDocument(templateInfo.getLetterSample());

        TemplateDto template = new TemplateDto();
        template.setTitle(templateInfo.getTitle());
        template.setResource(document.getResource());
        template.setFileName(templateInfo.getSampleName());
        template.setFileSize(templateInfo.getSampleSize());
        template.setFileCreateDate(templateInfo.getSampleCreateDate());
        template.setVariables(templateInfo.getVariables());

        return template;
    }
}
