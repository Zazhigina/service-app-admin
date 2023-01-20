package igc.mirror.service;

import igc.mirror.enums.InitParam;
import igc.mirror.model.Param;
import igc.mirror.repository.ParamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitService {
    static final Logger logger = LoggerFactory.getLogger(InitService.class);
    private ParamRepository paramRepository;

    @Value("${init-params}")
    private Boolean initParamFlag;
    @Autowired
    public InitService(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) {
        event.getApplicationContext().getBean(InitService.class).initialize();
    }

    public void initialize() {
        if(initParamFlag)
            initParams();
    }
    public void initParams(){
        List<Param> params = new ArrayList<>();

        for (InitParam param: InitParam.values()){
            params.add(new Param(param.getKey(), param.getName(), param.getDefaultValue().toString()));
        }

        try {
            paramRepository.saveList(params);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

}
