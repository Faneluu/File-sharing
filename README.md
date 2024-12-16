**Pornire container local de mysql**

Pentru a începe dezvoltarea backend-ului, va trebui să pornim un container local de mysql și minIO care va "imita" viitoarea bază de date și object storage-ul din infrastructura aplicației.

1. Asgurați-vă că aveți instalat docker și docker-compose.
2. Navigați către folderul proiectului.
3. Rulați următoarea comandă:
```
sudo docker-compose -f database.yaml up -d
```
4. Pentru a opri deployment-ul de mysql și minIO, rulați urmatoarea comandă (recomand utilizarea comenzii de fiecare dată când terminați de lucrat la proiect):
```
sudo docker-compose -f database.yaml down
```
