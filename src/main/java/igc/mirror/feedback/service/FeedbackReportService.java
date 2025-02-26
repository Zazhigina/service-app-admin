package igc.mirror.feedback.service;

import igc.mirror.feedback.dto.FeedbackReportDto;
import igc.mirror.feedback.repository.FeedbackRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.ByteArrayOutputStream;
import java.util.stream.Collectors;

@Service
public class FeedbackReportService {
    private final FeedbackRepository feedbackRepository;
    private final HttpServletRequest request;

    public FeedbackReportService(FeedbackRepository feedbackRepository, HttpServletRequest request) {
        this.feedbackRepository = feedbackRepository;
        this.request = request;
    }

    public Resource generateFeedbackReport(LocalDate date1, LocalDate date2) {

        ByteArrayResource resource;
        Workbook workbook;

        List<FeedbackReportDto> reportData = feedbackRepository.getReportData(
                date1 != null ? date1.atStartOfDay() : null,
                date2 != null ? date2.atTime(LocalTime.MAX) : null);

        workbook = new XSSFWorkbook();

        Map<String, List<FeedbackReportDto>> groupedReportData = reportData.stream()
                .collect(Collectors.groupingBy(FeedbackReportDto::getFbThemeName));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        CellStyle hyperlinkStyle = workbook.createCellStyle();
        Font hyperlinkFont = workbook.createFont();
        hyperlinkFont.setUnderline(Font.U_SINGLE);
        hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
        hyperlinkStyle.setFont(hyperlinkFont);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        String[] headers = {"№п/п", "Дата подачи обращения", "ФИО инициатора обращения", "Логин инициатора обращения", "Сообщение пользователя", "Наименование файла", "Ссылка на вложенный файл"};
        Integer[] columnWidth = {2000, 6000, 10000, 8000, 14000, 9000, 20000};

        if(groupedReportData.isEmpty()) {
            Sheet sheet = workbook.createSheet("Лист 1");
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("Нет данных для отчета");

            for (int i = 0; i < columnWidth.length; i++) {
                sheet.setColumnWidth(i, columnWidth[i]);
            }
        }

        for (Map.Entry<String, List<FeedbackReportDto>> theme : groupedReportData.entrySet()) {
            String themeName = theme.getKey();
            List<FeedbackReportDto> themeRecords = theme.getValue();

            Sheet sheet = workbook.createSheet(themeName);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int counterFeedback = 1;
            int counterRow = 1;

            Map<Long, List<FeedbackReportDto>> feedbackGrouped = themeRecords.stream()
                    .collect(Collectors.groupingBy(FeedbackReportDto::getId));

            for (Map.Entry<Long, List<FeedbackReportDto>> feedbackGroup : feedbackGrouped.entrySet()) {
                List<FeedbackReportDto> feedbackRecords = feedbackGroup.getValue();
                int startFeedbackRow = counterRow;
                Row feedbackRow = sheet.createRow(counterRow);
                feedbackRow.createCell(0).setCellValue(counterFeedback);
                feedbackRow.getCell(0).setCellStyle(cellStyle);
                LocalDateTime createDate = feedbackRecords.get(0).getCreateDate();
                feedbackRow.createCell(1).setCellValue(createDate != null ? createDate.format(dateFormatter) : "Дата отсутствует");
                feedbackRow.getCell(1).setCellStyle(cellStyle);
                feedbackRow.createCell(2).setCellValue(feedbackRecords.get(0).getUserFullname());
                feedbackRow.getCell(2).setCellStyle(cellStyle);
                feedbackRow.createCell(3).setCellValue(feedbackRecords.get(0).getCreateUser());
                feedbackRow.getCell(3).setCellStyle(cellStyle);
                String feedbackText = feedbackRecords.get(0).getFeedbackText();
                feedbackRow.createCell(4).setCellValue(feedbackText);
                feedbackRow.getCell(4).setCellStyle(cellStyle);

                int counterSubRows = 0;
                for (FeedbackReportDto record : feedbackRecords) {
                    Row row;
                    if (counterSubRows > 0) {
                        row = sheet.createRow(startFeedbackRow + counterSubRows);
                    }
                    else {
                        row = feedbackRow;
                    }

                    String fileName = record.getFilename();
                    row.createCell(5).setCellValue(fileName != null && !fileName.isEmpty() ? fileName : "Файл не прикреплен");
                    row.getCell(5).setCellStyle(cellStyle);
                    UUID uid = record.getUid();
                    Cell cell = row.createCell(6);
                    if (fileName != null && uid != null) {
                        String baseUrl = request.getScheme() + "://" + request.getServerName();
                        String uidLink = baseUrl + "/feedback/feedback-file?uid=" + uid;
                        Hyperlink hyperlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
                        hyperlink.setAddress(uidLink);
                        cell.setHyperlink(hyperlink);
                        cell.setCellValue(uidLink);
                        cell.setCellStyle(hyperlinkStyle);
                    } else {
                        cell.setCellValue("");
                    }

                    counterSubRows++;
                    counterRow++;
                }
                if(counterSubRows > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(startFeedbackRow, startFeedbackRow+counterSubRows-1, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(startFeedbackRow, startFeedbackRow+counterSubRows-1, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(startFeedbackRow, startFeedbackRow+counterSubRows-1, 2, 2));
                    sheet.addMergedRegion(new CellRangeAddress(startFeedbackRow, startFeedbackRow+counterSubRows-1, 3, 3));
                }
                counterFeedback++;
            }

            for (int i = 0; i < columnWidth.length; i++) {
                sheet.setColumnWidth(i, columnWidth[i]);
            }
        }

        try (ByteArrayOutputStream workBookOutputStream = new ByteArrayOutputStream()) {
            workbook.write(workBookOutputStream);
            resource = new ByteArrayResource(workBookOutputStream.toByteArray());
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }
}