public class TrapRoom extends Room{

    public TrapRoom(int maxHealth, int health, int shieldValue){
        super(maxHealth, health, shieldValue);
        giveDamage(20);
    }

}
