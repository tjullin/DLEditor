import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
//主类
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
//建立界面各元素对象
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
 {//向框架中加入菜单栏、菜单、弹出菜单、菜单项
  menuBar=new JMenuBar();
  setJMenuBar(menuBar);
  file=new JMenu("文件(F)");
  file.setMnemonic('F');
  menuBar.add(file);
  edit= new JMenu ("编辑");
  menuBar.add(edit);
  tool= new JMenu ("工具");
  menuBar.add(tool);
  help= new JMenu ("帮助");
  menuBar.add(help);
  create=new JMenuItem("新建(N)",'N');
  create.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
  file.add(create);
  open=new JMenuItem("打开…");
  open.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
  file.add(open);
  save=new JMenuItem("保存");
  save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
  file.add(save);
  saveAs=new JMenuItem("另存为…");
  saveAs.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
  file.add(saveAs);
  file.addSeparator();
  shutDownTabbed=new JMenuItem("关闭当前选项卡");
  shutDownTabbed.setEnabled(false);
  file.add(shutDownTabbed);
  exit=new JMenuItem("退出");
  file.add(exit);
  copy=new JMenuItem("复制");
  edit.add(copy);
  cut=new JMenuItem("剪切");
  edit.add(cut);
  paste=new JMenuItem("粘贴");
  edit.add(paste);
  delete=new JMenuItem("删除");
  edit.add(delete);
  edit.addSeparator();
  selectAll=new JMenuItem("全选");
  edit.add(selectAll);
  edit.addSeparator();
  find=new JMenuItem("查找…");
  edit.add(find);
  compile=new JMenuItem("编译");
  tool.add(compile);
  runFile=new JMenuItem("运行…");
  tool.add(runFile);
  JavaIDEHelp=new JMenuItem("Java IDE帮助…");
  help.add(JavaIDEHelp);
  APIDocument=new JMenuItem("API文档…");
  help.add(APIDocument);
  aboutJavaIDE=new JMenuItem("关于Java IDE…");
  help.add(aboutJavaIDE);
  popup=new JPopupMenu();
  copyP=new JMenuItem("复制");
  popup.add(copyP);
  cutP=new JMenuItem("剪切");
  popup.add(cutP);
  pasteP=new JMenuItem("粘贴");
  popup.add(pasteP);
  deleteP=new JMenuItem("删除");
  popup.add(deleteP);
  popup.addSeparator();
  selectAllP=new JMenuItem("全选");
  popup.add(selectAllP);
//建立源文件编辑区、提示区及编译结果区三个文本区
  text1=new JTextArea();
  textSP1=new JScrollPane(text1);
  textTP=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
  shutDown=new ImageIcon("shutDown.jpg");
  textTP.addTab("新建源文件-1",textSP1);
  notice=new JTextArea();
  noticeSP=new JScrollPane(notice);
  noticeL=new JLabel("提示区：",JLabel.LEFT);
  noticeP=new JPanel();
  noticeP.setLayout(new BorderLayout());
  noticeP.add(noticeL,BorderLayout.NORTH);
  noticeP.add(noticeSP);
  compileResult=new JTextArea();
  compileResultSP=new JScrollPane(compileResult);
  compileResultL=new JLabel("编译结果区：",JLabel.LEFT);
  compileResultP=new JPanel();
  compileResultP.setLayout(new BorderLayout());
  compileResultP.add(compileResultL,BorderLayout.NORTH);
  compileResultP.add(compileResultSP);
//建立打开保存文件对话框
  openChooser=new JFileChooser();
  saveChooser=new JFileChooser(); 
//建立编译和运行线程
  compiler=new Thread();
  run_file=new Thread();
//窗口关闭按钮加入监听器
  exitWindow=new WindowShutDown();
  addWindowListener(exitWindow);
//源文件编辑区加入文档监听器
  ifChanged=new documentE();
  text1.getDocument().addDocumentListener(ifChanged);
//在三个文本区中加入焦点监听器
  gainlost=new textFocusStatus(); 
  text1.addFocusListener(gainlost);
  notice.addFocusListener(gainlost);
  compileResult.addFocusListener(gainlost);
//在三个文本区中加入鼠标监听器
  menuPopup=new menuPopupE();
  text1.addMouseListener(menuPopup);
  notice.addMouseListener(menuPopup);
  compileResult.addMouseListener(menuPopup);
//在源文件编辑区加入选项卡改变监听器
  textRequestFocus=new tabPaneChangedE();
  textTP.addChangeListener(textRequestFocus);
//向“文件”菜单各项加入监听器
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
//向“编辑”菜单各项加入监听器
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
//向“工具”菜单各项加入监听器
  compiling=new compileE();
  compile.addActionListener(compiling);
  running=new runE();
  runFile.addActionListener(running);
//向“帮助”菜单各项加入监听器
  helpinfo=new HelpE(); 
  aboutJavaIDE.addActionListener(helpinfo);
//将三个文本区加入分割面板
  innerJspane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,textTP,noticeP);
  innerJspane.setOneTouchExpandable(true);
  innerJspane.setDividerLocation(720);
  outerJspane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,innerJspane,compileResultP);
  outerJspane.setDividerLocation(390);
  outerJspane.setOneTouchExpandable(true);
