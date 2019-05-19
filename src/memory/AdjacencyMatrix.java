package memory;

public class AdjacencyMatrix

{

	private final int adress;
	private int[][] m;

	public AdjacencyMatrix() {
		adress = 16;
		m = new int[adress][adress];
		for (int i = 0; i < adress; i++) {
			for (int j = 0; j < 16; j++)
				m[i][j] = 255;
		}
	}

	public void makeLocation(int to, int from, int value) {
		try {
			m[to][from] = value;
		} catch (ArrayIndexOutOfBoundsException index) {
			System.out.println("The vertices does not exists");
		}

	}

	public int getLocation(int to, int from) {
		try {
			return m[to][from];
		} catch (ArrayIndexOutOfBoundsException index) {

		}
		return -1;
	}

	public void print() {
		System.out.println("The Memory contents is : ");
		System.out.print("      ");

		for (int i = 0; i < 16; i++)
			System.out.print(Integer.toHexString(i).toUpperCase() + "   ");
		System.out.println();

		for (int i = 0; i < 16; i++) {
			if (i == 0)
				System.out.print("000" + Integer.toHexString(16 * i).toUpperCase() + " ");
			else {
				System.out.print("00" + Integer.toHexString(16 * i).toUpperCase() + " ");
			}
			for (int j = 0; j < 16; j++)
				System.out.print(" " + Integer.toHexString(getLocation(i, j)).toUpperCase() + " ");
			System.out.println();
		}

	}
}