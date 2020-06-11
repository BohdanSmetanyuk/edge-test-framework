package org.thingsboard.edgetest.black.box;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.thingsboard.edgetest.black.box.cases.docker.CloudContainer;
import org.thingsboard.edgetest.black.box.cases.docker.EdgeContainer;
import org.thingsboard.edgetest.black.box.cases.emulation.Emulator;
import org.thingsboard.edgetest.black.box.cases.entity.RelationBuilder;
import org.thingsboard.edgetest.black.box.cases.entity.EntityCreator;
import org.thingsboard.edgetest.black.box.cases.entity.EntityDeleter;
import org.thingsboard.edgetest.black.box.cases.entity.RelationDestroyer;
import org.thingsboard.edgetest.black.box.cases.rulechain.RuleChainManager;


@Slf4j
@RunWith(Suite.class)
@Suite.SuiteClasses({CloudContainer.class, RuleChainManager.class,
                     EntityCreator.class, RelationBuilder.class,
                     EdgeContainer.class, Emulator.class,
                     RelationDestroyer.class, EntityDeleter.class})
public class TestSuite {

}
