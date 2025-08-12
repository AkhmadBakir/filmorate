package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface Services<T, NewRequest, UpdateRequest> {

    T add(NewRequest newRequest);

    List<T> findAll();

    T update(UpdateRequest updateRequest);

    T findById(int id);

}