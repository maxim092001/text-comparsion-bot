# TextComparsionBot

#### Телеграм бот, основная (и на данный момент единственная) задача которого это сравнение двух текстов. 
* Для упрощения сравнения применяется [Стеммер Портера](https://ru.wikipedia.org/wiki/Стеммер_Портера).
* Для самого сравнения используется [Косинусное сходство](https://ru.wikipedia.org/wiki/Векторная_модель).
* В базу данных добавляется id пользователя, первый и второй текст, а также статус. Тексты удаляются сразу после вычисления косинусного расстояния. СУБД - [Postgres](https://www.postgresql.org/).
* Бот развернут на бесплатном хостинге [Heroku](https://www.heroku.com), поэтому возможны некоторые задержки в ответах.
* Протестировать бота можно тут [@maxon_verygood_first_bot](https://teleg.run/maxon_verygood_first_bot)


P.S. сравнение текстов происходит по словам, следовательно два текста "Hi" и "Hello" будут совершенно различны.

Регистрация
---
![Register](https://github.com/maxim092001/TextComparsionBot/blob/master/src/main/resources/start.png)

Первый текст
---
![FirstText](https://github.com/maxim092001/TextComparsionBot/blob/master/src/main/resources/firsttext.png)

Второй текст и мгновенный результат
---
![SecondText](https://github.com/maxim092001/TextComparsionBot/blob/master/src/main/resources/result.png)
