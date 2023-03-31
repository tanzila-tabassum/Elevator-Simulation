# Elevator-Simulation
# Project Detail
The Project is implemented for the course Modeling and Simulation during my Master's Degree at Queens College, CUNY.

The problem that was provided by the professor is given below -

**Elevator system in an office building**

There are 6 floors, 0,1,..5  in a building and 4 elevators. Most people arrive to the system via the zero floor.. They arrive with an exponential distribution with an average of 10/min. The elevators move from floor to floor in 6 sec. Like most buildings there are two buttons on a floor, one for up and one for down. Once an elevator gets to floor everyone that wants to get off on that floor exit the elevator at 2/sec before people 2/sec get on. Each elevator has buttons for all floors. If the floor button is not pressed for the floor someone entering the elevator wants to go to, they press the appropriate button. People on any floor arrive to take the elevator equilikely to want to go any floor at a rate of 5/min exponentially, except floor zero which is half the time the choice. Elevators will stay at a floor if no one wants in the elevator wants to go in that direction.  A central controller controls all elevators.
# Logic Implementation 
* There are 6 floors according to the problem, assigned as: [ 0, 1, 2, 3, 4, 5]
* The number of elevators are 4 assigned as: [0, 1, 2, 3]
* The simulation is based on generated events stored in the Future Event List.
* Different Event types has been used to go through all the scenarios such as - 
  * New Arriving person
  * Elevator Arrives at Floor
  * People gets off elevator
  * People get on elevator
* Each Event type has their own generated Event Time such as -
  * Use of exponential distribution formula to calculate the rate of newly arriving people is generated.
  * The Elevator takes 6 seconds to move floor to floor.
  * Takes 2 seconds to enter/exit the elevator per person.
* The Future Event List is implemented with a Priority Queue.
* All generated events are stored in the priority queue and sorted by their event times.
  * The first element of the priority queue should be the event with the smallest time.
  * Updates the clock with that event’s time, and pop that event from the queue to simulate that event is done.
  * If the popped event is a “Generate new arriving person” event, it will generate a new arriving person and subsequent events that will be added to the priority queue and sorted again.
# Output
<img width="975" alt="Screenshot 2023-03-30 at 8 29 44 PM" src="https://user-images.githubusercontent.com/25485530/228993650-d47c2479-fca9-48a2-b87e-ab722d2bb530.png">





