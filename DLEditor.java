import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.*;
import java.io.*;
import javax.swing.plaf.*;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/*==================directory=======================
1.menu layout-----------------------------------109
2.main body layout------------------------------183
3.mode changed----------------------------------272
4.syntax highlight------------------------------277
===================================================*/

/*=================function implements==============
1.syntax highligh------------------------other file
2.mode changed----------------------------------298
3.menu functions implementations(File)----------345
	-1.TabChanged-------------------------------346
	-2.CreateFile-------------------------------363
	-3.ShutDownFile-----------------------------393
	-4.OpenFile---------------------------------686
	-5.SaveFile---------------------------------772
	-6.SaveToAnotherFile------------------------985
4.menu fuctions implementations(Edit)----------1191
5.compile and run------------------------------1233
	-1.compile---------------------------------1235
	-2.run-------------------------------------1375
6.numberLine-----------------------------other file
7.replace---------------------------other file,1600
8.find------------------------------other file,1500

===================================================*/



//DL Code Editor's start-part 
public class DLEditor 
{
	DLEditor()
	{

	}
	//set the font
	private static void InitGlobalFont(Font font) 
	{  
		FontUIResource fontRes = new FontUIResource(font);  
		for (Enumeration<Object> keys = UIManager.getDefaults().keys();  keys.hasMoreElements(); ) 
		{  
			Object key = keys.nextElement();  
			Object value = UIManager.get(key);  
			if (value instanceof FontUIResource) 
			{  
				UIManager.put(key, fontRes);  
			}  
		}
	}

	public static void main ( String args[] )
	{
		InitGlobalFont(new Font("Frutiger", Font.PLAIN, 16));  
		// set the font in the global environment
		EventQueue.invokeLater( new Runnable ()
			{
				public void run ( )
				{
					IDE ide = new IDE();
					System.out.println("YES");
					ide.setVisible(true);	
				}
			});
	}
}

//The main body of the DL Code Editor
class IDE extends JFrame 
{
	/*==============Menu=============================*/
	JMenuBar menuBar;//menu of the window
	JPopupMenu popup;//rightClick menu
	JMenu file,edit,tool,help;//contents of the menu
	//MenuItems in File 
	JMenuItem create,open,save,saveAs,close,exit;
	//MenuItems in edit
	JMenuItem copy,cut,paste,delete,selectAll,find,replace;
	//MenuItems in tools
	JMenuItem compile,runFile;
	//MenuItems in help
	JMenuItem IDEHelp,aboutDL;
	//RightClick menu
	JMenuItem rcopy,rcut,rpaste,rdelete,rselectAll;
	//menuTab
	int numTab;
	/*===============================================*/

/*==================main body=============================*/

	JSplitPane outerSPane,middleSPane,innerSPane,boardSPane;
	/*==============editBoard========================*/
	JTextPane editBoard;
	JScrollPane editScroll;
	JTabbedPane editTab;
	JTextPane numLine;
	JFileChooser saveChooser,openChooser;
	LinkedList tabList;
	Paint painter;
	NumberLine numberLine;
	/*===============================================*/

	/*=============compileBoard======================*/
	JTextArea compileInfo;
	JScrollPane compileScroll;
	JPanel compileBoard;
	JLabel compileLabel;
	Thread compiler;
	Thread run_file;
	CompileE compileE;
	RunE runE;
	FindE findE;
	ReplaceE replaceE;
	/*=============compileBoard======================*/

	/*=============commandBoard======================*/
	JTextPane commandEdit;
	JScrollPane commandScroll;
	JPanel commandBoard;
	JLabel commandLabel;
	/*===============================================*/

	/*=============libraryBoard======================*/
	//JTextArea libraryList;
	JTree libraryList;
	JScrollPane libraryScroll;
	JPanel libraryBoard;
	JLabel libraryLabel;
	/*===============================================*/
/*========================================================*/


/*==========syntax highlight=============================*/
	CodeListener codeChanged;
/*=======================================================*/

	
/*=========Mode changed==============*/
	boolean focusOn;
	ModeAdapter modeAdapter;
	MenuPopupE menuPopup;
	JTextPane textFocusOn;
	CreateE createE;
	OpenE openE;
	SaveE saveE;
	PasteAdjuster pasteAdjuster;
	ShutDownTabbedE closeE;
	SaveAsE saveAsE;
	EditE editE;
	TabPaneChangedE tabPaneChangedE;
	DocumentE documentE;
/*====================================*/

