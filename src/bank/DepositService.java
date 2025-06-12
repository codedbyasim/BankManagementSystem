package bank;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DepositService {

    private final Connection connection;

    public DepositService(Connection connection) {
        this.connection = connection;
    }

    public boolean deposit(String accId, String custId, String date, double balance, double depositAmount) throws Exception {
        try {
            connection.setAutoCommit(false);

            String insertSQL = "INSERT INTO deposit(acc_id, cust_id, date, balance, deposit) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insert = connection.prepareStatement(insertSQL)) {
                insert.setString(1, accId);
                insert.setString(2, custId);
                insert.setString(3, date);
                insert.setDouble(4, balance);
                insert.setDouble(5, depositAmount);
                insert.executeUpdate();
            }

            String updateSQL = "UPDATE account SET balance = balance + ? WHERE acc_id = ?";
            try (PreparedStatement update = connection.prepareStatement(updateSQL)) {
                update.setDouble(1, depositAmount);
                update.setString(2, accId);
                update.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }
}
