package com.zufar.testtask.dao;

import com.zufar.testtask.model.Identified;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractCrudDao<E extends Identified<Id>, Id extends Long> implements CrudDao<E, Id> {

    protected Connection connection;

    /* Возвращает sql запрос для вставки новой записи в БД */
    protected abstract String getCreateQuery();

    /* Возвращает sql запрос для получения всех записей из БД */
    protected abstract String getSelectQuery();

    /* Возвращает sql запрос для обновления записи в БД */
    protected abstract String getUpdateQuery();

    /* Возвращает sql запрос для удаления записи из БД */
    protected abstract String getDeleteQuery();

    public AbstractCrudDao(Connection connection) {
        this.connection = connection;
    }

    /* Возвращает список объектов соответствующих содержимому ResultSet */
    protected abstract List<E> parseResultSet(ResultSet resultSet) throws SQLException;

    /* Устанавливает аргументы в запрос insert  в соответствии со значением полей объекта object */
    protected abstract void prepareStatementForInsert(PreparedStatement preparedStatement, E object) throws SQLException;

    /* Устанавливает аргументы в запрос update в соответствии со значением полей объекта object */
    protected abstract void prepareStatementForUpdate(PreparedStatement preparedStatement, E object) throws SQLException;

    @Override
    public void create(E object) throws SQLException {
        String createQuery = getCreateQuery();
        PreparedStatement statement = connection.prepareStatement(createQuery);
        prepareStatementForInsert(statement, object);
        statement.executeUpdate();
    }

    @Override
    public E get(Long id) throws SQLException {
        String selectQuery = String.format("%s WHERE id = ?", getSelectQuery());
        PreparedStatement statement = connection.prepareStatement(selectQuery);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        List<E> objects = parseResultSet(resultSet);
        return objects.iterator().next();
    }

    @Override
    public void update(E object) throws SQLException {
        String updateQuery = getUpdateQuery();
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        prepareStatementForUpdate(preparedStatement, object);
        preparedStatement.executeUpdate();
    }

    @Override
    public void delete(E object) throws SQLException {
        String deleteQuery = getDeleteQuery();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setObject(1, object.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<E> getAll() throws SQLException {
        String selectQuery = getSelectQuery();
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        return parseResultSet(resultSet);
    }
}