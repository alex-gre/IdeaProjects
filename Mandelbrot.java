/*
 This program is free software and is distributed in the hope that it will be useful.
 The contents of this file are subject to the Common Public Attribution License Version 1.0 (CPAL-1.0)(the “License”);
 you may not use this file except in compliance with the License.
 
 You may obtain a copy of the License at  http://opensource.org/licenses/CPAL-1.0.
 
 The License is based on the Mozilla Public License Version 1.1 
 but Sections 14 and 15 have been added to cover use of software over a computer network 
 and provide for limited attribution for the Original Developer. 
 In addition, Exhibit A has been modified to be consistent with Exhibit B.
 Software distributed under the License is distributed on an “AS IS” basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied, without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the License for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Massimo Perini.
 All portions of the code written by Massimo Perini are Copyright (c) 2014 Massimo Perini. All Rights Reserved.
 Attribution Phrase: Developed by Massimo Perini
 Attribution URL: http://www.perinimassimo.com
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.vecmath.Vector3f;

class MandelbrotDraw extends JPanel
{
  private static int maxIter=180;
  private double [] scala={28,28};
  private double [] posizione={0, 0};
  
  
  public double[] getPosizione() {
	return posizione;
}
public void setPosizione(double[] posizione) {
	this.posizione = posizione;
}
public void paint(Graphics g)
  {
    super.paint(g);
    Dimension d=getSize();
    int x;
    int y;
    int iterazioni;
    double reale=0, immaginaria=0;
    double cr, ci;
    double tr=0, ti=0;
    double mod=0;
    int color_r;
    int color_g;
    int color_b;
    float mu=0;
    // Per tutti immaginaria pixel. x ascissa pixel, y ordinata
    BufferedImage I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    for(x=0; x<d.width; x++)
    {
      for(y=0; y<d.height; y++)
      {
        //pixel in scala mandelbrot
        cr=((double) x-posizione[0])/scala[0];
        ci=-((double) y-posizione[1])/scala[1];
        
        // inizio dal numero C= 0+i0
        reale=0;
        immaginaria=0;
        for(iterazioni=0; iterazioni<maxIter; iterazioni++)
        {
          // Z=Z*Z+C
        	//immaginaria � complesso, al quadrato e' -1 quindi lo sottraggo. tr parte reale
        	//ti immaginaria, doppio prodotto + parte immaginaria numero base
        	
          tr=reale*reale-immaginaria*immaginaria+cr;
          ti=2*reale*immaginaria+ci;
          reale=tr;
          immaginaria=ti;
          mod=reale*reale+immaginaria*immaginaria;
          // se la distanza e' maggiore di 4 esci
          if(mod>4)
        	  break;
        }
        
             
        // coloro in base alle iterazioni. + scuro + iterazioni
       // iterazioni=(int)(maxIter*Math.sqrt((double)iterazioni/maxIter));
        mu =(float) (iterazioni - (Math.log (Math.log (Math.sqrt(mod))))/ Math.log (2.0));
        mu*=10;
       I.setRGB(x, y, (int)mu);
        
      }
      
    }
    g.drawImage(I, 0, 0, this);

  }
public double[] getScala() {
	return scala;
}
public void setScala(double[] scala) {
	this.scala = scala;
}
}

class Finestra extends JFrame
{
	double startX=0;
	double startY=0;
  public Finestra()
  {
	  this.setLayout(new BorderLayout ());
	  this.setBounds(300, 300, 300, 300);
	  final MandelbrotDraw mandelbrotdraw=new MandelbrotDraw();
	  final double [] arrPos=new double [2];
	    Dimension d=getSize();
	    arrPos[0]=d.width/2;
	    arrPos[1]=d.height/2;
	  mandelbrotdraw.setPosizione(arrPos);
	  repaint();
	  this.add(mandelbrotdraw, BorderLayout.CENTER);

	  MouseWheelListener mW=new MouseWheelListener ()
		{

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int scroll=arg0.getWheelRotation();
				double [] scala=mandelbrotdraw.getScala();
				scala[0]+=scroll;
				scala[1]+=scroll;
				mandelbrotdraw.setScala(scala);
				repaint ();
			}
			
		};
		
		MouseInputAdapter mI=new MouseInputAdapter ()
		{
			public void mousePressed(MouseEvent e)
			{
			    Point p = e.getPoint();
			    startX = p.x;
			    startY = p.y;			    
			    
			  }

			  public void mouseDragged(MouseEvent e)
			  {
			    Point p = e.getPoint();
			    int curX = p.x;
			    int curY = p.y;
			    
			    arrPos[0]+=(curX-startX);
			    arrPos[1]+=(curY-startY);
			    
			    mandelbrotdraw.setPosizione(arrPos);
			    repaint();
			    
			    startX=curX;
			    startY=curY;
			  }
		};
		this.addMouseListener(mI);
		this.addMouseMotionListener(mI);
	  this.addMouseWheelListener(mW);
	  
	  ActionListener tim=new ActionListener ()
	  {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			double [] scala=mandelbrotdraw.getScala();
			scala[0]+=3;
			scala[1]+=3;
			mandelbrotdraw.setScala(scala);
			repaint ();
		}
		  
	  };
	  
	  Timer t=new Timer (1, tim);
	  t.start();
	  
	  this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	  this.setVisible(true);
  }
}

public class Mandelbrot
{
	public static void main (String[] args)
	{
		Finestra m=new Finestra();
	}
}