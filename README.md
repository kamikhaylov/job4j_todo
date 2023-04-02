# job4j_todo
### Проект "Список задач"

Веб приложение на языке Java с библиотекой Spring boot.

Сайт по ведению дел. Главная страница со списком всех заданий. В таблице отображается имя, дата создания и состояние задание (выполнено или нет).

Доступны действия:
- Добавить задание.
- Списки: все задания, только выполненные или только новые.
- Подробное описанием.
- Действия над заданием: выполнить, отредактировать, удалить.
- Если нажали на кнокпу выполнить, то задание переводиться в состояние выполнено.
- Кнопка редактировать переводит пользователя на отдельную страницу для редактирования.
- Кнопка удалить, удаляет задание и переходит на список всех заданий.

### Используемые технологии
![tech-2.png](readme/images/tech-2.png)

### Архитектура приложения трехслойное
- Слой контроллеры
- Слой сервисы
- Слой работы с БД

### Требуемое окружение
- JDK 17
- Apache Maven 3.8.5
- PostgreSQL 13
- Браузер

### Подготовка к запуску приложения
- Создать БД todo хост `jdbc:postgresql://localhost:5432/todo`
- Собрать jar с приложением, выполнив команду `mvn install`
- Запустить приложение из папки target, выполнив команду: `java -jar job4j_todo-1.0-SNAPSHOT.jar`
- Перейти в браузере по ссылке `http://localhost:8080/todo`

### Главная страница, форма со всеми задачами
![tasks.png](readme/images/tasks.png)

### Страница с новыми задачами
![newTasks.png](readme/images/newTasks.png)

### Страница с выполненными задачами
![doneTasks.png](readme/images/doneTasks.png)

### Добавление новой задачи
![createTasks.png](readme/images/createTasks.png)

### Детальная информация по задаче
![task.png](readme/images/task.png)

### Редактирование задачи
![update.png](readme/images/update.png)

### Ошибка обновления задачи
![taskUpdateError.png](readme/images/taskUpdateError.png)

### Ошибка удаления задачи
![taskDeleteError.png](readme/images/taskDeleteError.png)

### Регистрация нового пользователя
![registration.png](readme/images/registration.png)

### Пользователь существует
![userFail.png](readme/images/userFail.png)

### Пользователь успешно зарегистрирован
![userSuccess.png](readme/images/userSuccess.png)

### Сраница авторизации
![loginPage.png](readme/images/loginPage.png)

### Неверный логин или пароль
![loginFail.png](readme/images/loginFail.png)

### Контакты
kanmikhaylov@gmail.com
