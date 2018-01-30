
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    public int w = 900; //Game width
    public int h = 900; //Game height
    public JFrame frame;
    public Random randy = new Random();
    public static int level = 0;
    int money = 100;
    public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    static ArrayList<Tower> towers = new ArrayList<Tower>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    int neutralTowers = 1;
    int fireTowers = 1;
    int waterTowers = 1;
    int grassTowers = 1;
    int farmTowers = 1;
    boolean inLevel = false;
    int numEnemies = 0;
    int numEnemiesSpawned = 0;
    int numEnemiesKilled = 0;
    int health = 100;
    int count = 0;
    boolean gameOver = false;
    boolean buying = false;
    int mouseX = 0;
    int mouseY = 0;
    int[][] board = new int[12][12];
    char typeBuying;
    Tower displayTower = null;
    public static final int maxRadius = 3;
    public static final int maxPower = 3;
    public static final int minSpeed = 10;
    boolean canUpgradeRadius = false;
    boolean canUpgradePower = false;
    boolean shoots;

    public static void main(String[] args) {
        Game project = new Game("Title");

    }

    Timer timer = new Timer(75/*change to vary frequency*/, new ActionListener() {
                public void actionPerformed(ActionEvent e) {//what the timer does every run through
                    if (inLevel) {
                        for (int i = bullets.size() - 1; i > -1; i--) {
                            Bullet b = bullets.get(i);
                            if (enemies.contains(b.getTarget())) {
                                b.move();
                                if (b.getRect().intersects(b.getTarget().getRect())) {
                                    b.getTarget().subtractHealth(b.getPower() * b.getMultiplier());
                                    if (b.getTarget().checkHealth()) {
                                        money += b.getTarget().getYield();
                                        enemies.remove(b.getTarget());
                                        numEnemiesKilled++;
                                        b.getTower().incrementNumKilled();
                                    }
                                    bullets.remove(b);
                                }
                            } else {
                                bullets.remove(b);
                            }
                        }
                        for (int i = enemies.size() - 1; i > -1; i--) {
                            enemies.get(i).move();
                            if (enemies.get(i).checkCoords()) {
                                health -= enemies.get(i).getHealth();
                                enemies.remove(i);
                                numEnemiesKilled++;
                                if (health <= 0) {
                                    inLevel = false;
                                    gameOver = true;
                                }
                            }
                        }
                        for (Tower t : towers) {
                            if (t.doesShoot()) {
                                if (count % (t.getSpeed()) == 0) {
                                    if (t.checkRadius() != null) {
                                        bullets.add(new Bullet(t.checkRadius(), t));
//                                    t.checkRadius().subtractHealth(t.getPower());
//                                    if (t.checkRadius().checkHealth()) {
//                                        money += t.checkRadius().getYield();
//                                        enemies.remove(t.checkRadius());
//                                        numEnemiesKilled++;
//                                    }
                                    }
                                }
                            }
                        }
                        if (numEnemiesSpawned < numEnemies && count % 25 == 0) {
                            enemies.add(new Enemy());
                            numEnemiesSpawned++;
                        }
                        count++;
                        if (numEnemiesKilled == numEnemies) {
                            inLevel = false;
                            int total = 0;
                            for (Tower t : towers) {
                                if (t instanceof Farm) {
                                    Farm f = (Farm) t;
                                    total += f.getReward();
                                    f.addTotal();
                                }
                            }
                            money += total * 25;
                            //nextLevel();
                        }
                    }
                }
            });

    public Game(String title) {
        frame = new JFrame(title);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(this);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        timer.start();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = 0;
            }
        }
        for (int i = 0; i < 3; i++) {
            board[0][i] = 1;
            board[10][2 + i] = 1;
        }
        for (int i = 0; i < 4; i++) {
            board[1][4 + i] = 1;
            board[10][7 + i] = 1;
            board[7 + i][10] = 1;
        }
        for (int i = 0; i < 11; i++) {
            board[i][2] = 1;
        }
        for (int i = 0; i < 10; i++) {
            board[1 + i][4] = 1;
            board[1 + i][7] = 1;
        }
        board[7][11] = 1;
        for (int[] i : board) {
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    public void paint(Graphics g2) {
        super.paint(g2);
        Graphics2D g = (Graphics2D) g2;
        g.setColor(Color.black);
        g.drawRect(0, 0, w, h);
        g.setColor(Color.yellow);
        for (int i = 0; i < 12; i++) {
            g.drawLine(0, 75 * i, 900, 75 * i);
            g.drawLine(75 * i, 0, 75 * i, 900);
        }
        g.setColor(Color.black);
        //DRAWING PATH
        g.drawLine(0, 75, 150, 75);
        g.drawLine(150, 75, 150, 825);
        g.drawLine(150, 825, 375, 825);
        g.drawLine(375, 825, 375, 150);
        g.drawLine(375, 150, 525, 150);
        g.drawLine(525, 150, 525, 825);
        g.drawLine(525, 825, 825, 825);
        g.drawLine(825, 825, 825, 600);
        g.drawLine(825, 600, 900, 600);

        g.drawLine(225, 0, 225, 750);
        g.drawLine(225, 750, 300, 750);
        g.drawLine(300, 750, 300, 75);
        g.drawLine(300, 75, 600, 75);
        g.drawLine(600, 75, 600, 750);
        g.drawLine(600, 750, 750, 750);
        g.drawLine(750, 750, 750, 525);
        g.drawLine(750, 525, 900, 525);
        //END OF DRAWING PATH
        g.drawString("Health: " + health, width - 200, 15);
        g.drawString("Money: " + money, width - 200, 30);
        g.drawString("Level: " + level, width - 200, 45);
        g.drawString("Enemies this level: " + numEnemies, width - 200, 60);
        g.drawString("Enemies Spawned: " + numEnemiesSpawned, width - 200, 75);
        g.drawString("Enemies killed: " + numEnemiesKilled, width - 200, 90);
        g.setColor(colorType('n'));
        g.drawRect(1000, 15, 75, 75);
        g.drawString("Neutral Tower Price: " + neutralTowers * 25, 975, 100);
        g.setColor(colorType('f'));
        g.drawRect(1150, 15, 75, 75);
        g.drawString("Fire Tower Price: " + fireTowers * 25, 1135, 100);
        g.setColor(colorType('w'));
        g.drawRect(1000, 115, 75, 75);
        g.drawString("Water Tower Price: " + waterTowers * 25, 975, 200);
        g.setColor(colorType('g'));
        g.drawRect(1150, 115, 75, 75);
        g.drawString("Grass Tower Price: " + grassTowers * 25, 1135, 200);
        g.setColor(colorType('b'));
        g.drawRect(1000, 215, 75, 75);
        g.drawString("Farm Price: " + farmTowers * 25, 975, 300);
        for (Tower t : towers) {
            g.setStroke(new BasicStroke(3));
            g.setColor(colorType(t.getType()));
            g.drawRect(t.getX(), t.getY(), 75, 75);
            g.drawString("Num: " + t.getNum(), t.getX() + 5, t.getY() + 15);
        }
        g.setStroke(new BasicStroke(1));
        if (!inLevel) {
            g.setColor(Color.black);
            g.drawString("Click here to start level", width - 200, height - 100);
            g.drawRect(width - 200, height - 115, 150, 30);
        }
//        g.setColor(Color.black);
//        g.drawString("Click here to speed up", width - 200, height - 70);
//        g.drawRect(width - 200, height - 85, 150, 30);
        if (inLevel) {
            for (Enemy e : enemies) {
                g.setColor(colorType(e.getType()));
                g.drawRect(e.getX(), e.getY(), 75, 75);
                g.drawString(e.getHealth() + "", e.getX() + 75 / 2, e.getY() + 75 / 2 - 5);
                g.drawString(e.getSpeed() + "", e.getX() + 75 / 2, e.getY() + 75 / 2 + 5);
                g.drawRect(e.getX(), e.getY() - 10, 75, 10);
                g.setColor(Color.red);
                g.fillRect(e.getX(), e.getY() - 10, (int) (e.getHealth() / (double) e.getOriginalHealth() * 75), 10);
            }
            for (Bullet b : bullets) {
                g.setColor(colorType(b.getType()));
                g.fillRect(b.getX(), b.getY(), 15, 15);
            }
        }
        if (buying) {
            g.setColor(colorType(typeBuying));
            g.setStroke(new BasicStroke(3));
            g.drawRect(mouseX, mouseY, 75, 75); //DRAW A RECTANGLE TO FOLLOW MOUSE'S MOVEMENT WHEN BUYING A TOWER
            g.setStroke(new BasicStroke(3));
        }
        if (gameOver) {
            g.drawString("GAME OVER", width / 2 - 100, height / 2 - 100);
        }
        g.setColor(Color.black);
        if (displayTower != null) {
            g.drawString("Tower: " + displayTower.getNum(), 1000, 900);
            String type = "Neutral";
            g.setColor(colorType(displayTower.getType()));
            g.setColor(Color.black);
            if (displayTower.getType() == 'w') {
                type = "Water";
            } else if (displayTower.getType() == 'f') {
                type = "Fire";
            } else if (displayTower.getType() == 'g') {
                type = "Grass";
            } else if (displayTower.getType() == 'b') {
                type = "Farm";
                canUpgradeRadius = false;
                canUpgradePower = false;
            }
            if (displayTower.doesShoot()) {
                g.drawString("Type: " + type, 1000, 930);
                g.drawOval(displayTower.getX() - 105 * (displayTower.getRadius() + 1) / 2, displayTower.getY() - 100 * (displayTower.getRadius() + 1) / 2, (displayTower.getRadius() + 2) * 100, (displayTower.getRadius() + 2) * 100);
                g.drawString("Num Enemies Killed: " + displayTower.getNumEnemiesKilled(), 1000, 915);
                g.drawString("Radius: " + displayTower.getRadius(), 1000, 945);
                g.drawString("Power: " + displayTower.getPower(), 1000, 990);
                if (displayTower.getRadius() < maxRadius) {
                    canUpgradeRadius = true;
                    g.drawString("Upgrade radius: " + displayTower.getRadius() * 100, 1100, 945);
                    g.drawRect(1100, 930, 120, 15);
                } else {
                    canUpgradeRadius = false;
                }
                if (displayTower.getPower() < maxPower) {
                    canUpgradePower = true;
                    g.drawString("Upgrade power: " + displayTower.getPower() * 150, 1100, 990);
                    g.drawRect(1100, 975, 120, 15);
                } else {
                    canUpgradePower = false;
                }
            }
            if (displayTower instanceof Farm) {
                Farm f = (Farm) displayTower;
                g.drawString("Reward: " + f.getReward() * 25, 1000, 930);
                if (f.getReward() < 4) {
                    g.drawString("Upgrade reward: " + f.getReward() * 200, 1100, 945);
                    g.drawRect(1100, 930, 120, 15);
                    g.drawString("Total reward: " + f.getTotal(), 1000, 945);
                }
            }
        }
        repaint();
    }

    public Color colorType(char c) {
        if (c == 'f') {
            return Color.RED;
        } else if (c == 'g') {
            return new Color(46, 139, 87);
        } else if (c == 'w') {
            return Color.BLUE;
        } else if (c == 'b') {
            return new Color(255, 215, 0);
        }
        return Color.GRAY;
    }

    public void nextLevel() {
        inLevel = true;
        level++;
        numEnemies = 2 * level + 1;
        numEnemiesSpawned = 0;
        numEnemiesKilled = 0;
        //mouseX = me.getX();
        //mouseY = me.getY();
        shoots = true;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(69);
        }

        if (ke.getKeyCode() == KeyEvent.VK_C) {
            //
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (!gameOver) {
            if (me.getX() >= width / 2 - 100 && me.getX() <= width / 2 + 100 && me.getY() >= height / 2 - 100 && me.getY() <= height / 2 + 100) {
                //when the mouse clicks the center, execute this code
            }
            if (!buying && !inLevel && me.getX() > width - 200 && me.getX() < width - 50 && me.getY() > height - 85 && me.getY() < height - 55) {
                //System.out.println("NEXT LEVEL");
                nextLevel();
                //(width - 200, height - 115, 150, 30)
            }
            if (!buying && me.getX() > 1000 && me.getX() < 1075 && me.getY() > 15 && me.getY() < 90 && money >= neutralTowers * 25) {
                money -= neutralTowers * 25;
                typeBuying = 'n';
                neutralTowers++;
                buying = true;
                mouseX = me.getX();
                mouseY = me.getY();
                shoots = true;
            }
            if (!buying && me.getX() > 1150 && me.getX() < 1225 && me.getY() > 15 && me.getY() < 90 && money >= fireTowers * 25) {
                money -= fireTowers * 25;
                typeBuying = 'f';
                fireTowers++;
                buying = true;
                mouseX = me.getX();
                mouseY = me.getY();
                shoots = true;
            }
            if (!buying && me.getX() > 1000 && me.getX() < 1075 && me.getY() > 115 && me.getY() < 200 && money >= waterTowers * 25) {
                money -= waterTowers * 25;
                typeBuying = 'w';
                waterTowers++;
                buying = true;
                mouseX = me.getX();
                mouseY = me.getY();
                shoots = true;
            }
            if (!buying && me.getX() > 1150 && me.getX() < 1225 && me.getY() > 115 && me.getY() < 200 && money >= grassTowers * 25) {
                money -= grassTowers * 25;
                typeBuying = 'g';
                grassTowers++;
                buying = true;
                mouseX = me.getX();
                mouseY = me.getY();
                shoots = true;
            }
            if (!buying && me.getX() > 1000 && me.getX() < 1075 && me.getY() > 215 && me.getY() < 300 && money >= farmTowers * 25) {
                money -= farmTowers * 25;
                typeBuying = 'b';
                farmTowers++;
                buying = true;
                mouseX = me.getX();
                mouseY = me.getY();
                shoots = false;
            }
            if (buying && me.getY() / 75 < 12 && me.getX() / 75 < 12) {
                if (board[me.getY() / 75][me.getX() / 75] == 0) {
                    board[me.getY() / 75][me.getX() / 75] = 2;
                    buying = false;
                    if (shoots) {
                        towers.add(new Tower(typeBuying, me.getX() / 75 * 75, me.getY() / 75 * 75, shoots));
                    } else {
                        towers.add(new Farm(typeBuying, me.getX() / 75 * 75, me.getY() / 75 * 75, false, 1));
                    }
                }
            }
            if (!buying && me.getY() <= 900 && me.getX() <= 900 && board[me.getY() / 75][me.getX() / 75] != 0 && board[me.getY() / 75][me.getX() / 75] != 1) {
                for (Tower t : towers) {
                    if (t.getX() == me.getX() / 75 * 75 && t.getY() == me.getY() / 75 * 75) {
                        displayTower = t;
                        break;
                    }
                }
            }
            if (!buying && canUpgradeRadius && me.getX() > 1100 && me.getX() < 1220 && me.getY() > 955 && me.getY() < 970 && money >= displayTower.getRadius() * 100) {
                money -= displayTower.getRadius() * 100;
                displayTower.upgradeRadius();
            }
            if (!buying && canUpgradePower && me.getX() > 1100 && me.getX() < 1220 && me.getY() > 1000 && me.getY() < 1015 && money >= displayTower.getPower() * 150) {
                money -= displayTower.getPower() * 150;
                displayTower.upgradePower();
            }
            if (displayTower instanceof Farm) {
                Farm f = (Farm) displayTower;
                if (f.getReward() < 4) {
                    if (me.getX() > 1100 && me.getX() < 1220 && me.getY() > 955 && me.getY() < 970 && money >= f.getReward() * 200) {
                        money -= f.getReward() * 200;
                        f.upgradeReward();
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (buying) {
            mouseX = me.getX();
            mouseY = me.getY();
        }
    }

}
