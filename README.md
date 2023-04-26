# Microservicio Item
Curso de Udemy - Microservicios con Spring Boot y Spring Cloud Netflix Eureka

## Agregando Cliente REST de Feign desde Spring Tool Suite
- Click derecho al proyecto: Spring/Add Starters Se abrirá una ventana para agregar nuevas dependencias 
- (click) Spring Cloud Routing
- [Check] OpenFeign

## Para trabajar con el balanceador de carga Ribbon
- Para usar la dependencia de ribbon debemos bajar la verión actual de spring boot a la versión 2.3.12.RELEASE
La versión 2.4 en adelante no es compatible con ribbon, en esas versiones ya se usa Spring Cloud Load Balancer, pero solo para efectos de usar ribbon como balanceo de carga sin EUREKA es necesario cambiar las versiones.
- También se cambia la versión de Spring-cloud a Hoxton.SR12
- Es importante también cambiar la versión de java a la 1.8. Estuve realizando el repaso del curso con java 17 en IntelliJ IDEA y da errores con esta versión de Ribbon.
- Luego agregamos en el pom.xml la dependencia de Ribbon

- Configurar ribbon: feignClient y el archivo application.properties (solo trabajaremos con 2 instancias [dos direcciones donde estarán alojados nuestros servicios productos])


## Ejecutar varias instancias del proyecto en puertos distintos
En el puerto 9001

```
click derecho/Run AS.../Run Configurations...
En la lista izquierda opción Spring Boot App, seleccinonamos el servicio que ejecutaremos
En la vista derecha seleccionamos la pestaña Arguments
En la opción de VM arguments, escribimos: 
	-Dserver.port=9001
Click en Run
```

Puerto por defecto

```
Si anteriormente habíamos ejecutar una instancia en un puerto distinto	
Ejemplo
	-Dserver.port=9001
Lo quitamos ya que queda guardado en memoria, damos aplicar y ejecutar
```

Funcionamiento del balanceo de carga con Ribbon

```
Ejecutaremos dos instancias del microservicios de productos (port 8001 y 9001). 
Ejecutaremos el microservicio items. 
Al llamar desde POSTMAN al microservicio ITEMS y al estar este 
configurado con Ribbon como su balanceador de carga, 
lo que hará será seleccionar por debajo la mejor instancia disponible
del microservicio productos, nosotros no lo vemos pero sí realiza el balanceo de carga con uno de 
los dos microservicios de productos
```