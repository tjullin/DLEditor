import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
//����
public class JH
{ public static void main(String args[])
  {EventQueue.invokeLater(new Runnable()
   {
   public void run()
   {JavaIDE j=new JavaIDE();
   j.setVisible(true);
   }
  });
 }
}
class JavaIDE extends JFrame
{
//���������Ԫ�ض���
JMenuBar menuBar;
JMenu file,edit,tool,help;
JPopupMenu popup;
JMenuItem create,open,save,saveAs,shutDownTabbed,exit,copy,cut,paste,delete,selectAll,find,compile,runFile,JavaIDEHelp,APIDocument,aboutJavaIDE,copyP,cutP,pasteP,deleteP,selectAllP;
JPanel noticeP,compileResultP;
JLabel noticeL, compileResultL;
JSplitPane innerJspane,outerJspane;
JTextArea text1,notice,compileResult,text,textFocusOn,textOpen,textSave,textCompile;
JTextField noticeField;
JScrollPane textSP1,noticeSP,compileResultSP,textSP,textSPFocusOn,textSPOpen,textSPSave,textSPCompile;
JTabbedPane textTP;
JButton tabbedButton;
boolean textFocus,noticeFocus,compileResultFocus,newFirstSave,askIfCover=false;
ImageIcon shutDown;
documentE ifChanged;
textFocusStatus gainlost;
menuPopupE menuPopup;
createE createFiles;
openE openFiles;
saveE saveFiles;
saveAsE saveAsFiles;
shutDownTabbedE shutDownSelectedTabbed;
exitE shutDownF;
editE copypaste;
compileE compiling;
runE running;
tabPaneChangedE textRequestFocus;
JFileChooser openChooser,saveChooser;
int createTab=2,selectedIndex,openChooserState,saveChooserState,confirmDialogState,confirmDialog2State,ifCover; 
File openFile=null,saveFile=null,compileFile=null;
FileReader file_reader;
FileWriter file_writer;
BufferedReader in;
BufferedWriter out;
InputStream in2;
BufferedInputStream in3;
String readText,openFilePath;
String[] lines;
LinkedList tabbed=null; 
Thread compiler=null,run_file=null;
Runtime ce;
WindowShutDown exitWindow;
HelpE helpinfo;
JavaIDE()
 {//�����м���˵������˵��������˵����˵���
  menuBar=new JMenuBar();
  setJMenuBar(menuBar);
  file=new JMenu("�ļ�(F)");
  file.setMnemonic('F');
  menuBar.add(file);
  edit= new JMenu ("�༭");
  menuBar.add(edit);
  tool= new JMenu ("����");
  menuBar.add(tool);
  help= new JMenu ("����");
  menuBar.add(help);
  create=new JMenuItem("�½�(N)",'N');
  create.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
  file.add(create);
  open=new JMenuItem("�򿪡�");
  open.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
  file.add(open);
  save=new JMenuItem("����");
  save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
  file.add(save);
  saveAs=new JMenuItem("���Ϊ��");
  saveAs.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
  file.add(saveAs);
  file.addSeparator();
  shutDownTabbed=new JMenuItem("�رյ�ǰѡ�");
  shutDownTabbed.setEnabled(false);
  file.add(shutDownTabbed);
  exit=new JMenuItem("�˳�");
  file.add(exit);
  copy=new JMenuItem("����");
  edit.add(copy);
  cut=new JMenuItem("����");
  edit.add(cut);
  paste=new JMenuItem("ճ��");
  edit.add(paste);
  delete=new JMenuItem("ɾ��");
  edit.add(delete);
  edit.addSeparator();
  selectAll=new JMenuItem("ȫѡ");
  edit.add(selectAll);
  edit.addSeparator();
  find=new JMenuItem("���ҡ�");
  edit.add(find);
  compile=new JMenuItem("����");
  tool.add(compile);
  runFile=new JMenuItem("���С�");
  tool.add(runFile);
  JavaIDEHelp=new JMenuItem("Java IDE������");
  help.add(JavaIDEHelp);
  APIDocument=new JMenuItem("API�ĵ���");
  help.add(APIDocument);
  aboutJavaIDE=new JMenuItem("����Java IDE��");
  help.add(aboutJavaIDE);
  popup=new JPopupMenu();
  copyP=new JMenuItem("����");
  popup.add(copyP);
  cutP=new JMenuItem("����");
  popup.add(cutP);
  pasteP=new JMenuItem("ճ��");
  popup.add(pasteP);
  deleteP=new JMenuItem("ɾ��");
  popup.add(deleteP);
  popup.addSeparator();
  selectAllP=new JMenuItem("ȫѡ");
  popup.add(selectAllP);
//����Դ�ļ��༭������ʾ�����������������ı���
  text1=new JTextArea();
  textSP1=new JScrollPane(text1);
  textTP=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
  shutDown=new ImageIcon("shutDown.jpg");
  textTP.addTab("�½�Դ�ļ�-1",textSP1);
  notice=new JTextArea();
  noticeSP=new JScrollPane(notice);
  noticeL=new JLabel("��ʾ����",JLabel.LEFT);
  noticeP=new JPanel();
  noticeP.setLayout(new BorderLayout());
  noticeP.add(noticeL,BorderLayout.NORTH);
  noticeP.add(noticeSP);
  compileResult=new JTextArea();
  compileResultSP=new JScrollPane(compileResult);
  compileResultL=new JLabel("����������",JLabel.LEFT);
  compileResultP=new JPanel();
  compileResultP.setLayout(new BorderLayout());
  compileResultP.add(compileResultL,BorderLayout.NORTH);
  compileResultP.add(compileResultSP);
//�����򿪱����ļ��Ի���
  openChooser=new JFileChooser();
  saveChooser=new JFileChooser(); 
//��������������߳�
  compiler=new Thread();
  run_file=new Thread();
//���ڹرհ�ť���������
  exitWindow=new WindowShutDown();
  addWindowListener(exitWindow);
//Դ�ļ��༭�������ĵ�������
  ifChanged=new documentE();
  text1.getDocument().addDocumentListener(ifChanged);
//�������ı����м��뽹�������
  gainlost=new textFocusStatus(); 
  text1.addFocusListener(gainlost);
  notice.addFocusListener(gainlost);
  compileResult.addFocusListener(gainlost);
//�������ı����м�����������
  menuPopup=new menuPopupE();
  text1.addMouseListener(menuPopup);
  notice.addMouseListener(menuPopup);
  compileResult.addMouseListener(menuPopup);
//��Դ�ļ��༭������ѡ��ı������
  textRequestFocus=new tabPaneChangedE();
  textTP.addChangeListener(textRequestFocus);
//���ļ����˵�������������
  createFiles=new createE();
  create.addActionListener(createFiles); 
  openFiles=new openE();
  open.addActionListener(openFiles); 
  saveFiles=new saveE();
  save.addActionListener(saveFiles); 
  saveAsFiles=new saveAsE();
  saveAs.addActionListener(saveAsFiles);
  shutDownSelectedTabbed=new shutDownTabbedE();
  shutDownTabbed.addActionListener(shutDownSelectedTabbed);
  shutDownF=new exitE();
  exit.addActionListener(shutDownF);
//�򡰱༭���˵�������������
  copypaste=new editE();
  copy.addActionListener(copypaste);
  cut.addActionListener(copypaste);
  paste.addActionListener(copypaste);
  delete.addActionListener(copypaste);
  selectAll.addActionListener(copypaste);
  copyP.addActionListener(copypaste);
  cutP.addActionListener(copypaste);
  pasteP.addActionListener(copypaste);
  deleteP.addActionListener(copypaste);
  selectAllP.addActionListener(copypaste);
//�򡰹��ߡ��˵�������������
  compiling=new compileE();
  compile.addActionListener(compiling);
  running=new runE();
  runFile.addActionListener(running);
//�򡰰������˵�������������
  helpinfo=new HelpE(); 
  aboutJavaIDE.addActionListener(helpinfo);
//�������ı�������ָ����
  innerJspane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,textTP,noticeP);
  innerJspane.setOneTouchExpandable(true);
  innerJspane.setDividerLocation(720);
  outerJspane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,innerJspane,compileResultP);
  outerJspane.setDividerLocation(390);
  outerJspane.setOneTouchExpandable(true);
