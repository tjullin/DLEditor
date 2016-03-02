import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class NumberLine 
{
	private JTextPane numLine;
	private JTextPane codeText;
	private NumLinesListener numLineListener; 
	private int cnt;
	private boolean mark[] = new boolean [40000];
	private SimpleAttributeSet attr;

	public NumberLine ( JSplitPane newSplit	)
	{
		this.numLine = (JTextPane) newSplit.getLeftComponent();
		this.codeText = (JTextPane) newSplit.getBottomComponent();
		numLineListener = new NumLinesListener ( );
		/*for ( int i = 0 ; i < 40000 ; i++ )
			mark[i] = false;*/
		for ( int i = 1; i < 17 ; i++ )
		{
			//mark[i] = true;
			String s = i+"";
			try
			{
				numLine.getDocument().insertString ( numLine.getDocument().getLength() , s+"\n" , attr );
			}
			catch ( BadLocationException error )
			{

			}
		}
		cnt = 17;
		attr = new SimpleAttributeSet ();
		StyleConstants.setForeground ( attr , Color.BLACK );
		StyleConstants.setFontSize ( attr , 16 );
		StyleConstants.setFontFamily ( attr , "Frutiger" );
		//StyleConstants.setBold ( attr , true );
		codeText.getDocument().addDocumentListener ( numLineListener );
	}

	class NumLinesListener implements DocumentListener
	{
		/*public void insertUpdate ( DocumentEvent e )
		{
			Document doc = e.getDocument();
			Document doc2 = numLine.getDocument();
			int offset = e.getOffset();
			int len = e.getLength();
			String text =null;
			try
			{
				text = doc.getText ( offset , len );
			}
			catch ( BadLocationException error )
			{
			}
			for ( int i = 0 ; i < text.length() ; i++ )
				if ( text.charAt(i) == '\n' )
				{
					if ( cnt > 10000 ) return;
					if ( mark[cnt] ) 
					{
						cnt++;
						continue;
					}
					String s = ""+cnt+"\n";
					try
					{
						doc2.insertString ( doc2.getLength() , s , attr );
					}
					catch ( BadLocationException error )
					{
					}
					mark[cnt] = true;
					cnt++;
				}
		}*/

		public void insertUpdate ( DocumentEvent e )
		{
			Document doc = e.getDocument();
			Document doc2 = numLine.getDocument();
			int offset = e.getOffset();
			int len = e.getLength();
			int current = DocAttribute.getLineAtCaret ( codeText );
			while ( current >= cnt ) 
			{
				String s = ""+cnt +"\n";
				try
				{
					doc2.insertString ( doc2.getLength() , s , attr );
				}
				catch ( BadLocationException error )
				{
				}
				cnt++;
			}
		}

		public void removeUpdate ( DocumentEvent e )
		{

		}

		public void changedUpdate ( DocumentEvent e )
		{
		}
	}

}
