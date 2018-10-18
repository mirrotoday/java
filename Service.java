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
			/*�Դӽ����¼��ѡ���һ����Ϊ������������ͼƬ����ʾ��ֻ���ڻ���*/
			int index = book.indexOf(',');
			book = book.substring(0,index);
			imgFile = dataOperate.imgFileQuery1(index);	
		}else {
			/*�Դӽ����¼��ѡ���һ����Ϊ������������ͼƬ����ʾ*/
			if(book.endsWith(" .jpg") || book.endsWith(" .jpeg")) {
				int index = book.lastIndexOf(",");
				imgFile = book.substring(index + 1);				
			}
		}
		return imgFile;
	}//getImgFile�����������
	
	
	
	public static Vector<String>seek(int operateFlag,String name,String author,
			String publishers,String condition){
		//�÷������ݲ�ѯ��������ѯͼ����Ϣ������¼
		       //��ʼ��֯��ѯ��䣬sqlΪ��ѯ����ַ���
		String sql = "select * from books";
		int selectFlag = 0;
		if(name !=null && ! name.equals("")) {
			sql +="WHERE name LIKE��";
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
		if(condition.equals("ģ����ѯ")) {
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
			 c = '��';
		 if(operateFlag == BooksManager.RETURN_RECORD)
			 c = '��';
			String details = "��Ҫ�鿴��һ��" + c
					+"���¼����ϸ��Ϣ����\n" +  "��¼��ţ�"+ str[0] + "\n"
					+"���:" + str[1] + "\n" + "������"+ str[2] + "\n"
					+"����:" + str[3] + "\n" + "�����磺"+ str[4] + "\n"
					+"����ʱ��:" + str[5] + "\n" +c + "����������" + str[6] + "\n"
					+ c +"���˵�λ��"+ str[7] + "\n" + "������������" + str[8] + "\n"
					+ c +"��ʱ�䣺" + book;
					return details;		
	}
	
	
	public static int deleteBook(Component c, String book) {  //server���deleteBook(Component,String)����
		// TODO Auto-generated method stub
		String str[] = new String[8];
		int index = -1;
		for(int i=0;i<8;i++){
			index = book.indexOf(',');
			str[i]= book.substring(0, index);
			book = book.substring(index+1);
		}
		int confirm = JOptionPane.showConfirmDialog(c,
				"������Ҫɾ����һ�������Ϣ���£�\n"
				+"���:" + str[1] + "\n" + "������"+ str[2] + "\n"
				+"����:" + str[3] + "\n" + "�����磺"+ str[4] + "\n"
				+"�۸�:" + str[5] + "\n" + "����ʱ�䣺"+ str[6] + "\n"
				+"���λ��:" + str[7] + "\n" + "ȷʵ��Ҫɾ����");
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
		hintMessage.append("������Ҫ���ĵ�һ�������Ϣ���£�\n"
				+"���:" + str[1] + "\n" + "������"+ str[2] + "\n"
				+"����:" + str[3] + "\n" + "�����磺"+ str[4] + "\n"
				+"�۸�:" + str[5] + "\n" + "����ʱ�䣺"+ str[6] + "\n"
				+"���λ��:" + str[7] + "\n" + "���������" + remainder + "\n");
		if(remainder>0) {
			int confirm = JOptionPane.showConfirmDialog(c,hintMessage + "ȷ��������");
			if(confirm>0)
				return 1;
			int id = Integer.parseInt(str[0]);
			int lentQuantity = Integer.parseInt(str[9] + 1);
			String s1 = JOptionPane.showInputDialog(c,"���������������");
			String s2 = JOptionPane.showInputDialog(c,"��������������ڵ�λ");
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
		 //���ݽ����¼�е�״ֵ̬���ж��Ƿ��ѻ�
		 JOptionPane.showMessageDialog(c,"�����ѻ���");
		 return 1;	
	 }
	 //���ݽ����¼��ʾ��ѯ��ͼ���ʶ
	 int lendRecordId = Integer.parseInt(book.substring(0,index));
	 int bookId = dataOperate.bookIdQueryWithLendRecordId(lendRecordId); 
	 //����ͼ���ʶ��ѯ��ͼ����Ϣ
	 String bookInfo = dataOperate.bookInfoQueryWithBookId(bookId);
	 String hintMessage = "������Ҫ����һ�������Ϣ����:\n" + bookInfo + "\n ȷ��������";
	 int confirm = JOptionPane.showConfirmDialog(c, hintMessage);
	 if(confirm>0)
		 return 1;
	 index = bookInfo.lastIndexOf(':');
	 int lentQuantity = Integer.parseInt(bookInfo.substring(index + 1).trim()) -1;
	 String s1 = JOptionPane.showInputDialog(c,"�����뻹����������");
	 String s2 = JOptionPane.showInputDialog(c,"�����뻹�������ڵ�λ");
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


	

	
	
	
	 

