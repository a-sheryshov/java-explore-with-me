package ru.practicum.ewm.stats.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.HitRequestDto;
import ru.practicum.ewm.stats.service.model.App;

@UtilityClass
public class AppMapper {
    public static App toApp(HitRequestDto dto) {
        App app = new App();
        app.setName(dto.getApp());
        return app;
    }
}
