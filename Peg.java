import java.util.Stack;

//Peg class which functions like a POJO but contains critical functions which a POJO could not accept
//stores information like which disks (in the form of a Stack) and has methods to add disks
//and retrieve data
public class Peg
{
	//each lastPeg will store a Stack collection of disks
	protected Stack<Disk> disks;

	public Peg()
	{
		disks = new Stack<Disk>();
	}

	//initializes a lastPeg with N starting disks, in ascending order
	//this is for a standard game where all disks start on one lastPeg
	public void addStartingDisks(int numberOfDisks)
	{
		//this loop will only run if we pass a number greater than 0 for number of starting disks
		//since disks is a stack, we add in descending order
		for(int i = numberOfDisks; i > 0; i--)
		{
			disks.add(new Disk(i, this));
		}
	}

	//returns a copy of the stack, protecting the original stack
	public Stack<Disk> getCopy()
	{
		Stack<Disk> copy = new Stack<>();
		copy.addAll(disks);
		return copy;
	}

	//returns the mutable stack
	public Stack<Disk> getStack()
	{
		return disks;
	}

	//returns the stack in a string, sorted in ascending order
	public String stackToString()
	{
		StringBuilder sb = new StringBuilder("[");
		var copyStack = getCopy();
		for(int i = 0; i < copyStack.size(); i++)
		{
			sb.append(copyStack.get(i).getDiskSize() + ", ");
		}
		sb.append("]");
		//cleaning up the end of the string, will not do anything if this string isn't found
		//if we don't do this, calling toString() will leave an empty ", " before the ]
		//just makes it prettier to read
		int cleanup = sb.indexOf(", ]");
		if(cleanup > 0)
		{
			sb.replace(cleanup, (cleanup + 2), "");
		}
		return sb.toString();
	}



}
