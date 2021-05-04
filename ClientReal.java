//�ʐM�p

import java.io.*;
import java.net.*;
//���C�u�����p
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
//Timer
import java.util.Timer;
import java.util.TimerTask;

public class ClientReal {
  
  private int x = 0;
  private static JFrame fr;//�t���[��
  private static JLabel startLb;//�E���`�������̕�������Ă�
  private static JButton startBt;//�X�^�[�g�{�^��

  //Player
  private static JLabel playerLb;//�v���C���[�摜�𒣂��Ă���ꏊ
  private static ImageIcon img;//�v���C���[�摜
  private static Image newImg;//�v���C���[�摜�̃T�C�Y�ύX�p
  private static Image getImg;//�v���C���[�摜�̃T�C�Y�ύX�p

  //Unchi
  private static JLabel unchiLb;//�E���`�摜�����Ă���ꏊ
  private static ImageIcon img_unchi;//�E���`�摜
  private static Image newImg_unchi;//�E���`�摜�̃T�C�Y�ύX�p
  private static Image getImg_unchi;//�E���`�摜�̃T�C�Y�ύX�p
  private static int fall = 0;//���Ƃ��p�ϐ�
  private static int count = 0;

  //�ʒu���̔z��
  private static int unchiX;//�z��w��p
  private static int unchiY;//�z��w��p
  private static int[] unchi_x = new int[]{-10, 200, 400};//�E���`�ړ��p��X�̈ʒu�z��
//  private static int[] unchi_y = new int[]{-10, 100, 200, 300, 400, 500, 610};//�E���`�ړ��p��Y�̈ʒu�z��
  private static int playerX;
  private static int[] player_x = new int[]{-120, 100, 300};//�v���C���[�ړ��p��X�̈ʒu�z��

  public static void main(String args[]) {
    new ClientReal();
  }

  //GUI�̕\��
  public ClientReal() {

        //�O�g�̃t���[���͕ς�炸�ɕ\��
        fr = new JFrame("UnchiGame");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(700, 1000);
        fr.setLayout(null);
        fr.setLocationRelativeTo(null);

        //����������
        startLb = new JLabel("�E���`��5������悤");
        startLb.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
        startLb.setBounds(160, 100, 500, 50);
        fr.add(startLb);

        //Start�{�^��
        startBt = new JButton("Start");
        startBt.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 30));
        startBt.setBounds(250, 200, 180, 80);

        startBt.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent e) {

              //�Q�[���ɂ���Ȃ�Object��������
//              fr.remove(startLb);
              startLb.setText("");
              fr.remove(startBt);
              fr.validate();
              fr.repaint();
              fr.requestFocusInWindow();//�L�[�{�[�h���͂̃t�H�[�J�X�����̃t���[���ɂ���

              //���񂿕\��
              unchiLb = new JLabel();
              UnchiDraw();
              fr.add(unchiLb);

              //�v���C���[�\��
              playerLb = new JLabel();
              PlayerDraw();
              fr.add(playerLb);

              System.out.println("�T�[�o�[���猋�ʂ��󂯎��");
              Server();

              UnchiMove();//�E���`�����鏈��

              //�L�[�{�[�h����
              fr.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                  
                  //�L�[�{�[�h����
                  switch (e.getKeyCode()) {
                      //��
                    case KeyEvent.VK_LEFT:
                      //System.out.println("���G��������H");
                      playerX--;
                      break;
                    
                      //�E
                    case KeyEvent.VK_RIGHT:
                      //System.out.println("�E�G��������H");
                      playerX++;
                      break;

                  }
                  //�v���C���[�ړ�
                  playerX = (playerX < 0 ? 0 : playerX);//( ���� �H �^�̎��̏����@�F �U�̎��̏��� )
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
    
    System.out.println("�t���[���\��");
    fr.setVisible(true);
  
  }

  //�E���`�̈ړ�
  private void UnchiMove() {
    //Timer
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      
      @Override
      public void run() {
        //int fall = 6;//�O���[�o���ϐ�fall
        //System.out.println("in run() fall:" + fall);

        if (unchiY < 610) unchiY += 30;
        else{//���܂ŗ����؂�����
          timer.cancel();
          
          //�����蔻��
          if(playerX == unchiX) {
            System.out.println("���ɃE���`���h�������B");
            startLb.setText("Game Over");
          }else if(count == 4){
            startLb.setText("Game Clear");
          }else{
//            System.out.println("No.1 �E���`��Y�ʒu" + unchiY);
            unchiY = 0;
//            System.out.println("No.2 �E���`��Y�ʒu" + unchiY);
            Server();
            UnchiMove();
            count ++;
          }
        }
        
        unchiLb.setLocation(unchi_x[unchiX], unchiY);
        //System.out.println("unchi pos:" + unchiLb.getLocation());
      }
    
    }, 0, 33);//1000 / 30fps = 33
    
    //timer.cancel();//run���̏������Ɉړ���
  
  }
  
  //�v���C���[����
  public static void PlayerDraw() {
    img = new ImageIcon("Player.png");

    //�摜��������
    getImg = img.getImage();
    newImg = getImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
    img = new ImageIcon(newImg);
    //�T�C�Y�ύX�����摜���Z�b�g
    playerLb = new JLabel(img);
    playerX = 0;
    playerLb.setBounds(player_x[playerX], 700, 500, 300);
    playerLb.setIcon(img);

    System.out.println("PlayerDraw() end");
  }
  
  //���񂿐���
  public static void UnchiDraw() {
    img_unchi = new ImageIcon("Unchi.png");
    //�摜��������
    getImg_unchi = img_unchi.getImage();
    newImg_unchi = getImg_unchi.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
     //�T�C�Y�ύX�����摜���Z�b�g
    img_unchi = new ImageIcon(newImg_unchi);
    unchiLb = new JLabel(img_unchi);
    unchiLb.setIcon(img_unchi);

    System.out.println("UnchiDraw() end");
  }

  //�E���`�̊J�n�ʒu�ݒ�(�Ō�̃f�[�^�̂ݎg�p�\)
  public static void UnchiPosition(int num) {

    unchiX = num;
    unchiLb.setBounds(unchi_x[unchiX], -10, 300, 200);

  }
  
  //�T�[�o����
  public static void Server() {
    
    try {
      // �T�[�o�[�ƃ|�[�g���擾����
      String server = "192.168.1.36";
      int port = 1101;

      for (int j = 0; j < 1; j++) {
        // �\�P�b�g���쐬����
        Socket s = new Socket(InetAddress.getLocalHost().getHostAddress(), port); // �v���C���[��PC���T�[�o�̎��Ɋy���邽�߂̕�

        System.out.println("�ڑ������F" + s);

        // �T�[�o�[���痐����ǂݎ��
        InputStream is = s.getInputStream();
        DataInputStream dis = new DataInputStream(is);//�o�C�g�̓ǂݍ���

        int num = dis.readInt();
        UnchiPosition(num);

        System.out.println("��M�f�[�^ data�F" + num);//Int�ɕϊ�

        // �X�g���[���ƃ\�P�b�g���N���[�Y����
        is.close();
        s.close();
        System.out.println("�ڑ�����");
      }
    } catch (Exception e) {

      System.out.println("�l�b�g���[�N�G���[");


    }
    
  }

}