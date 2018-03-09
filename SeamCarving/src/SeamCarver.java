import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SeamCarver
{

	private SmC_Picture curr;
	private BufferedImage buff;
	private double[][] engr;

	public SeamCarver(SmC_Picture pictureP)
	{
		if (pictureP == null)
		{
			throw new NullPointerException();
		}

		curr = pictureP;
		engr = new double[curr.width()][curr.height()];
	}

	public SmC_Picture picture()
	{
		return curr;
	}

	public int width()
	{
		return curr.width();
	}

	public int height()
	{
		return curr.height();
	}

	public double energy(int x, int y)
	{
		if (x < 0 || y < 0 || x >= width() || y >= height())
		{
			throw new IndexOutOfBoundsException();
		}

		if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
		{
			return 1000;
		}

		Color w = curr.get(x, y - 1);
		Color a = curr.get(x - 1, y);
		Color s = curr.get(x, y + 1);
		Color d = curr.get(x + 1, y);

		double vR = w.getRed() - s.getRed();
		double vG = w.getGreen() - s.getGreen();
		double vB = w.getBlue() - s.getBlue();

		double hR = a.getRed() - d.getRed();
		double hG = a.getGreen() - d.getGreen();
		double hB = a.getBlue() - d.getBlue();

		double p1 = (vR * vR) + (vG * vG) + (vB * vB);
		double p2 = (hR * hR) + (hG * hG) + (hB * hB);

		return Math.sqrt(p1 + p2);
	}

	public int[] findHorizontalSeam()
	{
		double[] cost = new double[curr.height()];
		int[][] prevs = new int[curr.width()][curr.height()];
		for (int i = 0; i < curr.width(); i++)
		{
			double[] tcost = new double[curr.height()];
			int[] prev = prevs[i];
			for (int j = 0; j < curr.width(); j++)
			{
				if (i == 0)
					tcost[j] = energy(i, j);
				else
				{
					int ind = Math.max(0, j - 1);
					for (int n = ind; n <= Math.min(curr.height() - 1, j + 1); n++)
						if (cost[n] < cost[ind])
							ind = n;
					tcost[j] = cost[ind] + energy(i, j);
					prev[j] = ind;
				}
			}
			cost = tcost;
		}
		int ind = 0;
		for (int n = 0; n < cost.length; n++)
			if (cost[n] < cost[ind])
				ind = n;
		int[] seam = new int[curr.width()];
		seam[curr.width() - 1] = ind;
		for (int i = curr.width() - 1; i > 0; i--)
		{
			seam[i - 1] = prevs[i][seam[i]];
		}
		return seam;
	}

	public int[] findVerticalSeam()
	{
		double[] cost = new double[curr.width()];
		int[][] prevs = new int[curr.height()][curr.width()];
		for (int j = 0; j < curr.height(); j++)
		{
			double[] tcost = new double[curr.width()];
			int[] prev = prevs[j];
			for (int i = 0; i < curr.width(); i++)
			{
				if (j == 0)
					tcost[i] = energy(i, j);
				else
				{
					int ind = Math.max(0, i - 1);
					for (int n = ind; n <= Math.min(curr.width() - 1, i + 1); n++)
						if (cost[n] < cost[ind])
							ind = n;
					tcost[i] = cost[ind] + energy(i, j);
					prev[i] = ind;
				}
			}
			cost = tcost;
		}
		int ind = 0;
		for (int n = 0; n < cost.length; n++)
			if (cost[n] < cost[ind])
				ind = n;
		int[] seam = new int[curr.height()];
		seam[curr.height() - 1] = ind;
		for (int j = curr.height() - 1; j > 0; j--)
		{
			seam[j - 1] = prevs[j][seam[j]];
		}
		return seam;
	}

	public void removeHorizontalSeam(int[] a)
	{
		if (a == null)
		{
			throw new NullPointerException();
		}
	}

	public void removeVerticalSeam(int[] a)
	{
		if (a == null)
		{
			throw new NullPointerException();
		}
	}
}
