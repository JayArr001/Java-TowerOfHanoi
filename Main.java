import java.util.*;

//A basic application that solves the toy-game Tower of Hanoi
//(aka The problem of Benares Temple or Tower of Brahma or Lucas' Tower and sometimes pluralized as Towers, or simply pyramid puzzle)
//this program will use the recursion to solve in the fewest moves
//while there exists a methodology to solve without using recursion, it will not appear in this application
public class Main
{
	private static int moveCount = 0;

	//this will be calculated later, for right now it's just a big number
	private static int maxMoves = Integer.MAX_VALUE;
	private static int gameCurrentDiskCount = 0;

	public static void main(String[] args)
	{
		//the game typically has 3 disks, but we can plug in larger numbers and still solve correctly
		int disks = 3;

		//starts a game with disks on a single lastPeg, using recursion
		startGame(disks);
	}

	//method that will be called recursively and contains the bulk of logic
	private static void moveRecursion(Peg[] pegs)
	{
		//printing info about the pegs and movecount before executing a move
		System.out.println("===");
		System.out.println("start of moveRecursion() peg states:");

		//tower of hanoi has exactly 3 pegs
		if(pegs.length != 3)
		{
			System.out.println("invalid number of pegs");
			return;
		}

		//creating pointers with easily reference-able names to work with
		//this does not change anything, we can still use pegs[0] to refer to the first peg in the array
		Peg A = pegs[0];
		Peg B = pegs[1];
		Peg C = pegs[2];
		System.out.println("A: " + A.stackToString());
		System.out.println("B: " + B.stackToString());
		System.out.println("C: " + C.stackToString());

		//if the pegs are not valid, stop recursion
		//we call the stop here so we can print out the most recent game state for inspection
		if(!validateDisksPegs(pegs))
		{
			System.out.println("invalid stacks on pegs");
			return;
		}

		//the maximum amount of moves is 2^n - 1, where n = number of disks
		maxMoves = (int) Math.pow(2.0, (double)gameCurrentDiskCount) - 1;
		if(moveCount >= maxMoves)
		{
			System.out.println("maximum moves reached: " + maxMoves + ". terminating");
			return;
		}

		//in order to solve in the fewest steps, we need to track the smallest disk's last peg
		//without this logic, this will still solve by following the logic but will take more moves

		//in descending order of what to attempt
		//if a step isn't legal at this move, skip it and go to the next step
		//only call recursively at the end, after we attempt all steps
		//first step: move the second smallest disk and try to move it to a legal peg
		//second: find the smallest disk, move it to a legal peg that it wasn't on last
		//recursively call

		//identify pegs and if we have smallest/second smallest available
		Peg smallest = null;
		Peg secondSmallest = null;

		//initialize arraylist with 3 slots because that's typical for the game
		//java will automatically allocate more memory if we need to make the list bigger
		List<Disk> compareDisks = new ArrayList<Disk>(3);

		//loop through all the pegs
		for(Peg p : pegs)
		{
			//the stack that is on the peg we are currently inspecting in the loop
			var stack = p.getStack();

			//if the peg has disks on it
			if(stack.size() > 0)
			{
				//add the top disk to a list that will be sorted
				Disk d = stack.peek();
				d.setCurrentPeg(p);
				compareDisks.add(d);
			}

			//if the peg is empty, we don't have any actions to take
		}

		//sort the list so we can pick the first 2 objects
		Collections.sort(compareDisks);
		smallest = compareDisks.get(0).getCurrentPeg();

		//if the list of disks we are comparing has more than 1 unit
		//it means we have a smaller/bigger (2 disks)
		//this seems obvious to point out (identity property) but is critical to decision-making
		if(compareDisks.size() > 1)
		{
			secondSmallest = compareDisks.get(1).getCurrentPeg();
		}

		//sometimes we will not have a second smallest (see the above conditional and explanation)
		//for this code to execute, it means we have 2 disks (smaller and larger)
		if(secondSmallest != null)
		{
			//pop the top disk of the peg
			Disk d = secondSmallest.getStack().pop();
			d.setLastPeg(secondSmallest); //tell the disk to remember the last peg it was on as second smallest
			moveCount++;
			//remove the current peg as a possible legal move for logic
			List<Peg> pegList = new ArrayList<>(List.of(pegs));
			pegList.remove(secondSmallest);

			//now we don't need any additional logic, just find the first legal move
			for(Peg p : pegList)
			{
				var stack = p.getStack(); //stack of disks on the peg currently inspected
				if(stack.size() > 0)
				{
					//if the peg we're looking at has a top disk with bigger size than our popped disk d
					//it is a legal move, we can just move it on there and end our search
					if(stack.peek().getDiskSize() > d.getDiskSize())
					{
						System.out.println("MOVE " + moveCount + ": moving disk " + d.getDiskSize() + " from "
								+ secondSmallest.toString() + " -> " + p.toString());
						stack.add(d);
						break;
					}
				}
				//if the peg is empty, it's also a legal move
				else
				{
					System.out.println("MOVE " + moveCount + ": moving disk " + d.getDiskSize() + " from "
							+ secondSmallest.toString() + " -> " + p.toString());
					stack.add(d);
					break;
				}
			}
		}

		//for the smallest disk, we will need some extra logic to solve in the fewest moves possible
		//we want to avoid moving the smallest disk back to a peg it was on for the previous move
		//without this check, we might be bouncing the top disk around back and forth,
		//theoretically recurring forever without solving (until the move cap)

		//pop the top disk of the peg
		Disk d = smallest.getStack().pop();
		moveCount++;
		//remove the current peg as a possible legal move for logic
		List<Peg> pegList = new ArrayList<>(List.of(pegs));
		pegList.remove(smallest);

		//this is the logic that lets us solve in the fewest moves possible
		//we also don't need a loop to search for a legal move since this leaves only one move left
		pegList.remove(d.getLastPeg());

		Peg p = pegList.get(0);
		var stack = p.getStack();
		System.out.println("MOVE " + moveCount + ": moving disk " + d.getDiskSize() + " from "
				+ smallest.toString() + " -> " + p.toString());
		stack.add(d);
		d.setLastPeg(smallest);

		//now check if the game is solved
		List<Peg> pegsWithDisks = new ArrayList<>(List.of(pegs));
		for(Peg p2 : pegs)
		{
			//if the stack is empty
			if(p2.getCopy().isEmpty())
			{
				//take it off the list, this peg has no disks
				pegsWithDisks.remove(p2);
			}
		}
		//if there is exactly one peg with all the disks, we the game is solved
		//there are many approaches to check if we win, this is one of many
		if(pegsWithDisks.size() == 1)
		{
			System.out.println("Solved in: " +moveCount+ " moves. Final state: ");
			System.out.println("A: " + A.stackToString());
			System.out.println("B: " + B.stackToString());
			System.out.println("C: " + C.stackToString());
			return;
		}

		//if we made it this far in the method, it's time to call itself again
		moveRecursion(pegs);

	}

