import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SeamCarver
{

	private SmC_Picture curr;

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
			for (int j = 0; j < curr.height(); j++)
			{
				if (i == 0)
				{
					tcost[j] = energy(i, j);
				}
				else
				{
					int ind = Math.max(0, j - 1);
					for (int n = ind; n <= Math.min(curr.height() - 1, j + 1); n++)
					{
						if (cost[n] < cost[ind])
						{
							ind = n;
						}
					}
					tcost[j] = cost[ind] + energy(i, j);
					prev[j] = ind;
				}
			}
			
			cost = tcost;
		}
		
		int ind = 0;
		for (int n = 0; n < cost.length; n++)
		{
			if (cost[n] < cost[ind])
			{
				ind = n;
			}
		}
	
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
				{
					tcost[i] = energy(i, j);
				}
				else
				{
					int ind = Math.max(0, i - 1);
					for (int n = ind; n <= Math.min(curr.width() - 1, i + 1); n++)
					{
						if (cost[n] < cost[ind])
						{
							ind = n;
						}
					}
					tcost[i] = cost[ind] + energy(i, j);
					prev[i] = ind;
				}
			}
			cost = tcost;
		}
		
		int ind = 0;
		
		for (int n = 0; n < cost.length; n++) 
		{
			if (cost[n] < cost[ind])
			{
				ind = n;
			}
		}
		
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
		
		if (a.length != width())
		{
            throw new IllegalArgumentException();
        }
		
		for (int i = 0; i < a.length; i++)
		{
			if (a[i] < 0 || a[i] >= height()) 
			{
                throw new java.lang.IllegalArgumentException();
            }
			
			 if (a[i] < a[i - 1] - 1 || a[i] > a[i - 1] + 1) 
			 {
	                throw new java.lang.IllegalArgumentException();
	         }
		}
		
		BufferedImage buff = new BufferedImage(curr.width(), curr.height() - 1, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < curr.width(); i++)
		{
			for (int j = 0; j < a[i]; j++)
			{
				buff.setRGB(i, j, curr.get(i, j).getRGB());
			}
			
			for (int j = a[i] + 1; j < curr.height(); j++)
			{
				buff.setRGB(i, j - 1, curr.get(i, j).getRGB());
				engr[i][j - 1] = engr[i][j];
			}
		}
		
		curr = new SmC_Picture(buff);
		for (int i = 0; i < curr.width(); i++)
		{
			engr[i][Math.max(a[i] - 1, 0)] = energy(i, Math.max(a[i] - 1, 0));
			engr[i][a[i]] = energy(i, a[i]);
		}
	}

	public void removeVerticalSeam(int[] a)
	{	
		if (a == null)
		{
			throw new NullPointerException();
		}
		
		if (a.length != height())
		{
            throw new IllegalArgumentException();
        }
		
		for (int i = 0; i < a.length; i++)
		{
			if (a[i] < 0 || a[i] >= width()) 
			{
                throw new java.lang.IllegalArgumentException();
            }
			
			 if (a[i] < a[i - 1] - 1 || a[i] > a[i - 1] + 1) 
			 {
	                throw new java.lang.IllegalArgumentException();
	         }
		}
		
		BufferedImage buff = new BufferedImage(curr.width() - 1, curr.height(), BufferedImage.TYPE_INT_ARGB);
		
		for (int j = 0; j < curr.height(); j++)
		{
			for (int i = 0; i < a[j]; i++)
			{
				buff.setRGB(i, j, curr.get(i, j).getRGB());
			}
			
			for (int i = a[j] + 1; i < curr.width(); i++)
			{
				buff.setRGB(i - 1, j, curr.get(i, j).getRGB());
				engr[i - 1][j] = engr[i][j];
			}
		}
		
		curr = new SmC_Picture(buff);
		for (int j = 0; j < curr.height(); j++)
		{
			engr[Math.max(a[j] - 1, 0)][j] = energy(Math.max(a[j] - 1, 0), j);
			engr[a[j]][j] = energy(a[j], j);
		}
	}
}
