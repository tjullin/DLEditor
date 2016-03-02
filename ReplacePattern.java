import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;

public class ReplacePattern 
{
	SimpleAttributeSet attr,attr1;
	StyledDocument doc;
	Component parentComponent;
	Paint painter;
	String [] lines;
	int number;

	static final char [] operator =
	{
		'<',',','>','?','[',']','+',
		'=','-','(',')','*','&','^',
		'%','$','#','@','!','~',' ','\n'
	};

	public ReplacePattern ( JTextPane temp , Component parentComponent )
	{
		this.parentComponent = parentComponent;
		doc = ( StyledDocument) temp.getDocument();
		painter = new Paint();
		number = 1;
		attr = new SimpleAttributeSet ();
		attr1 = new SimpleAttributeSet ();
		StyleConstants.setForeground ( attr , Color.BLACK );
		StyleConstants.setFontSize ( attr , 16 );
		StyleConstants.setFontFamily ( attr , "Frutiger" );
		StyleConstants.setBold ( attr , true );
		StyleConstants.setForeground ( attr1 , Color.RED );
		StyleConstants.setFontSize ( attr1 , 16 );
		StyleConstants.setFontFamily ( attr1 , "Frutiger" );
		StyleConstants.setBold ( attr1 , true );
		StyleConstants.setBackground ( attr1 , Color.CYAN );
	}
	
	private boolean check ( char ch )
	{
		for ( int i = 0 ; i < operator.length ; i++ )
			if ( ch == operator[i] )
				return true;
		return false;
	}

	private char charAt ( Document doc , int index )
	{
		String text=null;
		try 
		{
			text = doc.getText ( index , 1 );
		}
		catch  ( BadLocationException error )
		{

		}
		return text.charAt(0);
	}

	public void replace ( String pattern , String goal )
	{
		int id = 0;
		while ( id < doc.getLength() )
		{
			char ch = charAt( doc , id);
			if ( Character.isLetter ( ch ) || Character.isDigit ( ch ) )
			{
				int lpos = id;
				int rpos = id;
				char cc = charAt ( doc , rpos );
				while ( rpos < doc.getLength() && (Character.isLetter(cc) || Character.isDigit(cc) ) )
				{
					rpos++;
					if ( rpos < doc.getLength() ) 
						cc = charAt ( doc , rpos );
					System.out.println ( rpos +" "+ cc );
				}
				System.out.println ( "from : " + lpos + " to " + rpos );
				String str = null;
				try
				{
					str = doc.getText ( lpos , rpos-lpos );
				}
				catch ( BadLocationException error )
				{

				}
				System.out.println ( "Always : " + str + " end");
				boolean flag = false;
				if ( pattern.equals ( str ) )
				{
					flag = true;
					System.out.println ( "YES" );
					try
					{
						doc.remove ( lpos , rpos-lpos );
					}
					catch ( BadLocationException error )
					{
					}
					try
					{
						doc.insertString ( lpos , goal , attr );
					}
					catch ( BadLocationException error )
					{
					}
				}
				if ( flag ) id += goal.length();
				else id += str.length();
			}
			else id++;	
		}
	}

}