//���ָ���������
  setVisible(true);
  setBounds(50,50,960,540);
  Container con=getContentPane();
  con.setLayout(new BorderLayout());
  con.add(outerJspane,BorderLayout.CENTER);
  text1.requestFocus(); 
  validate();
  setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
//����һ��ѡ�����ѡ���Ϣ����
  tabbed=new LinkedList();
  tabbed_property tab_imfo=new tabbed_property(textTP.getTabCount()-1,false,null);
  tabbed.add(tab_imfo);
}
//�ڲ���ʵ��Դ�ļ��༭���ı��䶯����
class documentE implements DocumentListener
{tabbed_property temp;
 public void insertUpdate(DocumentEvent e10)
  {     for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
            break;   
        }
        temp.changed=true;
  }       
 public void removeUpdate(DocumentEvent e16)
 {     for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
            break;   
        }
        temp.changed=true;
  }
 public void changedUpdate(DocumentEvent e17)
  {}
}
//�ڲ���ʵ�ָ��ı��򽹵��ڵ�����༭�����˵���֮ǰ״̬���ж�
class textFocusStatus extends FocusAdapter
{
  public void focusGained(FocusEvent e2)
   {textFocusOn=(JTextArea)e2.getSource();
    if(textFocusOn==notice)
    {noticeFocus=true;compileResultFocus=false;}
    else if(textFocusOn==compileResult)
    {noticeFocus=false;compileResultFocus=true;}
    else  {noticeFocus=false;compileResultFocus=false;
          textSPFocusOn=(JScrollPane)textTP.getSelectedComponent();
          textFocusOn=(JTextArea)textSPFocusOn.getViewport().getView();
        }
   }
  public void focusLost(FocusEvent ee3)
  {noticeFocus=false;compileResultFocus=false;
   textSPFocusOn=(JScrollPane)textTP.getSelectedComponent();
   textFocusOn=(JTextArea)textSPFocusOn.getViewport().getView(); 
  }  
}
//�ڲ���ʵ�������ı������ݵĸ��ơ����С�ճ����ɾ����ȫѡ
class editE implements ActionListener
  {public void actionPerformed(ActionEvent e3)
   {if(noticeFocus==true)
    {if(e3.getSource()==copy||e3.getSource()=copyP)
     notice.copy();
     else if(e3.getSource()==cut||e3.getSource()==cutP)
     notice.cut();
     else if(e3.getSource()==paste||e3.getSource()==pasteP)
     notice.paste();
     else if(e3.getSource()==delete||e3.getSource()==deleteP)
     notice.replaceSelection("");
     else if(e3.getSource()==selectAll||e3.getSource()==selectAllP)
     notice.selectAll();
    }
   else if(compileResultFocus==true)
   {if(e3.getSource()==copy||e3.getSource()==copyP)
     compileResult.copy();
     else if(e3.getSource()==cut||e3.getSource()==cutP)
     compileResult.cut();
     else if(e3.getSource()==paste||e3.getSource()==pasteP)
     compileResult.paste();
     else if(e3.getSource()==delete||e3.getSource()==deleteP)
     compileResult.replaceSelection("");
     else if(e3.getSource()==selectAll||e3.getSource()==selectAllP)
     compileResult.selectAll();
    }
   else
    {if(e3.getSource()==copy||e3.getSource()==copyP)
     textFocusOn.copy();
     else if(e3.getSource()==cut||e3.getSource()==cutP)
     textFocusOn.cut();
     else if(e3.getSource()==paste||e3.getSource()==pasteP)
     textFocusOn.paste();
     else if(e3.getSource()==delete||e3.getSource()==deleteP)
     textFocusOn.replaceSelection("");
     else if(e3.getSource()==selectAll||e3.getSource()==selectAllP)
     textFocusOn.selectAll();
    }
   }
  }
