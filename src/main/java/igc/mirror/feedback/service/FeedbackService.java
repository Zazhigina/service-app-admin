package igc.mirror.feedback.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.doc.DocService;
import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.feedback.dto.FeedbackFileDto;
import igc.mirror.feedback.dto.FeedbackThemeDto;
import igc.mirror.feedback.dto.FeedbackDto;
import igc.mirror.feedback.repository.FeedbackRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private DSLContext dsl;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserDetails userDetails;
    @Autowired
    private DocService docService;

    public List<FeedbackThemeDto> findThemes() {
        return feedbackRepository.findThemes();
    }
    public FeedbackThemeDto addTheme(FeedbackThemeDto theme) {
        theme.setCreateUser(userDetails.getUsername());
        return feedbackRepository.addTheme(theme);
    }
    public FeedbackThemeDto changeTheme(Long id, FeedbackThemeDto theme) {
        theme.setLastUpdateUser(userDetails.getUsername());
        return feedbackRepository.updateThemeById(id, theme);
    }
    public void deleteTheme(Long id) {
        feedbackRepository.deleteThemeById(id);
    }

    public void addFeedback(FeedbackDto feedbackDto, MultipartFile[] fileBlobs) {
        feedbackDto.setCreateUser(userDetails.getUsername());
        feedbackDto.setUserFullname(userDetails.getUserFullname());

        FeedbackDto feedback = feedbackRepository.addFeedback(feedbackDto);

        if (fileBlobs != null) {
            for (MultipartFile fileBlob : fileBlobs) {
                String fileType = fileBlob.getContentType();
                String fileName = fileBlob.getOriginalFilename();
                if (fileName != null && fileType != null && !isSupportedFileType(fileType)) {
                    throw new IllegalArgumentException("Неподдерживаемый тип файла: " + fileName.substring(fileName.lastIndexOf('.') + 1));
                }
            }
            for (MultipartFile fileBlob : fileBlobs) {
                if (!fileBlob.isEmpty()) {
                    DocumentDto document = docService.uploadDocument(fileBlob);

                    feedbackRepository.addFeedbackFile(
                            new FeedbackFileDto(
                                    feedback.getId(),
                                    document.getId(),
                                    fileBlob.getOriginalFilename(),
                                    userDetails.getUsername()
                            )
                    );
                }
            }
        }
    }

    public DocumentDto getFeedbackFile(UUID uid) {
        return docService.downloadDocument(
                feedbackRepository.getFeedBackFile(uid).getDocumentId()
        );
    }

    private boolean isSupportedFileType(String fileType) {
        return fileType.equals("application/vnd.ms-excel") ||
                fileType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                fileType.equals("application/msword") ||
                fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                fileType.equals("application/pdf") ||
                fileType.equals("image/jpeg") ||
                fileType.equals("image/jpg") ||
                fileType.equals("image/png") ||
                fileType.equals("application/zip") ||
                fileType.equals("application/x-rar-compressed") ||
                fileType.equals("application/vnd.rar") ||
                fileType.equals("application/x-7z-compressed");
    }
}
