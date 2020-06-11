import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.*;
//import javax.swing.event.ChangeListener;

public class Menu extends JFrame
{
  private static final long serialVersionUID = 1L;
	
  public static final int DEFAULT_WIDTH = 350;
  public static final int DEFAULT_HEIGHT = 200;
  
  private JPanel sliderPanel;
  //private ChangeListener listener;
  
  ArrayList<JSlider> sliders = new ArrayList<JSlider>();
  ArrayList<JCheckBox> checks = new ArrayList<JCheckBox>();
   
   
   public Menu()
   {
      setTitle("John's Chess Menu");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      sliderPanel = new JPanel();
      sliderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

      /*
      // common listener for all sliders
      listener = new ChangeListener()
      {
        public void stateChanged(ChangeEvent event)
        {
          //JSlider source = (JSlider) event.getSource();
        }
      };
      */
      
      

      JSlider slider1 = new JSlider();
      //slider1 = new JSlider();
      slider1.setPaintTicks(true);
      slider1.setPaintLabels(true);
      slider1.setMinimum(1);
      slider1.setMaximum(5);
      slider1.setValue(4);
      slider1.setMajorTickSpacing(1);
      slider1.setSnapToTicks(true);
      addSlider(slider1, "Difficulty");
      sliders.add(slider1);
      
      //checkboxes
      JCheckBox testMode = new JCheckBox();
      testMode.setSelected(false);
      JPanel checkPanel = new JPanel();
      checkPanel.add(testMode);
      checkPanel.add(new JLabel("Testing Mode"));
      checks.add(testMode);
      
      
      //button when finished changing settings
      JPanel buttonPanel = new JPanel();
      
      JButton button = new JButton("START");
      button.addActionListener(new ActionListener() 
                                 {
        
        @Override
        public void actionPerformed(ActionEvent e) //when the button is pressed
        {
          Chess.runChess();
          
        }
      });
      buttonPanel.add(button);
      
      
      add(sliderPanel, BorderLayout.NORTH);
      add(checkPanel, BorderLayout.CENTER);
      add(buttonPanel, BorderLayout.SOUTH);
      
   }

   /**
    * Adds a slider to the slider panel and hooks up the listener
    * @param s the slider
    * @param description the slider description
    */
   private void addSlider(JSlider s, String description)
   {
      //s.addChangeListener(listener);
      JPanel panel = new JPanel();
      
      panel.add(new JLabel(description));
      panel.add(s);
      sliderPanel.add(panel);
   }
   
   
   public boolean getCheckBoxValue(int i)
   {
     return checks.get(i).isSelected();
   }
   
   
   public int getSliderValue(int i)
   {
     return sliders.get(i).getValue();
   }

   
}