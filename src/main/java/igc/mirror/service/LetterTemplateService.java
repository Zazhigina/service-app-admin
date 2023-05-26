package igc.mirror.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.doc.DocService;
import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.dto.LetterTemplateDto;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.filter.LetterTemplateSearchCriteria;
import igc.mirror.model.LetterTemplate;
import igc.mirror.ref.LetterTemplateAcceptableDocType;
import igc.mirror.repository.LetterTemplateRepository;
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

import java.util.stream.Collectors;

@Service
@Validated
public class LetterTemplateService {
    static final Logger logger = LoggerFactory.getLogger(LetterTemplateService.class);
    private final LetterTemplateRepository letterTemplateRepository;
    private final UserDetails userDetails;
    private final DocService docService;

    @Autowired
    public LetterTemplateService(LetterTemplateRepository letterTemplateRepository,
                                 UserDetails userDetails,
                                 DocService docService) {
        this.letterTemplateRepository = letterTemplateRepository;
        this.userDetails = userDetails;
        this.docService = docService;
    }

    /**
     * Возвращает список найденных шаблонов по фильтру
     *
     * @param filter набор критериев поиска
     * @param pageable настройки пэджинации и сортировки
     * @return список шаблонов
     */
    public Page<LetterTemplateDto> findLetterTemplatesByFilters(DataFilter<LetterTemplateSearchCriteria> filter, Pageable pageable){
        Page<LetterTemplate> letterTemplatePage = letterTemplateRepository.findByFilters(filter, pageable);

        return new PageImpl<>(letterTemplatePage.getContent().stream().map(letterTemplate -> LetterTemplateDto.fromModel(letterTemplate)).collect(Collectors.toList()),
                pageable, letterTemplatePage.getTotalElements());
    }

    /**
     * Поиск шаблона по наименованию параметра
     *
     * @param letterType наименование параметра
     * @return шаблон
     */
    public LetterTemplateDto findByLetterType(String letterType){
        return LetterTemplateDto.fromModel(letterTemplateRepository.findByLetterType(letterType));
    }

    /**
     * Выполняет сохранение нового шаблона и загрузку документа в сервис doc
     *
     * @param letterTemplateRequest данные шаблона
     * @param file файл для загрузки в сервис doc
     * @return данные сохраненного шаблона
     */
    @Validated(CreateGroup.class)
    @Transactional
    public LetterTemplateDto saveLetterTemplate(@Valid LetterTemplateDto letterTemplateRequest, MultipartFile file){
        logger.info("Сохранение шаблона с letter_type - {}", letterTemplateRequest.getLetterType());

        LetterTemplateAcceptableDocType acceptableDocType = LetterTemplateAcceptableDocType.getByExtension(StringUtils.getFilenameExtension(file.getOriginalFilename()));
        if(acceptableDocType == null)
            throw new EntityNotSavedException("Недопустимый тип файла", null, null);

        DocumentDto documentUpload = docService.uploadDocument(file);

        LetterTemplate letterTemplate = new LetterTemplate();
        letterTemplate.setLetterType(letterTemplateRequest.getLetterType());
        letterTemplate.setTypeTemplate(letterTemplateRequest.getTypeTemplate().getName());
        letterTemplate.setTitle(letterTemplateRequest.getTitle());
        letterTemplate.setCreateUser(letterTemplateRequest.getCreateUser());
        letterTemplate.setAcceptableDocumentFormat(acceptableDocType.getExtension());
        letterTemplate.setLetterSample(documentUpload.getId());

        try{
            return LetterTemplateDto.fromModel(letterTemplateRepository.save(letterTemplate));
        }catch (Exception e){
            docService.deleteUploadedDocument(documentUpload.getId());
            throw e;
        }
    }

    /**
     * Заменяет загруженный файл шаблона
     *
     * @param id ID шаблона
     * @param file файл для замены
     */
    public void replaceLetterTemplate(Long id, MultipartFile file){
        logger.info("Замена файла для шаблона с ID - {}", id);

        LetterTemplateAcceptableDocType acceptableDocType = LetterTemplateAcceptableDocType.getByExtension(StringUtils.getFilenameExtension(file.getOriginalFilename()));
        if(acceptableDocType == null)
            throw new EntityNotSavedException("Недопустимый тип файла", null, null);

        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        docService.changeUploadedDocument(letterTemplate.getLetterSample(), file);

        letterTemplate.setAcceptableDocumentFormat(acceptableDocType.getExtension());
        letterTemplate.setLastUpdateUser(userDetails.getUsername());
        letterTemplateRepository.save(letterTemplate);
    }

    /**
     * Изменяет данные шаблона
     *
     * @param id ID шаблона
     * @param letterTemplateRequest данные для изменения шаблона
     * @return данные измененного шаблона
     */
    @Validated(ChangeGroup.class)
    @Transactional
    public LetterTemplateDto changeLetterTemplate(Long id, @Valid LetterTemplateDto letterTemplateRequest){
        logger.info("Изменение шаблона с letter_type - {}, id - {}", letterTemplateRequest.getLetterType(), id);

        LetterTemplate letterTemplate = letterTemplateRepository.find(id);
        letterTemplate.setTypeTemplate(letterTemplateRequest.getTypeTemplate().getName());
        letterTemplate.setTitle(letterTemplateRequest.getTitle());

        return LetterTemplateDto.fromModel(letterTemplateRepository.save(letterTemplate));
    }

    /**
     * Удаляет шаблон
     *
     * @param id ID шаблона
     */
    public void deleteLetterTemplate(Long id){
        logger.info("Удаление шаблона с ID - {}", id);

        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        docService.deleteUploadedDocument(letterTemplate.getLetterSample());
        letterTemplateRepository.delete(id);
    }

    /**
     * Выгружает файл шаблона из сервиса doc
     *
     * @param id ID шаблона
     * @return данные файла
     */
    public DocumentDto downloadLetterTemplate(Long id){
        LetterTemplate letterTemplate = letterTemplateRepository.find(id);

        return docService.downloadDocument(letterTemplate.getLetterSample());
    }
}
