package com.openclassrooms.starterjwt.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootTest
class DatabaseSeederTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseSeeder seeder;

    @Test
    void run_shouldSeedDatabase() throws Exception {
        seeder.run();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {

            rs.next();
            int count = rs.getInt(1);

            assert(count > 0);
        }
    }
}
