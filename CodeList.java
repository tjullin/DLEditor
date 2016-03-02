import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class CodeList
{
	private JTree tree;
	private int cnt;
	Vector vector;

	static final String [] family = 
	{
		"string algorithm","dynamic plan","sort algorithm",
		"number theory","graph theory","data structure"
	};
	
	static final String[][] code=
	{
		{"kmp","extend-kmp","AC automation" , "trie" , "splay", "LCT" },
		{"0/1 bag","complete bag","multiple bags" , "lca" , "rmq"},
		{"merge sort", "quick sort"},
		{"getPrime"},
		{"hungary","dinic","floyd","dijkstra","spfa","prime","kruskal"},
		{"segmentTree","splay","LCT"}
	};
	
	
	CodeList ( )
	{
		cnt = 0;
		vector = new Vector();
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("code");
		for ( int i = 0 ; i < family.length ; i++ )
		{
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode (family[i]);
			top.add ( node1 );
			for ( int j = 0 ; j < code[i].length ; j++ )
			{
				vector.addElement ( "Hello World!" );
				node1.add( new DefaultMutableTreeNode ( new Code ( code[i][j] , cnt++ ) ) );
			}
		}
		tree = new JTree ( top );
	}

	JTree getTree ( )
	{
		return tree;
	}

}

class Code
{
	private int id;
	private String title;

	Code ( String title , int id )
	{
		this.title = title;
	}
	
	public String toString ( )
	{
		return title;
	}

}
