import java.awt.Component;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Service {
	private static final Component c = null;
	private static DataOperator dataOperate = new DataOperator();
	
	public static Vector<String>publishers(){
		return dataOperate.publishersQuery();
	}

	public static String getImgFile(int operateFlag,String book) {
		String imgFile = null;
		if(operateFlag == BooksManager.RETURN) {
			/*以从借书记录中选择的一本书为索引，查找其图片并显示。只用于还书*/
			int index = book.indexOf(',');
			book = book.substring(0,index);
			imgFile = dataOperate.imgFileQuery1(index);	
		}else {
			/*以从借书记录中选择的一本书为索引，查找其图片并显示*/
			if(book.endsWith(" .jpg") || book.endsWith(" .jpeg")) {
				int index = book.lastIndexOf(",");
				imgFile = book.substring(index + 1);				
			}
		}
		return imgFile;
	}//getImgFile方法定义结束
	
	
	
	public static Vector<String>seek(int operateFlag,String name,String author,
			String publishers,String condition){
		//该方法根据查询条件，查询图书信息或借书记录
		       //开始组织查询语句，sql为查询语句字符串
		String sql = "select * from books";
		int selectFlag = 0;
		if(name !=null && ! name.equals("")) {
			sql +="WHERE name LIKE？";
			selectFlag = 1;
			if(author!= null && !author.equals("")) {
				sql +="AND author LIKE?";
				selectFlag = 2;
			}
			if(!publishers.equals("")) {
				sql +="AND publisher LIKE?";
				selectFlag = 3;					
			}else {
				if(!publishers.equals("")) {
					sql +="AND publisher LIKE?";
					selectFlag = 4;	
				}
			}
		}else {
			if(author != null && !author.equals("")) {
				sql +="WHERE pauthor LIKE?";
				selectFlag = 5;
				if(!publishers.equals("")) {
					sql +="AND publisher LIKE?";
					selectFlag = 6;	
					}
				}else {
					if(!publishers.equals("")) {
						sql +="AND publisher LIKE?";
						selectFlag = 7;	
				}
			}
		}
		if(operateFlag == BooksManager.LEND_RECORD || 
				operateFlag == BooksManager.RETURN) {
			StringBuffer sb = new StringBuffer(sql);
			sb.replace(7,20,"lendRecord.id.books.no,books.name,"
			+ "books.author,books.publisher,books.pubDate,"
		    + "lendRecord.borrower,lendRecord.borrowerUnit,"
			+ "users.userName,lendRecord.borrowDate,lendRecord.state"
		    + "FROM lendRecord"
			+ "INSERT JOIN books ON lendRecord.bookId = books.id"
			+ "INSERT JOIN users ON lendRecord.bookId = books.id");
			sql = sb.toString();		
		}
		if(operateFlag == BooksManager.LEND_RECORD || 
				operateFlag == BooksManager.RETURN) {
			StringBuffer sb = new StringBuffer(sql);
			sb.replace(7,20,"lendRecord.id.books.no,books.name,"
			+ "books.author,books.publisher,books.pubDate,"
		    + "returnRecord.returner,returnRecord.returnerUnit,"
			+ "users.userName,returnRecord.returnDate"
		    + "FROM returnRecord"
			+ "INSERT JOIN books ON lendRecord.bookId = books.id"
			+ "INSERT JOIN users ON lendRecord.bookId = books.id");
			sql = sb.toString();		
		}
		if(condition.equals("模糊查询")) {
			name = "%" + name + "%";
			author = "%" + author + "%";
			publishers = "%" + publishers + "%";
		}
		Vector<String>infoStringCollection = dataOperate.generalQuery(operateFlag,
				sql,selectFlag,name,author,publishers);
		   return infoStringCollection;
	}
	public static String detailsOfBook(int operateFlag,String book) {
		 String str[] = new String[9];
		 int index = -1;
		 for(int i=0;i<9;i++) {
			 index = book.indexOf(',');
			 str[i] = book.substring(0,index);
			 book = book.substring(index +1);			 
		 }
		 char c = (char) 0;
		 if(operateFlag == BooksManager.LEND_RECORD)
			 c = '借';
		 if(operateFlag == BooksManager.RETURN_RECORD)
			 c = '还';
			String details = "您要查看的一条" + c
					+"书记录的详细信息如下\n" +  "记录序号："+ str[0] + "\n"
					+"书号:" + str[1] + "\n" + "书名："+ str[2] + "\n"
					+"作者:" + str[3] + "\n" + "出版社："+ str[4] + "\n"
					+"出版时间:" + str[5] + "\n" +c + "书人姓名：" + str[6] + "\n"
					+ c +"书人单位："+ str[7] + "\n" + "操作人姓名：" + str[8] + "\n"
					+ c +"书时间：" + book;
					return details;		
	}
	
	
	public static int deleteBook(Component c, String book) {  //server类的deleteBook(Component,String)方法
		// TODO Auto-generated method stub
		String str[] = new String[8];
		int index = -1;
		for(int i=0;i<8;i++){
			index = book.indexOf(',');
			str[i]= book.substring(0, index);
			book = book.substring(index+1);
		}
		int confirm = JOptionPane.showConfirmDialog(c,
				"您决定要删除的一本书的信息如下：\n"
				+"书号:" + str[1] + "\n" + "书名："+ str[2] + "\n"
				+"作者:" + str[3] + "\n" + "出版社："+ str[4] + "\n"
				+"价格:" + str[5] + "\n" + "出版时间："+ str[6] + "\n"
				+"存放位置:" + str[7] + "\n" + "确实需要删除吗");
		if(confirm>0)
			return 1;
		int deleteBookID = Integer.parseInt(str[0]);
		if(dataOperate.delete(deleteBookID) == -1)
			return -1;
		return 0;
	}
	
	public static int lendBook(Component c, int userId, String book, StringBuffer hintMessage) {
		String str[] = new String[10];
		int index = -1;
		for(int i=0;i<10;i++) {
			index = book.indexOf(0,index);
			book = book.substring(index +1);
		}
		int remainder = Integer.parseInt(str[8]) - Integer.parseInt(str[9]);
		hintMessage.append("您决定要借阅的一本书的信息如下：\n"
				+"书号:" + str[1] + "\n" + "书名："+ str[2] + "\n"
				+"作者:" + str[3] + "\n" + "出版社："+ str[4] + "\n"
				+"价格:" + str[5] + "\n" + "出版时间："+ str[6] + "\n"
				+"存放位置:" + str[7] + "\n" + "库存数量：" + remainder + "\n");
		if(remainder>0) {
			int confirm = JOptionPane.showConfirmDialog(c,hintMessage + "确定借阅吗？");
			if(confirm>0)
				return 1;
			int id = Integer.parseInt(str[0]);
			int lentQuantity = Integer.parseInt(str[9] + 1);
			String s1 = JOptionPane.showInputDialog(c,"请输入借书人姓名");
			String s2 = JOptionPane.showInputDialog(c,"请输入借书人所在单位");
			if(dataOperate.lend(id,lentQuantity,s1,s2,userId) == -1)
				return -1;
			return 0; 
		}else {
			return 2;
		}
	}
	
	public static int returnBook(Component c,int userId,String book) {
		 int index = book.indexOf(',');
		 if(book.charAt(book.length() -1) == '1') {
		 //根据借书记录中的状态值，判断是否已还
		 JOptionPane.showMessageDialog(c,"该书已还！");
		 return 1;	
	 }
	 //根据借书记录表示查询出图书标识
	 int lendRecordId = Integer.parseInt(book.substring(0,index));
	 int bookId = dataOperate.bookIdQueryWithLendRecordId(lendRecordId); 
	 //根据图书标识查询出图书信息
	 String bookInfo = dataOperate.bookInfoQueryWithBookId(bookId);
	 String hintMessage = "您决定要还的一本书的信息如下:\n" + bookInfo + "\n 确定还书吗？";
	 int confirm = JOptionPane.showConfirmDialog(c, hintMessage);
	 if(confirm>0)
		 return 1;
	 index = bookInfo.lastIndexOf(':');
	 int lentQuantity = Integer.parseInt(bookInfo.substring(index + 1).trim()) -1;
	 String s1 = JOptionPane.showInputDialog(c,"请输入还书人姓名！");
	 String s2 = JOptionPane.showInputDialog(c,"请输入还书人所在单位");
	 if(dataOperate.returnB(lendRecordId,bookId,lentQuantity,s1,s2,userId)!=0)
		return -1;
	 return 0; 
	 }

	public static int addBook(Vector<String> bookinfo) {
		// TODO Auto-generated method stub
		if(dataOperate.insert(bookinfo) == -1)
			return -1;
		return 0;

	}

	@SuppressWarnings("static-access")
	public static int modifybook(int updateBookID, Vector<String> bookinfo) {
		// TODO Auto-generated method stub
		if(dataOperate.update(updateBookID,bookinfo) == -1)
			return -1;
		return 0;
		
	}

	private static DataOperator dataOperate1 =  new DataOperator();
	public static int login(String userName,String password) {
		dataOperate.loadDatabseDriver();
		dataOperate.connect();
//		dataOperate.addSuperUser();
		return dataOperate.userQuery(userName,password);
	}
	public static void quit() {
		dataOperate.disconnect();
	}
}


	

	
	
	
	 

