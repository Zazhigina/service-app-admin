package igc.mirror.faq.ref;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MirrorService {
    MA("Поиск поставщиков"),
    BP("База цен"),
    EP("Расчет цены закупки"),
    PRCAT("Справочник наименований расценок"),
    MTR_MA("Поиск поставщиков МТР"),
    MTR_EP("Расчет цены закупки МТР"),
    ANALYTICS("Аналитика"),
    PDD("Конструктор документов"),
    EBASE("База опыта");

    private final String name;
}
