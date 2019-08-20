package com.github.lany192.service;


import com.github.lany192.domain.AlreadyExistsException;
import com.github.lany192.domain.EntityNotFoundException;
import com.github.lany192.domain.JsonObjects;
import com.github.lany192.domain.NotImplementException;

public interface CommonServiceInterface<T> {


    default JsonObjects<T> list(int pageNum,
                                int pageSize,
                                String sortField,
                                String sortOrder) {
        throw new NotImplementException();
    }

    default T create(T t) throws AlreadyExistsException {
        throw new NotImplementException();
    }

    default T retrieveById(long id) throws EntityNotFoundException {
        throw new NotImplementException();
    }

    default T updateById(T t) throws EntityNotFoundException {
        throw new NotImplementException();
    }

    default void deleteById(long id) {
        throw new NotImplementException();
    }

    default void updateRecordStatus(long id, int recordStatus) {
        throw new NotImplementException();
    }
}
