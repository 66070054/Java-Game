
import java.awt.*;
import java.awt.geom.*;

public abstract class Object {

    private DataBase database = new DataBase();
    private double x;
    private double y;
    private int hp;
    private int max_hp;
    private int attack;
    private float speed;
    private Image image;
    private Image hp_image;
    private Image max_hp_image;
    private List_Bullet bullets = null;
    private Area area;
    private Path2D path2D = new Path2D.Double();
    private int count_dead;
    private int count_golden_time = 0;
    private int enemy_left;
    private int type_bullet;

    public void draw(Graphics2D g2D, Color color) {

        if (getGolden_Time() > 0 && this instanceof Player && get_HP() > 0) {
            setImage(database.getPlayer_Get_Damage_Image());
        }
        else if (this instanceof Player && getGolden_Time() == 0 && get_HP() > 0) {
            setImage(database.getPlayer_Image());
        }

        if (this instanceof Victory) {
            g2D.drawImage(getImage(), (int)database.getScreen().getWidth()/2 - getImage().getWidth(null)/2, (int)database.getScreen().getHeight()/2 - getImage().getHeight(null), null);
        }
        else {
            g2D.drawImage(getImage(), (int)getX(), (int)getY(), null);
        }

        if ((this instanceof Enemy_01 || this instanceof Enemy_02 || this instanceof Enemy_03 || this instanceof Enemy_04 || this instanceof Enemy_05) && get_HP() > 0) {
            g2D.drawImage(getMax_HP_Image(), (int)getX(), (int)getY() + (int)getHeight() + 10, null);
            g2D.drawImage(getHP_Image(), (int)getX(), (int)getY() + (int)getHeight() + 10, null);
        }
        else if (this instanceof Player) {
            int count = 5;
            int count_hp = get_HP()/20;
            Player player = (Player)this;
            for (int i = 0; i < count; i++) {
                if (count_hp > 0) {
                    g2D.drawImage(getHP_Image(), 5 + i*getHP_Image().getWidth(null), 5, null);
                }
                else {
                    g2D.drawImage(getMax_HP_Image(), 5 + i*getHP_Image().getWidth(null), 5, null);
                }
                count_hp --;
            }
            g2D.setFont(new Font("Times", Font.PLAIN, (int)getHP_Image().getHeight(null)/2));
            g2D.drawString("Enemy Left : " + getEnemy_Left(), (int)database.getScreen().getWidth()-350, 50);

            if (getType_Bullet() == 1 && player.getDelay_Change_Bullet() == 0) {
                g2D.drawImage(database.getIcon_Bullet_Type_01() , -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
            }
            else if (getType_Bullet() == 2 && player.getDelay_Change_Bullet() == 0) {
                g2D.drawImage(database.getIcon_Bullet_Type_02(), -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
            }
            else if (getType_Bullet() == 3 && player.getDelay_Change_Bullet() == 0) {
                g2D.drawImage(database.getIcon_Bullet_Type_03(), -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
            }
            else if (getType_Bullet() == 4 && player.getDelay_Change_Bullet() == 0) {
                g2D.drawImage(database.getIcon_Bullet_Type_04(), -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
            }
            else if (getType_Bullet() == 1 && player.getDelay_Change_Bullet() > 0) {
                g2D.drawImage(database.getIcon_Change_01_Image() , -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
                player.setDelay_Change_Bullet(player.getDelay_Change_Bullet() - 1);
            }
            else if (getType_Bullet() == 2 && player.getDelay_Change_Bullet() > 0) {
                g2D.drawImage(database.getIcon_Change_02_Image() , -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
                player.setDelay_Change_Bullet(player.getDelay_Change_Bullet() - 1);
            }
            else if (getType_Bullet() == 3 && player.getDelay_Change_Bullet() > 0) {
                g2D.drawImage(database.getIcon_Change_03_Image() , -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
                player.setDelay_Change_Bullet(player.getDelay_Change_Bullet() - 1);
            }
            else if (getType_Bullet() == 4 && player.getDelay_Change_Bullet() > 0) {
                g2D.drawImage(database.getIcon_Change_04_Image() , -3, (int)database.getScreen().getHeight()-95, 100, 100, null);
                player.setDelay_Change_Bullet(player.getDelay_Change_Bullet() - 1);
            }

            if (player.getUse_DM2() == true && player.getUse_FR2() == false) {
                g2D.drawImage(database.getIcon_DM2(), 100, (int)database.getScreen().getHeight()-95, null);
                player.setTime_Use_Icon_DM2(player.getTime_Use_DM2() - 1);
            }
            else if (player.getUse_DM2() == false && player.getUse_FR2() == true) {
                g2D.drawImage(database.getIcon_FR2(), 100, (int)database.getScreen().getHeight()-95, null);
                player.setTime_Use_Icon_FR2(player.getTime_Use_FR2() - 1);
            }
            else if (player.getUse_DM2() == true && player.getUse_FR2() == true) {
                g2D.drawImage(database.getIcon_DM2(), 100, (int)database.getScreen().getHeight()-95, null);
                g2D.drawImage(database.getIcon_FR2(), 200, (int)database.getScreen().getHeight()-95, null);
                player.setTime_Use_Icon_DM2(player.getTime_Use_DM2() - 1);
                player.setTime_Use_Icon_FR2(player.getTime_Use_FR2() - 1);
            }
         }

        if (bullets != null) {
            bullets.draw(g2D, color);
        }

        if (color != null) {
            Shape shape = getShape();
            g2D.setColor(color);
            g2D.draw(shape);
        }
    }

    public void setType_Bullet(int type_bullet) {
        this.type_bullet = type_bullet;
    }
    public void setEnemy_Left(int enemy_left) {
        if (enemy_left >= 0) {
            this.enemy_left = enemy_left;
        }
    }
    public void setCount_Dead(int count_dead) {
        this.count_dead = count_dead;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public void setHP(int hp) {
        this.hp = hp;
    }
    public void setListBullets(List_Bullet bullets) {
        this.bullets = bullets;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public void setHP_Image(Image hp_Image) {
        if (this instanceof Player) {
            hp_image = hp_Image.getScaledInstance(hp_Image.getWidth(null)/2, hp_Image.getHeight(null)/2, Image.SCALE_SMOOTH);;
        }
        else {
            hp_image = hp_Image.getScaledInstance((int)getWidth(), (int)hp_Image.getHeight(null), Image.SCALE_SMOOTH);
        }
    }
    public void setHP_Image() {
        int true_hp = (int)((double)get_HP()/(double)get_Max_HP()*(double)getWidth());
        if (this instanceof Player) {}
        else if (true_hp > 0) {
            hp_image = hp_image.getScaledInstance(true_hp, (int)hp_image.getHeight(null), Image.SCALE_SMOOTH);
        }
    }
    public void setMax_HP_Image(Image max_Image, int max_hp) {
        if (this instanceof Player) {
            max_hp_image = max_Image.getScaledInstance(max_Image.getWidth(null)/2, max_Image.getHeight(null)/2, Image.SCALE_SMOOTH);
            setHP(max_hp);
        }
        else {
            max_hp_image = max_Image.getScaledInstance((int)getWidth(), (int)max_Image.getHeight(null), Image.SCALE_SMOOTH); 
        }
        this.max_hp = max_hp;
    }
    public void setArea() {
        path2D.moveTo(0, 0);
        path2D.lineTo(0, getHeight());
        path2D.lineTo(getWidth(), getHeight());
        path2D.lineTo(getWidth(), 0);
        area =  new Area(path2D);
    }
    public void setGolden_Time(int count_golden_time) {
        this.count_golden_time = count_golden_time;
    }
    public void changeLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getType_Bullet() {
        return type_bullet;
    }
    public List_Bullet getList_Bullet() {
        return bullets;
    }
    public int getEnemy_Left() {
        return enemy_left;
    }
    public int getCount_Dead() {
        return count_dead;
    }
    public int get_HP() {
        return hp;
    }
    public int get_Max_HP() {
        return max_hp;
    }
    public int getAttack_to_HP() {
        return attack;
    }
    public int getGolden_Time() {
        return count_golden_time;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public float getSpeed() {
        return speed;
    }
    public Image getImage() {
        return image;
    }
    public Image getHP_Image() {
        return hp_image;
    }
    public Image getMax_HP_Image() {
        return max_hp_image;
    }
    public double getWidth() {
        return image.getWidth(null);
    }
    public double getHeight() {
        return image.getHeight(null);
    }
    public Area getArea() {
        return area;
    }
    public Area getShape() {
        AffineTransform afx = new AffineTransform();
        afx.translate(getX(), getY());
        return new Area(afx.createTransformedShape(getArea()));
    }

    public void getDamage(int damage) {
        if (getGolden_Time() == 0 && this instanceof Player) {
            setHP(get_HP() - damage);
            setGolden_Time(database.getCount_Dead());
        }

        if (this instanceof Player != true) {
            setHP(get_HP() - damage);
            setHP_Image();
        }
    }

    public abstract void update();
}
