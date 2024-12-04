package igc.mirror.feedback.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.doc.DocService;
import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.feedback.dto.FeedbackFileDto;
import igc.mirror.feedback.dto.FeedbackThemeDto;
import igc.mirror.feedback.dto.FeedbackDto;
import igc.mirror.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserDetails userDetails;
    private final DocService docService;

    public FeedbackService(FeedbackRepository feedbackRepository, UserDetails userDetails, DocService docService) {
        this.feedbackRepository = feedbackRepository;
        this.userDetails = userDetails;
        this.docService = docService;
    }

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
                String fileName = fileBlob.getOriginalFilename();
                if (fileName != null) {
                    String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if(!isSupportedFileType(fileExtension)) {
                        throw new IllegalArgumentException("Неподдерживаемый тип файла: " + fileExtension);
                    }
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

    private boolean isSupportedFileType(String fileExtension) {
        String[] extensionWhiteList = {"xls", "xlsx", "doc", "docx", "pdf", "jpeg", "jpg", "png", "zip", "rar", "7z"};
        return Arrays.asList(extensionWhiteList).contains(fileExtension);
    }
}
