package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Модель пользователя
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"login"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** Идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /** Имя */
    @Column(name = "name")
    private String name;
    /** Логин */
    @Column(name = "login")
    private String login;
    /** Пароль */
    @Column(name = "password")
    private String password;
}