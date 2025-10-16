## Задание №2
### Быстрый запуск:
Поднимаем контейнер с PostgresSQL
```bash
docker run \
    --name postgresdb \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=secret \
    -e POSTGRES_DB=2nd_task \
    -p 5432:5432 \
    -d postgres:16
```
Клонируем этот репозиторий (или скачиваем .zip)
```bash
git glone https://github.com/a1pha-dev/2nd_task
```
Запускаем `Main.main()`
