# TextComparsionBot

#### Telegram bot, the main (and currently the only) task of which is to compare two texts.
* To simplify comparison, [Porter's Stemmer](https://tartarus.org/martin/PorterStemmer/) is used.
* For the comparison itself, [Cosine Similarity](https://en.wikipedia.org/wiki/Cosine_similarity) is used.
* The user id, the first and second text, and the status are added to the database. The texts are deleted immediately after calculating the cosine distance. DBMS -    [Postgres](https://www.postgresql.org/).
* The bot is deployed on free hosting [Heroku](https://www.heroku.com), so there may be some delays in responses.
* You can test the bot here [@maxon_verygood_first_bot](https://teleg.run/maxon_verygood_first_bot).


P.S. The comparison of texts is carried out by words, therefore the two texts "Hi" and "Hello" will be completely different.

Registration
---
![Register](https://github.com/maxim092001/TextComparsionBot/blob/master/src/main/resources/start.png)

First test
---
![FirstText](https://github.com/maxim092001/TextComparsionBot/blob/master/src/main/resources/firsttext.png)

Second text and result
---
![SecondText](https://github.com/maxim092001/TextComparsionBot/blob/master/src/main/resources/result.png)
