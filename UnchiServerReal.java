import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

//Timer
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

//乱数を生成してクライアントに送信するのみ
class UnchiServerReal{
  
  private static int ram;
  private static double num;
  private static int counter = 0;
  
  public static void main(String args[]) throws ParseException{
    try{
      //ポートを取得
      int port = 1101;
      
      //サーバソケット作成
      ServerSocket server = new ServerSocket(port);
      Socket socket = null;
      System.out.println("サーバ準備完了");
      System.out.println("port:"+port);
      
      while(true){
        try {
          //接続回数
          int i=0;
          //クライアントからの要求を受け取る設定
          socket = server.accept();
          System.out.println("接続されました " + socket.getRemoteSocketAddress());
          
          //表示+接続先のソケットアドレス
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
            Thread.sleep(1000);// 定期実行を待って休止
            //乱数を出して出力ストリームで出力
            data.writeInt(ram);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          timer.cancel();
          
          //ストリームとソケットを閉じる
          out.close();
          socket.close();
        }
        catch (IOException e) {
          System.err.println("閉じられん。");
        }
        System.out.println("------------------------");
      }
      
    }catch (Exception e){
      System.out.println("Exception:" + e);
    }
  }
}