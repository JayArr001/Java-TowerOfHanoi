//Disk class which functions similar to a POJO but contains code that is not acceptable for a POJO
//implementing Comparable so that disk size can be compared easily in the game logic
public class Disk implements Comparable<Disk>
{
	private int diskSize;

	//there is some logic in the main recursive function that requires knowing what is the last peg
	//this logic guarantees a solve in the fewest moves possible
	private Peg lastPeg;
	private Peg currentPeg;

	public Disk(int newSize, Peg peg)
	{
		lastPeg = peg;
		diskSize = newSize;
	}

	//getters and setters
	public void setLastPeg(Peg p) {lastPeg = p;}
	public void setCurrentPeg(Peg p) {currentPeg = p;}

	public int getDiskSize() {return diskSize;}
	public Peg getLastPeg() {return lastPeg;}
	public Peg getCurrentPeg() {return currentPeg;}

	@Override
	public String toString()
	{
		return "diskSize: " + diskSize;
	}

	//this override just gets the disk size and compares
	//it is not strictly required
	//but having this override saves visual space in the recursive body,
	//makes the code more intuitive and easy to read
	@Override
	public int compareTo(Disk d)
	{
		int returnValue = 0;
		if(this.getDiskSize() > d.getDiskSize())
		{
			returnValue = 1;
		}
		else if(this.getDiskSize() < d.getDiskSize())
		{
			returnValue = -1;
		}
		return returnValue;
	}
}
