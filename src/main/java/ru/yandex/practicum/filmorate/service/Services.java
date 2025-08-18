package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface Services<T> {

    void add(T t);

    List<T> findAll();

    void update(T t);

    T findById(int id);

}