import java.io.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class PasteAdjuster  extends KeyAdapter
{
	JTextPane board;
	SimpleAttributeSet attr;
	Paint painter;
	StyledDocument doc;
	CodeListener listener;

	public PasteAdjuster ( JTextPane board , CodeListener listener )
	{
		painter = new Paint ( );
		this.board = board;
		this.listener = listener;
		doc = (StyledDocument) board.getDocument();
		attr = new SimpleAttributeSet ( );
		StyleConstants.setForeground ( attr , Color.BLACK );
		StyleConstants.setBold ( attr , true );
		StyleConstants.setFontFamily ( attr , "Frutiger" );
		StyleConstants.setFontSize ( attr , 16 );
	}
	
	public void keyPressed ( KeyEvent e )
	{
		//System.out.println ( "we get the code" );
		if ( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V )
		{
			//System.out.println ( "in the code " );
			doc.removeDocumentListener( listener );
			//painter.paintCode ( doc );
			//doc.addDocumentListener ( listener );
		}
	}

	public void keyReleased ( KeyEvent e )
	{
		if ( e.isControlDown() && e.getKeyCode () == KeyEvent.VK_V )
		{
			//System.out.println ( "hello world !" );
			painter.paintCode( doc );
			doc.addDocumentListener ( listener );
		}
	}

	

}