//将分割面板加入框架
  setVisible(true);
  setBounds(50,50,960,540);
  Container con=getContentPane();
  con.setLayout(new BorderLayout());
  con.add(outerJspane,BorderLayout.CENTER);
  text1.requestFocus(); 
  validate();
  setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
//将第一个选项卡加入选项卡信息链表
  tabbed=new LinkedList();
  tabbed_property tab_imfo=new tabbed_property(textTP.getTabCount()-1,false,null);
  tabbed.add(tab_imfo);
}
//内部类实现源文件编辑区文本变动监听
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
//内部类实现各文本框焦点在点击“编辑”各菜单项之前状态的判断
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
//内部类实现三个文本框内容的复制、剪切、粘贴、删除、全选
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
//内部类实现鼠标触发器事件，弹出菜单
class menuPopupE extends MouseAdapter
 {   public void mouseReleased(MouseEvent e1)
     {if(popup.isPopupTrigger(e1)) 
     popup.show(e1.getComponent(),e1.getX(),e1.getY());
     }
 }
//内部类实现源文件编辑区选项卡改变时，编辑区获得焦点
class tabPaneChangedE implements ChangeListener
 {   public void stateChanged(ChangeEvent e5)
    {textSPFocusOn=(JScrollPane)textTP.getSelectedComponent();
     textFocusOn=(JTextArea)textSPFocusOn.getViewport().getView();
     textFocusOn.requestFocus();
    }
 }
