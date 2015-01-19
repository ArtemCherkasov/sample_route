/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample_route;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author artemcherkasov
 */
public class ExtentedPanel extends JPanel implements MouseMotionListener{
    private List<Point> pointList;
    private Random random;
    private int RADIUS_X = 10;
    private int RADIUS_Y = 10;
    private int RADIUS_X_DELTA = RADIUS_X/2;
    private int RADIUS_Y_DELTA = RADIUS_Y/2;
    private int mouse_x = 0;
    private int mouse_y = 0;
    private boolean movie_flag = false;
    public ExtentedPanel() {
        pointList = new ArrayList<Point>();
        random = new Random();
        for(int i = 0; i < 10; ++i){
            Point point = new Point(i*70 +random.nextInt(50)*1.0, 300 + 40*random.nextInt(4)*1.0);
            pointList.add(point);
        }
        this.addMouseMotionListener(this);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.drawString(String.valueOf(mouse_x), 100, 100);
        g.drawString(String.valueOf(mouse_y), 100, 120);
        for(Point p: pointList){
            
        }
        g.drawOval(pointList.get(0).getX().intValue() - RADIUS_X_DELTA, pointList.get(0).getY().intValue() - RADIUS_Y_DELTA,  RADIUS_X,  RADIUS_Y);
        int l = pointList.size();
        for(int i = 0; i < l - 1; ++i){
            g.drawLine(pointList.get(i).getX().intValue(), pointList.get(i).getY().intValue(),pointList.get(i + 1).getX().intValue(), pointList.get(i + 1).getY().intValue());
            g.drawOval(pointList.get(i + 1).getX().intValue() - RADIUS_X_DELTA, pointList.get(i + 1).getY().intValue() - RADIUS_Y_DELTA,  RADIUS_X,  RADIUS_Y);
        }
        
        if (movie_flag){
            getNearPointOnLine(mouse_x, mouse_y, g);
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouse_x = me.getX();
        mouse_y = me.getY();
        if (!movie_flag) movie_flag = true;
        repaint();
    }
    
    private int nearPoint(int x, int y){
        double min_length = Math.sqrt(Math.pow(pointList.get(0).getX() - x, 2) + Math.pow(pointList.get(0).getY() - y, 2));
        double length = 0;
        int min_index_point = 0;
        for(Point p:pointList){
            length = Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
            if(length < min_length){
                min_length = length;
                min_index_point = pointList.indexOf(p);
            }
        }
        return min_index_point;
    }
    
    private void getNearPointOnLine(int x, int y, Graphics g){

        double cos_alfa;
        double cos_beta;
        double a;
        double b;
        double c;
        double c_delta;
        double p;
        double h = 0;
        double min_h = 0;
        int min_index = 0;
        boolean flag_in_line = false;
        
        List<Double> h_list = new ArrayList<Double>();
        Map<Integer, Double> index_list = new HashMap<Integer, Double>();
        
        /*
        a = Math.sqrt(Math.pow(pointList.get(0).getX() - x, 2) + Math.pow(pointList.get(0).getY() - y, 2));
        b = Math.sqrt(Math.pow(pointList.get(1).getX() - x, 2) + Math.pow(pointList.get(1).getY() - y, 2));
        c = Math.sqrt(Math.pow(pointList.get(0).getX() - pointList.get(1).getX(), 2) + Math.pow(pointList.get(0).getY() - pointList.get(1).getY(), 2));
        cos_alfa = (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2))/(2*b*c);
        cos_beta = (Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2))/(2*a*c);
        half_perimetr = (a + b + c)/2;
        */
        int l = pointList.size();
        System.out.println("---------------------START------------------------");
        for(int i = 0; i < l - 1; ++i){
            a = Math.sqrt(Math.pow(pointList.get(i + 1).getX() - x, 2) + Math.pow(pointList.get(i + 1).getY() - y, 2));
            b = Math.sqrt(Math.pow(pointList.get(i).getX() - x, 2) + Math.pow(pointList.get(i).getY() - y, 2));
            c = Math.sqrt(Math.pow(pointList.get(i).getX() - pointList.get(i + 1).getX(), 2) + Math.pow(pointList.get(i).getY() - pointList.get(i + 1).getY(), 2));
            cos_alfa = (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2))/(2*b*c);
            cos_beta = (Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2))/(2*a*c);
            p = (a + b + c)/2.0;
            if ((cos_alfa > 0) && (cos_beta > 0)){
                flag_in_line = true;
                h = (2.0/c)*Math.sqrt(p*(p - a)*(p - b)*(p - c));
                h_list.add(h);
                index_list.put(i, h);
                min_index = i;
                System.out.println("i) " + i + "; a = " + a + "; " + "b = " + b + "; " + "c = " + c + ";" + "h = " + h + ";");
            }
            
        }
        min_h = h;
        for (Map.Entry<Integer, Double> m: index_list.entrySet()){
            //System.out.println("index list " + m.getKey() + " " + m.getValue());
            if (m.getValue() < min_h) {
                min_h = m.getValue();
                min_index = m.getKey();
            }
        }
        
        b = Math.sqrt(Math.pow(pointList.get(min_index).getX() - x, 2) + Math.pow(pointList.get(min_index).getY() - y, 2));
        c = Math.sqrt(Math.pow(pointList.get(min_index).getX() - pointList.get(min_index + 1).getX(), 2) + Math.pow(pointList.get(min_index).getY() - pointList.get(min_index + 1).getY(), 2));
        c_delta = Math.sqrt(Math.pow(b, 2) - Math.pow(min_h, 2));
        System.out.println("c delta " + c_delta + " c " + c);
        Double x_projection = (pointList.get(min_index + 1).getX() - pointList.get(min_index).getX())*c_delta/c + pointList.get(min_index).getX();
        Double y_projection = (pointList.get(min_index + 1).getY() - pointList.get(min_index).getY())*c_delta/c + pointList.get(min_index).getY();
        double min_length = Math.sqrt(Math.pow(pointList.get(0).getX() - x, 2) + Math.pow(pointList.get(0).getY() - y, 2));
        double length = 0;
        int min_index_point = 0;
        for(Point point:pointList){
            length = Math.sqrt(Math.pow(point.getX() - x, 2) + Math.pow(point.getY() - y, 2));
            if(length < min_length){
                min_length = length;
                min_index_point = pointList.indexOf(point);
            }
        }
        //System.out.println("minimal index point " + min_index + " " + min_length);
        System.out.println("---------------------STOP------------------------");
        if(flag_in_line /* && (min_length > min_h)*/){
            g.drawLine(pointList.get(min_index).getX().intValue(), pointList.get(min_index).getY().intValue(), x, y);
            g.drawLine(pointList.get(min_index + 1).getX().intValue(), pointList.get(min_index + 1).getY().intValue(), x, y);
            g.drawOval(x_projection.intValue() - RADIUS_X_DELTA, y_projection.intValue() - RADIUS_Y_DELTA,  RADIUS_X,  RADIUS_Y);
            g.drawLine(x_projection.intValue(), y_projection.intValue(), x, y);
        } else {
            g.drawLine(pointList.get(min_index_point).getX().intValue(), pointList.get(min_index_point).getY().intValue(), x, y);
        }
    }
   
}