
import java.awt.Rectangle;

public class Bullet {

    Enemy target;
    int x;
    int y;
    int speed;
    Rectangle rect;
    int power;
    char type;
    Tower tower;

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public Bullet(Enemy e, Tower t) {
        target = e;
        x = t.getX();
        y = t.getY();
        speed = e.getSpeed() + 3;
        rect = new Rectangle(this.x, this.y, 15, 15);
        power = t.getPower();
        type = t.type;
        tower = t;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect() {
        rect = new Rectangle(this.x, this.y, 15, 15);
    }

    public double getMultiplier() {
        if (this.type == 'w' && target.getType() == 'f' || this.type == 'f' && target.getType() == 'g' || this.type == 'g' && target.getType() == 'w') {
            return 2;
        }
        if (this.type == 'f' && target.getType() == 'w' || this.type == 'g' && target.getType() == 'f' || this.type == 'w' && target.getType() == 'g') {
            return 0.5;
        }
        return 1;
    }

    public void move() {
        int deltaY = target.getY() - this.y;
        int deltaX = target.getX() - this.x;
        if (deltaX == 0) {
            if (deltaY < 0 ^ deltaX < 0) {
                this.y = this.y - speed;
            } else {
                this.y = this.y + speed;
            }
        } else if (deltaY == 0) {
            this.x = this.x + this.speed * Integer.signum(deltaX);
        } else {
            double slope = Math.abs(deltaY / deltaX);
            this.x = this.x + (int) (this.speed / slope * Integer.signum(deltaX));
            this.y = this.y + (int) (this.speed * slope * Integer.signum(deltaY));
        }
        setRect();
    }
}
