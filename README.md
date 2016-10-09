Allgemein Information
-------------------------------
Kriminaldatenbank ist ein System zur Verwaltung von Kriminalfällen.
Alle Informationen werden in einer SQLite Datenbank gespeichert.

Das Programm wurde von
	Sebastian Meyer
	Matrikelnr.: ############
	########################
geschrieben.

Anforderungen
-------------------------------
Java 8
Das Programm wurde unter Windows 7/10 getestet.


Benutzung
-------------------------------
Jede Ansicht der Kriminaldatenbank verfügt über zwei Buttons oben rechts.
- Start: ruft die Startansicht auf, so gelangen Sie zurück zum Anfang
- Zurück: ruft die vorherige Ansicht auf. Es ist nur möglich zwischen der
          aktuellen und vorherigen Ansicht zu wechseln. Sie gelagen so nicht
          wieder zum Anfang.

Nach dem Ausführen befinden Sie sich in der Startansicht.
Hier haben 3 Möglichkeiten
- ER-Modell
	- Zeigt das zugrundeliegende ER-Modell an
- Entitäten & Relationen
	- zeigt eine Übersicht über alle Entitäten und Relationen der Kriminaldatenbank
- Personensuche
	- Textfeld zur Eingabe einer beliebgen Zeichenfolge
	- Combobox zur Auswahl der zu suchenden Person
		- Polizisten
		- Opfer
		- Verdächtiger
	- Button zum Ausführen der Suche
	- Es werden die Felder Name und Vorname mit der eingegeben Zeichenfolge überprüft

Nach dem Klick auf "Entitäten & Relationen" befinden Sie sich in der Entitäts-Übersicht.
Hier können Sie per Klick auf einen Eintrag der Liste die entsprechende Entität/Beziehung
aufrufen.

In der Entitäts-Ansicht erhalten Sie alle Datensätze der jeweiligen Entität.
Sie haben hier die Möglichkeit Einträge zu
- ändern
	- klicken Sie dazu einfach in die jeweilige Zelle Sie erhalten dann ein Textfeld und können
    den Wert ändern. Bestätigen Sie ihre Eingabe durch das Drücken der "Enter"-Taste. Durch 
    drücken der "ESC"-Taste beenden Sie die Bearbeitung ohne den Wert zu ändern. Nach drücken
    der "Enter"-Taste wird der eingebene Wert auf Gültigkeit überprüft und sofern dies erfolgreich
    war geändert.
- löschen
	- wählen Sie den zu löschenden Datensatz einfach per Mausklick aus und drücken Sie dann auf den 
    Button "Löschen". Der ausgewählte Datensatz wird dann entfernt.
- einfügen
	- durch einen Klick auf "Einfügen" wird ein neuer Dummy-Datensatz erstellt mit allen notwendigen
    Werten. Sie können diesen dann nach belieben ändern.
	- Spezialfall "Indizien": für Indizien ist es zusätzlich möglich ein Bild auszuwählen. Dazu finden
    Sie neben dem Label "Indizien" einen Button "Bild auswählen" dieser öffnet einen Datei-Dialog. In
    diesem können Sie ein Bild auswählen. Akzeptierte Dateiformate sind png, jpeg, gif. Nachdem Sie ein
    Bild erfolgreich ausgewählt haben wird Ihnen der Dateiname des Bildes neben dem Button angezeigt.
    Drücken Sie nun auf den Button "Einfügen" wird ein neuer Dummy-Datensatz mit dem entsprechenden Bild
    erzeugt und dieses im Verzeichnis "images" des Programms gespeichert. Sollte ein Bild mit gleichen
    Namen bereits vorhanden sein, so wird das einzufügende Bild umbenannt.
- anzeigen
	- durch einen Klick auf den Button "Anzeigen" wird für den ausgewählten Datensatz die Detailansicht aufgerufen.

Die Detailansicht eines Datensatzes zeigt alle entsprechenden Werte für die direkt in Beziehung stehenden
Entitäten/Beziehungen an.
- Für Indizien wird hier zusätzlich das Bild angezeigt
- Sie haben auch hier die Möglichkeit Einträge zu
	- ändern
	- löschen
	- bearbeiten
	- einfügen
		- zum Einfügen müssen Sie hier zuerst die gewünschte Entität auswählen, indem Sie einen Klick auf die
      zugehörige Tabelle machen.

In der Suchansicht erhalten Sie die Personendaten zu allen gefunden Personen. In dieser Ansicht können die
Einträge nicht bearbeitet werden. Durch Auswahl eines Datensatzes und betätigen des "Anzeige"-Buttons
gelangen Sie in die Detailansicht, in welcher Sie ändern vornehmen können.


FAQ
-------------------------------
Q: Das Programm startet nicht.
A: Stellen Sie sicher, dass sie Java 8 korrekt installiert haben und die aktuellste Version verwenden.

Q: Das Bild wurde trotz löschen des Datensatzes nicht gelöscht.
A: Bilder von Indizen werden genau dann gelöscht, wenn der letzte Datensatz, der dieses Bild nutzt gelöscht wird.

Q: Wie kann ich nachträglich ein Bild für einen Indiz ändern?
A: Dies ist ohne Umwege nicht möglich, da ein Indiz möglichst immer mit Bild angelegt werden sollte. Als Workaround
   können Sie folgendes tun: Ein Bild auswählen, ein Indiz einfügen, den Wert aus Bild des neuen Indiz kopieren und
   bei dem gewünschten Datensatz einfügen. Nun können Sie den "Dummy" wieder entfernen und das Bild wurde im System
   hinterlegt und mit dem gewünschten Datensatz verknüpft.
