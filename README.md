
# Laboratorio 4 - 1TEL05

MealExplorer es una aplicación Android desarrollada en Java que permite explorar recetas de cocina de todo el mundo consumiendo la API pública TheMealDB. La app cuenta con tres fragmentos principales (Categorías, Platos y Receta) conectados mediante Navigation Component y un BottomNavigationView, implementa llamadas HTTP asíncronas con Retrofit y carga de imágenes con Glide, y usa el sensor acelerómetro del dispositivo para obtener una receta aleatoria al detectar una agitación superior a 4 m/s².

## Video de demostración



https://github.com/user-attachments/assets/4196c3f1-5302-4623-8dad-753001f468db



## Principales prompts utilizados para el desarrollo del laboratorio

| # | Prompt                                                                                                                                                                                                                                                                                                                  | Motivo | Modelo |
|---|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------|--------|
| 1 | Necesito crear las clases Java para mapear el JSON que devuelve ThemealDB. Por ejemplo la respuesta de categorías tiene un campo "categories" con una lista que tiene idCategory, strCategory, strCategoryThumb y strCategoryDescription. Crea las clases con Gson y los getters.                                       | Crear los modelos de datos antes de empezar con la lógica para tener las clases listas al consumir la API. | Claude Sonnet 4.6 |
| 2 | Como hago para crear el cliente de Retrofit en Android? necesito consumir estos endpoints de themealdb: categories.php, filter.php con query "c" para categoría y query "i" para ingrediente, lookup.php con query "i" para el id, y random.php. quiero que sea un singleton para no instanciarlo varias veces          | Configurar Retrofit de forma centralizada para hacer las llamadas a la API desde cualquier fragmento sin repetir código. | Claude Sonnet 4.6 |
| 3 | tengo que hacer un RecyclerView para mostrar categorías de comida, cada item tiene una imagen y un nombre. como hago el adapter en Java con ViewHolder? también necesito manejar el click en cada item para navegar al siguiente fragmento                                                                              | Implementar el RecyclerView de categorías con su adapter siguiendo el patrón visto en clase. | Claude Sonnet 4.6 |
| 4 | en mi AppActivity tengo un BottomNavigationView con 3 tabs y un NavHostFragment. el problema es que cuando navego entre tabs se van acumulando los fragmentos en el backstack y al presionar atrás no regresa al MainActivity sino que va al fragmento anterior. como lo soluciono?                                     | Corregir el comportamiento del backstack para que el botón atrás siempre regrese al MainActivity sin importar cuántos fragmentos se hayan visitado. | Claude Sonnet 4.6 |
| 5 | mi fragmento de Meals tiene que funcionar de dos formas: si llego desde un click en una categoría tiene que cargar automáticamente los platos de esa categoría, pero si abro el tab directamente tiene que mostrar un campo de texto para buscar por ingrediente. como detecto desde donde vine y manejo los dos casos? | Implementar la lógica del fragmento de Platos que cambia su comportamiento según si recibe argumentos de navegación o no. | Claude Sonnet 4.6 |
| 6 | como uso el acelerómetro en Android para detectar cuando agito el celular? el umbral es 4 m/s² y cuando se detecte tengo que llamar a la api de random.php y mostrar un dialog con el resultado. el sensor solo debe estar activo en un fragmento específico                                                            | Implementar la detección de agitación con el sensor acelerómetro para el requisito de la Pregunta 2 del laboratorio. | Claude Sonnet 4.6 |
