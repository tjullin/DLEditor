import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Paint
{
	private SimpleAttributeSet attr;
	private DicTree [] dicTree = new DicTree[6];
	private String [] segment;

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
	public Paint ( )
	{
		attr = new SimpleAttributeSet ( );
		StyleConstants.setBold( attr , true );
		StyleConstants.setFontFamily ( attr , "Frutiger" );
		StyleConstants.setFontSize ( attr , 16 );
		//attr = new SimpleAttributeSet ( );
		//StyleConstants.setForeground ( attr[3] , Color.GRAY );
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

	void paintCode ( StyledDocument doc  )
	{
		String codeText=null;
		try
		{
			codeText = doc.getText( 0 , doc.getLength());
		}
		catch ( BadLocationException error )
		{

		}
		String pattern=null;
		for ( int i = 0 ; i < codeText.length() ; i++ )
		{
			int x;
			StyleConstants.setForeground ( attr , Color.BLACK );
			if ( codeText.charAt(i) == ' ' || codeText.charAt(i) == '\n' || i == codeText.length()-1 )
			{
				x = i-1;
				while ( true )
				{
					if ( x == 0 ) break;
					if ( codeText.charAt(x) == ' ' || codeText.charAt(x) == '\n' )
						break;
					x--;
				}
				if ( codeText.charAt(x) == ' ' || codeText.charAt(x) == '\n' ) x++;
				pattern = codeText.substring ( x , i );
				int cc = -1;
				for ( int j = 0 ; j < 5 ; j++ )
					if ( dicTree[j].check ( pattern ) )
					{
						cc = j;
						break;
					}
				//System.out.println ( pattern +   cc );
				if ( cc == -1 )
				{
					boolean flag = true;
					for ( int j = 0 ; j < pattern.length() ; j++ )
						if ( !Character.isDigit ( pattern.charAt(j) ) )
							flag = false;
					if ( flag ) cc = 5;
				}
				if ( cc != -1 )
				{
					StyleConstants.setForeground ( attr , colors[cc] );
					doc.setCharacterAttributes ( x , i-x+1 , attr , true );
					StyleConstants.setForeground ( attr , Color.BLACK );
				}	
			}	
		}
		for ( int i = 0 ; i < codeText.length() ; i++ )
		{
			boolean flag = false;
			for ( int j = 0 ; j < operator.length ; j++ )
				if ( operator[j] == codeText.charAt ( i ) )
				{
					flag = true;
					break;
				}
			if ( flag )
			{
				StyleConstants.setForeground ( attr , Color.ORANGE );
				doc.setCharacterAttributes ( i , 1 , attr , true );
				StyleConstants.setForeground ( attr , Color.BLACK );
			}
		}
	}	

}