//�ڲ���ʵ����괥�����¼��������˵�
class menuPopupE extends MouseAdapter
 {   public void mouseReleased(MouseEvent e1)
     {if(popup.isPopupTrigger(e1)) 
     popup.show(e1.getComponent(),e1.getX(),e1.getY());
     }
 }
//�ڲ���ʵ��Դ�ļ��༭��ѡ��ı�ʱ���༭����ý���
class tabPaneChangedE implements ChangeListener
 {   public void stateChanged(ChangeEvent e5)
    {textSPFocusOn=(JScrollPane)textTP.getSelectedComponent();
     textFocusOn=(JTextArea)textSPFocusOn.getViewport().getView();
     textFocusOn.requestFocus();
    }
 }
//�ڲ���ʵ�֡��½���
class createE implements ActionListener
  { public void actionPerformed(ActionEvent e4)
   {textTP.addTab("�½�Դ�ļ�-"+createTab,new JScrollPane(text=new JTextArea()));
    createTab++;
    textTP.setSelectedIndex(textTP.getTabCount()-1);
    tabbed_property tab_imfo=new tabbed_property(textTP.getTabCount()-1,false,null);
    tabbed.add(tab_imfo);
    text.getDocument().addDocumentListener(ifChanged);
    text.addFocusListener(gainlost);
    text.addMouseListener(menuPopup);
    text.requestFocus();
    shutDownTabbed.setEnabled(true);
   }
  }
