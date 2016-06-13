
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * �����߳�
 * 
 *
 */
public class M2MSend extends Thread {
	
	//�û�����
	private ArrayList<Socket> list = M2MServer.getList();
	//��ǰ�û�
	private Socket s;
	
	public M2MSend(Socket s){
		this.s = s;
	}
	
	public void run(){
		
		//��ȡ���û� IP
		String ip = s.getInetAddress().getHostAddress();
		System.out.println(ip);
		
		try {
			
			//��ȡ�û���Ϣ
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("read start");
			//���ϵĶ�ȡд������
			while(true){
				
				//��������
				String info = null;
				info=reader.readLine();
				System.out.println(info);
		
			
				//�����ȡ��Ϣ��Ϊ��
				//if((info=reader.readLine()) != null){
				String sql="select * from `order` where name ='"+info+"';";
				String sql2="delete from `order` where name ='"+info+"';";
	    		 try{
	    			 Statement stmt = null;
	    			 Connection conn = null;
	    			 conn=DB.getConn();
	    			 stmt=DB.createStmt(conn);
	    			 ResultSet rs = stmt.executeQuery(sql);
	    			 while(rs.next()){
	    				 info=(rs.getString("comd"));
	    			 }
	    			 stmt.execute(sql2);
	    			 }
	    		 catch(Exception e){
	    			 e.printStackTrace();
	    			 }
	    		 String [] comds = info.split(",");
	    		 System.out.println(comds.length);
	    		//��ȡ����������
				if(comds.length>1){
	    		 for(int i =0;i<comds.length/20;i++){
	    			 StringBuilder comd = new StringBuilder();
	    			 for(int j = 0 ;j<20;j++){
	    				 comd.append(comds[i*20+j]+",");
	    			 }
	    			//д����Ϣ
	    			 PrintWriter pw;
	 				pw = new PrintWriter(s.getOutputStream());
						pw.println(comd.toString());
						System.out.println(comd.toString());
						pw.flush();
						Thread.sleep(5000);
	    		 }
	    		 StringBuilder comd = new StringBuilder();
	    		 for(int i = 120;i<139;i++){
	    			 comd.append(comds[i]+",");
	    		 }
	    		PrintWriter pw;
				pw = new PrintWriter(s.getOutputStream());
	    		pw.println(comd.toString());
				System.out.println(comd.toString());
				pw.flush();
				System.out.println(info);
	    		 Thread.sleep(5000);
				}else{
					System.out.println("no comd");
				}
				//}
			}
			
		} catch (IOException | InterruptedException e1) {
			//�û�����
			list.remove(s);
			System.err.println(ip + " ������ , ��ǰ��������Ϊ: " + list.size() + " �� !");
		}
		
		
	}

}
