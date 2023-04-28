# Microservicio Item
Curso de Udemy - Microservicios con Spring Boot y Spring Cloud Netflix Eureka

## Agregando Cliente REST de Feign desde Spring Tool Suite
- Click derecho al proyecto: Spring/Add Starters Se abrirá una ventana para agregar nuevas dependencias 
- (click) Spring Cloud Routing
- [Check] OpenFeign

## Para trabajar con el balanceador de carga Ribbon
- **Para usar la dependencia de ribbon** debemos **bajar la versión actual de spring boot** a la versión **2.3.12.RELEASE**.
**La versión 2.4 en adelante no es compatible con ribbon**, en esas versiones ya **se usa Spring Cloud Load Balancer**, pero solo para efectos de usar ribbon como balanceo de carga sin EUREKA es necesario cambiar las versiones.
- También se cambia la versión de Spring-cloud a Hoxton.SR12
- Es importante también cambiar la versión de java a la 1.8. Estuve realizando el repaso del curso con java 17 en IntelliJ IDEA y da errores con esta versión de Ribbon.
- Luego agregamos en el pom.xml la dependencia de Ribbon

- Configurar ribbon: feignClient y el archivo application.properties (solo trabajaremos con 2 instancias [dos direcciones donde estarán alojados nuestros servicios productos])

# Quitando dependencia balanceador de carga

Quitamos la dependencia de **spring-cloud-starter-netflix-ribbon** porque como agregamos la
dependencia de **spring-cloud-starter-netflix-eureka-client**, éste último ya lo trae incorporado.
Ojo, que a esta altura del curso bajamos la versión de Spring a **2.3.12.RELEASE** por lo que estamos
trabajando con ribbon.

## Hystrix: Para trabajar con tolerancia a fallos

- Al igual que sucede con ribbon, **hystrix** es compatible hasta la versión de Spring Boot **2.3.12.RELEASE**.
  Versiones posteriores se usa **Resilience4j**.
- Luego de agregar la dependencia de Hystrix al pom.xml, la debemos habilitar en la clase principal
  usando la siguiente anotación **@EnableCircuitBreaker**.
- Hystrix, envuelve a **ribbon** para la tolerancia a fallos, manejo de latencia y timeout. Recordar que si bien ribbon
  no está como dependencia explícita, en realidad cuando agregamos la dependencia de **eureka client**, ésta ya lo
  trae internamente.

## Trabajando con Hystrix. Ejemplo camino alternativo

- Nuestro microservicio item, consume el microservicio de productos.
- Cuando ms-item llame con FeignClient (o RestTemplate) al método **verProducto** del ms-productos, recibirá un
  error, ya que intencionalmente modificamos el código para que lance el error:

````
@GetMapping(path = "/{id}")
public ResponseEntity<Producto> verProducto(@PathVariable Long id) {
    Producto producto = this.productoService.findById(id);
    boolean thereIsAnError = false;
    if (thereIsAnError) {
        throw new RuntimeException("No se pudo cargar el producto!"); <----- Lanzará el error
    }
    return ResponseEntity.ok(this.productoConPuerto(producto));
}
````

- En el método handler del ms-item, agregamos la anotacón **@HystrixCommand(fallbackMethod = "metodoAlternativo")**
  indicando el método alternativo a llamar cuando ocurra un error en la comunicación
  del método actual.

````
@HystrixCommand(fallbackMethod = "metodoAlternativo")
@GetMapping(path = "/producto/{productoId}/cantidad/{cantidad}")
public ResponseEntity<Item> getItem(@PathVariable Long productoId, @PathVariable Integer cantidad) {
    return ResponseEntity.ok(this.itemService.findByProductId(productoId, cantidad));
}
````

- El método alternativo al que se llamará cuando ocurra un error,
  debe ser exactamente igual al método donde se agregó la anotación **@HystrixCommand(...)**:

````
public ResponseEntity<Item> metodoAlternativo(Long productoId, Integer cantidad) {
    Producto producto = new Producto();
    producto.setId(productoId);
    producto.setNombre("Cámara Sony");
    producto.setPrecio(500D);

    Item item = new Item();
    item.setCantidad(cantidad);
    item.setProducto(producto);

    return ResponseEntity.ok(item);
}
````

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