//�ڲ���ʵ�֡��򿪡�
class openE implements ActionListener
  {public void actionPerformed(ActionEvent e6)
   {openChooser.setCurrentDirectory(new File("."));
    openChooserState=openChooser.showOpenDialog(JavaIDE.this);
    openFile=openChooser.getSelectedFile();
    if(openFile!=null&&openChooserState==JFileChooser.APPROVE_OPTION)
    {try{file_reader=new FileReader(openFile);
          in=new BufferedReader(file_reader);
          textTP.addTab(openChooser.getSelectedFile().getName(),new JScrollPane(new JTextArea()));
          textTP.setSelectedIndex(textTP.getTabCount()-1);
          textSPOpen=(JScrollPane)textTP.getSelectedComponent();
          textOpen=(JTextArea)textSPOpen.getViewport().getView();
          while((readText=in.readLine())!=null)
          {textOpen.append(readText+'\n');}
           in.close();
           file_reader.close();
           openFilePath=openChooser.getSelectedFile().getCanonicalPath();
         }
     catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"���ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}   
           tabbed_property tab_imfo=new tabbed_property(textTP.getTabCount()-1,false,openFilePath);
           tabbed.add(tab_imfo);
           textOpen.getDocument().addDocumentListener(ifChanged);
           textOpen.addFocusListener(gainlost);
           textOpen.addMouseListener(menuPopup);
           textOpen.requestFocus();
           shutDownTabbed.setEnabled(true);       
    }
   if(openChooserState==JFileChooser.CANCEL_OPTION)
   {return;}
   }
  }   
