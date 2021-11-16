import java.io.*;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

class Surface extends JPanel implements ActionListener {

    private Timer timer;
    private String repeatSt, speedSt, angleSt;
    private int repeat=1, repeatTemp = 0, speed, angle, time=0, firstSpeed=0, counter=0;
    private final double G = 9.8;
    private final int DELAY = 10;
    private double yIncrease =0;


    BufferedImage ballImage;

    {
        try {
            ballImage = ImageIO.read(new FileImageInputStream(new File("Ball.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ZEllipse ball;
    ZLine line;

    public Surface() {
        while (true) {
            repeatSt = JOptionPane.showInputDialog(null, "How many times should it repeat?");
            try {
                repeat = Integer.parseInt(repeatSt);
                if (repeat <= 0) {
                    JOptionPane.showMessageDialog(null, "Value cannot be less than 1!");
                } else {
                    break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Wrong value entered, try again!");
            }
        }

        while (true) {
            angleSt = JOptionPane.showInputDialog(null, "Enter the angle value");
            try {
                angle = Integer.parseInt(angleSt);
                if (angle > 90 || angle < 0) {
                    JOptionPane.showMessageDialog(null, "Angle value must be between 0 and 90!");
                } else {
                    break;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Wrong value entered, try again!");
            }
        }
        while (true) {
            speedSt = JOptionPane.showInputDialog(null, "Enter the speed value");
            try {
                speed = Integer.parseInt(speedSt);
                if (speed <= 0) {
                    JOptionPane.showMessageDialog(null, "Value cannot be less than 1!");
                } else {
                    break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Wrong value entered, try again!!");
            }
        }
        firstSpeed = speed;

        ball = new ZEllipse(0, 730, 50, 50);
        line = new ZLine((int) ball.getX(),(int) ball.getY(),(int) ball.getX(),(int) ball.getY());

        initTimer();
    }


    private void initTimer() {
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public Timer getTimer() {
        return timer;
    }


    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;


        if(counter ==1){
            g2d.setColor(Color.black);
            g2d.fill(ball);
            g2d.setColor(Color.blue);
            g2d.drawImage( ballImage, (int) ball.getX()+2,(int) ball.getY(), 48, 48, null);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine((int) ball.getX()-1,(int) ball.getY(),(int) ball.getX()-1,(int) ball.getY()); //I shifted the line's x values to the left by -1 to make it visible.
            //System.out.println(ball.getY());
            counter++;
        }else{

            g2d.setColor(getBackground());
            g2d.fill(ball);
            g2d.setColor(Color.black);

            counter =1;
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(ball.getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        //Here I have it drawn as much as the repeat value.
        if(ball.getY() >= 699){
            yIncrease = 0;
            time = 0;
            counter = 0;
            ball = new ZEllipse(0, 700, 50, 50);
            super.paintComponent(g);
            repeatTemp++;
            //System.out.println(repeatTemp);
            //System.out.println(repeat);
        }
        if (repeatTemp == repeat + 1){
            //System.out.println(repeat);
            //System.out.println(repeatTemp);
            timer.stop();
            System.out.println("Done");
        }

        doDrawing(g);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        double radValue = angle * Math.PI / 180;    //Angle value converts to radian.
        double xIncrease = firstSpeed * Math.cos(radValue);
        yIncrease = firstSpeed * Math.sin(radValue)-((G /2)*((Math.pow((time),2))));


        if(counter ==1){

            ball.addX((float) xIncrease);
            ball.addY((float) yIncrease);

        }else{
            time += 1;
        }
        repaint();
    }


}

class ZEllipse extends Ellipse2D.Float {
    //I created the black ball object below our ball image.
    public ZEllipse(float x, float y, float width, float height) {
        setFrame(x, y, width, height);
    }
    public void addX(double x) {
        this.x += x;
    }
    public void addY(double y) {
        this.y -= y;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

}


class ZLine extends Line2D.Float{
    //I created the line object.
    public ZLine(float x, float y, float width, float height) {
        setLine(x,y,width,height);
    }
}


public class BallThrow extends JFrame {

    public BallThrow() {
        initUI();
    }

    private void initUI() {

        setTitle("Ball Throwing");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        final Surface surface = new Surface();
        add(surface);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Timer timer = surface.getTimer();
                timer.stop();
            }
        });

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                BallThrow ex = new BallThrow();
                ex.setVisible(true);
            }
        });
    }
}