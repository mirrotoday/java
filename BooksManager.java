
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
@SuppressWarnings("serial")
public class BooksManager extends JFrame implements ActionListener{
	public final static int INSERT = 0;
	public final static int SELECT = 1;
	public final static int UPDATE = 2;
	public final static int DELETE = 3;
	public final static int LEND = 4;
	public final static int LEND_RECORD = 5;
	public final static int RETURN = 6;
	public final static int RETURN_RECORD = 7;
	private JDesktopPane m_desktop = new JDesktopPane();
	private JMenuItem insertMenuItem;
	private JMenuItem selectMenuItem;
	private JMenuItem updateMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem lendMenuItem;
	private JMenuItem lendRecordMenuItem;
	private JMenuItem returnMenuItem;
	private JMenuItem returnRecordMenuItem;
    private JInternalFrame titleFrame;
    private JMenu returnMenu;
    private int numberOfInternalFrame = 0;
    private int userId;
    public BooksManager(int userId) {
    	super("资料室图书管理信息系统");
    	this.userId = userId;
    	JMenuBar theMenuBar = new JMenuBar();
    	JMenu managerMenu = new JMenu("图书管理");
    	JMenu lendMenu = new JMenu("借书");
    	JMenu retuenMenu = new JMenu("还书");
    	insertMenuItem = new JMenuItem("录入");
    	selectMenuItem = new JMenuItem("检索");
    	updateMenuItem = new JMenuItem("修改");
    	deleteMenuItem = new JMenuItem("删除");
    	lendMenuItem = new JMenuItem("借书");
    	lendRecordMenuItem = new JMenuItem("借书记录");
    	returnMenuItem = new JMenuItem("还书");
    	returnRecordMenuItem = new JMenuItem("还书记录");
    	setJMenuBar(theMenuBar);
    	theMenuBar.add(managerMenu);
    	theMenuBar.add(lendMenu);
    	theMenuBar.add(retuenMenu);
    	managerMenu.add(insertMenuItem);
    	managerMenu.add(selectMenuItem);
    	managerMenu.add(updateMenuItem);
    	managerMenu.add(deleteMenuItem);
    	lendMenu.add(lendMenu);
    	lendMenu.add(lendRecordMenuItem);
    	returnMenu.add(returnMenuItem);
    	returnMenu.add(returnRecordMenuItem);
    	Container theContainer = getContentPane();
    	theContainer.add(m_desktop);
    	JLabel label = new JLabel("资料室图书管理信息系统",JLabel.CENTER);
    	label.setFont(new Font("隶书",Font.BOLD,30)); 
    	label.setForeground(Color.blue);
    	titleFrame = new JInternalFrame(null,true);
    	Container c = titleFrame.getContentPane();
    	c.add(label,BorderLayout.CENTER);
    	titleFrame.setSize(500, 300);
    	m_desktop.add(titleFrame);
    	titleFrame.setVisible(true);
    	setSize(505,358);
    	insertMenuItem.addActionListener(this);
    	selectMenuItem.addActionListener(this);
    	updateMenuItem.addActionListener(this);
    	deleteMenuItem.addActionListener(this);
    	lendMenuItem.addActionListener(this);
    	lendRecordMenuItem.addActionListener(this);
    	returnMenuItem.addActionListener(this);
    	returnRecordMenuItem.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
    	JPanel panel = null;
    	String s = null;
    	Object source = e.getSource();
    	if(source == insertMenuItem);{
    		s = "录入图书";
    	    panel = new UpdatePanel(INSERT);
    	}
    	if(source == selectMenuItem) {
    		s = "查询图书";
    		panel = new QueryPanel(SELECT,userId);
    	}
    	if(source == updateMenuItem) {
    		s = "修改图书信息";
    		panel = new CardPanel(userId);
    	}
    	if(source == deleteMenuItem) {
    		s = "删除图书";
    		panel = new QueryPanel(DELETE,userId);
    	}
    	if(source == lendMenuItem) {
    		s = "借阅图书";
    		panel = new QueryPanel(LEND,userId);
    	}
    	if(source == returnMenuItem) {
    		s = "还书";
    		panel = new QueryPanel(RETURN,userId);
    	}
    	if(source == lendRecordMenuItem) {
    		s = "借书记录";
    		panel = new QueryPanel(LEND_RECORD,userId);
    	}
    	if(source == returnRecordMenuItem) {
    		s = "还书记录";
    		panel = new QueryPanel(RETURN_RECORD,userId);
    	}
    JInternalFrame internalFrame = new JInternalFrame(s,true,true,true,true);
    numberOfInternalFrame ++;
    Container c = internalFrame.getContentPane();
    c.setLayout(new BorderLayout());
    c.add(panel,BorderLayout.CENTER);
    internalFrame.pack();
    m_desktop.add(internalFrame);
    
    Dimension d = internalFrame.getSize();
    setSize(new Dimension((int)d.getWidth() + 5,(int)d.getHeight() + 58));
    internalFrame.setVisible(true);
    MenuItemsEnabled(false);
    internalFrame.addInternalFrameListener(new InternalFrameHandler());
    }
      private void MenuItemsEnabled(boolean b) {
    	   insertMenuItem.setEnabled(b);
    	   selectMenuItem.setEnabled(b);
    	   updateMenuItem.setEnabled(b);
    	   deleteMenuItem.setEnabled(b);
    	   lendMenuItem.setEnabled(b);
    	   returnMenuItem.setEnabled(b);
    	   }
      /*以下是内部窗口框架监听器类，被定义为BooksManager类中的内部类，用来处理内部窗口被关闭的事件*/
       private class InternalFrameHandler extends InternalFrameAdapter{
    	   public void internalFrameClosing(InternalFrameEvent e) {
    		   numberOfInternalFrame --;
    		   if(numberOfInternalFrame == 0)
    			     MenuItemsEnabled(true);
    		   Dimension d1 = titleFrame.getSize();
    		  setSize(new Dimension((int)d1.getWidth(),
    				  (int)d1.getHeight() + 55));
    	     }
    	   }  
       }
    
    
 