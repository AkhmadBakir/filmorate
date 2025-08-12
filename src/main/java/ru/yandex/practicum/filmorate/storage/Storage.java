package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {

    boolean checkExists(int id);

    T findById(int id);

    T add(T o);

    void update(T o);

    List<T> findAll();

}