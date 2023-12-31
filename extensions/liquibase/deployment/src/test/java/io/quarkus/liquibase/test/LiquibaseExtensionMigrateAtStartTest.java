package io.quarkus.liquibase.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.QuarkusUnitTest;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSetStatus;

public class LiquibaseExtensionMigrateAtStartTest {
    // Quarkus built object
    @Inject
    LiquibaseFactory liquibaseFactory;

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .withApplicationRoot((jar) -> jar
                    .addAsResource("db/changeLog.xml", "db/changeLog.xml")
                    .addAsResource("migrate-at-start-config.properties", "application.properties"));

    @Test
    @DisplayName("Migrates at start correctly")
    public void testLiquibaseConfigInjection() throws Exception {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            List<ChangeSetStatus> status = liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(),
                    liquibaseFactory.createLabels());
            assertNotNull(status);
            assertEquals(1, status.size());
            assertEquals("id-1", status.get(0).getChangeSet().getId());
            assertFalse(status.get(0).getWillRun());
        }
    }
}
