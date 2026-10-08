public class HealingRoom extends Room{

    public HealingRoom(int maxHealth, int health, int shieldValue){
        super(maxHealth, health, shieldValue);
        heal(15);
    }

}
