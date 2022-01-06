import java.awt.Point;
import java.io.*;


public class Recognizer {
    public static final int STROKESIZE = 150; 
    private static final int NUMSTROKES = 10; 
    private Point[] userStroke; 
    private int nextFree;
    private Point[][] baseSet; 

   
    public Recognizer()
    {
        int row, col, stroke, pointNum, x, y;
        String inputLine;

        userStroke = new Point[STROKESIZE];
        baseSet = new Point[NUMSTROKES][STROKESIZE];

        try {
            FileReader myReader = new FileReader("strokedata.txt");
            BufferedReader myBufferedReader = new BufferedReader(myReader);
            for (stroke = 0; stroke < NUMSTROKES; stroke++)
                for (pointNum = 0; pointNum < STROKESIZE; pointNum++) {
                    inputLine = myBufferedReader.readLine();
                    x = Integer.parseInt(inputLine);
                    inputLine = myBufferedReader.readLine();
                    y = Integer.parseInt(inputLine);
                    baseSet[stroke][pointNum] = new Point(x, y);
                }
            myBufferedReader.close();
            myReader.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to file.\n");
        }
    }

    
    public int findMinX()
    {
        int minX = userStroke[0].x;
        for(int i=0; i<nextFree; i++){
            if(userStroke[i].x < minX){
                minX = userStroke[i].x;
            }
        }
        return minX;    
    }
    
     public int findMinY()
    {
        int minY = userStroke[0].y;
        for(int i=0; i<nextFree; i++){
            if(userStroke[i].y < minY){
                minY = userStroke[i].y;
            }
        }
        return minY;    
    }
    
    public int findMaxY()
    {
        int maxY = userStroke[0].y;
        for(int i=0; i<nextFree; i++){
            if(userStroke[i].y > maxY){
                maxY = userStroke[i].y;
            }
        }
        return maxY;    
    }
    
    public int findMaxX()
    {
        int maxX = userStroke[0].x;
        for(int i=0; i<nextFree; i++){
            if(userStroke[i].x > maxX){
                maxX = userStroke[i].x;
            }
        }
        return maxX;    
    }
    
    public void translate()
    {
        int finalX = findMinX();
        int finalY = findMinY();
        
        for(int i=0; i<nextFree; i++){
                userStroke[i].x = userStroke[i].x -finalX;
                userStroke[i].y = userStroke[i].y - finalY;
            }
        
    }
    
    public void scale()
    {
        int maxX = findMaxX();
        int maxY = findMaxY();
        int max = 0;
        if(maxX < maxY)
            max = maxY;
        else
            max = maxX;
        double scaleFactor = (double)250/max;
        for( int i=0; i<nextFree; i++){
            userStroke[i].x = (int)(userStroke[i].x*scaleFactor);
            userStroke[i].y = (int)(userStroke[i].y*scaleFactor);
        }
    }
    
    private void insertOnePoint()
    {
        int maxPosition = 0, newX, newY, distance;
        
        int maxDistance = (int) userStroke[0].distance(userStroke[1]);
      
          
      
      for(int i=0; i<nextFree-1;i++){
            if((int)userStroke[i].distance(userStroke[i+1]) > maxDistance){
                maxDistance = (int)userStroke[i].distance(userStroke[i+1]);
                maxPosition = i;
            }
        }
      
      
      
       
        for (int i = nextFree; i > maxPosition + 1; i--)
            userStroke[i] = userStroke[i - 1];

        
        newX = (int) (userStroke[maxPosition].getX() + userStroke[maxPosition + 2]
                .getX()) / 2;
        newY = (int) (userStroke[maxPosition].getY() + userStroke[maxPosition + 2]
                .getY()) / 2;
        userStroke[maxPosition + 1] = new Point(newX, newY);

        nextFree++;
    }

    
    public void normalizeNumPoints()
    {
        while (nextFree < STROKESIZE) {
            insertOnePoint();
        }
    }

    
    public double computeScore(int digitToCompare)
    {
        double score = 0;
        for(int i=0; i<nextFree; i++){
            score +=   userStroke[i].distance(baseSet[digitToCompare][i]);
        }
        return score;
    }
    
    public int findMatch()
    {
        translate();
        scale();
        normalizeNumPoints();
        double min = computeScore(0);
        int index = 0;
        for(int i=1; i<=9; i++){
               if( min > computeScore(i)){
               min = computeScore(i);
               index = i;
              }
        }
        return index; 
    }
       
    public void resetUserStroke()
    {
        nextFree = 0;
    }

    
    public int numUserPoints()
    {
        return nextFree;
    }

    
    public int getUserPointX(int i)
    {
        if ((i >= 0) && (i < nextFree))
            return ((int) userStroke[i].getX());
        else {
            System.out.println("Invalid value of i in getUserPoint");
            return (0);
        }
    }

    
    public int getUserPointY(int i)
    {
        if ((i >= 0) && (i < nextFree))
            return ((int) userStroke[i].getY());
        else {
            System.out.println("Invalid value of i in getUserPoint");
            return (0);
        }
    }

    public void addUserPoint(Point newPoint)
    {
        if (nextFree < STROKESIZE) {
            userStroke[nextFree] = newPoint;
            nextFree++;
        }
    }
}
