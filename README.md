**RestFul API архитектура - это архитектура клиент-серверного приложения, базирующаяся на 6 принципах.**

1. Универсальный интерфейс взаимодействия. (Uniform Interface)

2. Запросы без состояния. (Stateless)

3. Поддержка кеширования. (Cacheable)

4. Клиент-серверная ориентация.

5. Поддержка слоев (Layered System)

6. Возможность выполнять код на стороне клиента (Code on Demand)


**Все запросы используют указания на методы. По сути они говорят серверу, что нужно сделать (поэтому проименованы глаголами).**

1. GET - используется для получения данных с сервера. Не должен изменять данные на сервере. В основном применяется только для чтения данных с сервера.

2. HEAD - аналогично GET. но не содержит тела ответа.

3. POST - используется для создания новых ресурсов на сервере (добавление данных).

4. PUT - аналог POST, но предназначен для обновления или замены уже существующего ресурса на сервере.

5. DELETE - применяется для удаления ресурса (данных) с сервера. Не содержит тела ответа.

6. PATCH - применяется для изменения ресурса (данных) на сервере.

7. OPTIONS - извлекает список поддерживаемых опций на сервере.

8. CONNECT - проверяет соединение с сервером.

9. TRACE - используется для тестирования соединения с дополнительной информацией.