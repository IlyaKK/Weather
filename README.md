# Weather
Тестовое приложение Avito

## Задание
Необходимо реализовать приложение, отображающее прогноз погоды.

### Требования к приложению:
Отображение прогноза погоды за текущий день
Отображение прогноза погоды за неделю
Возможность увидеть прогноз погоды в текущем городе
Возможность выбрать любой другой город, и узнать прогноз погоды в нем
### API
В качестве API, для получения прогноза, можно использовать любой сервис; допускается использование бесплатных/ограниченных/пробных версий.
Пример: OpenWeatherMap API

## Описание
В приложении используется [One Call API 3.0](https://openweathermap.org/api/one-call-3) для получения данных о погоде и [Geocoding API](https://openweathermap.org/api/geocoding-api) для поиска города, который можно добавить для отслеживания погоды. Данные с добавленными городами хранятся в базе данных.

Для API используется Retrofit, для базы данных Room.

Чтобы собрать проект и использовать API, необходимо получить API Key с [сайта](https://openweathermap.org/api). После этого создать файл ***apikey.properties*** в корне проекта.
В нем сохранить строчку:
>weather_one_call_api_key="полученный API Key"

Также, необходимо использовать VPN.
