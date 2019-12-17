Dukes of the Realm

Le jeu se déroule dans un royaume lointain, à la stabilité discutable, dans lequel le trône a été laissé vacant suite à diverses intrigues qu’il ne nous appartient pas de décrire ici. Chaque joueur incarne un duc, en lutte pour devenir le nouveau roi. Pour cela, ils doivent vassaliser les autres en conquérant leur terres.

Les joueurs disposent de châteaux générant des richesses avec lesquels ils pourront lever des osts pour attaquer les châteaux adverses. Le jeu se termine dès qu’un seul duc possède encore au moins un château.

Le jeu se déroule en pseudo-temps réel, c’est-à-dire que le jeu est divisé en tour de jeu s’enchaînant de manière automatique toutes les X secondes (X à déterminer pour rendre le jeu jouable). À chaque tour, chaque acteur du jeu agit (s’il dispose d’un ordre).
D’une manière pratique, ce X seconde est déterminé par le timer de ApplicationHandler. Vous vous efforcerez d’adapter les valeurs du jeu pour que le jeu ait l’air fluide.
De la même manière, il est fait mention de cases dans le sujet. Ces «cases» sont à comprendre de la même manière dans le cadre d’un jeu en pseudo-temps réel : des cases suffisamment petites pour que les déplacements aient l’air continu. Donc les châteaux et éventuellement les unités occuperont plusieurs cases.
