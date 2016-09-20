package com.faforever.gw.repository;

import com.faforever.gw.model.SunSystem;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.annotations.JsonApiFindManyTargets;
import io.katharsis.repository.annotations.JsonApiFindOneTarget;
import io.katharsis.repository.annotations.JsonApiRelationshipRepository;
import io.katharsis.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JsonApiRelationshipRepository(source = SunSystem.class, target = SunSystem.class)
@Component
public class SunSystemToSunSystemRepository {

    private final SunSystemRepository sunSystemRepository;

    @Autowired
    public SunSystemToSunSystemRepository(SunSystemRepository sunSystemRepository) {
        this.sunSystemRepository = sunSystemRepository;
    }

    @JsonApiFindOneTarget
    public SunSystem findOneTarget(Long id, String fieldName, QueryParams requestParams) {
        SunSystem sunSystem = sunSystemRepository.findOne(id, requestParams);
        try {
            return (SunSystem) PropertyUtils.getProperty(sunSystem, fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @JsonApiFindManyTargets
    public Iterable<SunSystem> findManyTargets(Long id, String fieldName, QueryParams requestParams) {
        SunSystem sunSystem = sunSystemRepository.findOne(id, requestParams);
        try {
            return (Iterable<SunSystem>) PropertyUtils.getProperty(sunSystem, fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
