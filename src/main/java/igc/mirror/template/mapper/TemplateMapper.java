package igc.mirror.template.mapper;

import igc.mirror.template.dto.LetterTemplateInfoDto;
import igc.mirror.template.model.LetterTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TemplateMapper {

    public abstract List<LetterTemplateInfoDto> templateToInfoDto(List<LetterTemplate> templateList);
}
