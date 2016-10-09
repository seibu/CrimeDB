Allgemein Information
-------------------------------
Kriminaldatenbank ist ein System zur Verwaltung von Kriminalf�llen.
Alle Informationen werden in einer SQLite Datenbank gespeichert.

Das Programm wurde von
	Sebastian Meyer
	Matrikelnr.: ########
	#################
geschrieben.

Anforderungen
-------------------------------
Java 8
Das Programm wurde unter Windows 7/10 getestet.


Benutzung
-------------------------------
Jede Ansicht der Kriminaldatenbank verf�gt �ber zwei Buttons oben rechts.
- Start: ruft die Startansicht auf, so gelangen Sie zur�ck zum Anfang
- Zur�ck: ruft die vorherige Ansicht auf. Es ist nur m�glich zwischen der
          aktuellen und vorherigen Ansicht zu wechseln. Sie gelagen so nicht
          wieder zum Anfang.

Nach dem Ausf�hren befinden Sie sich in der Startansicht.
Hier haben 3 M�glichkeiten
- ER-Modell
 -- Zeigt das zugrundeliegende ER-Modell an
- Entit�ten & Relationen
 -- zeigt eine �bersicht �ber alle Entit�ten und Relationen der Kriminaldatenbank
- Personensuche
 -- Textfeld zur Eingabe einer beliebgen Zeichenfolge
 -- Combobox zur Auswahl der zu suchenden Person
  --- Polizisten
  --- Opfer
  --- Verd�chtiger
 -- Button zum Ausf�hren der Suche
 -- Es werden die Felder Name und Vorname mit der eingegeben Zeichenfolge �berpr�ft

Nach dem Klick auf "Entit�ten & Relationen" befinden Sie sich in der Entit�ts-�bersicht.
Hier k�nnen Sie per Klick auf einen Eintrag der Liste die entsprechende Entit�t/Beziehung
aufrufen.

In der Entit�ts-Ansicht erhalten Sie alle Datens�tze der jeweiligen Entit�t.
Sie haben hier die M�glichkeit Eintr�ge zu
- �ndern
 -- klicken Sie dazu einfach in die jeweilige Zelle Sie erhalten dann ein Textfeld und k�nnen
    den Wert �ndern. Best�tigen Sie ihre Eingabe durch das Dr�cken der "Enter"-Taste. Durch 
    dr�cken der "ESC"-Taste beenden Sie die Bearbeitung ohne den Wert zu �ndern. Nach dr�cken
    der "Enter"-Taste wird der eingebene Wert auf G�ltigkeit �berpr�ft und sofern dies erfolgreich
    war ge�ndert.
- l�schen
 -- w�hlen Sie den zu l�schenden Datensatz einfach per Mausklick aus und dr�cken Sie dann auf den 
    Button "L�schen". Der ausgew�hlte Datensatz wird dann entfernt.
- einf�gen
 -- durch einen Klick auf "Einf�gen" wird ein neuer Dummy-Datensatz erstellt mit allen notwendigen
    Werten. Sie k�nnen diesen dann nach belieben �ndern.
 -- Spezialfall "Indizien": f�r Indizien ist es zus�tzlich m�glich ein Bild auszuw�hlen. Dazu finden
    Sie neben dem Label "Indizien" einen Button "Bild ausw�hlen" dieser �ffnet einen Datei-Dialog. In
    diesem k�nnen Sie ein Bild ausw�hlen. Akzeptierte Dateiformate sind png, jpeg, gif. Nachdem Sie ein
    Bild erfolgreich ausgew�hlt haben wird Ihnen der Dateiname des Bildes neben dem Button angezeigt.
    Dr�cken Sie nun auf den Button "Einf�gen" wird ein neuer Dummy-Datensatz mit dem entsprechenden Bild
    erzeugt und dieses im Verzeichnis "images" des Programms gespeichert. Sollte ein Bild mit gleichen
    Namen bereits vorhanden sein, so wird das einzuf�gende Bild umbenannt.
- anzeigen
 -- durch einen Klick auf den Button "Anzeigen" wird f�r den ausgew�hlten Datensatz die Detailansicht aufgerufen.

Die Detailansicht eines Datensatzes zeigt alle entsprechenden Werte f�r die direkt in Beziehung stehenden
Entit�ten/Beziehungen an.
- F�r Indizien wird hier zus�tzlich das Bild angezeigt
- Sie haben auch hier die M�glichkeit Eintr�ge zu
 -- �ndern
 -- l�schen
 -- bearbeiten
 -- einf�gen
  --- zum Einf�gen m�ssen Sie hier zuerst die gew�nschte Entit�t ausw�hlen, indem Sie einen Klick auf die
      zugeh�rige Tabelle machen.

In der Suchansicht erhalten Sie die Personendaten zu allen gefunden Personen. In dieser Ansicht k�nnen die
Eintr�ge nicht bearbeitet werden. Durch Auswahl eines Datensatzes und bet�tigen des "Anzeige"-Buttons
gelangen Sie in die Detailansicht, in welcher Sie �ndern vornehmen k�nnen.


FAQ
-------------------------------
Q: Das Programm startet nicht.
A: Stellen Sie sicher, dass sie Java 8 korrekt installiert haben und die aktuellste Version verwenden.

Q: Das Bild wurde trotz l�schen des Datensatzes nicht gel�scht.
A: Bilder von Indizen werden genau dann gel�scht, wenn der letzte Datensatz, der dieses Bild nutzt gel�scht wird.

Q: Wie kann ich nachtr�glich ein Bild f�r einen Indiz �ndern?
A: Dies ist ohne Umwege nicht m�glich, da ein Indiz m�glichst immer mit Bild angelegt werden sollte. Als Workaround
   k�nnen Sie folgendes tun: Ein Bild ausw�hlen, ein Indiz einf�gen, den Wert aus Bild des neuen Indiz kopieren und
   bei dem gew�nschten Datensatz einf�gen. Nun k�nnen Sie den "Dummy" wieder entfernen und das Bild wurde im System
   hinterlegt und mit dem gew�nschten Datensatz verkn�pft.