public class ArtifactRoom extends Room {

    public ArtifactRoom(int maxHealth, int health, int shieldValue){
        super(maxHealth, health, shieldValue);
    }

    public void relicHeal(){
        heal(10);
    }
    public void armorRelic(){
        setShieldValue(6);
    }

    public void boostRelic(){
        setShieldValue(4);
    }
}
