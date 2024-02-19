package igc.mirror.faq.ref;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MirrorService {
    MA("Поиск поставщиков"),
    BP("База цен"),
    EP("Расчет цены закупки");

    private final String name;
}
