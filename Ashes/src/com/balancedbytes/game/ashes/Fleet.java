package com.balancedbytes.game.ashes;

/**
 *
 */
public class Fleet {

  private int tr = 0;
  private int fi = 0;
  private Player player = null;

  /**
   *
   */
  public Fleet(Player player) {
    this.player = player;
  }

  /**
   *
   */
  public Fleet(Player player, int fi, int tr) {
    this(player);
    this.fi = fi;
    this.tr = tr;
  }

  /**
   * Adds another fleet to this one without checking player number.
   */
  public void add(Fleet anotherFleet) {
    fi += anotherFleet.getFighter();
    tr += anotherFleet.getTransporter();
  }

  /**
   *
   */
  public int getFighter() {
    return fi;
  }

  /**
   *
   */
  public Player getPlayer() {
    return this.player;
  }

  /**
   *
   */
  public int getTransporter() {
    return tr;
  }

  /**
   *
   */
  public void setFighter(int fi) {
    if (fi > 0) {
      this.fi = fi;
    } else {
      this.fi = 0;
    }
  }

  /**
   *
   */
  public void setTransporter(int tr) {
    if (tr > 0) {
      this.tr = tr;
    } else {
      this.tr = 0;
    }
  }

  /**
   *
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("Fleet of Player " + player.getNumber() + ": ");
    if (fi > 0) {
      buffer.append(fi + " FI ");
    }
    if (tr > 0) {
      buffer.append(tr + " TR");
    }

    return buffer.toString();
  }
  
}