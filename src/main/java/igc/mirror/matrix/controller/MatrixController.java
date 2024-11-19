package igc.mirror.matrix.controller;

import igc.mirror.matrix.dto.*;
import igc.mirror.matrix.filter.MatrixSearchCriteria;
import igc.mirror.matrix.filter.InitiatorSearchCriteria;
import igc.mirror.matrix.filter.OrgSearchCriteria;

import igc.mirror.matrix.model.*;
import igc.mirror.matrix.service.MatrixService;

import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Матрица Компания организатор / Организатор - Заказчик / Инициатор")
@RestController
public class MatrixController {
    static final Logger logger = LoggerFactory.getLogger(MatrixController.class);
    private final MatrixService matrixService;

    @Autowired
    public MatrixController(MatrixService matrixService){
        this.matrixService = matrixService;
    }

    @Operation(summary = "Получить заказчиков по списку кодов")
    @PostMapping(path = "/customer/map")
    public List<Customer> getCustomerByCodes(@RequestBody(required = false) List<String> codes) {
        return matrixService.getCustomerByCodesAsList(codes);
    }

    @Operation(summary = "Получить заказчиков по фильтру")
    @PostMapping(path = "/customer/filter")
    public List<Customer> getCustomerByFiltersAsMap(@RequestBody(required = false) DataFilter<MatrixSearchCriteria> filter,
                                                        Pageable pageable) {
        return matrixService.getCustomerByFilterAsList(filter, pageable);
    }

    @Operation(summary = "Получить инициаторов по фильтру")
    @PostMapping(path = "/initiator/filter")
    public List<Initiator> getInitiatorByFiltersAsList(@RequestBody(required = false) DataFilter<InitiatorSearchCriteria> filter,
                                                           Pageable pageable) {
        return matrixService.getInitiatorByFilterAsList(filter, pageable);
    }

    @Operation(summary = "Получить организаторов по фильтру")
    @PostMapping(path = "/org/filter")
    public List<Org> getOrgByFiltersAsMap(@RequestBody(required = false) DataFilter<OrgSearchCriteria> filter,
                                              Pageable pageable) {
        return matrixService.getOrgByFilterAsList(filter, pageable);
    }

    @Operation(summary = "Получить связи")
    @GetMapping(path = "/matrix/get-info")
    public List<Matrix> matrixGetInfo() {
        return matrixService.getMatrixInfo();
    }

    @Operation(summary = "Сохранить матрицу")
    @PostMapping(path = "/matrix/add-info")
    public String matrixAddInfo(@RequestBody List<MatrixDTO> matrixDTO) {
        matrixService.addMatrixInfo(matrixDTO);
        return "Запись добавлена";
    }

    @Operation(summary = "Удалить компанию организатора и его связи")
    @PostMapping(path = "/matrix/delete-by-code")
    public String matrixDeleteByCompanyCode(@RequestBody String companyCode) {
        matrixService.deleteInfoByCompanyCode(companyCode);
        return "Запись удалена";
    }

    @Operation(summary = "Удалить заказчика и инициатора")
    @PostMapping(path = "/matrix/delete-by-object")
    public String matrixDeleteByObject(@RequestBody List<MatrixDTO> matrixDTO) {
        matrixService.deleteInfoByObject(matrixDTO);
        return "Запись удалена";
    }
}
