# Destinder

1. Tener levantado redis 

	sudo systemctl start redis
	
2. Iniciar y detener la aplicacion

	2.1 Via Script (Recomendado)
		2.1.1 Iniciar
			./startDestinder.sh
		2.1.2 Detener
			./stopDestinder.sh
			
	2.2 Directamente por consolta (No recomendado)
		2.2.1 Iniciar
			mvn spring-boot:run
		2.2.2 Detener
			Precionando ctrl + c

4. Consideracions

4.1 Se obtuvieron 1000 hoteles para el snapshot de las ciudades con los siguientes IDs:
	982, 4544, 5296, 2820, 52606, 31651

Se trajeron hoteles de estas ciudades ya que los primeros 1000 hoteles que trae la api no contienen detalle de precios. En estas ciudades hay hoteles que contienen detalles de precios y hoteles que no. 

4.2 La api de Despegar trae como maximo el detalle de precios de hasta 10 hoteles asi que la respuesta del servicio http://localhost:8080/destinder/hotels/prices?city_id="cityID" esta limitada a 10 hoteles. En caso de necesitar traer todos la modificacion en el codigo es menor. 


4.3 Se expuso el servicio http://localhost:8080/destinder/myLovedAndHatedHotels que devuelve un objeto json con 2 listas, la de hoteles que me gustan y la de hoteles que no me gustan.

	{
    "likedHotels": [
        "879783",
        "879784"
    ],
    "notlikedHotels": [
    	"879782"
    ]
	}

4.4 En Redis se persiste la lista de likedHotels y notlikedHotels. No se persistio el contador de likes de cada hotel. 

4.5 En caso de que redis no este disponible la solucion aplicable es procesar los datos en la aplicacion directamtente en forma de cache y cuando redis este disponible volcar los datos a la base. El problema de esto es que si la aplicacion se baja no se persisten estos datos. La implementacion de esta solucion no se aplico por cuestiones de tiempo.

5. El snapshot es guardado en disco en la siguiente ruta /destinder/snapshot/Snapshot.bin es un archivo binario con el contenido del HashMap del HotelManager. El mapa es clave: Id_Hotel valor: Objeto Hotel.
