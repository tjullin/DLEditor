
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import java.io.*;

public class CodeListener implements DocumentListener
{
	private DicTree [] dicTree = new DicTree[6];
	private SimpleAttributeSet [] attr = new SimpleAttributeSet[5];
	
/*================keyWords List==================================*/
	static final String [] dataType=
	{
		"int","double","float","long","union","struct","class",
		"short","bool","void"
	};
	static final String [] keyWords1=
	{
		"typedef","const","using","namespace","std","static","extern",
		"intern","return"
	};

	static final String [] keyWords2=
	{
		"for","if","else","break","continue","goto","while","do","new",
		"delete"
	};

	static final String [] STL=
	{
		"set","vector","priority_queue","queue","stack","map","list",
		"multiset","pair","begin","iterator","end","find","pop","push",
		"push_back","pop_back","clear","front","top","lower_bound",
		"upper_bound","endl","cin","cout"
	};

	static final char [] operator =
	{
		'+','-','*','(',')','{','}','/','[',']','<','>','=','^','|','&','%','?',':',';',
		','
	};

	static final String [] command =
	{
		"#include","#define"
	};

	static final Color [] colors=
	{
		Color.BLUE,Color.YELLOW,Color.GREEN,Color.PINK,Color.GRAY,Color.RED
	};
/*===============================================================*/

	CodeListener ( )
	{
		for ( int i = 0 ;  i < 5 ; i++ )
		{
			attr[i] = new SimpleAttributeSet ( );
			StyleConstants.setBold( attr[i] , true );
			StyleConstants.setFontFamily ( attr[i] , "Frutiger" );
			StyleConstants.setFontSize ( attr[i] , 16 );
		}
		StyleConstants.setForeground ( attr[3] , Color.GRAY );
		for ( int i = 0 ; i < 5; i++ )
			dicTree[i] = new DicTree();
		for ( int i = 0 ; i < dataType.length ; i++ )
			dicTree[0].add ( dataType[i] );
		for ( int i = 0 ; i < keyWords1.length ; i++ )
			dicTree[1].add ( keyWords1[i] );
		for ( int i = 0 ; i < keyWords2.length ; i++ )
			dicTree[2].add ( keyWords2[i] );
		for ( int i = 0 ; i < STL.length ; i++ )
			dicTree[3].add ( STL[i] );
		for ( int i = 0 ; i < command.length ; i++ )
			dicTree[4].add ( command[i] );
	}
	
	private int match ( String pattern )
	{
		boolean flag = true;
		if ( Character.isDigit ( pattern.charAt(0) ) )
		{
			for ( int i = 0 ; i < pattern.length() ; i++ )
				if ( !Character.isDigit(pattern.charAt(i)) ) 
					flag = false;
			if ( flag ) 
			{
				StyleConstants.setForeground ( attr[1] , colors[5] );
				return 5;
			}
		}
		else
		{	
			for ( int i = 0 ; i < 5 ; i ++ )
		   		if ( dicTree[i].check ( pattern ) ) 
		   		{
					StyleConstants.setForeground ( attr[0] , colors[i] );
			   		return i;
		   		}
		}
		StyleConstants.setForeground ( attr[2] , Color.BLACK );
		return -1;	
	}

	private char charAt ( Document doc , int pos )
		throws BadLocationException
	{
		return doc.getText(pos,1).charAt(0);
	}

	
	private int getStart ( Document doc , int pos )
		throws  BadLocationException
	{
		while ( pos > 0 && isWord ( doc , pos-1 ) )
				pos--;
		return pos;
	}

	private int getEnd ( Document doc , int pos )
		throws BadLocationException
	{
		while ( isWord ( doc , pos ) )
			pos++;
		return pos;
	}
	
	private boolean isWord ( Document doc , int pos )
		throws BadLocationException
	{
		char ch = charAt ( doc , pos );
		if ( Character.isLetter(ch) || Character.isDigit(ch)
			|| ch == '_' || ch =='#' )
			return true;
		return false;
	}

