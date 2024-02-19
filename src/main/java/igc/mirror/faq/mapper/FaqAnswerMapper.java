package igc.mirror.faq.mapper;

import igc.mirror.faq.dto.FaqAnswerDto;
import igc.mirror.faq.dto.FaqAnswerSaveDto;
import igc.mirror.faq.model.FaqAnswer;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class FaqAnswerMapper {
    public abstract FaqAnswerDto modelToDto(FaqAnswer answer);
    public abstract FaqAnswer saveDtoToModel(FaqAnswerSaveDto saveDto);
    public abstract void saveDtoToModel(@MappingTarget FaqAnswer faqAnswer, FaqAnswerSaveDto answerSaveDto);

}
