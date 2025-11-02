## [9.6.5] - 2025-10-26
### Modificado
- Mejora visual en los botones, de modo que ya no estan adentro de una caja y ademas, si el mouse esta encima de ellos, cambia de color junto a su texto

### Añadido
- Un nuevo tipo de enemigo que es parecido al tirador pero es mas duro y resistente.

## [9.6.4] - 2025-10-24
### Modificado
- Se corrigio la destruccion de la caja.

### Añadido
- Se agrego otro tipo de enemigo que persigue al jugador.

## [9.5.4] - 2025-10-23
### Modificado
- Se mejoro y corrigio el comportamiento de los enemigos. Ademas, se soluciono un error en las balas que disparaba el jugador

## [9.4.4] - 2025-10-22
### Añadido
- Se añadió la mecanica de parry (repeler un ataque enemigo en el momento justo)

### Modificado
- Se mejoro la camara de modo que al morir un jugador, la calidad de renderizacion del otro personaje no decaiga
- Se arregló la mecanica de balas tras algunos errores que surgieron previamente. Ademas, se corrigio la velocidad en la que salian las balas
- Se realizo una mejora en la organizacion de clases y paquetes (especificamente en las que son de pantallas de menu)


## [8.4.4] - 2025-10-21
### Modificado
- Se volvio a implementar la desaparicion de enemigos al ser asesinados tras unos errores que surgieron previamente
- Se mejoro el codigo de destruccion de caja, ahora solo desapareciendo una y no todas las del mapa al golpear una.

## [8.4.3] - 2025-10-20
### Modificado
- Se realizaron arreglos en el codigo de atacar ya que antes sucedia que la memoria se llenaba

## [8.3.3] - 2025-10-19
### Modificado
- Se corrigieron algunos errores relacionados con la camara que surgieron con la implementacion de niveles.
- Se corrigio un error al abrir opciones que provocaba que el otro jugador siguiera moviendose

## [8.3.2] - 2025-10-14
### Añadido
- Ahora hay cambio de niveles a llegar a un punto de cada mapa.

## [7.3.2] - 2025-10-14
### Añadido
- Ahora algunos personajes pueden disparar en vez de atacar a distancia

## [7.2.2] - 2025-10-13
### Añadido
- Pequeña implementacion para que algunos personajes ataquen a 
distancia

## [7.1.2] - 2025-10-07
### Modificado
- Modificaciones en los movimientos de los enemigos de manera que actualmente cuando ven a un jugador, se quedan quietos y disparan.

## [7.0.2] - 2025-10-06
### Modificado
- Cambios en la camara de Partida para solucionar un error que provocaba cambio de ubicacion de los personajes al llegar al limite del mapa

## [7.0.1] - 2025-10-03
### Modificado
- Reemplazo de las clases Leone y Akame por un enum (factory) llamado FabricaDePersonajes que se encarga de instanciar a Leone y Akame y sus sprites.

## [7.0.0] - 2025-10-01
### Modificado
- Correcciones en como se dibujan los personajes, enemigos y las balas, de forma que ahora estos se extienden de actor y el stage partida se encarga de dibujarlos (con el fin de seguir convenciones).

## [6.9.6] - 2025-09-28
### Modificado
- Correcciones en el ataque del personaje

## [6.9.6] - 2025-09-24
### Modificado
- Correcciones en InputController para que se ajusten al principio de
responsabilidad.

## [6.9.5] - 2025-09-13
### Modificado
- Bug que permitia al jugador duplicar enemigos con entrar al menu

## [6.9.4] - 2025-09-08
### Añadido
- Agregado de volumen para el disparo del enemigo.
- Cambio al funcionamiento del spawn de enemigos y su cantidad
- 

## [6.9.3] - 2025-09-05
### Añadido
- El jugador puede matar al enemigo.

## [6.9.2] - 2025-09-04
### Añadido
- El enemigo puede disparar y ser capaz de matar a los jugadores

## [6.9.1] - 2025-09-03
### Modificado
- El codigo no muere si uno de los personajes al morir sigue presionando teclas.
- El codigo termina cuando ambos personajes mueren.
- Si un jugador muere y el otro jugador destruye una caja, el primero revive.
- Los jugadores no pueden salirse de la camara.
- Se soluciono el problema de la tecla para atacar que consistia en que para funcionar adecuadamente el input se encontraba en Personaje y no en InputController.

## [6.8.0] - 2025-08-31
### Añadido
- Implementacion de dos jugadores en el mapa de forma local.

## [5.7.9] - 2025-08-29
### Añadido
- Implementacion de enemigo moviendose en el mapa.

## [5.6.9] - 2025-08-20
### Modificado
- Destruccion permanente de la caja, Ya no reaparece al presionar opciones.
- Cambio de personaje y HUD al instante de ser destruida la caja.

