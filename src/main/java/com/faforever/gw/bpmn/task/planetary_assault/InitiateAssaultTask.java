package com.faforever.gw.bpmn.task.planetary_assault;

import com.faforever.gw.bpmn.accessors.PlanetaryAssaultAccessor;
import com.faforever.gw.model.Battle;
import com.faforever.gw.model.BattleParticipant;
import com.faforever.gw.model.BattleRole;
import com.faforever.gw.model.Faction;
import com.faforever.gw.model.GwCharacter;
import com.faforever.gw.model.Planet;
import com.faforever.gw.model.ValidationHelper;
import com.faforever.gw.model.repository.BattleRepository;
import com.faforever.gw.model.repository.CharacterRepository;
import com.faforever.gw.model.repository.PlanetRepository;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Slf4j
@Component
public class InitiateAssaultTask implements JavaDelegate {
    private final EntityManager entityManager;
    private final CharacterRepository characterRepository;
    private final PlanetRepository planetRepository;
    private final BattleRepository battleRepository;
    private final ValidationHelper validationHelper;

    @Inject
    public InitiateAssaultTask(EntityManager entityManager, CharacterRepository characterRepository, PlanetRepository planetRepository, BattleRepository battleRepository, ValidationHelper validationHelper) {
        this.entityManager = entityManager;
        this.characterRepository = characterRepository;
        this.planetRepository = planetRepository;
        this.battleRepository = battleRepository;
        this.validationHelper = validationHelper;
    }

    @Override
    @Transactional(dontRollbackOn = BpmnError.class)
    public void execute(DelegateExecution execution) {
        PlanetaryAssaultAccessor accessor = PlanetaryAssaultAccessor.of(execution);
        log.debug("validateAssault for battle {}", accessor.getBusinessKey());

        log.info("Battle {} initiated by character {}", accessor.getBattleId(), accessor.getRequestCharacter());

        GwCharacter character = characterRepository.getOne(accessor.getRequestCharacter());
        Planet planet = planetRepository.getOne(accessor.getPlanetId());
        Faction attackingFaction = character.getFaction();
        Faction defendingFaction = planet.getCurrentOwner();

        validationHelper.validateCharacterFreeForGame(character);
        validationHelper.validateAssaultOnPlanet(character, planet);

        Battle battle = new Battle(accessor.getBattleId(), planet, attackingFaction, defendingFaction);
        BattleParticipant battleParticipant = new BattleParticipant(battle, character, BattleRole.ATTACKER);
        battle.getParticipants().add(battleParticipant);

        battleRepository.save(battle);

        accessor.setBattleId(battle.getId())
                .setAttackingFaction(attackingFaction)
                .setDefendingFaction(defendingFaction);
    }
}
