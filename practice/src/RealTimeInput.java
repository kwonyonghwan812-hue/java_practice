
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RealTimeInput {
    public static void main(String[] args) {
        JFrame window = new JFrame("키보드 감시창");
        window.setBackground(Color.blue);
        window.setSize(300, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 💡 엔터 없이 키보드가 눌리는 순간을 실시간으로 감시하는 센서 부착!
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 키보드를 '누르는 순간' 엔터 없이 즉시 실행됨!
                System.out.println("눌린 키: " + e.getKeyChar() + " (코드: " + e.getKeyCode() + ")");
                
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    System.out.println("🤖 로봇 전진 명령!");
                }
            }

            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {} // 키에서 손을 뗄 때
        });

        window.setVisible(true);
    }
}