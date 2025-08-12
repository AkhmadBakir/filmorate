package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface Services<T, N, U> {

    T add(N newRequest);

    List<T> findAll();

    T update(U updateRequest);

    T findById(int id);

}