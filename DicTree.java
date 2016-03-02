import java.io.*;
import javax.swing.*;

public class DicTree implements Cloneable
{
	static final int NUM = 28;
	private Node root;
	class Node 
	{
		Node [] branch = new Node[NUM];
		int num;
		Node ( )
		{
			for ( int i = 0 ; i < NUM ; i++ )
			   branch[i] = null;	
			num = 0;
		}
	};
	
	public Object clone() throws CloneNotSupportedException
	{
		Object result = super.clone();
		return result;
	}

	DicTree ( )
	{
		root = new Node ( );
	}

	public void add ( String str )
	{
		Node u = root;
		for ( int i = 0 ; i < str.length() ; i++ )
		{
			int x = str.charAt(i)-'a';
			if ( str.charAt(i) == '_' ) x = 26;
			if ( str.charAt(i) == '#' ) x = 27;
			if ( u.branch[x] == null ) u.branch[x] = new Node ( );
			u = u.branch[x];
		}
		u.num = 1;
	}

	public boolean check ( String str )
	{
		Node u = root;
		for ( int i = 0 ; i < str.length() ; i++ )
		{
			int x = str.charAt(i)-'a';
			if ( str.charAt(i) == '_' ) x = 26;
			if ( str.charAt(i) == '#' ) x = 27;
			if ( x < 0 || x > 27 ) return false;
			if ( u.branch[x] == null ) return false;
			u = u.branch[x];
		}
		if ( u.num == 1 ) return true;
		else return false;
	}

	/*public static void main ( String [] args )
	{
		DicTree temp;
		temp = new DicTree ( );
		temp.add ( "aadd" );
		if ( temp.check ( "aadd" ) )
			System.out.println ( "YES ");
		else System.out.println ( "No" );
	}*/
	
}
