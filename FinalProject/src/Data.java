
import java.util.*;

public class Data {
	private HashMap<Integer, Double> x;
	private int y;
 
	private int totalRows;
	private int totalColumns;
 	
	public Data(HashMap<Integer, Double> x, int y) {
		this.x = x;
		this.y = y;
		this.totalRows = this.totalRows++;
		this.totalColumns = this.totalColumns++;
 	}
	
	public HashMap<Integer, Double> getX()
	{
		return x;
	}
	
	public int getY()
	{
		return this.y;
	}
	public int getTotalRows()
	{
		return this.totalRows;
	}
	public int getTotalColumns()
	{
		return this.totalColumns;
	}
	
	
}