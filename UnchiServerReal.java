import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

//Timer
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

//�����𐶐����ăN���C�A���g�ɑ��M����̂�
class UnchiServerReal{
  
  private static int ram;
  private static double num;
  private static int counter = 0;
  
  public static void main(String args[]) throws ParseException{
    try{
      //�|�[�g���擾
      int port = 1101;
      
      //�T�[�o�\�P�b�g�쐬
      ServerSocket server = new ServerSocket(port);
      Socket socket = null;
      System.out.println("�T�[�o��������");
      System.out.println("port:"+port);
      
      while(true){
        try {
          //�ڑ���
          int i=0;
          //�N���C�A���g����̗v�����󂯎��ݒ�
          socket = server.accept();
          System.out.println("�ڑ�����܂��� " + socket.getRemoteSocketAddress());
          
          //�\��+�ڑ���̃\�P�b�g�A�h���X
          OutputStream out = socket.getOutputStream();
          DataOutputStream data = new DataOutputStream(out);
          
          //Timer
          Timer timer = new Timer();
          timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
              ram = (int)(Math.random()*3);
            }
          },  0, 500);
          try {
            Thread.sleep(1000);// ������s��҂��ċx�~
            //�������o���ďo�̓X�g���[���ŏo��
            data.writeInt(ram);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          timer.cancel();
          
          //�X�g���[���ƃ\�P�b�g�����
          out.close();
          socket.close();
        }
        catch (IOException e) {
          System.err.println("������B");
        }
        System.out.println("------------------------");
      }
      
    }catch (Exception e){
      System.out.println("Exception:" + e);
    }
  }
}