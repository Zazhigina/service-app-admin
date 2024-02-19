package igc.mirror.faq.mapper;

import igc.mirror.faq.dto.FaqQuestionDto;
import igc.mirror.faq.dto.FaqQuestionSaveDto;
import igc.mirror.faq.model.FaqQuestion;
import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.ref.QuestionType;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {FaqAnswerMapper.class}
)
public abstract class FaqQuestionMapper {
    public abstract FaqQuestion saveDtoToModel(FaqQuestionSaveDto saveDto);
    public abstract void saveDtoToModel(@MappingTarget FaqQuestion faqQuestion, FaqQuestionSaveDto saveDto);
    @Mappings({
            @Mapping(source = "questionType", target = "questionTypeName", qualifiedByName = "questionTypeToName"),
            @Mapping(source = "serviceName", target = "serviceFullName", qualifiedByName = "mirrorServiceToFullName")
    })
    public abstract FaqQuestionDto modelToDto(FaqQuestion question);

    @Named("questionTypeToName")
    protected String questionTypeToDescription(QuestionType questionType){
        return questionType != null ? questionType.getDescription() : null;
    }

    @Named("mirrorServiceToFullName")
    protected String mirrorServiceToFullName(MirrorService mirrorService){
        return mirrorService != null ? mirrorService.getName() : null;
    }
}
