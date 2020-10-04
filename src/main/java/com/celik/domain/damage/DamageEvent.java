package com.celik.domain.damage;

public class DamageEvent {
    Damager damager;
    Damage damage;

    DamageEvent(Damager damager, Damage damage) {
        this.damager = damager;
        this.damage = damage;
    }

    public Damage getDamage() {
        return damage;
    }

    public boolean isDamageSource( Damager damager ) {
        return this.damager == damager;
    }
}
