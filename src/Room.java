public class Room {

    private final int maxHealth;
    private int health;
    private int shieldValue;

    public Room(int maxHealth, int health, int shieldValue){
        this.maxHealth=maxHealth;
        this.health=health;
        this.shieldValue=shieldValue;
    }

    public int getHealth() {
        return this.health;
    }

    public int getShieldValue() {
        return this.shieldValue;
    }

    public void heal(int amount) {
        // Increases the health by given amount
        this.health += amount;

        // Health cannot exceed the maximum health limit
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void setShieldValue(int amount){
        // Updates the current shield value of the character
        this.shieldValue = amount;
    }

    public void giveDamage(int amount){
        // Calculates the remaining damage after the shield absorbs it
        int effectiveDamage = amount - this.shieldValue;

        // Shield breaks after taking a hit
        this.shieldValue = 0;

        // Applies the remaining damage to the current health
        this.health -= effectiveDamage;

        // Health cannot drop below zero
        if (this.health<0){
            this.health = 0;
        }
    }

}
