# GeoQuizzAPI

Add Description

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
```
* 
* 
```
### Installing and Running

In order to run the project, use he following commands

```
* 
* 
```

Then, a server is running on your local machine shown in your terminal.


## Built With

* [VueJs](https://github.com/vuejs/vue) (example)

# Creators

* [Quentin RENOUARD](https://github.com/Quinou-kun)
* [Luc ANDRE](https://github.com/lucandreiut)
* [Quentin DELAMARRE](https://github.com/windos757)
* [Allan DEMARBRE](https://github.com/demarbre1u)
* [Nacera ELIAS](https://github.com/EliasNacera)

# Available routes

## Series

Display the list of all available series : http://localhost:8080/geoquizzapi/api/series (GET)

Display the information on a given series : http://localhost:8080/geoquizzapi/api/series/uidSerie?size=[size] (GET), where __[size]__ is the number of photos to be fetched. If size <= 0, then it defaults to 10. Photos are given in a random order

__(ADMIN)__ Create a new serie : http://localhost:8080/geoquizzapi/api/series (POST)

```json
{
  "mapOptions":"lat;lng;zoom",
  "ville":"nancy"
}
```

__(ADMIN)__ Add a photo to a serie : http://localhost:8080/geoquizzapi/api/series/uidSerie?desc=[desc]&pos=[pos] (POST), while sending an image via a form 

## Games

To create a new game : http://localhost:8080/geoquizzapi/api/games?idSerie=[uidSerie] (POST). 

To update a game and change its status : http://localhost:8080/geoquizzapi/api/games/uidGame?token=[token]&score=[score]&playerName=[playerName]&mode=[mode] (PUT), where __token__ is the token of the game and where mode is the difficulty level

To see the list of all finished games of a specified serie : http://localhost:8080/geoquizzapi/api/games?idSerie=[idSerie]&mode=[mode] (GET)

## Photos

Get the image of a given photo : http://localhost:8080/geoquizzapi/api/photos/uidPhoto (GET)

## Users

Create a user : http://localhost:8080/geoquizzapi/api/users (POST)

```json
{
  "password":"test",
  "fullname":"test",
  "email":"test@test.fr"
}
```

Login : http://localhost:8080/geoquizzapi/api/users?email=[email]&password=[password (GET)]
