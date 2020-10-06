package org.easystogu.indicator;

/*买1买2 指标
 * Var1:=(2*CLOSE+HIGH+LOW+OPEN)/5;
 Var2:= EMA(EMA(EMA(Var1,4),4),4);
 SJ:= (Var2-REF(Var2,1))/REF(Var2,1)*100;
 SD:= MA(SJ,2);
 SK:= MA(SJ,1);
 Var3:=CROSS(SK,SD) AND SK<0;
 Var4:=CROSS(SK,0);
 Var5:=CROSS(SK,SD) AND SK>0;
 买1 : Var3;
 买2 : Var4 OR Var5;
 Var6:=CROSS(SD,SK);
 卖 : Var6;
 */
public class Mai1Mai2Helper {
	TALIBWraper talib = new TALIBWraper();

	public double[][] getMai1Mai2List(double[] var1) {
		int length = var1.length;
		double[][] sdsk = new double[2][length];

		double[] var11 = talib.getEma(var1, 4);
		double[] var12 = talib.getEma(var11, 4);
		double[] var2 = talib.getEma(var12, 4);
		//System.out.println("var2=" + var2[length - 1]);

		double[] sj = new double[length];
		for (int i = 1; i < length; i++) {
			sj[i] = (var2[i] - var2[i - 1]) / var2[i - 1] * 100;
		}
		//System.out.println("sj=" + sj[length - 1]);

		for (int i = 1; i < length; i++) {
			sdsk[0][i] = (sj[i] + sj[i - 1]) / 2;
		}
		for (int i = 1; i < length; i++) {
			sdsk[1][i] = sj[i];
		}

		//System.out.println("sd=" + sdsk[0][length - 1]);
		//System.out.println("sk=" + sdsk[1][length - 1]);

		return sdsk;
	}
}
