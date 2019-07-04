# Einkaufsliste

## Beschreibung
Diese App zeigt Läden in einer Spalte, Waren des jeweiligen Ladens in einer zweiten Spalte.

## Funktion
So sieht die App jetzt aus.
Wenn man einen Laden auswählt, werden dessen Waren angezeigt.
![Ein Laden und seine Waren](Readme Unterstützung/Laden und seine Waren.jpg)
Man kann die RecyclerView mit dem Finger wischen, um Tasten zu enthüllen.
![Ware wischen und Hintergrund enthüllen](Readme Unterstützung/Ware wischen.jpg)
Ein Dialogfenster öffnet sich, wo man die Ware ändern kann.
![Eine Ware verarbeiten](Readme Unterstützung/Ware bearbeiten.jpg)

## Anwendung
Einige Merkmale lassen sich zu anderen Zwecken verwenden.
    Fragmente für Übersicht und Einzelheiten (master/detail) kommunizieren über ein ViewModel.
    Die RecyclerView enthält eine zwei-schichtige View, die man zur Seite wischen kann, um Tasten auf dem Hintergrund zu betätigen. 
    Die üblichen SQLite Datenbankoperationen werden hier auf Kotlin dargestellt. Die Verbindung zur Datenbank wird
    innerhalb von jeder Funktion auf- und zugeschlossen, um Zusammenstöße zu vermeiden.

## Beiträge
Vorerst bitte ich um Ihre Geduld. Die Hochladung des ersten Entwurfs wird einige Wochen dauern. Anregungen zur Verbesserung 
des Codes werden jederzeit begrüsst. Von fortgeschrittenen Features darf man natürlich träumen, und diese füge ich gerne 
zu den [Issues](https://github.com/Galajda/Einkaufsliste/issues?q=is%3Aopen+is%3Aissue+label%3Aenhancement) mit dem Untertitel "enhancements" hinzu.


## Danke für Hilfe
  ### SQLite Datenbank Verbindung
  
  ### RecyclerView
  
  ### Tasten unter RecyclerView
  
## Lizenz
[GPLv3](https://github.com/Galajda/Einkaufsliste/blob/master/LICENSE)

