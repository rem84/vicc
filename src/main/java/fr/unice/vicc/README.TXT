FONCTIONNEMENT DES DIFFERENTS SCHEDULERS

Naive scheduler:
---------------

Ce scheduler place la VM sur le premier host rencontré.
- La méthode getHost(Vm) retourne l'hôte qui contient la Vm passée en paramètre.
- Host.vmCreate(Vm) sert à créer une VM sur un host ; elle retourne True si l'opération peut être réalisée, et false sinon.
- La méthode getHostList retourne la liste des hôtes dans le datacenter.
- Host.vmDestroy() supprime la VM sur le hôte en question.
- allocateHostForVm(Vm) alloue une VM à un hôte.

Anti affinity:
-------------

Ce scheduler regarde pour la VM à créer la plage à laquelle elle appartient ; ensuite, il parcourt les hôtes jusqu'à trouver un qui ne contient pas une VM appatenant à cette même plage.

L'observer vérifie toutes les secondes que deux VM appatrenant à la même plage ne sont pas sur le même hôte. Pour celà, une liste des plages qui sont prises est enregistrée sur chaque hôte, et une vérification est effectuée par rapport à la VM à créer pour chaque hôte.

Load Balancer:
-------------

Ce scheduler cherche l'hôte qui possède le plus de ressources disponibles pour mettre la VM en question dessus.

L'observer garde en mémoire deux valeurs : l'hôte avec le plus de ressources disponibles, et celui avec le moins de ressources. Il affiche à la fin le quotient du taux de charge minimal par le taux de charge maximal.

No Violation:
------------

Ce scheduler vérifie si le pic de demandes de la VM à allouer n'est pas supérieur à ses ressources disponibles, afin d'éviter les pénalités.

