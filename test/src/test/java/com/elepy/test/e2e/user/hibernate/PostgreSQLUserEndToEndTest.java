package com.elepy.test.e2e.user.hibernate;

import com.elepy.Configuration;
import com.elepy.database.DatabaseConfigurations;
import com.elepy.test.e2e.user.ElepyUserEndToEndTest;

public class PostgreSQLUserEndToEndTest extends ElepyUserEndToEndTest {
    @Override
    public Configuration configuration() {

        return DatabaseConfigurations.PostgreSQL;
    }
}
