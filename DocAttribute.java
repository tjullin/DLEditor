import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class DocAttribute
{
	//return the column number(start at 1)  where the cursor at
	public static int getColumnAtCaret (  JTextComponent component )
	{
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		int line = root.getElementIndex ( caretPosition );
		int lineStart = root.getElement ( line ).getStartOffset();
		return caretPosition - lineStart + 1;
	}

	//return the start of the required line 
	public static int getLineStart ( JTextComponent component , int line )
	{
		int lineNumber = line -1;
		Element root = component.getDocument().getDefaultRootElement();
		int lineStart = root.getElement ( lineNumber ).getStartOffset();
		return lineStart;
	}
	
	//return the sum of the character selected
	public static int geSelectedNumber ( JTextComponent component )
	{
		if ( component.getSelectedText() == null ) 
			return 0;
		else 
			return component.getSelectedText().length();
	}

	//return the line number where the cursor at
	public static int getLineAtCaret ( JTextComponent component )
	{
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		return root.getElementIndex( caretPosition ) + 1;	
	}

	//return the sum of the lines in the editBoard
	public static int getLines ( JTextComponent component )
	{
		Element root = component.getDocument().getDefaultRootElement();
		return root.getElementCount();
	}

	//return the sum of the characters in the editBoard
	public static int getCharNumber ( JTextComponent component )
	{
		Document doc = component.getDocument();
		return doc.getLength();
	}

}
