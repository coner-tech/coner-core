package org.coner.core.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMultimap;

public class HsqlDatabaseManagerSwingTaskTest {

    private static final String URL_HSQLDB = "jdbc:hsqldb:mem:coner";
    private static final String URL_NON_HSQLDB = "jdbc:sqlite::memory";
    private final Map<String, String> properties = new HashMap<>();

    private HsqlDatabaseManagerSwingTask task;

    @Before
    public void setup() {
        properties.clear();
    }

    @Test
    public void whenShouldRegisterWithEverythingRequired() {
        task = new HsqlDatabaseManagerSwingTask(URL_HSQLDB);
        properties.put(HsqlDatabaseManagerSwingTask.class.getCanonicalName(), Boolean.TRUE.toString());

        boolean actual = task.shouldRegister(properties);

        assertThat(actual).isTrue().isEqualTo(task.ok);
    }

    @Test
    public void whenShouldRegisterWithoutHsqldbUrl() {
        task = new HsqlDatabaseManagerSwingTask(URL_NON_HSQLDB);
        properties.put(HsqlDatabaseManagerSwingTask.class.getCanonicalName(), Boolean.TRUE.toString());

        boolean actual = task.shouldRegister(properties);

        assertThat(actual).isFalse().isEqualTo(task.ok);
    }

    @Test
    public void whenExecuteValidItShouldRunExecution() throws Exception {
        task = new HsqlDatabaseManagerSwingTask(URL_HSQLDB);
        task.execution = mock(Runnable.class);
        task.ok = true;

        task.execute(mock(ImmutableMultimap.class), mock(PrintWriter.class));

        verify(task.execution).run();
    }

    @Test
    public void whenExecuteInvalidItShouldThrow() throws Exception {
        task = new HsqlDatabaseManagerSwingTask(URL_HSQLDB);
        task.execution = mock(Runnable.class);
        task.ok = false;

        try {
            task.execute(mock(ImmutableMultimap.class), mock(PrintWriter.class));
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException e) {
            verify(task.execution, never()).run();
        }
    }

}
