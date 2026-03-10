# La Buvette de Bel'Air : construyendo un backend para la famosa buvette del festival eXalt. Con Kotlin, IA y amor.

Versión inglesa : [README.md](README.md)  
Versión francesa : [README_fr.md](README_fr.md)

>[!note]
> 
> Este proyecto forma parte del camino de aprendizaje eXalt IT augmented engineer, ubicado en su [academy](https://academy.exalt-company.com/paths/699c49f3a1dffef24c46c739/home).

Hola y bienvenido al repositorio del proyecto La Buvette de Bel'Air!

Este proyecto es tu terreno de juego para crear un backend robusto de gestión de bebidas y snacks.

Vas a construir el mejor backend posible usando Kotlin.

Pero, lo más importante, tu nuevo mejor amigo: GitHub Copilot, tu pato de goma / becario demasiado entusiasta para el pair programming!

## Estructura del proyecto

```
belairs-buvette/
 application/      # Punto de entrada  conecta el dominio y la infraestructura
 domain/           # Lógica de negocio y modelo de dominio
 infrastructure/   # Adaptadores, persistencia, integraciones externas
 build-logic/      # Plugins de convención de Gradle
```

## Instalación de la cadena de herramientas

| Herramienta | Versión | Documentación |
|-------------|---------|---------------|
| JDK | 21+ | [Adoptium Temurin](https://adoptium.net/) |
| Git | latest | [git-scm.com](https://git-scm.com/downloads) |

> El proyecto usa el wrapper de Gradle (`./gradlew`), por lo que no necesitas instalar Gradle por separado.

## Cómo empezar

### Requisitos previos

- JDK 21+
- Git

### Fork & Clone

Haz fork de este repositorio en tu propia cuenta de Gitlab (solo rama main) y luego clónalo:

```bash
git clone <URL_DE_TU_FORK>
cd belairs-buvette
```

### Espejar en GitHub

Para poder usar correctamente las funcionalidades de IA avanzadas con Copilot, espeja este repositorio en tu cuenta GitHub:

```bash
git remote add github <the URL of your new GitHub repository>
git branch -M main
git push -u github main
```

### Compilar

```bash
./gradlew build
```

### Ejecutar los tests

```bash
./gradlew test
```

## Próximos pasos

Comienza siguiendo el material de formación en la [academy](https://academy.exalt-company.com/paths/699c49f3a1dffef24c46c739/home).

Consulta [FEATURES_es.md](./FEATURES_es.md) para la lista completa de historias de usuario y criterios de aceptación.

Feliz programación!
