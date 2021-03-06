# Курсовой проект "Сетевой чат"

## Технические характеристики

+ IDE: Idea
+ Управление проектом: Maven
+ Управление версиями: github

## Функциональность

### Регистрация

+ Пользователь может зарегистрироваться, но если есть пользователь с таким именем, регистрация не удастся.

### Настройки

+ Настройки для запуска сервера и клиента считываются из файлов с настройками.

### Логирование

+ На стороне сервера происходит логирование всех событий: включение, отключение сервера, подключение нового клиентского соединения,
регистрация новых пользователей, сообщений пользователей, отключений пользователей.

+ На стороне клиента происходит логирование всех отправленных и полученных сообщений чата.

## Описание проекта

Реализовано приложение для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями,
состоящее из:

*1. Серверной части.*

*2. Клиентской части.*

## Описание работы приложения
Запускается серверная часть приложения, настройки сервера берутся из файла "settings.txt".
Сервер находится в состоянии ожидания новых подключений пользователей. При подключении нового пользователя(каждый пользователь - это запуск клиентской части),
сервер ожидает от клиента наименование соединения(никнэйм пользователя в чате):

+ *клиент передает никнэйм*

+ *сервер проверяет наличие такого никнэйма в чате*

+ *если никнэйм уже используется - пользователю отправляется соответствующее сообщение, сервер ожидает другое значение от пользователя*

+ *при получении корректного значения, создается новое соединение(Connect), которое сохраняется в списке активных соединений сервера.*

При получении сообщения от пользователя, оно отправляется всем активным пользователям из списка соединений с сервером.
При выходе пользователя из чата, его соединение закрывается и удаляется из списка активных соединений.

## Структура проекта

+ package Server    - серверная часть
+ package Client    - клиентская часть
+ package Service   - сервисы(настройки, логирование)
+ package Constants - используемые константы
