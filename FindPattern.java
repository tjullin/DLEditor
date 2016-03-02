import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class FindPattern 
{
	SimpleAttributeSet attr,attr1;
	StyledDocument doc;
	Component parentComponent;
	int index;
	int number;

	static final  char[] operator=
	{
		'<',',','>','?','[',']','+',
		'=','-','(',')','*','&','^',
		'%','$','#','@','!','~',' ','\n'
	};

	public FindPattern ( JTextPane temp , Component parentComponent )
	{
		this.parentComponent = parentComponent;
		doc = (StyledDocument)temp.getDocument();
		index = 0;
		number = 1;
		attr = new SimpleAttributeSet ( );
		attr1 = new SimpleAttributeSet ( );
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

	public void find ( String pattern )
	{
		//System.out.println ( "show the string :" + pattern );
		String content=null;
		try
		{
			content = doc.getText ( 0 , doc.getLength() );
		}
		catch ( BadLocationException error )
		{

		}
		//System.out.println ( "text:\n" + content );
		for ( int i = 0 ; i < content.length() ; i++ )
		{
			if ( check ( content.charAt(i) ) )
			{
				int rpos = i;
				int lpos = i-1;
				boolean flag = false;
				while ( lpos >= 0 && (Character.isLetter ( content.charAt(lpos) )||
						Character.isDigit ( content.charAt(lpos) ) ) )
				{
					if ( rpos-lpos > pattern.length()+1 ) 
					{
						flag = true;
						break;
					}
					lpos--;
				}
				if ( flag ) continue;
				lpos++;
				String str = content.substring ( lpos , rpos );
				//System.out.println ( "the pattern here : " + str );
				if ( str.equals ( pattern ) )
				{
					doc.setCharacterAttributes ( lpos , rpos-lpos , attr1 , true );
					int confirmDialogState = 
						JOptionPane.showConfirmDialog (
								parentComponent,
								"find the " + number + "th matched pattern",
								"Confirm Info",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
					if ( confirmDialogState == JOptionPane.CANCEL_OPTION || 
						 confirmDialogState == JOptionPane.NO_OPTION )
					{
						doc.setCharacterAttributes ( lpos , rpos-lpos , attr , true );
						number = 1;
						return;
					}
					if ( confirmDialogState == JOptionPane.YES_OPTION )
					{
						doc.setCharacterAttributes ( lpos , rpos-lpos , attr , true );
						number++;
					}
				}
			}
		}
		if ( number == 1 )
		{
			JOptionPane.showMessageDialog (
					parentComponent,
					"No segment can match the pattern given",
					"Search Result",
					JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog (
					parentComponent,
					"No more segmengs can matcch the pattern given",
					"Search Result",
					JOptionPane.ERROR_MESSAGE);
		}
		number = 1;
	}
}
