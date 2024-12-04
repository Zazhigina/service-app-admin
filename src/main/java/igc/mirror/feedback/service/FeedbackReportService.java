package igc.mirror.feedback.service;

import igc.mirror.feedback.dto.FeedbackReportDto;
import igc.mirror.feedback.repository.FeedbackRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
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

        String[] headers = {"№п/п", "Дата подачи обращения", "ФИО инициатора обращения", "Сообщение пользователя", "Наименование файла", "Ссылка на вложенный файл"};
        Integer[] columnWidth = {2000, 6000, 10000, 14000, 9000, 20000};

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

        for (Map.Entry<String, List<FeedbackReportDto>> entry : groupedReportData.entrySet()) {
            String themeName = entry.getKey();
            List<FeedbackReportDto> records = entry.getValue();

            Sheet sheet = workbook.createSheet(themeName);

            Row headerRow = sheet.createRow(0);


            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            CellStyle hyperlinkStyle = workbook.createCellStyle();
            Font hyperlinkFont = workbook.createFont();
            hyperlinkFont.setUnderline(Font.U_SINGLE);
            hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
            hyperlinkStyle.setFont(hyperlinkFont);

            CellStyle centerAlignStyle = workbook.createCellStyle();
            centerAlignStyle.setAlignment(HorizontalAlignment.CENTER);
            centerAlignStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            int counter = 1;
            for (FeedbackReportDto record : records) {
                Row row = sheet.createRow(counter);
                row.createCell(0).setCellValue(counter);
                    row.getCell(0).setCellStyle(centerAlignStyle);
                LocalDateTime createDate = record.getCreateDate();
                row.createCell(1).setCellValue(createDate != null ? createDate.format(dateFormatter) : "Дата отсутствует");
                    row.getCell(1).setCellStyle(centerAlignStyle);
                row.createCell(2).setCellValue(record.getUserFullname());
                    row.getCell(2).setCellStyle(centerAlignStyle);
                row.createCell(3).setCellValue(record.getFeedbackText());
                    row.getCell(3).setCellStyle(centerAlignStyle);

                String fileName = record.getFilename();
                row.createCell(4).setCellValue(fileName != null && !fileName.isEmpty() ? fileName : "Файл не прикреплен");
                    row.getCell(4).setCellStyle(centerAlignStyle);
                UUID uid = record.getUid();
                Cell cell = row.createCell(5);
                if (fileName != null && uid != null) {
                    String baseUrl = request.getScheme() + "://" + request.getServerName();
                    String uidLink = baseUrl + "/api/admin/feedback/file?uid=" + uid.toString();
                    Hyperlink hyperlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
                    hyperlink.setAddress(uidLink);
                    cell.setHyperlink(hyperlink);
                    cell.setCellValue(uidLink);
                    cell.setCellStyle(hyperlinkStyle);
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(centerAlignStyle);
                counter++;
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