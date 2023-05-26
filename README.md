# 343KnapsackTempHeuristic

What did we make different from the original main function and why did we make changes in the main function?

In this version, you have a main method that creates an initial solution (in the form of an ArrayList of integers), creates an instance of Simulated Annealing, and runs it. The final solution is then printed. If we need to compare our main function with the original version, two parallel arrays containing the values and weights of the items were used in the original one. Now, we have created an Item object which contains the values and weights of each item. These are kept together in this single object. Instead of initializing the first solution as part of the classes, we created it inside the main method. This makes it clearer that it's part of the setup for the algorithm. Similarly, we created the ObjectiveSolution class where we keep the solution and its objective value together in a single object to make the code more understandable and applicable. The Simulated Annealing algorithm is now encapsulated in its class. This separates the concerns of running the algorithm from the setup and final printing of the result, making the code easier to understand and maintain. Also, in the new main function, unlike the original, we are reading from the CSV file and writing it there. It reduces the possibility of breaking its structure when testing the code because the data resides in a different place.
We've opened a class called ObjectiveSolution and we're using it to keep track of both the best solution found here and the current solution. In the original version, these were not kept together, but we believe that keeping these variables together would make the code easier to follow and more organized. It would also make the sequences and all objective values more accessible. The temperature schedule for the annealing process is the same as in the original version: it starts at an initial high temperature and cools down at a fixed rate until it reaches a stop temperature. The way we generate the neighbor of the solution is slightly different. We randomly choose to either add an item to the knapsack, remove an item from it, or swap an item in the knapsack with one outside it. This is a more flexible approach than the one in the original version, which only flipped the state of a random item. We use file I/O operations to be able to record the current objective value at each step and observe how the objective value changes over the course of the algorithm.
Overall, the changes made in our version have resulted in a more flexible, modular, and maintainable code. The use of objects to group related data together and encapsulating the simulated annealing algorithm in its own class makes the code easier to understand and work with. Adding file I/O operations allows us to work with larger, more complex problems and keep a record of the algorithm's progress.

Parts of the Code:

Item.java: It defines the Item class. An Item has a name, a value, and a weight. The class also includes a method for reading a list of items from a CSV file.
Items.csv: The CSV file that contains the list of items. Each line represents an item with a name, value, and weight.
ObjectiveSolution.java: It defines the ObjectiveSolution class. An ObjectiveSolution represents a solution to the problem, which is a list of binary integers indicating which items to include in the knapsack and its objective value.
Main.java: It contains the main function that drives the program. It reads the list of items from the CSV file, initializes the initial and best solutions, creates an instance of SimulatedAnnealing, and runs the algorithm. At the end, it prints out the best solution, the selected items with IDs, values, and weights, and the total execution time.
SimulatedAnnealing.java: This class implements the simulated annealing algorithm.
•	SimulatedAnnealing(List<Item> items, int knapsackCapacity, String outputFile): The constructor method for the SimulatedAnnealing class. It initializes various variables which are items, knapsackCpacity, outputFile, and generates an initial feasible solution using a greedy heuristic. Our greedy heuristic approach is to generate a list of indices sorted by value-to-weight ratio. Then, we add the items with the highest value per weight to the knapsack and while doing this, we also check the capacity of the knapsack. We stop adding items when it exceeds the capacity. At the end of the method, it initializes a BufferedWriter to write the current objective value to a file in each iteration. 
•	run(): This is where the simulated annealing algorithm runs. It starts at an initial temperature and decreases it in each iteration until it reaches the stop temperature. In each iteration, it generates a neighbor solution, decides whether to move to the neighbor based on its value and the current temperature and writes the current objective value to a file. In this way, we have the opportunity to observe how the objective value changes. Moreover, we can visually examine the rise and fall in the objective value over time by pasting this data in CSV file into an Excel file and inserting a graph. We can also reach the global min/global max values by looking at this chart.
•	generateNeighbor(): This method generates a neighbor solution by randomly choosing to add an item, remove an item, or swap an item, as long as it does not violate the knapsack's weight limit. To add diversity in the search space, the acceptance of the removal of items is linked with the current solution's value. Also, we created a free variable through trial and error and we decided that it should be 0.965 because it provided more logical graphs for our purposes and problem. How it works is that if the current solution is lower than the best-found solution*0.965, then the removal of items is prevented until it goes back to the expected domain.
•	calculateValue() and calculateWeight(): These methods calculate the total value and total weight of a solution, respectively.
•	calculateAcceptanceProbability(): This method calculates the probability of accepting a worse solution in simulated annealing. It's higher when the temperature is high and decreases as the temperature cools down. This allows the algorithm to explore the solution space widely in the beginning and then converge to a good solution.

With the given dataset when we run the algorithm several times with set parameters, we get the following results:

On average: 1642 as value

Max value: 1655
ID: Item 2, Value: 64, Weight: 11
ID: Item 4, Value: 55, Weight: 10
ID: Item 5, Value: 72, Weight: 14
ID: Item 7, Value: 81, Weight: 12
ID: Item 9, Value: 72, Weight: 17
ID: Item 10, Value: 80, Weight: 13
ID: Item 11, Value: 62, Weight: 11
ID: Item 15, Value: 68, Weight: 16
ID: Item 16, Value: 51, Weight: 10
ID: Item 18, Value: 68, Weight: 10
ID: Item 19, Value: 83, Weight: 16
ID: Item 26, Value: 71, Weight: 13
ID: Item 27, Value: 82, Weight: 17
ID: Item 30, Value: 63, Weight: 13
ID: Item 32, Value: 75, Weight: 11
ID: Item 34, Value: 76, Weight: 10
ID: Item 36, Value: 60, Weight: 12
ID: Item 37, Value: 75, Weight: 12
ID: Item 38, Value: 68, Weight: 14
ID: Item 41, Value: 71, Weight: 11
ID: Item 42, Value: 58, Weight: 13
ID: Item 44, Value: 72, Weight: 14
ID: Item 46, Value: 78, Weight: 10
ID: Item 48, Value: 50, Weight: 10

Min value: 1623

Questions:

1)	How does the execution time change when cooling rate increased? Report it by running the same algorithm for the given dataset.

We ran the algorithm lots of times but recorded three of them with the cooling rate equal to 0.03. Our execution time results: 54ms on the first try, 67ms on the second try, and 64ms on the third try. On average, we can say that the execution time is around 61ms while the cooling rate is equal to 0.03. If we set the cooling rate to 0.2, the execution time equaled 44ms and it made a difference. If we increase the cooling rate further and equalize it to 0.5, the execution time became 35ms. As a result, we can say that as the cooling rate increases, the execution time decreases and there is a negative relationship between them. Since it cools faster, it will reach the stopping point in a lesser iteration. That is why, less iteration in run results in less time spent.

2)	How does the execution time and solution quality change when the difference between starting temperature and stopping temperature is increased? Report it by running the same algorithm for the given datasets.

From a logical perspective, we expect execution time to increase when the difference between the starting temperature and the final temperature increases, as there will be more iterations. To support this with data, we did a few tries with changing the stopping temperature. With all other variables held constant, we first set the stopping temperature to 30 and the execution time was 53ms. Then we lowered the stopping temperature to 1 in order to increase the difference between the starting temperature and the execution time was 63ms. Finally, we set the stopping temperature to 0.1 and the execution time was 71ms. Here, we can see concretely that when the difference between starting temperature and stopping temperature, execution time goes up. In addition, since the algorithm will make more iterations to reach the stopping temperature we have reduced, the probability of discovering new values increases and this improves our solution quality.

