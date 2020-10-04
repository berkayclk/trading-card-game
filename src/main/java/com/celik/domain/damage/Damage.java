package com.celik.domain.damage;

public abstract class Damage {

    private int damageAmount;

    public Damage(int damageAmount){
        this.damageAmount = damageAmount;
    }

    public int getDamageAmount() {
        return this.damageAmount;
    }

    public void setDamageAmount(int damageAmount) {
        this.damageAmount = damageAmount;
    }
}
