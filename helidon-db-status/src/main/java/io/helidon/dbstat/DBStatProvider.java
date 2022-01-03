
package io.helidon.dbstat;

import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provider for greeting message.
 */
@ApplicationScoped
public class DBStatProvider {
    private final AtomicReference<String> dbUser = new AtomicReference<>();
    private final AtomicReference<String> dbPwd = new AtomicReference<>();
    private final AtomicReference<String> dbUrl = new AtomicReference<>();

    /**
     * Create a new greeting provider, reading the message from configuration.
     *
     * @param message greeting to use
     */
    @Inject
    public DBStatProvider(@ConfigProperty(name = "db.user") String dbUser, @ConfigProperty(name = "db.pwd") String dbPwd, @ConfigProperty(name = "db.url") String dbUrl) {
        this.dbUser.set(dbUser);
        this.dbPwd.set(dbPwd);
        this.dbUrl.set(dbUrl);
    }

    String getDbUser() {
        return dbUser.get();
    }

    void setDbUser(String dbUser) {
        this.dbUser.set(dbUser);
    }
    String getDbPwd() {
        return dbPwd.get();
    }

    void setDbPwd(String dbPwd) {
        this.dbPwd.set(dbPwd);
    }
    String getDbUrl() {
        return dbUrl.get();
    }

    void setDbUrl(String dbUrl) {
        this.dbUrl.set(dbUrl);
    }
}