## [4.6.9] - 2025-08-19
### Añadido
- Creacion de la clase InputProcessor
### Modificado
- El registro de las teclas siendo presionadas ahora lo realiza la clase InputProcessor con el uso InputProcessor

## [4.5.9] - 2025-08-16
### Modificado
- El volumen del sonido de ataque ahora esta compartido con el de la musica, de modo que cuando el volumen de musica esta bajo, el del sonido tambien. La musica de derrota tambien. 
- Optimizacion del metodo atacar. 

## [4.5.8] - 2025-08-04
### Modificado
- La colision del objeto desaparece junto a este
- Cambio de nombre en variables y metodos en Partida

## [4.5.7] - 2025-08-03
### Modificado
- Colision del objeto interactivo
- Desaparicion del objeto al golpearlo

## [4.5.6] - 2025-08-02
### Añadido
- Avance en la interacción de elementos

## [4.4.6] - 2025-08-01
### Añadido
- Implementacion de animacion de ataque del personaje y efectos de sonido (sonido de espada para el personaje de Akame). Los sonidos se reproducen al atacar solamente.
- Implementacion de la posibilidad de abrir el menu de opciones tambien durante la partida.
### Modificado
- Cambio en el mapa y implementacion de objeto para evento.

## [4.3.6] - 2025-07-31
### Añadido
- Creacion de la clase Opciones. Su funcion es llevarte a otra pantalla donde podes elegir entre ver los controles o subir/bajar la musica.
### Modificado
- Modificacion del sistema de gravedad
- Modificacion del sistema de colisiones(Deteccion de Muros y techos)

## [3.3.6] - 2025-07-30
### Añadido
- Creacion de la clase "Configuracion" dentro del paquete juego.
- Modificacion de como se manipula la musica dentro, menu y la clase "Principal".
- Agregado de funciones para la clase Musica.
### Modificado
- Modificacion en las clases para que mantengan la misma variable de musica y no hayan reproducciones de musica sobreponiendose. 

## [3.2.6] - 2025-07-29
### Añadido
- Creacion de clase EstiloTexto para ajustar los textos de botones y labels (estilo, tamaño, color, etc) sin necesidad de repetir codigo y reutilizandolo. Reestructuramiento de codigo

## [3.2.5] - 2025-07-28
### Modificado
- Modificacion del cartel de GameOver, siendo ahora mas atractivo
- Modificacion en los labels (textos). Ahora son mas atractivos, tienen mejor nitidez/calidad y son pixel art. 

## [3.2.4] - 2025-07-27
### Añadido
- Implementacion de gravedad
- Implementacion de limites en el mapa. El jugador y la camara chocan con los limites, de modo que no pueden seguir avanzando
- Implementacion de la posibilidad de que el jugador muera. En caso de caer al vacio, el personaje muere, aparece un cartel de derrota y tras unos segundos, el codigo termina

## [2.2.4] - 2025-07-25
### Añadido
- Modificacion del mapa base y de las colisiones del mapa
- Reestructuramiento de la musica. Creacion de clase Musica

## [2.2.3] - 2025-07-24
### Añadido
- Implementacion de colisiones basicas en el archivo .tmx
- Implementacion de deteccion de colisiones en Personaje y Partida 

## [2.2.2] - 2025-07-24
### Añadido
- Implementación de eleccion de personaje al azar al inicio de la partida mediante el uso de un sprite de prueba junto al sprite de Akame.
- Implementación de clase Jugador y uso de herencia para administrar las funciones basicas compartidas y las propias de cada hija como sus sprites

## [2.1.2] - 2025-07-23
### Añadido
- Implementación del personaje en el mapa y ahora puede moverse por el mapa. Además, la camara lo sigue.
- Implementación de la musica con sus respectivas teclas para poder modificar el volumen y silenciarlo, ademas se colocan 2 musicas una para el menu y otra para la partida.
- Portada del juego añadida
- Launcher del juego a pantalla completa
### Modificado
- Eliminación de archivos innecesarios como el logotipo de libGDX, iniciarPartida, etc
- Modificacion y ordenamiento de archivos.

## [1.1.2] - 2025-06-30
### Modificado
- Se movieron los archivos de modo que ya no esta la carpeta Proyecto

## [1.1.1] - 2025-05-25
### Modificado
- Se actualizó `IniciarPartida.java`.
- Se realizaron cambios en los assets: agregación de nuevos archivos y eliminación de archivos innecesarios.


## [1.1.0] - 2025-05-07
### Añadido
- Creacion de una pantalla al ejecutar el archivo con dos botones. Uno para salir del juego y otro que te envia al mapa.
- Mapa de prueba para ver como importar un mapa creado en tiled a eclipse


## [1.0.0] - 2025-05-06
### Añadido
- Creacion del proyecto 
