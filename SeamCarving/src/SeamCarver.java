import java.awt.Color;

public class SeamCarver
{
	private SmC_Picture p;
	private SmC_Picture curr;
	
	public SeamCarver(SmC_Picture pictureP)
	{
		if (pictureP == null)
		{
			throw new NullPointerException();
		}
		
		p = pictureP;
		curr = pictureP;
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
		throw new UnsupportedOperationException();
	}

	public int[] findVerticalSeam()
	{
		throw new UnsupportedOperationException();
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