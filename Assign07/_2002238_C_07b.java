import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class _2002238_C_07b {
    public static void main(String[] args) {
        ConverterFrame cf=new ConverterFrame("Fahrenheit to Celsius Converter");
    }
}
class ConverterFrame extends JFrame implements ActionListener, ItemListener{
    JLabel jl1, jl2;
    JTextField jt1,jt2;
    JButton jb;
    ButtonGroup bg; JPanel jp;
    JRadioButton jrb1,jrb2;
    Boolean is_CtF;
    ConverterFrame(String title){
        jl1=new JLabel("Enter Temperature in Celsius: ");
        jl2=new JLabel ("Temperature in Fahrenheit: ");
        jt1=new JTextField(6); jt2=new JTextField(6);
        jt2.setEditable(false);
        jb=new JButton("Convert");
        jb.addActionListener(this);
        bg=new ButtonGroup();
        jrb1=new JRadioButton("Celsius to Fahrenheit");
        jrb1.addItemListener(this);
        bg.add(jrb1); 
        jrb2=new JRadioButton("Fahrenheit to Celsius");
        jrb2.addItemListener(this);
        bg.add(jrb2);
        jp=new JPanel();
        jp.add(jrb1); jp.add(jrb2);
        is_CtF=true;
        jrb1.setSelected(true);

        add(jl1); add(jt1); add(jl2); add(jt2); add(jb);
        add(jp);

        setTitle(title);
        setLayout(new FlowLayout());
        setSize(400,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent evt){
        try {
            float f1,f2;
            f1=Float.parseFloat(jt1.getText());
            if(is_CtF){
                f2=(f1*9/5)+32;
            }
            else{
                f2=(f1-32)*5/9;
            }
            jt2.setText(String.format("%.2f",f2));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void itemStateChanged(ItemEvent e){
        if (e.getSource() == jrb1) {
			if (e.getStateChange() == 1) {
				jl1.setText("Enter Temperature in Celsius: ");
                jl2.setText("Temperature in Fahrenheit: ");
                is_CtF=true;
			}
		}
		else {
			if (e.getStateChange() == 1) {
				jl1.setText("Enter Temperature in Fahrenheit: ");
                jl2.setText("Temperature in Celsius: ");
                is_CtF=false;
			}
		}
    }
}
