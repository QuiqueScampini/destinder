# Destinder

Autor: Enrique Scampini.
Aplicacion Api rest realizada en base a la consigna del examen "Examen JAVA Ssr - Tinder Despegar".

#### 1) Precondiciones de uso
- Tener instalado java 1.8.
- Tener instalado maven.
- Tener levantado redis en el puerto 6379.
	
#### 2) Iniciar y detener la aplicacion

	2.1) Descargar app del repositorio
		- git clone https://github.com/QuiqueScampini/destinder.git
		- cd destinder

	2.2) Iniciar via Script (Recomendado)
		2.2.1) Iniciar
			./startDestinder.sh
		2.2.2) Detener
			./stopDestinder.sh
			
	2.3) Iniciar directamente por consola (No recomendado)
		2.3.1) Iniciar
			mvn spring-boot:run
		2.3.2) Detener
			Precionando ctrl + c

#### 3) Consideracions

	- Servicios Expuestos
		GET 	http://localhost:8080/destinder/hotels/prices?city_id=982
		POST	http://localhost:8080/destinder/hotels/likes
		DELETE	http://localhost:8080/destinder/hotels/likes
		GET	http://localhost:8080/destinder/myLovedAndHatedHotels
		
	- La aplicacion fue desarrollada y probada en linux. No fue testeada en windows por lo cual no se garantiza su funcionamiento sobre esta plataforma.

	- Se obtuvieron 1000 hoteles para el snapshot de las ciudades con los siguientes IDs:
		- 982
		- 4544
		- 5296
		- 2820
		- 52606
		- 31651

	Se trajeron hoteles de estas ciudades ya que los primeros 1000 hoteles que trae la api no contienen detalle de precios. 	En estas ciudades hay hoteles que contienen detalles de precios y hoteles que no. 

	- La api de Despegar trae como maximo el detalle de precios de hasta 10 hoteles asi que la respuesta del servicio 	http://localhost:8080/destinder/hotels/prices?city_id="cityID" esta limitada a 10 hoteles. En caso de necesitar traer todos la 	modificacion en el codigo es menor. 


	- Se expuso el servicio http://localhost:8080/destinder/myLovedAndHatedHotels que devuelve un objeto json con 2 listas, la de hoteles que me gustan y la de hoteles que no me gustan.

	{
    "likedHotels": [
        "879783",
        "879784"
    ],
    "notlikedHotels": [
    	"879782"
    ]
	}

	- En Redis se persiste la lista de likedHotels y notlikedHotels. No se persistio el contador de likes de cada hotel. 

	- En caso de que redis no este disponible la solucion aplicable es procesar los datos en la aplicacion directamtente en forma de cache y cuando redis este disponible volcar los datos a la base. El problema de esto es que si la aplicacion se baja no se persisten estos datos. La implementacion de esta solucion no se aplico por cuestiones de tiempo.

	- El snapshot es guardado en disco en la siguiente ruta /destinder/snapshot/Snapshot.bin es un archivo binario con el contenido del HashMap del HotelManager. El mapa es clave: Id_Hotel valor: Objeto Hotel.
