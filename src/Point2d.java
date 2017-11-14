import java.io.*;

class Point2d implements Serializable
{
    // The X and Y coordinates of the point
    private int x;
    private int y;

    // A trick to help with debugging
    private boolean debug;

    public void dprint (String s)
    {
        // print the debugging string only if the "debug"
        // data member is true
        if (debug)
            System.out.println("Debug: " + s);
    }

    public void setDebug (boolean b)
    {
        debug = b;
    }

    public Point2d (int px, int py)
    {
        x = px;
        y = py;

        // turn off debugging
        debug = false;
    }

    public Point2d ()
    {
        this (0, 0);
    }

    public Point2d (Point2d pt)
    {
        x = pt.getX();
        y = pt.getY();

        // a better method would be to replace the above code with
        //    this (pt.getX(), pt.getY());
        // especially since the above code does not initialize the
        // variable debug.  This way we are reusing code that is already
        // working.
    }

    public void setX(int px)
    {
        x = px;
    }

    public int getX()
    {
        return x;
    }

    public void setY(int py)
        {
        y = py;
    }

    public int getY()
    {
        return y;
    }

    public void setXY(int px, int py)
    {
        setX(px);
        setY(py);
    }

}
