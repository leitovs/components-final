package co.edu.eafit.componentes.bankagent.dto;

import co.edu.eafit.componentes.bankagent.connection.BankDatabase;
import co.edu.eafit.componentes.bankagent.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PaymentDao implements Dao<Payment> {

    private Connection connection;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;

    public PaymentDao() {
        try {
            this.connection = BankDatabase.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Payment> get(int id) throws SQLException {
        Payment Payment = null;
        String query = "SELECT id, payerId, amount, creditId, payment_date FROM Payment WHERE id = ?";
        statement = this.connection.prepareStatement(query);
        statement.setInt(1, id);
        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Payment = new Payment(
                    resultSet.getInt("id"),
                    resultSet.getInt("payerId"),
                    resultSet.getLong("amount"),
                    resultSet.getInt("creditId"),
                    resultSet.getDate("payment_date")
            );
        }
        return Optional.ofNullable(Payment);
    }

    @Override
    public Collection<Payment> getAll() throws SQLException {
        List<Payment> Payments = new ArrayList<>();
        String query = "SELECT * FROM Payment";
        statement = this.connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Payments.add(new Payment(
                    resultSet.getInt("id"),
                    resultSet.getInt("payerId"),
                    resultSet.getLong("amount"),
                    resultSet.getInt("creditId"),
                    resultSet.getDate("payment_date")
            ));
        }
        return Payments;
    }

    @Override
    public void save(Payment Payment) throws SQLException {
        String query = "INSERT INTO Payment (payerId, amount, creditId, payment_date) values(?, ?, ?, ?,?)";
        statement = this.connection.prepareStatement(query);
        statement.setInt(2, Payment.getPayerId());
        statement.setLong(3, Payment.getAmount());
        statement.setInt(4, Payment.getCreditId());
        statement.setDate(5, Payment.getTimestamp());
        statement.executeUpdate();
    }

    @Override
    public void update(Payment Payment) throws SQLException {
        String query = "UPDATE Payment SET payerId = ?, amount = ?, creditId = ?, payment_date = ? WHERE id = ?";
        statement = this.connection.prepareStatement(query);
        statement.setInt(1, Payment.getPayerId());
        statement.setLong(3, Payment.getAmount());
        statement.setInt(2, Payment.getCreditId());
        statement.setDate(4, Payment.getTimestamp());
        statement.setInt(5, Payment.getId());
        statement.executeUpdate();
    }

    @Override
    public void delete(Payment Payment) throws SQLException {
        String query = "DELETE FROM Payment WHERE id = ?";
        statement = this.connection.prepareStatement(query);
        statement.setInt(1, Payment.getId());
        statement.executeUpdate();
    }
}