	public IDE ( )
	{
/*=================Menu layout========================*/
		menuBar = new JMenuBar();
		setJMenuBar( menuBar );
		//set the "file" item
		file = new JMenu("File(F)");
		file.setMnemonic('F');
		menuBar.add( file );
		//set the "edit" item
		edit = new JMenu("Edit(E)");
		edit.setMnemonic('E');
		menuBar.add( edit );
		//set the "tool" item
		tool = new JMenu("Tool(T)");
		tool.setMnemonic('T');
		menuBar.add( tool );
		//set the "help" item
		help = new JMenu("Help(H)");
		help.setMnemonic('H');
		menuBar.add( help );
		//set the rightClick items
		popup = new JPopupMenu();
		popup.addSeparator();
	    /*======set items under the "File"=====*/
		create = new JMenuItem("New...",'N' );
		create.setAccelerator( KeyStroke.getKeyStroke("ctrl N"));
		file.add ( create );
		open = new JMenuItem("Open" );
		open.setAccelerator( KeyStroke.getKeyStroke("ctrl O"));
		file.add ( open );
		save = new JMenuItem("Save" );
		save.setAccelerator( KeyStroke.getKeyStroke("ctrl S"));
		file.add ( save );
		saveAs = new JMenuItem("SaveTo..." );
		open.setAccelerator( KeyStroke.getKeyStroke("ctrl T"));
		file.add ( saveAs );
		close = new JMenuItem("Close" );
		open.setAccelerator( KeyStroke.getKeyStroke("ctrl E"));
		file.add ( close );
		/*=====================================*/	

		/*====set items under the "Edit"=====*/
		find = new JMenuItem("Find...",'F');
		find.setAccelerator( KeyStroke.getKeyStroke("ctrl F"));
		edit.add ( find );
		replace = new JMenuItem("Replace...",'R');
		replace.setAccelerator( KeyStroke.getKeyStroke("ctrl R"));
		edit.add ( replace );
		/*===================================*/

		/*====set items under the "Tool"=====*/
		compile = new JMenuItem("Compile");
		compile.setAccelerator( KeyStroke.getKeyStroke("ctrl F5"));
		tool.add ( compile );
		runFile = new JMenuItem("Run");
		runFile.setAccelerator( KeyStroke.getKeyStroke("ctrl F9"));
		tool.add ( runFile );
		/*===================================*/

		/*====set items under the "Help"=====*/
		aboutDL = new JMenuItem("About DL...");
		help.add ( aboutDL );
		/*===================================*/

		/*===set items under the popup=======*/
		rcopy = new JMenuItem("Copy");
		popup.add ( rcopy );
		rcut = new JMenuItem("Cut");
		popup.add ( rcut );
		rpaste = new JMenuItem("Paste");
		popup.add ( rpaste );
		rdelete = new JMenuItem ("delete");
		popup.add ( rdelete );
		rselectAll = new JMenuItem ( "selectAll");
		popup.add ( rselectAll );
		/*===================================*/
/*===========================================================*/

/*====================main body layout=======================*/

		/*===========edit region=============*/
		saveChooser = new JFileChooser();
		openChooser = new JFileChooser();
		painter = new Paint ( );
		editTab = new JTabbedPane
			( JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT );
		editBoard = new JTextPane();
		editBoard.requestFocus();
		numLine = new JTextPane();
		numLine.setEditable (false);
		/*editScroll = new JScrollPane(editBoard);
		boardSPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
				true,
				numLine,
				editScroll
				);*/
		//*****************corrected**************************//
		boardSPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
				true,
				numLine,
				editBoard
				);
		editScroll = new JScrollPane ( boardSPane );
		//******************corrected*************************//
		//editTab.addTab ( "new_file_1" , boardSPane );
		editTab.addTab ( "new_file_1" , editScroll );
		numTab = 2;
		tabList = new LinkedList();
		TabProperty temp = new TabProperty ( 
			editTab.getTabCount()-1,
			false,
			null);
		tabList.add ( temp );
		/*===================================*/

		/*===========compile region==========*/
		compiler = new Thread ( );
		run_file = new Thread ( );
		compileInfo = new JTextArea();
		compileInfo.setEditable(false);
		compileScroll = new JScrollPane(compileInfo);
		compileLabel = 
			new JLabel ( "Compile Result" , JLabel.LEFT );
		compileBoard = new JPanel();
		compileBoard.setLayout( new BorderLayout() );
		compileBoard.add ( compileLabel , BorderLayout.NORTH);
		compileBoard.add ( compileScroll );
		/*===================================*/

		/*==========command region===========*/
		commandEdit = new JTextPane();
		commandScroll = new JScrollPane(commandEdit);
		commandLabel = 
			new JLabel ( "Command Edit" , JLabel.LEFT );
		commandBoard = new JPanel();
		commandBoard.setLayout( new BorderLayout() );
		commandBoard.add( commandLabel , BorderLayout.NORTH);
		commandBoard.add( commandScroll );
	    /*===================================*/	

		/*========library board==============*/
		CodeList codeList = new CodeList();
		libraryList = codeList.getTree();
		libraryList.setEditable(false);
		libraryScroll = new JScrollPane(libraryList);
		libraryLabel = 
			new JLabel ( "Code Library" , JLabel.LEFT );
		libraryBoard = new JPanel();
		libraryBoard.setLayout( new BorderLayout() );
		libraryBoard.add( libraryLabel , BorderLayout.NORTH);
		libraryBoard.add( libraryScroll );
		/*===================================*/

		/*========total Layout===============*/
        innerSPane =
			new JSplitPane ( JSplitPane.VERTICAL_SPLIT , 
					true ,
					editTab,
					commandBoard
					);
		middleSPane =
			new JSplitPane ( JSplitPane.HORIZONTAL_SPLIT ,
					true ,
					libraryBoard,
					innerSPane
					);
		outerSPane =
			new JSplitPane ( JSplitPane.VERTICAL_SPLIT ,
					true ,
					middleSPane,
					compileBoard
					);
		boardSPane.setOneTouchExpandable(true);
		innerSPane.setOneTouchExpandable(true);
		middleSPane.setOneTouchExpandable(true);
		outerSPane.setOneTouchExpandable(true);
		boardSPane.setDividerLocation(30);
		innerSPane.setDividerLocation(420);
		middleSPane.setDividerLocation(150);
		outerSPane.setDividerLocation(530);
		/*===================================*/

		/*========set listeners===============*/
		modeAdapter = new ModeAdapter();
		compileE = new CompileE ( );
		runE = new RunE();
		findE = new FindE();
		replaceE = new ReplaceE();
		menuPopup = new MenuPopupE();
		createE = new CreateE ( );
		openE = new OpenE();
		saveE = new SaveE();
		closeE = new ShutDownTabbedE ( );
		saveAsE = new SaveAsE ( );
		editE = new EditE();
		documentE = new DocumentE();
		tabPaneChangedE = new TabPaneChangedE();
		numberLine = new NumberLine ( boardSPane );
		textFocusOn = null;
		editTab.addChangeListener ( tabPaneChangedE );
		editBoard.addFocusListener ( modeAdapter );
		commandEdit.addFocusListener ( modeAdapter );
		editBoard.addMouseListener ( menuPopup );
		commandEdit.addMouseListener ( menuPopup );
		create.addActionListener ( createE );
		open.addActionListener ( openE );
		save.addActionListener ( saveE );
		close.addActionListener ( closeE );
		saveAs.addActionListener ( saveAsE );
		rcopy.addActionListener ( editE );
		rcut.addActionListener ( editE );
		rpaste.addActionListener ( editE );
		rdelete.addActionListener ( editE );
		rselectAll.addActionListener ( editE );
		compile.addActionListener ( compileE );
		runFile.addActionListener ( runE );
		find.addActionListener ( findE );
		replace.addActionListener ( replaceE );
		editBoard.getDocument().addDocumentListener ( documentE );
		/*===================================*/

/*===========================================================*/
				
/*=============syntax highlight==============================*/
		codeChanged = new CodeListener();
		modeAdapter = new ModeAdapter();
		editBoard.getDocument().addDocumentListener( codeChanged );
		pasteAdjuster = new PasteAdjuster ( editBoard , codeChanged );
		editBoard.addKeyListener ( pasteAdjuster );
		editBoard.addFocusListener ( modeAdapter );
		commandBoard.addFocusListener ( modeAdapter );
/*===========================================================*/		
		//start!!!!
		setVisible(true);
		Container con = getContentPane();
		setBounds(50,50,960,680);
		con.setLayout(new BorderLayout());
		con.add( outerSPane , BorderLayout.CENTER );
		validate();
	}

