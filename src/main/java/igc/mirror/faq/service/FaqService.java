package igc.mirror.faq.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.doc.DocService;
import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.faq.dto.DocumentInfoDto;
import igc.mirror.faq.dto.FaqQuestionDto;
import igc.mirror.faq.dto.FaqQuestionSaveDto;
import igc.mirror.faq.dto.UserGuideDto;
import igc.mirror.faq.filter.FaqQuestionSearchCriteria;
import igc.mirror.faq.mapper.FaqQuestionMapper;
import igc.mirror.faq.model.FaqQuestion;
import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.repository.FaqAnswerRepository;
import igc.mirror.faq.repository.FaqQuestionRepository;
import igc.mirror.template.filter.LetterTemplateSearchCriteria;
import igc.mirror.template.model.LetterTemplate;
import igc.mirror.template.ref.LetterTemplateType;
import igc.mirror.template.ref.TemplateStatus;
import igc.mirror.template.repository.LetterTemplateRepository;
import igc.mirror.utils.qfilter.DataFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Validated
public class FaqService {
    @Autowired
    private LetterTemplateRepository letterTemplateRepository;
    @Autowired
    private DocService docService;
    @Autowired
    private FaqQuestionRepository faqQuestionRepository;
    @Autowired
    private FaqAnswerRepository faqAnswerRepository;
    @Autowired
    private FaqQuestionMapper faqQuestionMapper;
    @Autowired
    private UserDetails userDetails;

    public List<UserGuideDto> getUserGuides(@Valid @NotNull MirrorService mirrorService) {
        LetterTemplateSearchCriteria letterTemplateSearchCriteria = new LetterTemplateSearchCriteria();

        letterTemplateSearchCriteria.setLetterTemplateType(LetterTemplateType.DOCUMENT);
        letterTemplateSearchCriteria.setStatus(TemplateStatus.ACTUAL);

        var letterTypeRightLikeList = new ArrayList<String>();
        letterTemplateSearchCriteria.setLetterTypeRightLikeList(letterTypeRightLikeList);

        letterTypeRightLikeList.add(String.format("%s_USER_GUIDE", mirrorService != null ? mirrorService.name() : ""));
        letterTypeRightLikeList.add(String.format("%s_PARTNER_GUIDE", mirrorService != null ? mirrorService.name() : ""));
//        letterTemplateSearchCriteria.setLetterTypeRightLikeList(List.of(
//                String.format("%s_USER_GUIDE", mirrorService != null ? mirrorService.name() : ""),
//                String.format("%s_PARTNER_GUIDE", mirrorService != null ? mirrorService.name() : "")
//        ));
        if (!mirrorService.name().startsWith("MTR_"))
            letterTypeRightLikeList.add("COMMON_USER_GUIDE");
        else
            letterTypeRightLikeList.add("MTR_COMMON_USER_GUIDE");


        var userGuideTemplates = letterTemplateRepository.findByFilters(new DataFilter<>(letterTemplateSearchCriteria, null),
                Sort.by(new Sort.Order(Sort.Direction.DESC,"letterType")));
        var userGuideTemplateDocIds = userGuideTemplates.stream()
                .map(LetterTemplate::getLetterSample)
                .filter(Objects::nonNull)
                .toList();

        List<DocumentDto> documents;
        Map<Long, DocumentDto> documentMap = new HashMap<>();
        if (!userGuideTemplateDocIds.isEmpty()) {
            documents = docService.retrieveDocumentsInfo(userGuideTemplateDocIds);

            documentMap.putAll(documents.stream()
                    .collect(Collectors.toMap(DocumentDto::getId, Function.identity())));
        }

        return userGuideTemplates.stream()
                .map(template -> {
                    DocumentInfoDto fileInfo = null;
                    if (template.getLetterSample() != null) {
                        DocumentDto document = documentMap.get(template.getLetterSample());
                        if (document != null)
                            fileInfo = DocumentInfoDto.builder()
                                    .name(document.getFilename())
                                    .mimeType(document.getFileMimeType())
                                    .size(document.getFileSize())
                                    .createDate(document.getCreateDate())
                                    .lastUpdateDate(document.getLastUpdateDate())
                                    .build();
                    }

                    return UserGuideDto.builder()
                            .templateId(template.getId())
                            .description(template.getTitle())
                            .documentInfo(fileInfo)
                            .build();
                })
                .toList();
    }

    public Page<FaqQuestionDto> findQuestionsByFilters(DataFilter<FaqQuestionSearchCriteria> dataFilter, Pageable pageable) {
        DataFilter<FaqQuestionSearchCriteria> filter = Optional.of(dataFilter).orElseGet(DataFilter::new);
        FaqQuestionSearchCriteria criteria = Optional.of(filter.getSearchCriteria()).orElseGet(FaqQuestionSearchCriteria::new);
        filter.setSearchCriteria(criteria);

        Page<FaqQuestion> questionPage = faqQuestionRepository.findByFilters(filter, pageable);
        List<FaqQuestionDto> questionDtos = questionPage.getContent().stream()
                .map(faqQuestionMapper::modelToDto)
                .toList();
        return new PageImpl<>(questionDtos, pageable, questionPage.getTotalElements());
    }
    
    public FaqQuestionDto findQuestionById(Long id) {
        return faqQuestionMapper.modelToDto(faqQuestionRepository.findById(id));
    }

    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    @Transactional
    public FaqQuestionDto createQuestion(@Valid FaqQuestionSaveDto saveRequestDto) {
        FaqQuestion question = faqQuestionMapper.saveDtoToModel(saveRequestDto);

        question.setCreateUser(userDetails.getUsername());

        var savedQuestion = faqQuestionRepository.save(question);
        question.getAnswer().setQuestionId(savedQuestion.getId());
        question.getAnswer().setCreateUser(userDetails.getUsername());
        faqAnswerRepository.save(question.getAnswer());

        return findQuestionById(savedQuestion.getId());
    }

    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    @Transactional
    public FaqQuestionDto changeQuestion(Long id, @Valid FaqQuestionSaveDto saveRequestDto) {
        FaqQuestion question = faqQuestionRepository.findById(id);
        faqQuestionMapper.saveDtoToModel(question, saveRequestDto);

        question.setLastUpdateUser(userDetails.getUsername());
        faqQuestionRepository.save(question);

        var answer = question.getAnswer();
        answer.setLastUpdateUser(userDetails.getUsername());
        faqAnswerRepository.save(answer);

        return findQuestionById(id);
    }

    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    @Transactional
    public void removeQuestion(Long id){
        var question = faqQuestionRepository.findById(id);
        faqAnswerRepository.deleteById(question.getAnswer().getId());
        faqQuestionRepository.deleteById(id);
    }
}
