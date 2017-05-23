package org.coner.core.task;

import java.io.PrintWriter;
import java.util.Map;

import org.hsqldb.util.DatabaseManagerSwing;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;

public class HsqlDatabaseManagerSwingTask extends Task {

    private final String url;
    private String[] args;
    boolean ok = false;
    Runnable execution = () -> DatabaseManagerSwing.main(args);

    public HsqlDatabaseManagerSwingTask(String url) {
        super(DatabaseManagerSwing.class.getCanonicalName());
        this.url = url;
        this.args = new String[]{"--url", url, "--noexit"};
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        Preconditions.checkState(ok, "Registered without calling shouldRegister() or when returned false");
        execution.run();
    }

    public boolean shouldRegister(Map<String, String> properties) {
        String propertyName = HsqlDatabaseManagerSwingTask.class.getCanonicalName();
        ok = url.contains(":hsqldb:mem:")
                && properties.containsKey(propertyName)
                && "true".equals(properties.get(propertyName));
        return ok;
    }

}
