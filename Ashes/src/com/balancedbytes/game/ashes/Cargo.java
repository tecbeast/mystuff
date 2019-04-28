package com.balancedbytes.game.ashes;

import java.io.Serializable;

/**
 *
 */
public class Cargo implements Serializable {
  
  public final static int CARGOTYPES = 10;
  private int[] cargo = null;
  
  /**
   *
   */
  public Cargo(int type, int quantity) {
    cargo = new int[CARGOTYPES];
    setCargo(type, quantity);
  }
  
  /**
   * Adds another fleet to this one.
   */
  public void add(Cargo anotherCargo) {
    for (int i = 0; i < cargo.length; i++) {
      cargo[i] = anotherCargo.getCargo(i);
    }
  }
  
  /**
   *
   */
  public void clearCargo() {
    for (int i = 0; i < cargo.length; i++) {
      cargo[i] = 0;
    }
  }

  /**
   *
   */
  public int[] getCargo() {
    return cargo;
  }

  /**
   *
   */
  public int getCargo(int type) {
    if ((type >= 0) && (type < cargo.length)) {
      return cargo[type];
    } else {
      return -1;
    }
  }

  /**
   *
   */
  public boolean hasCargo() {
    for (int i = 0; i < cargo.length; i++) {
      if (cargo[i] > 0) {
        return true;
      }
    }
    return false;
  }

  /**
   *
   */
  public void setCargo(int type, int quantity) {
    if ((type >= 0) && (type < cargo.length)) {
      if (quantity > 0) {
        cargo[type] = quantity;
      } else {
        cargo[type] = 0;
      }
    }
  }

  /**
   *
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("CargoFleet: ");
    for (int i = 0; i < cargo.length; i++) {
      if (cargo[i] > 0) {
        buffer.append(" " + cargo[i] + " C" + i);
      }
    }

    return buffer.toString();
  }
  
}