/*===================Mode Changed implements==================*/
	class ModeAdapter extends FocusAdapter
	{
		public void focusGained ( FocusEvent e )
		{
			textFocusOn = (JTextPane)e.getSource();
			if ( textFocusOn == commandEdit )
				focusOn = false;
			else focusOn  = true;
		}

		public void focusLost ( FocusEvent e )
		{
			textFocusOn = (JTextPane)e.getSource();
		}
	}
/*===========================================================*/

/*====================Menu function implements(File)===============*/
	class TabPaneChangedE implements ChangeListener
	{
		public void stateChanged ( ChangeEvent e )
		{
			/*JSplitPane temp = 
				(JSplitPane)editTab.getSelectedComponent();
			JScrollPane middle = 
				(JScrollPane)temp.getBottomComponent();
			textFocusOn =
				(JTextPane)middle.getViewport().getView();
			textFocusOn.requestFocus();*/
			//textFocusOn.getDocument().addDocumentListener ( pasteAdjuster );
			JScrollPane temp =
				(JScrollPane)editTab.getSelectedComponent();
			JSplitPane middle =  
				(JSplitPane)temp.getViewport().getView();
			textFocusOn =
				(JTextPane)middle.getBottomComponent();
			textFocusOn.requestFocus();
		}
	}

	class CreateE implements ActionListener
	{
		public void actionPerformed ( ActionEvent e )
		{
			/*JTextPane newText = new JTextPane();
			JTextPane newNum = new JTextPane();
			JScrollPane  newScroll = new JScrollPane(newText);
			JSplitPane newSplit = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT,
					true,
					newNum,
					newScroll);*/
			JTextPane newText = new JTextPane();
			JTextPane newNum = new JTextPane();
			JSplitPane newSplit = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT,
					true,
					newNum,
					newText);
			JScrollPane newScroll = new JScrollPane ( newSplit );
			newNum.setEditable ( false );
			newSplit.setDividerLocation ( 30 );
			newSplit.setOneTouchExpandable( true );
			newText.requestFocus ( );
			numberLine = new NumberLine ( newSplit );
			editTab.addTab ( "new-file-" + numTab , newScroll );
			numTab++;
			editTab.setSelectedIndex ( editTab.getTabCount()-1 );
			TabProperty infoTab = new TabProperty(
					editTab.getTabCount()-1,false,null);
			tabList.add ( infoTab );
			newText.getDocument().addDocumentListener(codeChanged);
			newText.getDocument().addDocumentListener(documentE);
			newText.addFocusListener(modeAdapter);
			newText.addMouseListener( menuPopup );
			newText.requestFocus();
			newText.addKeyListener ( new PasteAdjuster ( newText , codeChanged ) );
			close.setEnabled(true);
		}
	}

	class ShutDownTabbedE implements ActionListener
	{

		public void actionPerformed ( ActionEvent e )
		{
			File saveFile = null;
			int saveChooserState;
			BufferedWriter out;
			TabProperty selectedTab=null;
			FileWriter file_writer;
			JScrollPane saveScroll;
			JSplitPane saveSplit;
			JTextPane saveText;
			boolean askIfCover;
			int shutDownIndex=0;
			int ifCover;
			String [] lines;
			int selectedIndex = editTab.getSelectedIndex();
			for ( int i = 0 ; i < tabList.size() ; i++ )
			{
				selectedTab = (TabProperty)tabList.get(i);
				if ( selectedTab.tabIndex == selectedIndex )
				{
					shutDownIndex = i; 
					break;
				}
			}
			if ( selectedTab.changed == true )
			{
				int confirmDialogState= 
					JOptionPane.showConfirmDialog(
						IDE.this,
						"save before shutdown?",
						"Confirm",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				// if user choose CANCEL
				if (confirmDialogState==JOptionPane.CANCEL_OPTION )
					return;
				// if user choose YES
				if ( confirmDialogState==JOptionPane.YES_OPTION )
				{
					if ( selectedTab.tabPath == null )
					{
						saveChooser.setCurrentDirectory(
								new File("."));
						saveChooserState=
							saveChooser.showSaveDialog(IDE.this);
						saveFile = saveChooser.getSelectedFile();
						askIfCover = false;
						while ( saveFile!=null&&
								saveChooserState==JFileChooser.APPROVE_OPTION )
						{
							if ( askIfCover== true )
							{
								saveChooser.setCurrentDirectory ( new File("."));
								saveChooserState = saveChooser.showSaveDialog ( IDE.this );
								saveFile = saveChooser.getSelectedFile();
							}
							if ( saveChooserState==JFileChooser.CANCEL_OPTION ) 
								return;
							if ((saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION))
							{
								if (!saveFile.exists())
								{
									try
									{
										saveFile.createNewFile();
									}
									catch ( Exception error )
									{
										JOptionPane.showMessageDialog(
												IDE.this,
												"save File meets errors", 
												"Errors Report!",
												JOptionPane.ERROR_MESSAGE);
										return;
									}
									try
									{
										file_writer = new FileWriter(saveFile);
										out = new BufferedWriter( file_writer );
										/*saveSplit = (JSplitPane)editTab.getSelectedComponent();
										saveScroll = (JScrollPane)saveSplit.getBottomComponent();
										saveText = (JTextPane)saveScroll.getViewport().getView();*/
										saveScroll = (JScrollPane)editTab.getSelectedComponent();
										saveSplit = (JSplitPane)saveScroll.getViewport().getView();
										saveText = (JTextPane)saveSplit.getBottomComponent();
										lines = saveText.getText().split("\n" );
										for ( int i = 0 ; i < lines.length; i++ )
											out.write(lines[i]+"\n");
										out.close();
										file_writer.close();
									}
									catch ( Exception error )
									{
										JOptionPane.showMessageDialog(
											IDE.this,
											"save file meets errors",
											"Errors Report",
											JOptionPane.ERROR_MESSAGE);
										return;
									}
									editTab.setTitleAt ( 
										editTab.getSelectedIndex(),
									    saveChooser.getSelectedFile().getName());
								selectedTab.changed=false;
									try
									{
										selectedTab.tabPath=
											saveFile.getCanonicalPath();
									}
									catch ( Exception error )
									{
										JOptionPane.showMessageDialog(
											IDE.this,
											"meets error when get the path of the file",
											"Errors Report",
											JOptionPane.ERROR_MESSAGE);
										return;
									}
									editTab.remove ( editTab.getSelectedIndex());
									if ( editTab.getTabCount()<2)
										close.setEnabled(false);	
									tabList.remove ( shutDownIndex );
									for ( int i = 0 ; i <tabList.size();i++)
									{
										selectedTab = ( TabProperty ) tabList.get(i);
										if ( selectedTab.tabIndex > shutDownIndex )
											selectedTab.tabIndex = selectedTab.tabIndex-1;
									}
									return;
								}
							}
							//if the required file exits
							else
							{
								ifCover = 
									JOptionPane.showConfirmDialog(
										saveChooser,
									   	"the file exits continue",
								 		"Confirm",
										JOptionPane.ERROR_MESSAGE
								);
								if ( ifCover == JOptionPane.NO_OPTION)
								{
									askIfCover = true;
									continue;
								}
								if ( ifCover == JOptionPane.YES_OPTION  )
								{
									try
									{
										file_writer = new FileWriter ( saveFile );
										out = new BufferedWriter(file_writer);
										/*saveSplit = (JSplitPane)
											editTab.getSelectedComponent();
										saveScroll = (JScrollPane) saveSplit.getBottomComponent();
										saveText = (JTextPane)saveScroll.getViewport().getView();*/
										saveScroll = (JScrollPane)editTab.getSelectedComponent();
										saveSplit = (JSplitPane)saveScroll.getViewport().getView();
										saveText = (JTextPane)saveSplit.getBottomComponent();
										lines = saveText.getText().split("\n");
										for ( int i = 0 ; i < lines.length ; i++ )
											out.write ( lines[i]+"\n" );
										out.close();
										file_writer.close();
									}
									catch ( Exception error )
									{
										JOptionPane.showMessageDialog ( IDE.this,
												"save files meets errors",
												"Errors Report",
												JOptionPane.ERROR_MESSAGE);
										return;
									}
									editTab.setTitleAt ( editTab.getSelectedIndex(),
											saveChooser.getSelectedFile().getName());
									selectedTab.changed=false;
									try
									{
										selectedTab.tabPath = saveFile.getCanonicalPath();
									}
									catch ( Exception error )
									{	
										JOptionPane.showMessageDialog ( IDE.this,
												"meets errors when get the path of the file",
												"Errors Report",
												JOptionPane.ERROR_MESSAGE);
										return;
									}
									editTab.remove ( editTab.getSelectedIndex());
									if ( editTab.getTabCount() < 2 )
									{
										close.setEnabled(false);
									}
									tabList.remove ( shutDownIndex );
									for ( int i = 0 ; i < tabList.size() ; i++ )
									{
										selectedTab = (TabProperty)tabList.get(i);
										if ( selectedTab.tabIndex > shutDownIndex )
											selectedTab.tabIndex = selectedTab.tabIndex-1;
									}
									return;
								}
								askIfCover = true; continue;		
							}
							return;
						}
						if ( saveChooserState == JFileChooser.CANCEL_OPTION )
							return;
						return;
					}
					//if the file doesn't exist
					else
					{
						saveFile = new File ( selectedTab.tabPath );
						if ( !saveFile.exists() )
						{
							try
							{
								saveFile.createNewFile();
							}
							catch ( Exception error )
							{
								JOptionPane.showMessageDialog ( IDE.this,
										"create files meets errors",
										"Errors Report",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
						try
						{
							file_writer = new FileWriter ( saveFile );
							out = new BufferedWriter ( file_writer );
							/*saveSplit = ( JSplitPane ) editTab.getSelectedComponent ();
							saveScroll = (JScrollPane) saveSplit.getBottomComponent ();
							saveText = (JTextPane)saveScroll.getViewport().getView();*/
							saveScroll = (JScrollPane)editTab.getSelectedComponent();
							saveSplit = (JSplitPane)saveScroll.getViewport().getView();
							saveText = (JTextPane)saveSplit.getBottomComponent();

							lines = saveText.getText().split("\n");
							for ( int i = 0 ; i < lines.length ; i++ )
								out.write ( lines[i]+"\n" );
							out.close();
							file_writer.close();
						}
						catch ( Exception error )
						{
							JOptionPane.showMessageDialog ( IDE.this,
									"fail to write the file",
									"Error Report",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						selectedTab.changed = false;
					}
					editTab.remove ( editTab.getSelectedIndex());
					if ( editTab.getTabCount() < 2 )
					{
						close.setEnabled ( false );
					}
					tabList.remove ( shutDownIndex );
					for ( int i = 0 ; i < tabList.size() ; i++ )
					{
						selectedTab = ( TabProperty ) tabList.get(i);
						if ( selectedTab.tabIndex > shutDownIndex )
						{
							selectedTab.tabIndex = selectedTab.tabIndex - 1;
						}
					}
					return;
				}
				return;
			}
			editTab.remove ( shutDownIndex );
			for ( int i = 0 ; i < tabList.size() ; i++ )
			{
				selectedTab = (TabProperty)tabList.get(i);
				if ( selectedTab.tabIndex > shutDownIndex )
					selectedTab.tabIndex = selectedTab.tabIndex-1;
			}
		}
	}
	// open file implementations
	class OpenE implements ActionListener
	{
		public void actionPerformed ( ActionEvent e )
		{
			int openChooserState;
			File openFile;
			FileReader file_reader=null;
			BufferedReader in=null;
			String readText=null;
			String openFilePath=null;
			openChooser.setCurrentDirectory( new File(".") );
			openChooserState = openChooser.showOpenDialog ( IDE.this );
			openFile = openChooser.getSelectedFile();
			SimpleAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setBold ( attr , true );
			StyleConstants.setFontFamily ( attr , "Frutiger" );
			StyleConstants.setFontSize ( attr , 16 );
			JTextPane newText;
			JTextPane newNum;
			JScrollPane newScroll;
			JSplitPane newSplit;
			if ( openFile != null && openChooserState == JFileChooser.APPROVE_OPTION )
			{
				try
				{
					System.out.println ( "in the pattern " );
					String [] segment = null;
					file_reader = new FileReader ( openFile );
					in = new BufferedReader ( file_reader );			
					newText = new JTextPane();
					newNum = new JTextPane();
					/*newScroll = new JScrollPane(newText);
					newSplit = new JSplitPane(
						JSplitPane.HORIZONTAL_SPLIT,
						true,
						newNum,
						newScroll);*/
					newSplit = new JSplitPane (
						JSplitPane.HORIZONTAL_SPLIT,
						true,
						newNum,
						newText);
					newScroll = new JScrollPane ( newSplit );
					newNum.setEditable ( false );
					newSplit.setDividerLocation ( 30 );
					newSplit.setOneTouchExpandable( true );
					newText.requestFocus ( );
					numberLine = new NumberLine ( newSplit );
					newText.getDocument().addDocumentListener ( documentE );
					newText.getDocument().addDocumentListener ( documentE );
					newText.addKeyListener ( new PasteAdjuster ( newText , codeChanged ) );
					editTab.addTab ( openChooser.getSelectedFile().getName() , newScroll );
					StyledDocument doc=null;
					while ( (readText=in.readLine())!=null )
					{
						doc = (StyledDocument)newText.getDocument();
						segment = readText.split ( " " );
						/*for ( int i = 0 ; i < segment.length ; i++ )
						{
							doc.insertString ( doc.getLength() , segment[i] + " " , attr );
							doc.remove ( doc.getLength()-1, 1 );
							doc.insertString ( doc.getLength() , " " , attr );
						}*/
						doc.insertString ( doc.getLength() , readText+"\n" , attr );
					}
					painter.paintCode ( doc );
					in.close();
					file_reader.close();
					openFilePath = openChooser.getSelectedFile().getCanonicalPath();
				}
				catch ( Exception error )
				{
					JOptionPane.showMessageDialog ( IDE.this,
							"open file meets errors",
							"Errors Report",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				TabProperty tabInfo = new TabProperty ( 
						editTab.getTabCount()-1,
						false,
						openFilePath);
				tabList.add ( tabInfo );
			    //focus listener;
				newText.addMouseListener ( menuPopup );
			    newText.requestFocus();
			    close.setEnabled(true);	
				newText.getDocument().addDocumentListener ( codeChanged );
			}
			if ( openChooserState == JFileChooser.CANCEL_OPTION )
				return;
		}
	}

	//save function implementation
	class SaveE implements ActionListener
	{
		public void actionPerformed ( ActionEvent e )
		{
			JSplitPane saveSplit=null;
			JScrollPane saveScroll=null;
			JTextPane saveText=null;
			TabProperty selectedTab=null;
			File saveFile=null;
			FileWriter file_writer=null;
			BufferedWriter out=null;
			String [] lines=null;
			boolean askIfCover=false;
			int saveChooserState;
			int ifCover=0;
			for ( int i = 0 ; i < tabList.size() ; i++ )
			{
				selectedTab = ( TabProperty ) tabList.get(i);
				if ( selectedTab.tabIndex == editTab.getSelectedIndex() )
					break;
			}
			//if the tab has not been saved
			if ( selectedTab.tabPath == null )
			{
				saveChooser.setCurrentDirectory ( new File(".") );
				saveChooserState = saveChooser.showSaveDialog ( IDE.this );
				saveFile = saveChooser.getSelectedFile();
				askIfCover = false;
				while ( (saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION) )
				{
					if ( askIfCover == true )
					{
						saveChooser.setCurrentDirectory ( new File(".") );
						saveChooserState = saveChooser.showSaveDialog ( IDE.this );
						saveFile = saveChooser.getSelectedFile();
					}
					if ( saveChooserState == JFileChooser.CANCEL_OPTION )
						return;
					if ( ( saveFile != null )&& (saveChooserState ==  JFileChooser.APPROVE_OPTION) )
					{
						if ( !saveFile.exists() )
						{
							try 
							{
								saveFile.createNewFile();
							}
							catch ( Exception error )
							{
								JOptionPane.showMessageDialog ( IDE.this,
										"meets errors when create a new file",
										"Errors Report",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							try
							{
								file_writer = new FileWriter ( saveFile );
								out = new BufferedWriter ( file_writer );
								/*saveSplit = (JSplitPane)editTab.getSelectedComponent ( );
								saveScroll = (JScrollPane)saveSplit.getBottomComponent ( );
								saveText = (JTextPane)saveScroll.getViewport().getView();*/
								saveScroll = (JScrollPane)editTab.getSelectedComponent();
								saveSplit = (JSplitPane)saveScroll.getViewport().getView();
								saveText = (JTextPane)saveSplit.getBottomComponent();
								lines = saveText.getText().split("\n");
								for ( int i = 0 ; i < lines.length ; i++ )
								{
									out.write ( lines[i] + "\n" );
								}
								out.close();
								file_writer.close();
							}
							catch ( Exception error )
							{
								JOptionPane.showMessageDialog ( IDE.this ,
										"meets errors when write to the file",
										"Errors Report",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						    editTab.setTitleAt ( editTab.getSelectedIndex() , saveChooser.getSelectedFile().getName());
							selectedTab.changed = false;
							try
							{
								selectedTab.tabPath = saveFile.getCanonicalPath();	
							}		
							catch ( Exception error )
							{
								JOptionPane.showMessageDialog ( IDE.this,
										"fail to find the path of the file ",
										"Errors Report",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							break;
						}
						//if the file exits
						else
						{
							ifCover = JOptionPane.showConfirmDialog ( saveChooser , 
								"file exists , continue to cover it ?",
								"Confirm Info",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);	
						}
						System.out.println ( "tell you i am running ");
						if ( ifCover == JOptionPane.NO_OPTION )
						{
							askIfCover = true;
							continue;
						}
						if ( ifCover == JOptionPane.YES_OPTION )
						{
							try
							{
								System.out.println ( "hello world!!!" );
								file_writer = new FileWriter ( saveFile );
								out = new BufferedWriter ( file_writer );
								/*saveSplit = (JSplitPane)editTab.getSelectedComponent ( );
								saveScroll = (JScrollPane)saveSplit.getBottomComponent ( );
								saveText = (JTextPane)saveScroll.getViewport().getView();*/
								saveScroll = (JScrollPane)editTab.getSelectedComponent();
								saveSplit = (JSplitPane)saveScroll.getViewport().getView();
								saveText = (JTextPane)saveSplit.getBottomComponent();
								lines = saveText.getText().split("\n");
								for ( int i = 0 ; i < lines.length ; i++ )
								{
									out.write ( lines[i] + "\n" );
								}
								out.close();
								file_writer.close();
							}
							catch ( Exception error )
							{
								JOptionPane.showMessageDialog ( IDE.this,
										"fail to write the file",
										"Errors Report",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							editTab.setTitleAt ( editTab.getSelectedIndex(),
									saveChooser.getSelectedFile().getName());
							selectedTab.changed = false;
							try
							{
								selectedTab.tabPath = saveFile.getCanonicalPath();
							}
							catch ( Exception error )
							{
								JOptionPane.showMessageDialog ( IDE.this,
										"fail to find the path of the file",
										"Errors Report",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							break;
						}
						askIfCover = true;
						continue;
					}
					return;
				}
				if ( saveChooserState == JFileChooser.CANCEL_OPTION )
					return;
				return;
			}
			// if the tab had been saved
			else
			{
				saveFile = new File ( selectedTab.tabPath );
				if ( saveFile.exists( ) )
				{
					saveFile.delete();
				}
				if ( !saveFile.exists())
				{
					try
					{
						saveFile.createNewFile();
					}
					catch ( Exception error )
					{
						JOptionPane.showMessageDialog ( IDE.this,
								"fail to create a new file",
								"Errors Report",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					try
					{
						file_writer = new FileWriter ( saveFile );
						out = new BufferedWriter ( file_writer );
						/*saveSplit = (JSplitPane)editTab.getSelectedComponent ( );
						saveScroll = (JScrollPane)saveSplit.getBottomComponent ( );
						saveText = (JTextPane)saveScroll.getViewport().getView();*/
						saveScroll = (JScrollPane)editTab.getSelectedComponent();
						saveSplit = (JSplitPane)saveScroll.getViewport().getView();
						saveText = (JTextPane)saveSplit.getBottomComponent();
						lines = saveText.getText().split("\n");
						for ( int i = 0 ; i < lines.length ; i++ )
							{
								out.write ( lines[i] + "\n" );
							}
						out.close();
						file_writer.close();
					}
					catch ( Exception error )
					{
						JOptionPane.showMessageDialog ( IDE.this,
								"fail to write to the file",
								"Errors Report",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					selectedTab.changed=false;
				}
			}
		}
	}

//save to another files
	class SaveAsE implements ActionListener
	{
		public void actionPerformed ( ActionEvent e )
		{
			String [] lines=null;
			FileWriter file_writer;
			BufferedWriter out;
			int saveChooserState;
			boolean askIfCover = false;
			File saveFile;
			JTextPane saveText;
			JSplitPane saveSplit;
			JScrollPane saveScroll;
			TabProperty selectedTab=null;
			saveChooser.setCurrentDirectory ( new File ( "." ) );
			saveChooserState = saveChooser.showSaveDialog ( IDE.this );
			saveFile = saveChooser.getSelectedFile();
			int ifCover = 0;
			while ( saveFile != null && saveChooserState == JFileChooser.APPROVE_OPTION )
			{
				if ( askIfCover==true )
				{
					saveChooser.setCurrentDirectory ( new File ("." ) );
					saveChooserState = saveChooser.showSaveDialog ( IDE.this );
					saveFile = saveChooser.getSelectedFile();
				}
				if ( saveChooserState == JFileChooser.CANCEL_OPTION )
					return;
				if ( (saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION))
				{
					//if the file doesn't exists
					if ( !saveFile.exists() )
					{
						try
						{
							saveFile.createNewFile();
						}
						catch ( Exception error )
						{
							JOptionPane.showMessageDialog ( IDE.this,
									"fail to create new file",
									"Error Report",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						try
						{	
							file_writer = new FileWriter ( saveFile );
							out = new BufferedWriter ( file_writer );
							/*saveSplit = (JSplitPane)editTab.getSelectedComponent ( );
							saveScroll = (JScrollPane)saveSplit.getBottomComponent ( );
							saveText = (JTextPane)saveScroll.getViewport().getView();*/
							saveScroll = (JScrollPane)editTab.getSelectedComponent();
							saveSplit = (JSplitPane)saveScroll.getViewport().getView();
							saveText = (JTextPane)saveSplit.getBottomComponent();
							lines = saveText.getText().split("\n");
							for ( int i = 0 ; i < lines.length ; i++ )
							{
								out.write ( lines[i] + "\n" );
							}
							out.close();
							file_writer.close();
						}
						catch ( Exception error )
						{
							JOptionPane.showMessageDialog ( IDE.this,
									"fail to write to the file",
									"Errors Report",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						editTab.setTitleAt ( editTab.getSelectedIndex(),
								saveChooser.getSelectedFile().getName());
						for ( int i = 0 ; i < tabList.size() ; i++ )
						{
							selectedTab = (TabProperty)tabList.get(i);
							if ( selectedTab.tabIndex == editTab.getSelectedIndex() )
								break;
						}
						selectedTab.changed=false;
						try
						{
							selectedTab.tabPath = saveFile.getCanonicalPath();
						}
						catch ( Exception error )
						{
							JOptionPane.showMessageDialog( IDE.this,
									"fail to find the path of the file",
									"Errors Report",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						break;
					}
					//if the file exists
					else
					{
						ifCover = JOptionPane.showConfirmDialog ( saveChooser,
							"file exists, continue?",
							"Confirm Info",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);			
					}
					if ( ifCover == JOptionPane.NO_OPTION )
					{
						askIfCover = true;
						continue;
					}
					if ( ifCover == JOptionPane.YES_OPTION )
					{
						try
						{
						file_writer = new FileWriter ( saveFile );
						out = new BufferedWriter ( file_writer );
						/*saveSplit = (JSplitPane)editTab.getSelectedComponent ( );
						saveScroll = (JScrollPane)saveSplit.getBottomComponent ( );
						saveText = (JTextPane)saveScroll.getViewport().getView();*/
						saveScroll = (JScrollPane)editTab.getSelectedComponent();
						saveSplit = (JSplitPane)saveScroll.getViewport().getView();
						saveText = (JTextPane)saveSplit.getBottomComponent();

						lines = saveText.getText().split("\n");
						for ( int i = 0 ; i < lines.length ; i++ )
							{
								out.write ( lines[i] + "\n" );
							}
						out.close();
						file_writer.close();
						}
						catch ( Exception error )
						{
							JOptionPane.showMessageDialog ( IDE.this,
									"fail to write to the file",
									"Error Report",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						editTab.setTitleAt ( editTab.getSelectedIndex(),
								saveChooser.getSelectedFile().getName());
						for ( int i = 0 ; i < tabList.size() ; i++ )
						{
							selectedTab = (TabProperty)tabList.get(i);
							if ( selectedTab.tabIndex == editTab.getSelectedIndex())
								break;
						}
						selectedTab.changed = false;
						try
						{
							selectedTab.tabPath = saveFile.getCanonicalPath();
						}
						catch ( Exception error )
						{
							JOptionPane.showMessageDialog ( IDE.this,
									"fail to find the path of the file",
									"Error Report",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						break;
					}
					askIfCover = true;
					continue;
				}
				return;
			}
			if ( ifCover==JOptionPane.CANCEL_OPTION )
				return;
			return;
		}
	}
	

/*===========================================================*/
	

/*===========menu function implementation(Edit)==============*/
	class EditE implements ActionListener 
	{
		public void actionPerformed ( ActionEvent e )
		{
			if ( focusOn )
			{
				if ( e.getSource() == rcopy )
					textFocusOn.copy();
				else if ( e.getSource() == rcut )
					textFocusOn.cut();
				else if ( e.getSource() == rpaste )
				{
					textFocusOn.paste();
					textFocusOn.getDocument().removeDocumentListener ( codeChanged );
					painter.paintCode ( (StyledDocument)textFocusOn.getDocument() );
					textFocusOn.getDocument().addDocumentListener ( codeChanged );
				}
				else if ( e.getSource() == rdelete )
					textFocusOn.replaceSelection("");
				else if ( e.getSource() == rselectAll )
					textFocusOn.selectAll();
			}
			else 
			{
				if ( e.getSource() == rcopy )
					commandEdit.copy();
				if ( e.getSource() == rcut )
					commandEdit.cut();
				if ( e.getSource() == rpaste )
					commandEdit.paste();
				if ( e.getSource() == rdelete )
					commandEdit.replaceSelection("");
				else if ( e.getSource() == rselectAll )
					commandEdit.selectAll();
			}
		}
	}
	
	class FindE implements ActionListener 
	{
		public void actionPerformed ( ActionEvent e )
		{
			String pattern=null;
			JScrollPane temp1 = (JScrollPane)editTab.getSelectedComponent();
			JSplitPane temp2 = (JSplitPane)temp1.getViewport().getView();
			JTextPane temp3 = (JTextPane)temp2.getBottomComponent();
			FindPattern findTool = new FindPattern ( temp3 , IDE.this );
			Document doc = commandEdit.getDocument();
			try
			{
				pattern = commandEdit.getDocument().getText( 0 , doc.getLength() );
			}
			catch ( BadLocationException error )
			{
			}
			findTool.find ( pattern );
		}
	}

	class ReplaceE implements ActionListener
	{
		public void actionPerformed ( ActionEvent e )
		{
			String pattern = null;
			String goal=null;
			String [] segment;
			JScrollPane temp1 = (JScrollPane) editTab.getSelectedComponent();
			JSplitPane temp2 = (JSplitPane) temp1.getViewport().getView();
			JTextPane temp3 = (JTextPane)temp2.getBottomComponent();
			ReplacePattern replaceTool = new ReplacePattern ( temp3 , IDE.this );
			Document doc = commandEdit.getDocument();
			try
			{
				pattern = commandEdit.getDocument().getText ( 0 , doc.getLength() );
				segment = pattern.split ( " " );
				pattern = segment[0];
				goal = segment[1];
			}
			catch ( BadLocationException error )
			{

			}
			System.out.println ( pattern + "   " + goal );
			temp3.getDocument().removeDocumentListener ( codeChanged );
			replaceTool.replace ( pattern , goal );
			painter.paintCode ( (StyledDocument)doc );
			temp3.getDocument().addDocumentListener ( codeChanged );
		}
	}
/*===========================================================*/

/*===================compile and run=========================*/
	class CompileE implements ActionListener,Runnable
	{
		TabProperty selectedTab;
		String path1=null;
		String path2=null;
		String [] lines;
		File compileFile=null;
		JTextPane compileText=null;
		JScrollPane compileScroll=null;
		JSplitPane compileSplit=null;
		BufferedWriter out=null;
		FileWriter file_writer=null;
		int n;

		public void actionPerformed ( ActionEvent e )
		{
			System.out.println ( "begin to compile the file " );
			for ( int i = 0 ; i < tabList.size() ; i++ )
			{
				selectedTab = (TabProperty)tabList.get(i);
				if ( selectedTab.tabIndex == editTab.getSelectedIndex() )
					break;
			}
			System.out.println ( selectedTab.tabPath );
			if ( selectedTab.tabPath == null )
			{
				JOptionPane.showMessageDialog( IDE.this,
						"the path of the file is null",
						"Warning Message",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if ( selectedTab.changed == true )
			{
				JOptionPane.showMessageDialog( IDE.this,
						"the file has changed , but not saved",
						"Warning Message",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			compileFile = new File ( selectedTab.tabPath );
			if ( !(compileFile.exists()))
			{
				try
				{
					compileFile.createNewFile();
				}
				catch ( Exception error )
				{
					JOptionPane.showMessageDialog ( IDE.this,
							"fail to build the compile file",
							"Error Report",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			try
			{
				file_writer = new FileWriter ( compileFile );
				out = new BufferedWriter ( file_writer );
				/*compileSplit = (JSplitPane)editTab.getSelectedComponent ( );
				compileScroll = (JScrollPane)compileSplit.getBottomComponent ( );
				compileText = (JTextPane)compileScroll.getViewport().getView();*/
				compileScroll = (JScrollPane)editTab.getSelectedComponent();
				compileSplit = (JSplitPane)compileScroll.getViewport().getView();
				compileText = (JTextPane)compileSplit.getBottomComponent();
				lines = compileText.getText().split("\n");
				for ( int i = 0 ; i < lines.length ; i++ )
				{
					out.write ( lines[i] + "\n" );
				}
				out.close();
				file_writer.close();
			}
			catch ( Exception error )
			{
				JOptionPane.showMessageDialog ( IDE.this,
					"fail to write to the file",
					"Error Report",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			path1 = selectedTab.tabPath;
			n = path1.lastIndexOf(File.separator);
			path2=path1.substring(0,n);
			if ( !(compiler.isAlive()))
			{
				compiler = new Thread ( this );
				try
				{
					compiler.start();
				}
				catch ( Exception error )
				{
					JOptionPane.showMessageDialog ( IDE.this,
						"start compiler failed...",
						"Warning Info",
						JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		}
		
		public void run ( )
		{
			Runtime ce;
			InputStream in2;
			BufferedInputStream in3;
			try
			{
				ce = Runtime.getRuntime();
				in2 = ce.exec ( "g++ "+selectedTab.tabPath ).getErrorStream();
				in3 = new BufferedInputStream( in2 );
				byte readCompileError [] = new byte[100];
				int n;
				boolean compileError = false;
				compileInfo.setText ( null );
			    while ( ( n = in3.read(readCompileError,0,100) ) != -1 )
				{
					String s = null;
					s = new String ( readCompileError , 0 , n );
					compileInfo.append ( s );
					if ( s != null ) 
					{
						compileError = true;
					}
				}
				in3.close();
				in2.close();
				if ( compileError == false )
				{
					compileInfo.append ( "sucessfully compile"+path2+"!!");
				}
			}
			catch ( Exception error )
			{
				JOptionPane.showMessageDialog( IDE.this,
					"fail to compile",
					"Error Report",
					JOptionPane.ERROR_MESSAGE);
					return;
			}
		}
	}
		//run the program
	class RunE implements ActionListener,Runnable
	{
		String path1 = null;
		String path2 = null;
		String path3 = null;
		String path4 = null;
		int n,l;
		File openFile;
		int openChooserState;
		public void actionPerformed ( ActionEvent e )
		{
			openChooserState = openChooser.showOpenDialog ( IDE.this );
			openFile = openChooser.getSelectedFile();
			if ( openFile != null && openChooserState == JFileChooser.APPROVE_OPTION )
				try
				{
					path1 = openFile.getCanonicalPath();
				}
				catch ( Exception ee ){}
			n = path1.lastIndexOf ( File.separator );
			path2 = path1.substring ( 0 , n );
			path3 = path1.substring ( n+1 );
			l = path3.lastIndexOf(".");
			path4 = path3.substring ( 0 , l );
			if ( !(run_file.isAlive() ) )
			{
				run_file = new Thread(this);
			}
			try
			{
				run_file.start();
			}
			catch ( Exception error )
			{
				JOptionPane.showMessageDialog ( IDE.this,
					"fail to start the program",
					"Warning",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		public void run ( )
		{
			try
			{
				Runtime ce = Runtime.getRuntime();
				System.out.println ( path1 );
				InputStream in2 = ce.exec (  path1 + " < input" ).getInputStream();
				System.out.println ( "source ok " );
				BufferedInputStream in3 = new BufferedInputStream ( in2 );
				byte runInfo[] = new byte[100];
				int m;
				System.out.println ( "the finish happy ending" );
				compileInfo.setText(null);
				while ( (m = in3.read(runInfo , 0 , 100 ) ) != -1 )
				{
					String s = null;
					s = new String ( runInfo , 0 , m );
					compileInfo.append ( s );
				}
				in3.close();
				in2.close();
			}
			catch ( Exception error )
			{
				JOptionPane.showMessageDialog ( IDE.this,
						"fail to run the program",
						"RunTime Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
/*===========================================================*/


/*===================assistant class===========================*/	
	class TabProperty
	{
		int tabIndex;
		boolean changed;
		String tabPath;
		TabProperty ( int index , boolean changed , String path )
		{
			this.tabIndex = index;
			this.changed = changed;
			tabPath = path;
		}
	}

	class MenuPopupE extends MouseAdapter
	{
		public void mouseReleased ( MouseEvent e )
		{
			if ( popup.isPopupTrigger(e))
				popup.show ( e.getComponent(),e.getX(),e.getY());
		}
	}

	class NeedSave implements DocumentListener
	{
		private TabProperty selectedTab;

		NeedSave ( )
		{
			for ( int i = 0 ; i < tabList.size() ; i++ )
			{
				selectedTab = (TabProperty)tabList.get(i);
				if ( selectedTab.tabIndex == editTab.getSelectedIndex() )
					break;
			}
		}
		
		public void insertUpdate ( DocumentEvent e )
		{
			selectedTab.changed = true;
		}

		public void removeUpdate ( DocumentEvent e )
		{
			selectedTab.changed = true;
		}
		
		public void changedUpdate ( DocumentEvent e )
		{
			
		}

	}

	class DocumentE implements DocumentListener 
	{
		TabProperty selectedTab;

		public void insertUpdate ( DocumentEvent e )
		{
			for ( int i = 0 ; i < tabList.size() ; i++ )
			{
				selectedTab = ( TabProperty ) tabList.get(i);
				if ( selectedTab.tabIndex == editTab.getSelectedIndex() )
					break;
			}
			selectedTab.changed = true;
		}

		public void removeUpdate ( DocumentEvent e )
		{
			for ( int i = 0 ; i < tabList.size() ; i++ )
			{
				selectedTab = ( TabProperty ) tabList.get(i);
				if ( selectedTab.tabIndex == editTab.getSelectedIndex () )
					break;
			}
			selectedTab.changed = true;
		}

		public void changedUpdate ( DocumentEvent e )
		{
		}	
	}

}




