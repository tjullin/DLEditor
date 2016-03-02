import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.awt.Color;


public class CodeListener implements DocumentListener
{
	//private Document doc;
	//private int offset,length,changeLen;
	private SimpleAttributeSet attr;
	
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
		"upper_bound"
	};

	static final Color [] colors=
	{
		Color.BLUE,Color.YELLOW,Color.GREEN,Color.PINK
	};

	CodeListener ( )
	{
		attr = new SimpleAttributeSet ( );
		StyleConstants.setBold( attr ,true);
		StyleConstants.setFontFamily ( attr , "Frutiger");
		StyleConstants.setFontSize ( attr , 16 );
	}

	private void Pattern ( Document doc , int offset , int changeLen )
	{
		length = doc.getLength();
		String pattern;
		int l,r,ll,rr,x;
		l = offset+changeLen-15;
		x = 15-changeLen;
		r = offset+15;
		if ( l < 0 ) 
		{
			x+=l;
			l = 0;
		}
		if ( r >= length ) r = length;
		pattern = doc.getText ( l , r-l );
			if ( pattern.charAt(x) == ' ' ||
				 pattern.charAt(x) == '\n' )
				return;
		for ( int i = x; i >= 0 ; i-- )
			if ( pattern.charAt(i) == ' ' || 
				pattern.charAt(i) == '\n' )
				break;
			else l = i;
		for ( int i = x; i < pattern.length() ; i++ )
			if ( pattern.charAt(i) == ' ' || 
				pattern.charAt(i) == '\n' )
				break;
			else r = i+1;
		pattern = pattern.substring ( l , r );
		System.out.println( " the pattern : " + pattern );
		ll = offset+l-x;
		rr = offset+r-x;
		return pattern;
	}

	private int check ( String pattern )
	{
		for ( int i = 0 ; i < keyWords1.length ; i++ )
			if ( pattern.equals ( keyWords1[i] ) )
				return 0;
		for ( int i = 0 ; i < keyWords2.length ; i++ )
			if ( pattern.equals ( keyWords2[i] ) )
				return 1;
		for ( int i = 0 ; i < dataType.length ; i++ )
			if ( pattern.equals ( dataType[i] ) )
				return 2;
		for ( int i = 0 ; i < STL.length ; i++ )
			if ( pattern.equals ( STL[i] ) )
				return 3;
		return -1;
	}

	public void insertUpdate ( DocumentEvent e )
	{
		doc = e.getDocument();
		offset = e.getOffset();
		changeLen = e.getLength();
		length = doc.getLength();
		String pattern;
		int l,r,ll,rr,x;
		l = offset+changeLen-15;
		x = 15-changeLen;
		r = offset+15;
		if ( l < 0 ) 
		{
			x+=l;
			l = 0;
		}
		if ( r >= length ) r = length;
		if ( changeLen == 1 )
		{
			//get the pattern
			try
			{
				pattern = doc.getText ( l , r-l );
				if ( pattern.charAt(x) == ' ' ||
					 pattern.charAt(x) == '\n' )
					return;
				for ( int i = x; i >= 0 ; i-- )
					if ( pattern.charAt(i) == ' ' || 
						 pattern.charAt(i) == '\n' )
						break;
					else l = i;
				for ( int i = x; i < pattern.length() ; i++ )
					if ( pattern.charAt(i) == ' ' || 
						 pattern.charAt(i) == '\n' )
						break;
					else r = i+1;
				pattern = pattern.substring ( l , r );
				System.out.println( " the pattern : " + pattern );
				ll = offset+l-x;
				rr = offset+r-x;
				int id = check ( pattern );
				if ( id != -1 )
				{
					System.out.println ( id + " " + ll + " " + rr );
					//doc.remove ( ll , rr-ll+1 );
					//System.out.println ( "OKAY" );
					StyleConstants.setForeground( attr , colors[id] );
					doc.insertString ( ll , pattern , attr );
					System.out.println ( "finish" );
				}
			}
			catch ( Exception e1 )
			{}
		}
	}

	public void removeUpdate ( DocumentEvent e )
	{

	}

	public void changedUpdate ( DocumentEvent e )
	{

	}

}
















