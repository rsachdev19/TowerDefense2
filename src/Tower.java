
public class Tower {

    char type;
    int speed;
    int radius;
    int power;
    int x;
    int y;
    int numEnemiesKilled;
    int num;
    boolean shoots;

    public Tower() {
    }

    public Tower(char c, int x, int y, boolean b) {
        this.x = x;
        this.y = y;
        power = 1;
        radius = 1;
        speed = 20;
        numEnemiesKilled = 0;
        num = Game.towers.size() + 1;
        type = c;
        shoots = b;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    public void upgradeRadius() {
        radius++;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
    
    public void upgradePower() {
        power++;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Enemy checkRadius() {
        for (Enemy e : Game.enemies) {
            if (Math.sqrt(Math.pow(e.getX() - this.x, 2) + Math.pow(e.getY() - this.y, 2)) <= 100 * radius) {
                return e;
            }
        }
        return null;
    }

    public int getNumEnemiesKilled() {
        return numEnemiesKilled;
    }

    public void setNumEnemiesKilled(int numEnemiesKilled) {
        this.numEnemiesKilled = numEnemiesKilled;
    }

    public void incrementNumKilled() {
        numEnemiesKilled++;
    }
    
    public boolean doesShoot() {
        return shoots;
    }
}
