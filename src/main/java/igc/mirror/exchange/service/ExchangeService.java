package igc.mirror.exchange.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.exception.common.IllegalEntityStateException;
import igc.mirror.exception.common.LoadFileException;
import igc.mirror.exchange.dto.ExternalSourceDto;
import igc.mirror.exchange.model.ExternalSource;
import igc.mirror.exchange.model.ProcedureData;
import igc.mirror.exchange.model.ProcedureItem;
import igc.mirror.exchange.model.ProcedureOffer;
import igc.mirror.exchange.repository.ExternalSourceRepository;
import igc.mirror.poi.model.*;
import igc.mirror.poi.service.POIService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeService {
    private final ExternalSourceRepository externalSourceRepository;
    private final UserDetails userDetails;
    private final POIService poiService;

    /**
     * Добавить запись о системе в справочник
     *
     * @param externalSourceDto DTO для сохранения данных
     * @return Идентификатор сохраненной записи
     */
    public Long createExternalSource(ExternalSourceDto externalSourceDto) {
        ExternalSource externalSource = externalSourceDto.toModel();
        externalSource.setCreateUser(userDetails.getUsername());

        return externalSourceRepository.createExternalSourceRecord(externalSource).getId();
    }

    /**
     * Изменить запись в справочнике систем
     *
     * @param externalSourceDto Запись для изменения
     */
    public void changeExternalSource(ExternalSourceDto externalSourceDto) {
        if (externalSourceDto.id() != null) {
            ExternalSource externalSource = externalSourceDto.toModel();
            externalSource.setLastUpdateUser(userDetails.getUsername());
            externalSource.setLastUpdateDate(LocalDateTime.now());

            externalSourceRepository.changeExternalSource(externalSource);
        } else {
            throw new EntityNotSavedException("Возникли ошибки при сохранении данных. Идентификатор записи не задан", null, ExternalSourceDto.class);
        }
    }

    /**
     * Пометить запись для удаления
     *
     * @param id Идентификатор записи из справочника систем
     */
    public void deleteExternalSource(Long id) {
        externalSourceRepository.markDeletedById(id, userDetails.getUsername(), LocalDateTime.now());
    }

    /**
     * Найти запись по ее идентификатору
     *
     * @param id Идентификатор записи из справочника систем
     * @return Данные
     */
    public ExternalSourceDto findExternalSourceById(Long id) {
        return Optional.ofNullable(externalSourceRepository.findById(id)).map(ExternalSourceDto::new).orElse(null);
    }

    /**
     * Список всех записей из справочника систем
     *
     * @return Список
     */
    public List<ExternalSourceDto> getExternalSources() {
        return externalSourceRepository.getAllExternalSources().stream().map(ExternalSourceDto::new).toList();
    }

    /**
     * Загрузка закупочных процедуры из файла
     *
     * @param source Источник
     * @param file   Файл с данными
     */
    public void loadPurchaseProcedureByFile(String source, MultipartFile file) {
        try (InputStream fileInputStream = file.getInputStream()) {
            List<PurchaseProcedureSheet> procedureRawData = processDataFromProcedureFile(fileInputStream);
            log.info("Excel data: {}", procedureRawData);
            ProcedureData procedureData = convertRawToProcedure(source, procedureRawData);
            // отправить в сервис интеграции
            sendPurchaseProcedureToIntegration(procedureData);
        } catch (IOException e) {
            throw new IllegalEntityStateException("Ошибка при загрузке файла c закупочными процедурами", null, MultipartFile.class);
        }
    }

    private List<PurchaseProcedureSheet> processDataFromProcedureFile(InputStream fileInputStream) {
        List<PurchaseProcedureSheet> dataSheet = new ArrayList<>();

        Workbook wb = poiService.parseExcel(fileInputStream);
        wb.getSheets().forEach(s -> dataSheet.add(processItemsFromSheet(s)));

        return dataSheet;
    }

    private PurchaseProcedureSheet processItemsFromSheet(Sheet sheet) {
        PurchaseProcedureSheet purchaseProcedureFileSheet = new PurchaseProcedureSheet();
        purchaseProcedureFileSheet.setSheetName(sheet.getName());
        purchaseProcedureFileSheet.setRows(
                sheet.getRows().stream()
                        .filter(
                                row -> (row.getNum() > 1) && Objects.nonNull(row.getCells()) && !row.getCells().isEmpty()
                        )
                        .map(this::readRow).toList()
        );
        return purchaseProcedureFileSheet;
    }

    private PurchaseProcedureRow readRow(Row row) {
        PurchaseProcedureRow data = new PurchaseProcedureRow();
        row.getCells().forEach(cell -> {
            try {
                String textValue = CellUtils.getTextValue(cell).trim();

                switch (cell.getNum()) {
                    case 0 -> data.setLotNr(textValue);
                    case 1 -> data.setLotId(textValue);
                    case 2 -> data.setLotName(textValue);
                    case 3 -> data.setProcedureNr(textValue);
                    case 4 -> data.setProcedureId(textValue);
                    case 5 -> data.setPlanCompletionDate(textValue);
                    case 6 -> data.setDateOfPublication(textValue);
                    case 7 -> data.setOrganizer(textValue);
                    case 8 -> data.setOrganizerCode(textValue);
                    case 9 -> data.setInitiator(textValue);
                    case 10 -> data.setPurchaseCategory(textValue);
                    case 11 -> data.setPurchaseForm(textValue);
                    case 12 -> data.setServiceCode(textValue);
                    case 13 -> data.setCustomer(textValue);
                    case 14 -> data.setNmc(textValue);
                    case 15 -> data.setProtocolDate(textValue);
                    case 16 -> data.setOkved(textValue);
                    case 17 -> data.setOkpd(textValue);
                    case 18 -> data.setOkato(textValue);
                    case 19 -> data.setContractorCode(textValue);
                    case 20 -> data.setContractorName(textValue);
                    case 21 -> data.setContractorEsuCode(textValue);
                    case 22 -> data.setInn(textValue);
                    case 23 -> data.setKpp(textValue);
                    case 24 -> data.setCost(textValue);
                    case 25 -> data.setContractDate(textValue);
                    case 26 -> data.setOfferStatus(textValue);

                    default -> {
                        break;
                    }
                }
            } catch (Exception e) {
                throw new LoadFileException(String.format("Некорректные данные в строке %d столбце %d", row.getNum() + 1, cell.getNum() + 1));
            }
        });

        return data;
    }

    private ProcedureData convertRawToProcedure(String source, List<PurchaseProcedureSheet> procedureRawData) {
        ProcedureData data = new ProcedureData();
        data.setSource(source);
        List<ProcedureItem> items = new ArrayList<>();

        procedureRawData.forEach(
                sheetData -> sheetData
                        .getRows().stream()
                        .collect(groupingBy(
                                PurchaseProcedureRow.KeyRecord::new,
                                mapping(m -> new ProcedureOffer(
                                                m.getContractorCode(),
                                                m.getContractorName(),
                                                m.getContractorEsuCode(),
                                                m.getInn(),
                                                m.getKpp(),
                                                m.getCost(),
                                                m.getContractDate(),
                                                m.getOfferStatus())
                                        , toList())
                        ))
                        .forEach(
                                (k, offers) -> items.add(
                                        new ProcedureItem(
                                                k.lotNr,
                                                k.lotId,
                                                k.lotName,
                                                k.procedureNr,
                                                k.procedureId,
                                                k.planCompletionDate,
                                                k.dateOfPublication,
                                                k.organizer,
                                                k.organizerCode,
                                                k.initiator,
                                                k.purchaseCategory,
                                                k.purchaseForm,
                                                k.serviceCode,
                                                k.customer,
                                                k.nmc,
                                                k.protocolDate,
                                                Stream.of(k.okved.split(",")).map(String::trim).toList(),
                                                Stream.of(k.okpd.split(",")).map(String::trim).toList(),
                                                Stream.of(k.okato.split(",")).map(String::trim).toList(),
                                                offers
                                        )
                                )
                        )
        );

        data.setItems(items);

        return data;
    }

    private void sendPurchaseProcedureToIntegration(ProcedureData procedureData) {
        log.info("Data to send: {}", procedureData);
    }

    public ProcedureData loadPurchaseProcedureByFileTest(String source, MultipartFile file) {
        ProcedureData procedureData;
        try (InputStream fileInputStream = file.getInputStream()) {
            List<PurchaseProcedureSheet> procedureRawData = processDataFromProcedureFile(fileInputStream);
            procedureData = convertRawToProcedure(source, procedureRawData);
            // отправить в сервис интеграции
            sendPurchaseProcedureToIntegration(procedureData);
        } catch (IOException e) {
            throw new IllegalEntityStateException("Ошибка при загрузке файла c закупочными процедурами", null, MultipartFile.class);
        }
        return procedureData;
    }

    /**
     * Вспомогательный класс для выполнения загрузки - отражает структуру файла
     */
    @Data
    private static class PurchaseProcedureRow {
        String lotNr;
        String lotId;
        String lotName;
        String procedureNr;
        String procedureId;
        String planCompletionDate;
        String dateOfPublication;
        String organizer;
        String organizerCode;
        String initiator;
        String purchaseCategory;
        String purchaseForm;
        String serviceCode;
        String customer;
        String nmc;
        String protocolDate;
        String okved;
        String okpd;
        String okato;
        String contractorCode;
        String contractorName;
        String contractorEsuCode;
        String inn;
        String kpp;
        String cost;
        String contractDate;
        String offerStatus;

        /**
         * Ключевые поля, по которым выполняется группировка строк (выборка контрагентов)
         */
        record KeyRecord(String lotNr, String lotId, String lotName, String procedureNr, String procedureId,
                         String planCompletionDate, String dateOfPublication, String organizer, String organizerCode,
                         String initiator, String purchaseCategory, String purchaseForm, String serviceCode,
                         String customer, String nmc, String protocolDate, String okved, String okpd, String okato) {
            KeyRecord(PurchaseProcedureRow row) {
                this(row.getLotNr(), row.getLotId(), row.getLotName(), row.getProcedureNr(), row.getProcedureId(),
                        row.getPlanCompletionDate(), row.getDateOfPublication(), row.getOrganizer(), row.getOrganizerCode(),
                        row.getInitiator(), row.getPurchaseCategory(), row.getPurchaseForm(), row.getServiceCode(),
                        row.getCustomer(), row.getNmc(), row.getProtocolDate(), row.getOkved(), row.getOkpd(), row.getOkato());
            }
        }
    }

    @Data
    private static class PurchaseProcedureSheet {
        String sheetName;
        List<PurchaseProcedureRow> rows;
    }
}
