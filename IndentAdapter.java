import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

/*public class IndentAdapter extends KeyAdapter
{
	JTextPane board;
	SimpleAttributeSet attr;
	String indent;

	public IndentAdapter ( JTextPane board )
	{
		indent = new String ();
		this.board = board;
		attr = new SimpleAttributeSet ( );
	}

	public void keyReleased ( KeyEvent e )
	{
		if ( e.getKeyCode() == '\n' )
		{
			Document doc = board.getDocument();
			int len = doc.getLength();
			try
			{
				String text = doc.getText ( len-2 , 1 );
				if ( text.charAt (0) == '{' )
					indent += "    ";
				if ( text.charAt (0) == '}' )
					indent = indent.substring ( 0 , indent.length() - 4 );
				if ( text.charAt (0) == ')' )
					doc.insertString ( len , "    " , attr );
				doc.insertString ( len , indent, attr );

			}
			catch ( BadLocationException e1 )
			{
			}
		}
	}

}*/


public class IndentAdapter extends KeyAdapter
{
	JTextBoard board;
	SimpleAttributeSet attr;
	String indent;


	public IndentAdapter ( JTextPane board )
	{
		indent = "    ";
		attr = new SimpleAttributeSet();
	}
	
	public void keyPressed ( KeyEvent e )
	{

	}

	public void keyReleased ( KeyEvent e )
	{
		int code = e.getKeyCode();
		if ( code == '\n' )
		{
			
		}
		if ( code == '\t' )
		{
			
		}

	}

	public void keyTyped ( KeyEvent e )
	{
		
	}
}
