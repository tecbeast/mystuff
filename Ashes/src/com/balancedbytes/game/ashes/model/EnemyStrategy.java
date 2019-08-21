package com.balancedbytes.game.ashes.model;

public enum EnemyStrategy {
	
	AGRESSIVE(60,  8, 13, 18, 68,  93, 100),
	 MODERATE(30, 30, 45, 60, 90,  95, 100),
	  PASSIVE( 0, 25, 33, 40, 90, 100, 100);
	
	private int fSendProb;
	
	// pdu, fp, op, rp, fy, ty
	private int fBuildProbPdu;
	private int fBuildProbFp;
	private int fBuildProbOp;
	private int fBuildProbRp;
	private int fBuildProbFy;
	private int fBuildProbTy;
	
	private EnemyStrategy(
		int sendProb,
		int buildProbPdu,
		int buildProbFp,
		int buildProbOp,
		int buildProbRp,
		int buildProbFy,
		int buildProbTy
	) {
		fSendProb = sendProb;
		fBuildProbPdu = buildProbPdu;
		fBuildProbFp = buildProbFp;
		fBuildProbOp = buildProbOp;
		fBuildProbRp = buildProbRp;
		fBuildProbFy = buildProbFy;
		fBuildProbTy = buildProbTy;
	};
		
	public int getSendProb() {
		return fSendProb;
	}
	
	public int getBuildProb(Unit unit) {
		if (unit == null) {
			return 0;
		}
		switch (unit) {
			case PLANETARY_DEFENSE_UNIT:
				return fBuildProbPdu;
			case FUEL_PLANT:
				return fBuildProbFp;
			case ORE_PLANT:
				return fBuildProbOp;
			case RARE_PLANT:
				return fBuildProbRp;
			case FIGHTER_YARD:
				return fBuildProbFy;
			case TRANSPORTER_YARD:
				return fBuildProbTy;
			default:
				return 0;	
		}
	}

}
