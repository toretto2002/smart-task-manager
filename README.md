Smart Task Manager

Descrizione Generale

"Smart Task Manager" è un'applicazione console sviluppata in Java 17 che consente di creare, gestire e organizzare task e sotto-task, con priorità e scadenze, sfruttando un'architettura a oggetti modulare ed estendibile. Il progetto è stato realizzato come prova finale per il corso di Programmazione a Oggetti, rispettando integralmente le specifiche descritte nella documentazione fornita dal docente.

Obiettivi Raggiunti

Implementazione di tutti i design pattern obbligatori richiesti dal progetto.

Realizzazione di componenti avanzati opzionali per massimizzare il punteggio (Stream API, Observer, Multithreading, Template Method).

Test unitari automatici per verificare il corretto funzionamento della logica core.

Gestione robusta degli input tramite validazioni e gestione degli errori.

Architettura del Progetto

src/
├── controller/
│ └── TaskManager.java # Singleton - logica principale
├── model/
│ ├── Task.java # Classe base con Composite
│ ├── SubTask.java # Estende Task
│ ├── Priority.java # Enum priorità
│ ├── TaskBuilder.java # Builder pattern
│ └── TaskFactory.java # Factory pattern
├── iterator/
│ └── TaskIterator.java # Filtro personalizzato con Predicate
├── observer/
│ ├── TaskObserver.java # Interfaccia observer
│ └── ConsoleNotifier.java # Notificatore da console
├── util/
│ ├── IOHandler.java # Gestione lettura/scrittura su file
│ ├── Validator.java # Sanitizzazione input
│ ├── LoggerUtil.java # Logging applicativo
│ ├── ConfigLoader.java # Caricamento config.properties
│ ├── ReflectionHelper.java # Modifica campi privati via reflection
│ └── ReminderService.java # Multithreading: reminder automatico
├── util/export/
│ ├── TaskExporter.java # Template Method Pattern (abstract)
│ ├── TxtExporter.java # Esportazione formato testuale
│ └── CsvExporter.java # Esportazione CSV
├── test/
│ └── TaskManagerTest.java # Test unitari con JUnit 5
└── Main.java # Entry point e interfaccia testuale

Design Pattern Utilizzati

Pattern

File / Classe

Descrizione Sintetica

Singleton

TaskManager.java

Accesso centralizzato alla logica di business

Factory

TaskFactory.java

Incapsula la creazione di Task/SubTask

Builder

TaskBuilder.java

Costruzione incrementale di oggetti Task

Composite

Task + SubTask

Gestione gerarchica tra Task e sotto-task

Iterator

TaskIterator.java

Iterazione filtrata tramite Predicate

Observer

TaskObserver + ConsoleNotifier

Notifiche automatiche su completamento Task

Template Method

TaskExporter & derivati

Esportazione con struttura fissa ma contenuto variabile

Reflection

ReflectionHelper.java

Modifica ID dei Task a runtime

Funzionalità Implementate

Creazione di task e sotto-task

Assegnazione priorità e scadenza

Marcatura come completato (anche condizionata su subtasks)

Filtraggio: task urgenti, completati, per ID

Salvataggio/caricamento su file custom

Reminder automatico con thread separato

Notifiche in tempo reale al completamento

Esportazione in TXT e CSV

Validazione degli input utente

Comandi Principali dell’Interfaccia Utente

1. Crea nuovo Task

2. Visualizza tutti i Task

3. Visualizza Task urgenti

4. Visualizza Task completati

5. Aggiungi sotto-task

6. Salva su file

7. Carica da file

8. Completa un task o sotto-task

9. Esporta in TXT

10. Esporta in CSV

11. Avvia reminder automatico

12. Ferma reminder automatico

Testing

La classe TaskManagerTest.java contiene test unitari JUnit 5 per:

Aggiunta e rimozione di task

Completamento di task con subtask

Salvataggio/caricamento su file

I test sono eseguibili con:

javac -cp .;junit-platform-console-standalone-1.9.1.jar -d out src/test/TaskManagerTest.java
java -jar junit-platform-console-standalone-1.9.1.jar -cp out --select-class test.TaskManagerTest

Requisiti

Java 17+

File config/config.properties per i percorsi e impostazioni

(opzionale) Libreria JUnit 5 per test locali

Note Finali

Il progetto è stato sviluppato interamente in Visual Studio Code su sistema Windows 10, seguendo una logica modulare e con attenzione particolare alla leggibilità del codice, all'espandibilità futura e alla copertura di tutti i requisiti richiesti dalle slide ufficiali del progetto.

Il codice è stato testato manualmente e automaticamente, e tutte le funzionalità sono state verificate in fase di collaudo.
