package ru.practicum.ewm.stats.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.service.model.App;

@UtilityClass
public class AppMapper {
    public static App toApp(String appName) {
        App app = new App();
        app.setName(appName);
        return app;
    }
}
