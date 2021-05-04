//通信用

import java.io.*;
import java.net.*;
//ライブラリ用
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
//Timer
import java.util.Timer;
import java.util.TimerTask;

public class ClientReal {
  
  private int x = 0;
  private static JFrame fr;//フレーム
  private static JLabel startLb;//ウンチを避けろの文字入れてる
  private static JButton startBt;//スタートボタン

  //Player
  private static JLabel playerLb;//プレイヤー画像を張っている場所
  private static ImageIcon img;//プレイヤー画像
  private static Image newImg;//プレイヤー画像のサイズ変更用
  private static Image getImg;//プレイヤー画像のサイズ変更用

  //Unchi
  private static JLabel unchiLb;//ウンチ画像張っている場所
  private static ImageIcon img_unchi;//ウンチ画像
  private static Image newImg_unchi;//ウンチ画像のサイズ変更用
  private static Image getImg_unchi;//ウンチ画像のサイズ変更用
  private static int fall = 0;//落とす用変数
  private static int count = 0;

  //位置情報の配列
  private static int unchiX;//配列指定用
  private static int unchiY;//配列指定用
  private static int[] unchi_x = new int[]{-10, 200, 400};//ウンチ移動用のXの位置配列
//  private static int[] unchi_y = new int[]{-10, 100, 200, 300, 400, 500, 610};//ウンチ移動用のYの位置配列
  private static int playerX;
  private static int[] player_x = new int[]{-120, 100, 300};//プレイヤー移動用のXの位置配列

  public static void main(String args[]) {
    new ClientReal();
  }

  //GUIの表示
  public ClientReal() {

        //外枠のフレームは変わらずに表示
        fr = new JFrame("UnchiGame");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(700, 1000);
        fr.setLayout(null);
        fr.setLocationRelativeTo(null);

        //説明文字列
        startLb = new JLabel("ウンチを5回避けよう");
        startLb.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
        startLb.setBounds(160, 100, 500, 50);
        fr.add(startLb);

        //Startボタン
        startBt = new JButton("Start");
        startBt.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 30));
        startBt.setBounds(250, 200, 180, 80);

        startBt.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {

              //ゲームにいらないObjectが消える
//              fr.remove(startLb);
              startLb.setText("");
              fr.remove(startBt);
              fr.validate();
              fr.repaint();
              fr.requestFocusInWindow();//キーボード入力のフォーカスをこのフレームにする

              //うんち表示
              unchiLb = new JLabel();
              UnchiDraw();
              fr.add(unchiLb);

              //プレイヤー表示
              playerLb = new JLabel();
              PlayerDraw();
              fr.add(playerLb);

              System.out.println("サーバーから結果を受け取る");
              Server();

              UnchiMove();//ウンチ落ちる処理

              //キーボード処理
              fr.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                  
                  //キーボード入力
                  switch (e.getKeyCode()) {
                      //左
                    case KeyEvent.VK_LEFT:
                      //System.out.println("左触っただろ？");
                      playerX--;
                      break;
                    
                      //右
                    case KeyEvent.VK_RIGHT:
                      //System.out.println("右触っただろ？");
                      playerX++;
                      break;

                  }
                  //プレイヤー移動
                  playerX = (playerX < 0 ? 0 : playerX);//( 条件 ？ 真の時の処理　： 偽の時の処理 )
                  playerX = (playerX >= player_x.length ? player_x.length - 1 : playerX);
                  playerLb.setLocation(player_x[playerX], 700);
                  //System.out.println("player X:" + playerLb.getLocation().x + " x:" + x);

                }

                @Override
                public void keyTyped(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {}
              
              });
              
              System.out.println("actionPerformed() end");

            }
            
          }
        );
    
    fr.add(startBt);
    
    System.out.println("フレーム表示");
    fr.setVisible(true);
  
  }

  //ウンチの移動
  private void UnchiMove() {
    //Timer
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      
      @Override
      public void run() {
        //int fall = 6;//グローバル変数fall
        //System.out.println("in run() fall:" + fall);

        if (unchiY < 610) unchiY += 30;
        else{//下まで落ち切った時
          timer.cancel();
          
          //当たり判定
          if(playerX == unchiX) {
            System.out.println("頭にウンチが刺さった。");
            startLb.setText("Game Over");
          }else if(count == 4){
            startLb.setText("Game Clear");
          }else{
//            System.out.println("No.1 ウンチのY位置" + unchiY);
            unchiY = 0;
//            System.out.println("No.2 ウンチのY位置" + unchiY);
            Server();
            UnchiMove();
            count ++;
          }
        }
        
        unchiLb.setLocation(unchi_x[unchiX], unchiY);
        //System.out.println("unchi pos:" + unchiLb.getLocation());
      }
    
    }, 0, 33);//1000 / 30fps = 33
    
    //timer.cancel();//run内の条件内に移動↑
  
  }
  
  //プレイヤー線画
  public static void PlayerDraw() {
    img = new ImageIcon("Player.png");

    //画像を小さく
    getImg = img.getImage();
    newImg = getImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
    img = new ImageIcon(newImg);
    //サイズ変更した画像をセット
    playerLb = new JLabel(img);
    playerX = 0;
    playerLb.setBounds(player_x[playerX], 700, 500, 300);
    playerLb.setIcon(img);

    System.out.println("PlayerDraw() end");
  }
  
  //うんち線画
  public static void UnchiDraw() {
    img_unchi = new ImageIcon("Unchi.png");
    //画像を小さく
    getImg_unchi = img_unchi.getImage();
    newImg_unchi = getImg_unchi.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
     //サイズ変更した画像をセット
    img_unchi = new ImageIcon(newImg_unchi);
    unchiLb = new JLabel(img_unchi);
    unchiLb.setIcon(img_unchi);

    System.out.println("UnchiDraw() end");
  }

  //ウンチの開始位置設定(最後のデータのみ使用可能)
  public static void UnchiPosition(int num) {

    unchiX = num;
    unchiLb.setBounds(unchi_x[unchiX], -10, 300, 200);

  }
  
  //サーバ処理
  public static void Server() {
    
    try {
      // サーバーとポートを取得する
      String server = "192.168.1.36";
      int port = 1101;

      for (int j = 0; j < 1; j++) {
        // ソケットを作成する
        Socket s = new Socket(InetAddress.getLocalHost().getHostAddress(), port); // プレイヤーのPCがサーバの時に楽するための文

        System.out.println("接続完了：" + s);

        // サーバーから乱数を読み取る
        InputStream is = s.getInputStream();
        DataInputStream dis = new DataInputStream(is);//バイトの読み込み

        int num = dis.readInt();
        UnchiPosition(num);

        System.out.println("受信データ data：" + num);//Intに変換

        // ストリームとソケットをクローズする
        is.close();
        s.close();
        System.out.println("接続解除");
      }
    } catch (Exception e) {

      System.out.println("ネットワークエラー");


    }
    
  }

}