//内部类实现“新建”
class createE implements ActionListener
  { public void actionPerformed(ActionEvent e4)
   {textTP.addTab("新建源文件-"+createTab,new JScrollPane(text=new JTextArea()));
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
//内部类实现“打开”
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
     catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"打开文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}   
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
//内部类实现“保存”
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
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"保存文件路径信息出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         break;
        }
        else
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"文件已存在，是否覆盖？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"保存文件路径信息出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
          {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
     temp.changed=false; 
    }   
 } 
}
//内部类实现“另存为”
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
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
        textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
        for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
         break;
        }
        temp.changed=false;
        try{temp.tabbedPath=saveFile.getCanonicalPath();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"获得文件路径信息出错,下次保存时会保存到原路径","错误信息",JOptionPane.ERROR_MESSAGE);return;}
        break;
        }
        else
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"文件已存在，是否覆盖？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
        textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
        for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
         break;
        }
        temp.changed=false;
        try{temp.tabbedPath=saveFile.getCanonicalPath();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"获得文件路径信息出错,下次保存时会保存到原路径","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
//内部类实现“关闭当前选项卡”
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
   {confirmDialogState=JOptionPane.showConfirmDialog(JavaIDE.this,"文件有改动，关闭前是否保存当前选项卡？","提示",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE); 
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
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"获得文件路径信息出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"文件已存在，是否覆盖？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"获得文件路径信息出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
        catch(Exception e){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;} 
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
          {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
//内部类实现“退出”
class exitE implements ActionListener
{tabbed_property temp; int i; boolean whetherChanged=false;
 public void actionPerformed(ActionEvent e)
 {for(i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.changed==true)
         {whetherChanged=true;confirmDialog2State=JOptionPane.showConfirmDialog(JavaIDE.this,"文件有改动，关闭前是否保存？","提示",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
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
         catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"保存文件路径信息出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         i++;continue loop;
        }
        else
        {ifCover=JOptionPane.showConfirmDialog(saveChooser,"文件已存在，是否覆盖？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);}
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
            {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
         textTP.setTitleAt(textTP.getSelectedIndex(),saveChooser.getSelectedFile().getName());
         temp.changed=false;
         try{temp.tabbedPath=saveFile.getCanonicalPath();}
         catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"保存文件路径信息出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
        catch(Exception ee){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
          {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
//内部类实现编译
class compileE implements ActionListener,Runnable
 {tabbed_property temp;String path1=null,path2=null;int n;
  public void actionPerformed(ActionEvent e)
  {for(int i=0;i<tabbed.size();i++)
        {temp=(tabbed_property)tabbed.get(i);
         if(temp.tabbedIndex==textTP.getSelectedIndex())
         break;
        }
   if(temp.tabbedPath==null)
   {JOptionPane.showMessageDialog(JavaIDE.this,"当前选项卡未保存，请保存后再编译","提醒信息",JOptionPane.WARNING_MESSAGE);return;}
   if(temp.changed==true)
   {JOptionPane.showMessageDialog(JavaIDE.this,"当前选项卡有改动，请保存后再编译","提醒信息",JOptionPane.WARNING_MESSAGE);return;}
   compileFile=new File(temp.tabbedPath);
   if(!(compileFile.exists()))
   {try{compileFile.createNewFile();}
     catch(Exception e32){JOptionPane.showMessageDialog(JavaIDE.this,"新建文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
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
    {JOptionPane.showMessageDialog(JavaIDE.this,"保存文件出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
    temp.changed=false;
    } 
   path1=temp.tabbedPath;
   n=path1.lastIndexOf(File.separator);
   path2=path1.substring(0,n);
   if(!(compiler.isAlive()))
   {compiler=new Thread(this);}
   try{compiler.start();}
   catch(Exception e31){JOptionPane.showMessageDialog(JavaIDE.this,"当前有文件未完成编译，请稍后再试","提醒信息",JOptionPane.WARNING_MESSAGE);return;}
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
    {compileResult.append("编译正确,class文件保存在"+path2+"目录中");}      
    }
   catch(Exception e30){JOptionPane.showMessageDialog(JavaIDE.this,"编译出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}
   }
  }
//内部类实现运行  
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
      catch(Exception e33){JOptionPane.showMessageDialog(JavaIDE.this,"当前有文件未停止运行，请停止运行后再试","提醒信息",JOptionPane.WARNING_MESSAGE);return;}
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
   catch(Exception e34){JOptionPane.showMessageDialog(JavaIDE.this,"运行文件或显示文件信息出错","错误信息",JOptionPane.ERROR_MESSAGE);return;}          
  }
 }
//内部类实现帮助
class HelpE implements ActionListener
{public void actionPerformed(ActionEvent e)
 {JOptionPane.showMessageDialog(JavaIDE.this,"作者：匿名","提示信息",JOptionPane.INFORMATION_MESSAGE);
 }
}
//内部类实现WindowsAdapter
class WindowShutDown extends WindowAdapter
{publ:ic void windowClosing(WindowEvent eee)
 {JOptionPane.showMessageDialog(JavaIDE.this,"请到“文件”菜单点击“退出”关闭程序","提示信息",JOptionPane.INFORMATION_MESSAGE);}
}
}
//选项卡状态类
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
