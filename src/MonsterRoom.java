public class MonsterRoom extends Room{

    public MonsterRoom(int maxHealth,int health, int shieldValue){
        super(maxHealth, health, shieldValue);
        giveDamage(30);
    }

}
