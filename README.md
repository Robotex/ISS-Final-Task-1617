# ISS-Final-Task-1617

OBIETTIVI:
1) Se un utente non imposta la distanza, prendere quella dello spawn di default.
(fare plan che faccia ciò)
2) cambiare il nome di stage0 in OnSonarLine
3) Fare in modo che un utente possa inserire più volte la startingDistance (prima dello start)
4) Rimuovere il "passo indietro" quando viene rilevato un dynamicObstacle
5) cambiare il nome di stage1 in GoToSonar
6) quando il robot arriva al sonar 2, deve tornare alla startingDistance iniziale

Domande e risposte per il progetto

Requisiti

Functional Requirements: STEP 1
Design and build a (prototype of a) software system that:

    starts the robot (already put in A) when the user sends to it a start command by using a remote console;
    drives the robot along a straight path from A to B if no onstacle is detected;
    avoids the mobile obstacle by waiting (when detected) that the obstacle disappears from the robot's front;
    avoids the fixed obstacles by finding some alternative path to reach its goal;
    stops the robot if an alarm event occurs.
   
Functional Requirements: STEP 2 (optional)
After completion of the first step, extend the system built at step1 so that a human user can take the control at any time after robot starting. In this case:

    the robot must stop its journey and execute movement commands sent by the user via the console. Obviously the robot should avoid to hit obstacles;
    among these commands, the user can tell the robot to return in autonomous way to its starting point along the same path already covered, by avoiding to hit (mobile) obstacles.
 
    
Ragionamento:
Secondo i requisiti funzionali dello step1 il robot si muove nei seguenti stage che compongono la sua 'journey':

stage0 - creazione) la console si connette ad unity e nello stesso momento viene creato un rover comandabile da remoto

stage1 - posizionamento sulla linea di partenza) questo è lo stage pre-start; il robot si posiziona sulla linea di partenza e alla distanza da sonar1 ('da') impostata dall'utente finale

stage2 - start ) il robot si muove dalla linea di rilevamento sonar1 alla linea di rilevamento sonar2

stage3 - approccio alla fine) il robot raggiunga la linea di rilevamento sonar2 si avvicina o allontana da sonar2 in base alla distanza iniziale da sonar1


	Argomento: Orientamento del rover
	
	Domanda: è più comodo lavorare sui comandi di base del rover o sui comandi di base costruire un sistema di orientamento che facilita il movimento del rover?

Teoria:
Il rover ha 5 primitive di movimento:
onward => traslazione frontale del rover,
left => rotazione di 90 gradi a sinistra,
right=> rotazione di 90 gradi a destra,
backwards=> retromarcia,
stop => arresto 

Ipotesi:
ci si domanda se sia più conveniente lavorare con i comandi di base o costruire un sistema di orientamento più complesso per gestire poi il rover più facilmente

Il sistema di base ha il vantaggio di essere semplice da usare ed è possibile usare le primitive di movimento all'interno di ogni plan.
Di contro il rover deve ricordare costantemente il suo orientamento 

Il sistema complesso consiste nel comporre due funzioni in più 'moveLeft' 'moveRight' composte da due o più funzioni semplici:
moveLeft:{left >> forward}
moveRight:{right >> forward}
Poichè nei requisiti funzionali non viene specificato che il rover deve rimanere in uno specifico orientamento è possibile far in modo che il rover,
dopo un moveLeft | moveRight effettui una rotazione opposta alla prima difatto rimuovendo il problema della direzione di movimento(e il ricordo dell'orientamento).
	


	Argomento: The starting point A is set by the final users at any distance 0<da<dm.
	Domanda: In che modo l'utente sceglie la distanza di partenza 'da'?
	
Bisogna valutare le abilità e conoscenze dell'utente nei confronti del sistema.
	
CASO A - L'utente fa fatica ad usare numeri o comandi; è giusto che si regoli sulla distanza 'da' ad occhio.L'utente muove quindi il robot lungo la linea di rilevamento del sonar1 con i tasti left and right della gui predisposta.

CASO B - L'utente conosce il comando da inserire alla riga di comando -> connectToUnity("IP").

CASO C - L'utente conosce il comando e lo inserisce ma preferisce assestare la distanza ad occhio

Soluzione
CASO A - Il robot si muove lungo la linea di rilevamento del sonar1 indipendentemente dalla distanza rilevata dai sonar. Nel momento in cui 		si preme il tasto start (per rispettare il requisito 2 e 4 dello step1), il rover deve prima valutare la distanza tra esso e il sonar1 per 	poi tornare sui "suoi passi" e muoversi fino alla linea di rilevamento sonar2 e distanza imposta con sonar1.

CASO B - Il robot, una volta creato, si deve mettere prima sulla linea di rilevamento di sonar1 e, impostata la distanza tramite riga di 		comando, deve avvicinarsi o allontanarsi da sonar1 fino alla distanza prefissata, sempre rimanendo sulla linea di rilevamento di sonar1. A 	questo punto si può procedere con la pressione del tasto start. 

CASO C - Il robot, una volta creato segue il CASO B...e poi il CASO A