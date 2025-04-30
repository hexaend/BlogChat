# Реализация микросервиса Post-service


## Задачи:
- [ ] Реализовать безопасность.
- [ ] Реализовать CRUD операции для постов.
- [ ] Реализовать CRUD операции для комментариев.

## Основные сущности:
### Post:
- id (Long) - уникальный идентификатор поста.
- title (String) - заголовок поста.
- content (String) - содержимое поста.
- author (User) - автор поста. (ManyToOne)
- createdAt (LocalDateTime) - дата создания поста.
- updatedAt (LocalDateTime) - дата обновления поста.
- likes (Set<User>) - список пользователей, которые поставили лайк на пост. (ManyToMany)
- comments (List<Comment>) - список комментариев к посту. (OneToMany)