	private boolean isOperator ( Document doc , int pos )
		throws BadLocationException
	{
		char ch = charAt ( doc , pos );
		for ( int i = 0 ; i < operator.length ; i++ )
			if ( ch == operator[i] ) return true;
		return false;
	}

	private void paint ( StyledDocument doc , int pos , int len )
		throws BadLocationException
	{
		int start = getStart ( doc , pos );
		int end = getEnd ( doc , pos+len );
		char ch;
		while ( start < end )
		{
			ch = charAt ( doc , start );
			if ( Character.isLetter(ch) || ch == '_' || ch == '#' )
				start = paintWord( doc , start );
			else if ( Character.isDigit(ch) )
				start = paintNum ( doc , start );
			else 
			{
				Color temp;
				if ( isOperator ( doc , start) )
					temp = Color.ORANGE;
				else temp = Color.BLACK;
				StyleConstants.setForeground ( attr[2] , temp );
				SwingUtilities.invokeLater ( new PaintTask ( doc , start , 1 , attr[2] ) );
				start++;
			}
		}

	}

	private int paintWord ( StyledDocument doc , int pos )
		throws BadLocationException
	{
		int wordEnd=getEnd ( doc ,pos );
		String word = doc.getText ( pos , wordEnd - pos );
		int x = match ( word );
		SwingUtilities.invokeLater ( 
			new PaintTask ( doc , pos , wordEnd-pos , x==-1?attr[2]:attr[0] ) );
		return wordEnd;
	}

	private int paintNum ( StyledDocument doc , int pos )
		throws BadLocationException
	{
		int numEnd = getEnd ( doc , pos );
		String num = doc.getText ( pos , numEnd - pos );
		int x = match ( num );
		SwingUtilities.invokeLater (
				new PaintTask ( doc , pos , numEnd-pos , x==-1?attr[2]:attr[1] ) );
		return numEnd;
	}

	public void insertUpdate ( DocumentEvent e )
	{
		try
		{
			StyledDocument doc = (StyledDocument)e.getDocument();
			int offset = e.getOffset();
			int lenth = e.getLength();
			int docLen = doc.getLength();
			char ch = charAt ( doc , offset );
			if ( lenth == 1 && ( ch == ' ' || ch == '\n' ))
			{
				if ( offset+1 != docLen )
				{
					if ( offset > 0 ) paint ( doc , offset-1 , 0 );
					paint ( doc , offset+1 , 0 );
				}
			}
			else 
			{
				paint ( doc , offset , lenth );
			}
		}
		catch ( BadLocationException e1 )
		{
			e1.printStackTrace();
		}
	}

	public void removeUpdate ( DocumentEvent e )
	{
		try
		{
			StyledDocument doc = ( StyledDocument)e.getDocument();
			int offset = e.getOffset();
			int lenth = e.getLength();
			int docLen = doc.getLength();
			if ( lenth == 1 )
			{
				if ( docLen == 0 ) return;
				if ( offset >= 0  )
					paint ( doc , offset , 0 );
				if ( offset+1 < docLen ) 
					paint ( doc , offset+1 , 0 );
			}
			else 
			{
				paint ( doc , offset , lenth );
			}
		}
		catch ( BadLocationException e1 )
		{
			e1.printStackTrace();
		}
	}

	public void changedUpdate ( DocumentEvent e )
	{
		
	}
}

class PaintTask implements Runnable 
{
	private StyledDocument doc;
	private SimpleAttributeSet attr;
	private int pos;
	private int len;

	public PaintTask ( StyledDocument doc , int pos , int len ,
			SimpleAttributeSet attr )
	{
		this.doc = doc;
		this.pos = pos;
		this.len = len;
		this.attr = attr;
	}
	
	public void run ( )
	{
		try
		{
			doc.setCharacterAttributes ( pos , len , attr , true );
		}
		catch ( Exception e )
		{
		}
	}
}
