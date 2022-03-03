# devoirSpark


## Réponse 1 : 
Resources : Le client veut stocker les données pour faire des statistiques dans la suite, mais il ne sait toujours pas combien de stokcage en total il aura besoin. En effet, nous devons stocker les données pour une durée indéterminée. Aussi, nous savons que la somme du poids de tous les rapports quotidiens est 200Go, nous pouvons conclure que le stockage de données doit être un lac de données -Data lake- (peut stocker pour toujours les données et bon pour les données énormes à stocker).

Cost : De plus, d’un point de vue financier, il est intéressant d’utiliser un data lake : par exemple, le prix mensuel du Azure Data Lake Gen1 est de 0,0329 €/GB/mois. Après calcul, les pacificateurs paieront € 14000 la première année et € 54000 l’année suivante (sans réduction de prix). Enfin, on peut dire que ces prix sont abordables pour un organisme gouvernemental.

## Réponse 2 :
le business constraint est le temps, l'alert doit etre déclancher le plus rapidement possible. On va utiliser a streaming component pour avoir les données en temps réél et les traiter au fure et à mesure. 


## Réponse 3 :

le fail peut être expliquer du fait que l'architecture doit être évolutifs ce qui veut dire la capacité de notre application à faire face à un nombre croissant d'utilisateurs interagissant simultanément avec l'application. Par conséquent, cela permettra le fonctionnement aussi bien avec un ou un millier d'utilisateurs et qui résiste aux hauts et aux bas du trafic.

Dans notre cas les data scientists seuls ne peuvent pas faire face à ce genre de problème. 

## Réponse 4 :

les informations a ajouter :

- la date ( pour chaque data ) #utile pour faire des statistiques.
- état de la batterie du drone ( peacemakers )
- number of agited people ( nombre de personne en état de colère ) utile pour pouvoir envoyer un grand nombre de drones pour les remettre en état stable
- ( pourcentage of agited people per zone ) pour les statistiques