//�ڲ���ʵ�֡����桱
class saveE implements ActionListener
{tabbed_property temp;
 public void actionPerformed(ActionEvent e18)
 { for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
            break;
        }
  if(temp.tabbedPath==null)
    {saveChooser.setCurrentDirectory(new File("."));
     saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
     saveFile=saveChooser.getSelectedFile();
     askIfCover=false;
     while((saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION))
        {if(askIfCover==true)
         {saveChooser.setCurrentDirectory(new File("."));
         saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
         saveFile=saveChooser.getSelectedFile();
         }
         if(saveChooserState==JFileChooser.CANCEL_OPTION)
         {return;}
         if((saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION))
        {if(!saveFile.exists())
        {try{saveFile.createNewFile();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         try {file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e20)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�·����Ϣ����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         break;
        }
        else
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"�ļ��Ѵ��ڣ��Ƿ񸲸ǣ�","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
         if(ifCover==JOptionPane.NO_OPTION)
         {askIfCover=true;continue;}
         if(ifCover==JOptionPane.YES_OPTION)
        {try {file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e20)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�·����Ϣ����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         break;
        }
         askIfCover=true;continue;
        }
         return;
        }
      if(openChooserState==JFileChooser.CANCEL_OPTION)
      {return;}
    return;      
    }
  else
    {saveFile=new File(temp.tabbedPath);
     if(!saveFile.exists())
        {try{saveFile.createNewFile();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
        }
     try {   file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
     catch(Exception e20)
          {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
     temp.changed=false; 
    }   
 } 
}
//�ڲ���ʵ�֡����Ϊ��
class saveAsE implements ActionListener
{tabbed_property temp; 
 public void actionPerformed(ActionEvent e22)
 {   saveChooser.setCurrentDirectory(new File("."));
     saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
     saveFile=saveChooser.getSelectedFile();
     askIfCover=false;
     while(saveFile!=null&&saveChooserState==JFileChooser.APPROVE_OPTION)
       {if(askIfCover==true)
        {saveChooser.setCurrentDirectory(new File("."));
         saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
         saveFile=saveChooser.getSelectedFile();
        } 
        if(saveChooserState==JFileChooser.CANCEL_OPTION)
         {return;}
        if((saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION))
        {if(!saveFile.exists())
        {try{saveFile.createNewFile();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
        try {file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e24)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
        textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
        for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
         break;
        }
        temp.changed=false;
        try{temp.tabbedPath=saveFile.getCanonicalPath();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"����ļ�·����Ϣ����,�´α���ʱ�ᱣ�浽ԭ·��","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
        break;
        }
        else
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"�ļ��Ѵ��ڣ��Ƿ񸲸ǣ�","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
         if(ifCover==JOptionPane.NO_OPTION)
         {askIfCover=true;continue;}
         if(ifCover==JOptionPane.YES_OPTION)
        {try {file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e24)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
        textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
        for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
         break;
        }
        temp.changed=false;
        try{temp.tabbedPath=saveFile.getCanonicalPath();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"����ļ�·����Ϣ����,�´α���ʱ�ᱣ�浽ԭ·��","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
        break;
        }
        askIfCover=true;continue;
        }
        return;
       }
     if(confirmDialogState==JOptionPane.CANCEL_OPTION)
    {return;}
   return;  
  }   
 } 
//�ڲ���ʵ�֡��رյ�ǰѡ���
class shutDownTabbedE implements ActionListener
{ int i,shutDownTabbedIndex,selectedTabbedIndex;
  tabbed_property temp;
  public void actionPerformed(ActionEvent e26)
 { selectedTabbedIndex=textTP.getSelectedIndex();
   for(i=0;i<tabbed.size();i++)
   {temp=(tabbed_property)tabbed.get(i);
     if(temp.tabbedIndex==selectedTabbedIndex)
        break; 
   }
  shutDownTabbedIndex=i;
  if(temp.changed==true)
   {confirmDialogState=JOptionPane.showConfirmDialog(JavaIDE.this,"�ļ��иĶ����ر�ǰ�Ƿ񱣴浱ǰѡ���","��ʾ",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE); 
    if(confirmDialogState==JOptionPane.CANCEL_OPTION)
    {return;}
    if(confirmDialogState==JOptionPane.YES_OPTION)
    {
		if(temp.tabbedPath==null)
     {saveChooser.setCurrentDirectory(new File("."));
      saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
      saveFile=saveChooser.getSelectedFile();
      askIfCover=false;
      while(saveFile!=null&&saveChooserState==JFileChooser.APPROVE_OPTION)
       { if(askIfCover==true)
         {saveChooser.setCurrentDirectory(new File("."));
         saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
         saveFile=saveChooser.getSelectedFile();
         }
         if(saveChooserState==JFileChooser.CANCEL_OPTION)
         {return;}
         if((saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION))
         {      
         if(!saveFile.exists())
         {try{saveFile.createNewFile();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         try {file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e20)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"����ļ�·����Ϣ����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
           textTP.remove(textTP.getSelectedIndex());
           if(textTP.getTabCount()<2)
           {shutDownTabbed.setEnabled(false);}
           tabbed.remove(shutDownTabbedIndex);
           for(int i=0;i<tabbed.size();i++)
           {temp=(tabbed_property)tabbed.get(i);
           if(temp.tabbedIndex>shutDownTabbedIndex)
           {temp.tabbedIndex=temp.tabbedIndex-1;}
           }
           return;
           }
         else
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"�ļ��Ѵ��ڣ��Ƿ񸲸ǣ�","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
         if(ifCover==JOptionPane.NO_OPTION)
         {askIfCover=true;continue;}
         if(ifCover==JOptionPane.YES_OPTION)
        {try {file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e20)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"����ļ�·����Ϣ����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         textTP.remove(textTP.getSelectedIndex());
         if(textTP.getTabCount()<2)
         {shutDownTabbed.setEnabled(false);}
         tabbed.remove(shutDownTabbedIndex);
         for(int i=0;i<tabbed.size();i++)
         {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex>shutDownTabbedIndex)
         {temp.tabbedIndex=temp.tabbedIndex-1;}
         }
         return;
       }
       askIfCover=true;continue;
       }
       return;
       }
       if(saveChooserState==JFileChooser.CANCEL_OPTION)
       {return;}
      return; 
      }
      else
     {saveFile=new File(temp.tabbedPath);
     if(!saveFile.exists())
        {try{saveFile.createNewFile();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;} 
        }
     try {file_writer=new FileWriter(saveFile);
          out=new BufferedWriter(file_writer);
          textSPSave=(JScrollPane)textTP.getSelectedComponent();
          textSave=(JTextArea)textSPSave.getViewport().getView();
          lines=textSave.getText().split( "\n "); 
          for(int i=0;i<lines.length;i++) 
          {out.write(lines[i]+"\n ");}
          out.close();
          file_writer.close();
         }
     catch(Exception e20)
          {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
     temp.changed=false; 
     }
  textTP.remove(textTP.getSelectedIndex());
  if(textTP.getTabCount()<2)
  {shutDownTabbed.setEnabled(false);}
  tabbed.remove(shutDownTabbedIndex);
  for(int i=0;i<tabbed.size();i++)
  {temp=(tabbed_property)tabbed.get(i);
  if(temp.tabbedIndex>shutDownTabbedIndex)
  {temp.tabbedIndex=temp.tabbedIndex-1;}
  }
  return;
  }
  if(confirmDialogState==JOptionPane.NO_OPTION)
    {textTP.remove(textTP.getSelectedIndex());
  if(textTP.getTabCount()<2)
  {shutDownTabbed.setEnabled(false);}
    tabbed.remove(shutDownTabbedIndex);
    for(int i=0;i<tabbed.size();i++)
    {temp=(tabbed_property)tabbed.get(i);
    if(temp.tabbedIndex>shutDownTabbedIndex)
    {temp.tabbedIndex=temp.tabbedIndex-1;}
    }
    return;
    }
   return;
  }
  textTP.remove(textTP.getSelectedIndex());
   if(textTP.getTabCount()<2)
  {shutDownTabbed.setEnabled(false);}
  tabbed.remove(shutDownTabbedIndex);
  for(int i=0;i<tabbed.size();i++)
  {temp=(tabbed_property)tabbed.get(i);
  if(temp.tabbedIndex>shutDownTabbedIndex)
  {temp.tabbedIndex=temp.tabbedIndex-1;}
  }       
 } 
}
//�ڲ���ʵ�֡��˳���
class exitE implements ActionListener
{tabbed_property temp; int i; boolean whetherChanged=false;
 public void actionPerformed(ActionEvent e)
 {for(i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.changed==true)
         {whetherChanged=true;confirmDialog2State=JOptionPane.showConfirmDialog(JavaIDE.this,"�ļ��иĶ����ر�ǰ�Ƿ񱣴棿","��ʾ",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
          break;
         }
        }
  if(whetherChanged==false)
  System.exit(0);
  if(confirmDialog2State==JOptionPane.NO_OPTION)
  {System.exit(0);} 
  if(confirmDialog2State==JOptionPane.CANCEL_OPTION)
  {return;} 
  if(confirmDialog2State==JOptionPane.YES_OPTION)
    {   i=0;
    loop:while(i<tabbed.size())
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.changed==true)
         {textTP.setSelectedIndex(temp.tabbedIndex);  
     if(temp.tabbedPath==null)
    {saveChooser.setCurrentDirectory(new File("."));
     saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
     saveFile=saveChooser.getSelectedFile();
     askIfCover=false;
     while(saveFile!=null&&saveChooserState==JFileChooser.APPROVE_OPTION)
       {if(askIfCover==true)
         {saveChooser.setCurrentDirectory(new File("."));
         saveChooserState=saveChooser.showSaveDialog(JavaIDE.this);
         saveFile=saveChooser.getSelectedFile();
         }
         if(saveChooserState==JFileChooser.CANCEL_OPTION)
         {return;}
        if((saveFile!=null)&&(saveChooserState==JFileChooser.APPROVE_OPTION))
        {if(!saveFile.exists())
        {try{saveFile.createNewFile();}
         catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         try {
             file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e20)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�·����Ϣ����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         i++;continue loop;
        }
        else
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"�ļ��Ѵ��ڣ��Ƿ񸲸ǣ�","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
         if(ifCover==JOptionPane.NO_OPTION)
        {askIfCover=true;continue;}
         if(ifCover==JOptionPane.YES_OPTION)
        {try {
             file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
         catch(Exception e20)
            {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�·����Ϣ����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
         i++;continue loop;
        }
        askIfCover=true;continue; 
       }
       return;
      }
   if(saveChooserState==JFileChooser.CANCEL_OPTION)
   {return;}
    return;
     }
  else
    {saveFile=new File(temp.tabbedPath);
     if(!saveFile.exists())
        {try{saveFile.createNewFile();}
        catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
        }
     try {   file_writer=new FileWriter(saveFile);
             out=new BufferedWriter(file_writer);
             textSPSave=(JScrollPane)textTP.getSelectedComponent();
             textSave=(JTextArea)textSPSave.getViewport().getView();
             lines=textSave.getText().split( "\n "); 
             for(int i=0;i<lines.length;i++) 
             {out.write(lines[i]+"\n ");}
             out.close();
             file_writer.close();
            }
     catch(Exception e20)
          {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
     temp.changed=false; 
    }
  }
    i++;
    }
  System.exit(0); 
  }
  return;    
 }
}
//�ڲ���ʵ�ֱ���
class compileE implements ActionListener,Runnable
 {tabbed_property temp;String path1=null,path2=null;int n;
  public void actionPerformed(ActionEvent e)
  {for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
         break;
        }
   if(temp.tabbedPath==null)
   {JOptionPane.showMessageDialog(JavaIDE.this,"��ǰѡ�δ���棬�뱣����ٱ���","������Ϣ",JOptionPane.WARNING_MESSAGE);return;}
   if(temp.changed==true)
   {JOptionPane.showMessageDialog(JavaIDE.this,"��ǰѡ��иĶ����뱣����ٱ���","������Ϣ",JOptionPane.WARNING_MESSAGE);return;}
   compileFile=new File(temp.tabbedPath);
   if(!(compileFile.exists()))
   {try{compileFile.createNewFile();}
     catch(Exception e32){JOptionPane.showMessageDialog(JavaIDE.this,"�½��ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
   try{file_writer=new FileWriter(compileFile);
      out=new BufferedWriter(file_writer);
      textSPCompile=(JScrollPane)textTP.getSelectedComponent();
      textCompile=(JTextArea)textSPCompile.getViewport().getView();
      lines=textCompile.getText().split( "\n "); 
      for(int i=0;i<lines.length;i++) 
      {out.write(lines[i]+"\n ");}
      out.close();
      file_writer.close();
      }
    catch(Exception e20)
    {JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
    temp.changed=false;
    } 
   path1=temp.tabbedPath;
   n=path1.lastIndexOf(File.separator);
   path2=path1.substring(0,n);
   if(!(compiler.isAlive()))
   {compiler=new Thread(this);}
   try{compiler.start();}
   catch(Exception e31){JOptionPane.showMessageDialog(JavaIDE.this,"��ǰ���ļ�δ��ɱ��룬���Ժ�����","������Ϣ",JOptionPane.WARNING_MESSAGE);return;}
  }
   public void run()
   {try{ce=Runtime.getRuntime();
    in2=ce.exec("javac "+'"'+temp.tabbedPath+'"').getErrorStream();
    in3=new BufferedInputStream(in2);
    byte readCompileError[]=new byte[100];
    int n; boolean compileError=false;
    compileResult.setText(null); 
    while((n=in3.read(readCompileError,0,100))!=-1)
    {String s=null;
     s=new String(readCompileError,0,n);
     compileResult.append(s);
     if(s!=null){compileError=true;}
    }
    in3.close();
    in2.close();
    if(compileError==false)
    {compileResult.append("������ȷ,class�ļ�������"+path2+"Ŀ¼��");}      
    }
   catch(Exception e30){JOptionPane.showMessageDialog(JavaIDE.this,"�������","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}
   }
  }
//�ڲ���ʵ������  
class runE implements ActionListener,Runnable
{String path1=null,path2=null,path3=null,path4=null;int n,l; 
 public void actionPerformed(ActionEvent e)
  {openChooser.setCurrentDirectory(new File("."));
    openChooserState=openChooser.showOpenDialog(JavaIDE.this);
    openFile=openChooser.getSelectedFile();
    if(openFile!=null&&openChooserState==JFileChooser.APPROVE_OPTION)
    try{path1=openFile.getCanonicalPath();}
    catch(Exception ee){}
    n=path1.lastIndexOf(File.separator);
    path2=path1.substring(0,n);
    path3=path1.substring(n+1);
    l=path3.lastIndexOf(".");
    path4=path3.substring(0,l);
     {if(!(run_file.isAlive()))
        {run_file=new Thread(this);}
      try{run_file.start();}
      catch(Exception e33){JOptionPane.showMessageDialog(JavaIDE.this,"��ǰ���ļ�δֹͣ���У���ֹͣ���к�����","������Ϣ",JOptionPane.WARNING_MESSAGE);return;}
     }  
  }
  public void run()
  {try{ Runtime ce=Runtime.getRuntime();
        in2=ce.exec("java "+"-classpath "+'"'+path2+'"'+" "+path4).getInputStream();
        in3=new BufferedInputStream(in2);
        byte runImfo[]=new byte[100];
        int m; 
        notice.setText(null); 
        while((m=in3.read(runImfo,0,100))!=-1)
        {String s=null;
         s = new String(runImfo,0,m);
         notice.append(s);
        }
        in3.close();
        in2.close(); 
      }
   catch(Exception e34){JOptionPane.showMessageDialog(JavaIDE.this,"�����ļ�����ʾ�ļ���Ϣ����","������Ϣ",JOptionPane.ERROR_MESSAGE);return;}          
  }
 }
//�ڲ���ʵ�ְ���
class HelpE implements ActionListener
{public void actionPerformed(ActionEvent e)
 {JOptionPane.showMessageDialog(JavaIDE.this,"���ߣ�����","��ʾ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
 }
}
//�ڲ���ʵ��WindowsAdapter
class WindowShutDown extends WindowAdapter
{publ:ic void windowClosing(WindowEvent eee)
 {JOptionPane.showMessageDialog(JavaIDE.this,"�뵽���ļ����˵�������˳����رճ���","��ʾ��Ϣ",JOptionPane.INFORMATION_MESSAGE);}
}
}
//ѡ�״̬��
class tabbed_property
{int tabbedIndex;
 boolean changed;
 String tabbedPath;
 tabbed_property(int tabbedIndex,boolean changed,String tabbedPath)
 {this.tabbedIndex=tabbedIndex;
  this.changed=changed;
  this.tabbedPath=tabbedPath;
 }
}