	//checking if there are duplicate disk sizes and if the disks are stacked in a valid order on each peg
	private static boolean validateDisksPegs(Peg[] pegs)
	{
		//using a variable so the method doesn't have to return immediately on finding an invalid condition
		//can check both conditions without having to run 2 separate times if both conditions fail
		boolean validGame = true;
		if(pegs.length < 3)
		{
			System.out.println("not enough pegs");
			return false;
		}
		//using a hashset to check for duplicate disk sizes
		HashSet<Integer> compiledDisks = new HashSet<>();

		//searching all pegs
		for(Peg p : pegs)
		{
			Stack<Disk> stackCheck = p.getCopy();
			if(stackCheck.size() > 0)
			{
				List<Integer> unsortedStack = new ArrayList<Integer>();
				List<Integer> sortedStack = new ArrayList<Integer>();
				while(!stackCheck.isEmpty())
				{
					Disk d = stackCheck.pop();
					unsortedStack.add(d.getDiskSize());
					sortedStack.add(d.getDiskSize());

					//hashset's add() will return false if the hashset already has the disk size
					if(!compiledDisks.add(d.getDiskSize()))
					{
						System.out.println("attempted to add duplicate size " + d.getDiskSize());
						validGame = false;
					}

				}
				Collections.sort(sortedStack);
				Collections.reverse(sortedStack);
				Collections.reverse(unsortedStack);

				if(!sortedStack.equals(unsortedStack))
				{
					System.out.println("a given peg has an invalid starting order");
					validGame = false;
				}
			}
		}

		if(!validGame)
		{
			System.out.println("game was found to be invalid. not solving");
			return false;
		}
		gameCurrentDiskCount = compiledDisks.size();
		return true;
	}

	private static void startGame(int startingDisks)
	{
		System.out.println("----------------------------");
		System.out.println("starting a new game with all disks on one peg");
		Peg A = new Peg();
		A.addStartingDisks(startingDisks);
		Peg B = new Peg();
		Peg C = new Peg();
		System.out.println("A: " + A.toString() + " | B: " + B.toString() + " | C: " + C.toString());
		System.out.println("pregame checks: ");
		System.out.println("number = diskSize of disk");
		System.out.println("<--- bottom of the stack, top of stack --->");

		Peg[] pegs = {A, B, C};
		//we validate if it's a legal game at the start of each recursion
		//in case we make a mistake or another disk somehow gets added between moves
		moveRecursion(pegs);

	}
}
