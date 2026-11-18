# Bookshelf

Aplicación de Android con Jetpack Compose que busca libros con Google Books API, solicita los detalles de cada volumen y muestra portadas en una cuadrícula.

## Requisitos

- Android Studio con Android SDK 35.
- JDK 17 o superior.
- Un teléfono Android con depuración USB activada para instalar y probar en dispositivo real.

## Abrir y ejecutar

Abre esta carpeta en Android Studio, espera a que Gradle sincronice y ejecuta la configuración `app` en el dispositivo conectado. El proyecto incluye Gradle Wrapper (`gradlew.bat`), así que también puedes compilar desde una terminal sin instalar Gradle por separado.

Para ejecutar las pruebas unitarias desde Android Studio: clic derecho en `app/src/test` y selecciona **Run tests**. Desde una terminal en Windows, ejecuta `.\gradlew.bat :app:testDebugUnitTest`.

La implementación sigue la búsqueda `volumes?q=...` del codelab. Para usar tu clave pública de Google Books, agrégala a `local.properties` como `booksApiKey=TU_CLAVE`, o define la variable de entorno `GOOGLE_BOOKS_API_KEY`. Gradle adjunta la clave a la búsqueda y a las solicitudes de volúmenes. `local.properties` no se incluye en Git ni en la descarga del proyecto.
