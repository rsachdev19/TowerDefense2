
import java.awt.Rectangle;

public class Enemy {

    int x;
    int y;
    int yield;
    char type;
    int speed;
    double health;
    int originalHealth;
    Rectangle rect;

    public Enemy() {
        x = 0;
        y = 0;
        speed = (int) (Math.random() * (Game.level + 1)) + 10;
        type = 'n';
        originalHealth = (int) (Math.random() * (Game.level / 2 + 1)) + 1;
        health = originalHealth;
        yield = 5 * ((int) (health + 1) / 2) * ((speed - 5) / 5);
        int temp = (int) (Math.random() * 4);
        if (temp == 0) {
            type = 'n';
        } else if (temp == 1) {
            type = 'f';
        } else if (temp == 2) {
            type = 'w';
        } else if (temp == 3) {
            type = 'g';
        }
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

    public int getYield() {
        return yield;
    }

    public void setYield(int yield) {
        this.yield = yield;
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

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void subtractHealth(double h) {
        health -= h;
    }

    public void move() {
        if (x < 150) {
            if (x + speed > 150) {
                y += x + speed - 150;
                x = 150;
            } else {
                x += speed;
            }
        } else if (x == 150 && y < 750) {
            if (y + speed > 750) {
                x += y + speed - 750;
                y = 750;
            } else {
                y += speed;
            }
        } else if (y == 750 && x < 300) {
            if (x + speed > 300) {
                y -= x + speed - 300;
                x = 300;
            } else {
                x += speed;
            }
        } else if (x == 300 && y > 75) {
            if (y - speed < 75) {
                x += 75 + speed - y;
                y = 75;
            } else {
                y -= speed;
            }
        } else if (y == 75 && x < 525) {
            if (x + speed > 525) {
                y += x + speed - 525;
                x = 525;
            } else {
                x += speed;
            }
        } else if (x == 525 && y < 750) {
            if (y + speed > 750) {
                x += y + speed - 750;
                y = 750;
            } else {
                y += speed;
            }
        } else if (y == 750 && x < 750) {
            if (x + speed > 750) {
                y -= x + speed - 750;
                x = 750;
            } else {
                x += speed;
            }
        } else if (x == 750 && y > 525) {
            if (y - speed < 525) {
                x += 525 - y + speed;
                y = 525;
            } else {
                y -= speed;
            }
        } else if (y == 525 && x < 900) {
            x += speed;
        }
        rect = new Rectangle(this.x, this.y, 75, 75);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public boolean checkHealth() {
        if (health <= 0) {
            return true;
        }
        return false;
    }

    public int getOriginalHealth() {
        return originalHealth;
    }

    public void setOriginalHealth(int originalHealth) {
        this.originalHealth = originalHealth;
    }

    public boolean checkCoords() {
        if (x >= 900) {
            return true;
        }
        return false;
    }

}
