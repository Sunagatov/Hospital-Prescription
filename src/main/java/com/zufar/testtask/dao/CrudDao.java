package com.zufar.testtask.dao;

import com.zufar.testtask.model.Identified;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<E extends Identified<Id>, Id> {

    /* Создает запись в БД, соответствующую обьекту object */
    void create(E object) throws SQLException;

    /* Возвращает обьект, соответствующий данному идентификатору */
    E get(Id id) throws SQLException;

    /* Возвращает список обьектов, соответствующим всем записям в БД */
    List<E> getAll() throws SQLException;

    /* Изменяет запись в БД в соответствии с состоянием object */
    void update(E object) throws SQLException;

    /* Удаляет запись в БД, соответствующую обьекту object*/
    void delete(E object) throws SQLException;

}