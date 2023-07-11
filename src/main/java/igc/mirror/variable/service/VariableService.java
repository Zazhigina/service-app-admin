package igc.mirror.variable.service;

import igc.mirror.variable.dto.VariableDto;
import igc.mirror.variable.repository.VariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariableService {

    @Autowired
    private VariableRepository variableRepository;

    /**
     * Находит переменные по указанным идентификаторам
     *
     * @param variableIds список ИД переменных
     * @return список переменных
     */
    public List<VariableDto> findVariables(List<Long> variableIds) {
        return variableRepository.findVariables(variableIds);
    }

}
