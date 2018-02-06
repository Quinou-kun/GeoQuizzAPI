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
* 
* 
* 

# Creators

* [Quentin RENOUARD](https://github.com/Quinou-kun)
* [Luc ANDRE](https://github.com/lucandreiut)
* [Quentin DELAMARRE](https://github.com/windos757)
* [Allan DEMARBRE](https://github.com/demarbre1u)
* [Nacera ELIAS](https://github.com/EliasNacera)

# Available routes

## Series

Display the list of all available series : http://localhost:8080/geoquizzapi/api/series (GET)

Create a new serie : http://localhost:8080/geoquizzapi/api/series (POST) 
```json
{
	"mapOptions":"lat;lng;zoom",
	"ville":"nancy"
}
```

Add a photo to a serie : http://localhost:8080/geoquizzapi/api/series (PUT) 
```json
{
	"description":"desc",
	"position":"lat;lng",
	"url":"www.test.fr"
}
```

## Games

To create a new game : http://localhost:8080/geoquizzapi/api/games?idSerie=[uidSerie]&playerName=[playerName] (POST). __playerName__ defaults to "Anonyme" if not specified

To update a game and change its status : http://localhost:8080/geoquizzapi/api/games/uidGame?token=[token]&score=[score] (PUT), where __token__ is the token of the game

To see the list of all finished games of a specified serie : http://localhost:8080/geoquizzapi/api/games?idSerie=[idSerie] (GET)