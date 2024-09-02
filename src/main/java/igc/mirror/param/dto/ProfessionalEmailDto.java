package igc.mirror.param.dto;

import igc.mirror.param.model.Param;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfessionalEmailDto {

    private String professionalEmailSender;
    private String professionalEmailCopySender;

    public ProfessionalEmailDto(String sender, String copySender) {
        this.professionalEmailSender = sender;
        this.professionalEmailCopySender = copySender;
    }

    public static ProfessionalEmailDto fromParamList(List<Param> paramList) {
        Param senderParam = paramList
                .stream()
                .filter(param -> param.getKey().equals("PROFESSIONAL.EMAIL_SENDER"))
                .findFirst().orElse(null);
        Param copySenderParam = paramList
                .stream()
                .filter(param -> param.getKey().equals("PROFESSIONAL.COPY_EMAIL_SENDER"))
                .findFirst().orElse(null);
        return new ProfessionalEmailDto(senderParam != null ? senderParam.getVal() : null,
                copySenderParam != null ? copySenderParam.getVal() : null);
    }
}
