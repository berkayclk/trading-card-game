package com.celik.domain.damage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Damager {

    protected final List<DamageEventListener> damageListenerList;

    public Damager() {
        this.damageListenerList = new ArrayList<>();
    }

    protected void inflictDamage(Damage damage) {
        DamageEvent damageEvent = new DamageEvent(this, damage);
        notifyDamageListeners(damageEvent);
    }

    protected void addDamageListener(DamageEventListener damageListener) {
        if (!damageListenerList.contains(damageListener)) {
            damageListenerList.add(damageListener);
        }
    }

    protected void removeDamageListener(DamageEventListener listener) {
        damageListenerList.remove(listener);
    }

    protected void clearDamageListener() {
        damageListenerList.clear();
    }

    protected void notifyDamageListeners(DamageEvent damageEvent) {
        damageListenerList.forEach(listener -> listener.takeDamage(damageEvent));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Damager damager = (Damager) o;
        return damageListenerList.equals(damager.damageListenerList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(damageListenerList);
    }
}
