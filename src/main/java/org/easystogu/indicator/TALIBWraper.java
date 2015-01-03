package org.easystogu.indicator;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

//refer to:
//https://code.google.com/p/quantitativeinvestment/source/browse/trunk/+quantitativeinvestment+--username+Huafeng.LOU@gmail.com/QuantitativeInvestment/Tools/TaLib.cs?spec=svn60&r=48
//https://github.com/chartsy/chartsy/blob/624d54224615bda9ec55bbaca6e62653550e4be5/Chartsy/Stochastic%20Fast/src/org/chartsy/stochf/StochF.java
public class TALIBWraper {
    private Core core = new Core();

    //简单平均的参数
    public double[] getSma(double[] prices, int ma) {

        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.sma(0, prices.length - 1, prices, ma, begin, length, tempOutPut);

        for (int i = 0; i < ma - 1; i++) {
            output[i] = 0;
        }
        for (int i = ma - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - ma + 1];
        }
        return output;
    }

    //加权移动平均指标
    public double[] getWma(double[] prices, int ma) {
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.wma(0, prices.length - 1, prices, ma, begin, length, tempOutPut);

        for (int i = 0; i < ma - 1; i++) {
            output[i] = 0;
        }
        for (int i = ma - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - ma + 1];
        }
        return output;
    }

    //抛物线指标
    public double[] getSar(double[] highPrices, double[] lowPrices, double optInAcceleration/* 加速度 */,
            double optInMaximum/* 最大值 */) {
        /*
         * SAR（n）=SAR（n－1）+AF[EP（N-1）－SAR（N-1）]；
         * 其中，SAR（n）为第n日的SAR值，SAR（n－1）为第（n－1）日的值；
         * AF为加速因子（或叫加速系数），EP为极点价（最高价或最低价）
         */
        double[] output = new double[lowPrices.length];
        double[] tempoutput = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.sar(0, lowPrices.length - 1, highPrices, lowPrices, optInAcceleration, optInMaximum, begin,
                length, tempoutput);

        for (int i = 1; i < lowPrices.length; i++) {
            output[i] = tempoutput[i - 1];
        }
        return output;

    }

    //相对强弱指标
    public double[] getRsi(double[] prices, int period) {
        /*
         * 假设A为N日内收盘价的正数之和，B为N日内收盘价的负数之和乘以（—1）
         * 这样，A和B均为正，将A、B代入RSI计算公式，则RSI（N）=A÷（A＋B）×100
         */
        double[] output = new double[prices.length];
        double[] tempOutPut = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.rsi(0, prices.length - 1, prices, period, begin, length, tempOutPut);

        for (int i = 0; i < period; i++) {
            output[i] = 0;
        }
        for (int i = period; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - period];
        }
        return output;
    }

    //平滑异同平均指标
    public double[][] getMACD(double[] prices, int optInFastPeriod, int optInSlowPeriod, int optInSignalPeriod) {
        /*
         * optInFastPeriod(短期)、optInSlowPeriod(长期)、optInSignalPeriod(信号期
         * ） ＥＭＡn=Ｐn*2/（n+1）+ＥＭＡn-1*（n-1）/（n+1） 式中
         * Ｐn:当日收盘价;移动平均线周期ＥＭＡn:第n日ＥＭＡ值
         * 在一般情况下，快速ＥＭＡ一般选６日，慢速ＥＭＡ一般选12日，此时差离值（ＤＩＦ）的计算为：
         * ＤＩＦ＝ＥＭＡ6－ＥＭＡ12 “差离平均值”用DEA来表示，其计算公式为：
         * ＤＥＡn＝ＤＥＡn-1*8/10+ＤＩＦn*2/10
         * ＭＡＣＤn＝（ＤＩＦn－ＭＡＣＤn-1）＊２／１０＋ＭＡＣＤn-1
         */
        double[] tempoutput1 = new double[prices.length];
        double[] tempoutput2 = new double[prices.length];
        double[] tempoutput3 = new double[prices.length];
        double[][] output = { new double[prices.length], new double[prices.length], new double[prices.length] };

        double[] result1 = new double[prices.length];
        double[] result2 = new double[prices.length];
        double[] result3 = new double[prices.length];

        double[] temp = new double[60];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.macd(0, prices.length - 1, prices, optInFastPeriod, optInSlowPeriod, optInSignalPeriod, begin,
                length, tempoutput1, tempoutput2, tempoutput3);

        for (int i = 0; i < prices.length - optInSlowPeriod; i++) {
            result1[i] = 0;
            result2[i] = 0;
            result3[i] = 0;
        }
        for (int i = prices.length - optInSlowPeriod; 0 < i && i < (prices.length); i++) {
            result1[i] = tempoutput1[i - (prices.length - optInSlowPeriod)];
            result2[i] = tempoutput2[i - (prices.length - optInSlowPeriod)];
            result3[i] = tempoutput3[i - (prices.length - optInSlowPeriod)];
        }

        for (int i = 0; i < prices.length; i++) {
            output[0][i] = result1[i];
            output[1][i] = result2[i];
            output[2][i] = result3[i];
        }
        return output;
    }

    //平均方向性运动指标
    public double[] getAdx(double[] lowPrices, double[] highPrices, double[] closePrices, int optInTimePeriod) {
        /*
         * ADX = SUM[(+DI-(-DI))/(+DI+(-DI)), N]/N N —
         * 是在计算中所使用的时间段的数值。
         */
        double[] output = new double[lowPrices.length];
        double[] tempOutPut = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.adx(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, optInTimePeriod, begin, length,
                tempOutPut);
        //Adx(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);

        for (int i = 0; i < lowPrices.length - length.value; i++) {
            output[i] = 0;
        }
        for (int i = lowPrices.length - length.value; 0 < i && i < (lowPrices.length); i++) {
            output[i] = tempOutPut[i - (lowPrices.length - length.value)];
        }

        return output;

    }

    //平均方向性运动指标
    public double[] getAdxr(double[] lowPrices, double[] highPrices, double[] closePrices, int optInTimePeriod) {
        /*
         * ADX = SUM[(+DI-(-DI))/(+DI+(-DI)), N]/N N —
         * 是在计算中所使用的时间段的数值。
         */
        double[] output = new double[lowPrices.length];
        double[] tempOutPut = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.adxr(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, optInTimePeriod, begin,
                length, tempOutPut);
        //Adx(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);

        for (int i = 0; i < lowPrices.length - length.value; i++) {
            output[i] = 0;
        }
        for (int i = lowPrices.length - length.value; 0 < i && i < (lowPrices.length); i++) {
            output[i] = tempOutPut[i - (lowPrices.length - length.value)];
        }

        return output;
    }

    //布林通道指标
    public double[][] getBbands(double[] prices, int optInTimePeriod, double optInNbDevUp, double optInNbDevDn) {
        /*
         * optInTimePeriod:时间，optInNbDevUp：上轨（UP线）,optInNbDevDn:下轨（Down线
         * ）
         * 中轨线=N个的移动平均线，上轨线=中轨线＋（D×标准差），下轨线=中轨线－（D×标准差），其中中轨线=SMA（close
         * ，N），D=标准差的参数，一般为默认值，如2，标准差=SUM[(Close-a)2，N]÷(N-1)的值的平方根.
         */
        double[] tempoutput1 = new double[prices.length];
        double[] tempoutput2 = new double[prices.length];
        double[] tempoutput3 = new double[prices.length];
        double[][] output = { new double[prices.length], new double[prices.length], new double[prices.length] };

        double[] result1 = new double[prices.length];
        double[] result2 = new double[prices.length];
        double[] result3 = new double[prices.length];

        double[] temp = new double[60];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;
        MAType optInMAType = MAType.Ema;

        retCode = core.bbands(0, prices.length - 1, prices, optInTimePeriod, optInNbDevUp, optInNbDevDn, optInMAType,
                begin, length, tempoutput1, tempoutput2, tempoutput3);
        //public static RetCode Bbands(int startIdx, int endIdx, float[] inReal, int optInTimePeriod, double optInNbDevUp, double optInNbDevDn, MAType optInMAType, out int outBegIdx, out int outNBElement, double[] outRealUpperBand, double[] outRealMiddleBand, double[] outRealLowerBand);

        for (int i = 0; i < optInTimePeriod - 1; i++) {
            result1[i] = 0;
            result2[i] = 0;
            result3[i] = 0;
        }
        for (int i = optInTimePeriod - 1; 0 < i && i < (prices.length); i++) {
            result1[i] = tempoutput1[i - optInTimePeriod + 1];
            result2[i] = tempoutput2[i - optInTimePeriod + 1];
            result3[i] = tempoutput3[i - optInTimePeriod + 1];
        }

        for (int i = 0; i < prices.length; i++) {
            output[0][i] = result1[i];
            output[1][i] = result2[i];
            output[2][i] = result3[i];
        }
        return output;
    }

    //商品通道指标
    public double[] getCci(double[] highPrices, double[] lowPrices, double[] closePrices, int inTimePeriod) {
        /*
         * (1) TP=（最高价+最低价+收盘价）÷3 。 (2) SMA TP=SMA (TP, N)注解：N为计算周期。
         * （3）MD（Mean Deviation）也就是平均差的值 MD=∑︳TP-SMA TP︱÷N注解：∑代表为总和
         * （4）可算出CCI的值 ，CCI=（TP-SMA TP）÷(0.015×MD)
         */
        double[] output = new double[lowPrices.length];
        double[] tempOutPut = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.cci(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, inTimePeriod, begin, length,
                tempOutPut);
        // Cci(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);

        for (int i = 0; i < inTimePeriod - 1; i++) {
            output[i] = 0;
        }
        for (int i = inTimePeriod - 1; 0 < i && i < (lowPrices.length); i++) {
            output[i] = tempOutPut[i - inTimePeriod + 1];
        }

        return output;
    }

    //EMA=Exponential Moving Average：指数平均数指标
    public double[] getEma(double[] prices, int optInTimePeriod) {
        /*
         * EXPMA＝(当日或当期收盘价－上一日或上期EXPMA)/Ｎ+上一日或上期EXPMA
         */
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.ema(0, prices.length - 1, prices, optInTimePeriod, begin, length, tempOutPut);
        //Ema(int startIdx, int endIdx, double[] inReal, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);
        for (int i = 0; i < optInTimePeriod - 1; i++) {
            output[i] = 0;
        }
        for (int i = optInTimePeriod - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - optInTimePeriod + 1];
        }
        return output;
    }

    // MACDEXT=MACD with controllable MA type：可控均线平滑异同平均指标
    public double[][] getMacdExt(double[] prices, int optInFastPeriod, int optInSlowPeriod, int optInSignalPeriod) {
        /*
         * DIFF线　（Difference）收盘价短期、长期指数平滑移动平均线间的差 DEA线　（Difference
         * Exponential Average）DIFF线的M日指数平滑移动平均线
         * MACD线　DIFF线与DEA线的差，彩色柱状线
         * 参数：SHORT(短期)、LONG(长期)、M天数，一般为12、26、9公式如下所示：
         * 加权平均指数（ＤＩ）=（当日最高指数+当日收盘指数+2倍的当日最低指数）
         * 十二日平滑系数（Ｌ１２）=2/（12+1）=0.1538二十六日平滑系数（Ｌ２６）=2/（26+1）=0.0741
         * 十二日指数平均值（１２日ＥＭＡ）=L12×当日收盘指数 + 11/（12+1）×昨日的12日EMA
         * 二十六日指数平均值（２６日ＥＭＡ）=L26×当日收盘指数 + 25/（26+1）×昨日的26日EMA
         * EMA（Exponential Moving
         * Average），指数平均数指标。也叫EXPMA指标，它也是一种趋向类指标
         * ，指数平均数指标是以指数式递减加权的移动平均。
         * 各数值的加权是随时间而指数式递减，越近期的数据加权越重，但较旧的数据也给予一定的加权。
         * 差离率（ＤＩＦ）=12日EMA-26日EMA九日DIF平均值（DEA）=最近9日的DIF之和/9
         * ＭＡＣＤ=（当日的DIFF-当日的DEA）×2
         */
        double[] tempoutput1 = new double[prices.length];
        double[] tempoutput2 = new double[prices.length];
        double[] tempoutput3 = new double[prices.length];
        double[][] output = { new double[prices.length], new double[prices.length], new double[prices.length] };

        double[] result1 = new double[prices.length];
        double[] result2 = new double[prices.length];
        double[] result3 = new double[prices.length];

        double[] temp = new double[60];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;
        MAType optInFastMAType = MAType.Ema;
        MAType optInSlowMAType = MAType.Ema;
        MAType optInSignalMAType = MAType.Ema;

        retCode = core.macdExt(0, prices.length - 1, prices, optInFastPeriod, optInFastMAType, optInSlowPeriod,
                optInSlowMAType, optInSignalPeriod, optInSignalMAType, begin, length, tempoutput1, tempoutput2,
                tempoutput3);
        //MacdExt(int startIdx, int endIdx, double[] inReal, int optInFastPeriod, MAType optInFastMAType, int optInSlowPeriod, MAType optInSlowMAType, int optInSignalPeriod, MAType optInSignalMAType, out int outBegIdx, out int outNBElement, double[] outMACD, double[] outMACDSignal, double[] outMACDHist);

        for (int i = 0; i < begin.value; i++) {
            result1[i] = 0;
            result2[i] = 0;
            result3[i] = 0;
        }
        for (int i = begin.value; 0 < i && i < (prices.length); i++) {
            result1[i] = tempoutput1[i - begin.value];
            result2[i] = tempoutput2[i - begin.value];
            result3[i] = tempoutput3[i - begin.value];
        }

        for (int i = 0; i < prices.length; i++) {
            output[0][i] = result1[i];
            output[1][i] = result2[i];
            output[2][i] = result3[i];
        }
        return output;
    }

    //MFI=Money Flow Index：资金流量指标
    public double[] getMfi(double[] highPrices, double[] lowPrices, double[] closePrices, double[] inVolume,
            int optInTimePeriod) {
        /*
         * 1.典型价格（TP）=N日内收盘价中最高价、最低价与最后一天收盘价的算术平均值
         * 2.货币流量（MF）＝典型价格（TP）×N日内成交金额
         * 3.如果当日MF＞昨日MF，则将当日的MF值视为正货币流量（PMF）
         * 4.如果当日MF＜昨日MF，则将当日的MF值视为负货币流量（NMF）
         * 5.MFI＝100-［100/(1＋PMF/NMF)］
         */
        double[] output = new double[lowPrices.length];
        double[] tempOutPut = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.mfi(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, inVolume, optInTimePeriod,
                begin, length, tempOutPut);

        //Mfi(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, double[] inVolume, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);
        for (int i = 0; i < optInTimePeriod; i++) {
            output[i] = 0;
        }
        for (int i = optInTimePeriod; 0 < i && i < (lowPrices.length); i++) {
            output[i] = tempOutPut[i - optInTimePeriod];
        }

        return output;
    }

    //能量潮指标
    public double[] getObv(double[] prices, double[] volume) {
        /*
         * 今日OBV=昨天OBV+sgn×今天的成交量其中sgn是符号的意思，sgn可能是+1，也可能是-1，这由下式决定。
         * Sgn=+1 今收盘价≥昨收盘价Sgn=―1 今收盘价<昨收盘价成交量指的是成交股票的手数，不是成交金额。
         */
        double[] output = new double[prices.length];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.obv(0, prices.length - 1, prices, volume, begin, length, output);
        //public static RetCode Obv(int startIdx, int endIdx, double[] inReal, double[] inVolume, out int outBegIdx, out int outNBElement, double[] outReal);
        return output;

    }

    //变化率
    public double[] getRoc(double[] prices, int optInTimePeriod) {
        /*
         * ((price/prevPrice)-1)*100
         */
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.roc(0, prices.length - 1, prices, optInTimePeriod, begin, length, tempOutPut);
        //Roc(int startIdx, int endIdx, float[] inReal, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);
        for (int i = 0; i < optInTimePeriod - 1; i++) {
            output[i] = 0;
        }
        for (int i = optInTimePeriod - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - optInTimePeriod + 1];
        }
        return output;
    }

    //百分数改变比例
    public double[] getRocP(double[] prices, int optInTimePeriod) {
        /*
         * (price-prevPrice)/prevPrice
         */
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.rocP(0, prices.length - 1, prices, optInTimePeriod, begin, length, tempOutPut);
        //Roc(int startIdx, int endIdx, float[] inReal, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);
        for (int i = 0; i < optInTimePeriod - 1; i++) {
            output[i] = 0;
        }
        for (int i = optInTimePeriod - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - optInTimePeriod + 1];
        }
        return output;
    }

    //随机指标指标
    public double[][] getStochF(double[] highPrices, double[] lowPrices, double[] closePrices, int optInFastK_Period,
            int optInFastD_Period) {
        /*
         * optInFastK_Period:快K周期，optInSlowK_Period：慢K周期，optInSlowD_Period
         * ：慢D周期 STOCH=Stochastic：随机指标,KDJ中K %K = 100*
         * (LOSE-LOW(%K))/(HIGH(%K)-LOW(%K)) CLOSE — 当天的收盘价格；LOW(%K) —
         * %K的最低值；HIGH(%K) — %K的最高值 %D的移动平均线：%D = SMA(%K， N)
         */

        double[][] output = { new double[lowPrices.length], new double[lowPrices.length] };
        double[] tempOutPut1 = new double[lowPrices.length];
        double[] tempOutPut2 = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        MAType optInFastD_MAType = MAType.Sma;

        retCode = core.stochF(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, optInFastK_Period,
                optInFastD_Period, optInFastD_MAType, begin, length, tempOutPut1, tempOutPut2);
        //StochF(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, int optInFastK_Period, int optInFastD_Period, MAType optInFastD_MAType, out int outBegIdx, out int outNBElement, double[] outFastK, double[] outFastD);

        for (int i = 0; i < lowPrices.length - length.value; i++) {
            output[0][i] = 0;
            output[1][i] = 0;
        }
        for (int i = lowPrices.length - length.value; 0 < i && i < (lowPrices.length); i++) {
            output[0][i] = tempOutPut1[i - (lowPrices.length - length.value)];
            output[1][i] = tempOutPut2[i - (lowPrices.length - length.value)];
        }

        return output;

    }

    //快随机指标
    public double[][] getStoch(double[] highPrices, double[] lowPrices, double[] closePrices, int optInFastK_Period,
            int optInSlowK_Period, int optInSlowD_Period) {
        /*
         * optInFastK_Period:快K周期，optInSlowK_Period：慢K周期，optInSlowD_Period
         * ：慢D周期 STOCH=Stochastic：随机指标,KDJ中K %K = 100*
         * (LOSE-LOW(%K))/(HIGH(%K)-LOW(%K)) CLOSE — 当天的收盘价格；LOW(%K) —
         * %K的最低值；HIGH(%K) — %K的最高值 MaxHigh(N) - N 个周期前的最高点； MinLow(N)
         * - N 个周期前的最低点； MA - 移动平均线；N - 高/低范围的计算长度； P - %D(i)的滤波周期。
         */

        double[][] output = { new double[lowPrices.length], new double[lowPrices.length] };
        double[] tempOutPut1 = new double[lowPrices.length];
        double[] tempOutPut2 = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        MAType optInSlowK_MAType = MAType.Sma;
        MAType optInSlowD_MAType = MAType.Sma;

        retCode = core.stoch(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, optInFastK_Period,
                optInSlowK_Period, optInSlowK_MAType, optInSlowD_Period, optInSlowD_MAType, begin, length, tempOutPut1,
                tempOutPut2);
        //Stoch(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, int optInFastK_Period, int optInSlowK_Period, MAType optInSlowK_MAType, int optInSlowD_Period, MAType optInSlowD_MAType, out int outBegIdx, out int outNBElement, double[] outSlowK, double[] outSlowD);

        for (int i = 0; i < lowPrices.length - length.value; i++) {
            output[0][i] = 0;
            output[1][i] = 0;
        }
        for (int i = lowPrices.length - length.value; 0 < i && i < (lowPrices.length); i++) {
            output[0][i] = tempOutPut1[i - (lowPrices.length - length.value)];
            output[1][i] = tempOutPut2[i - (lowPrices.length - length.value)];
        }

        return output;

    }

    //三重指数平滑移动平均指标
    public double[] getTrix(double[] prices, int period) {
        /*
         * (1)计算N天的收盘价的指数平均AX AX = (I日) 收盘价 * 2 /(N+1) + (I-1)日 AX
         * (N-1) *(N+1) (2)计算N天的AX的指数平均BX BX = (I日) AX * 2 /(N+1) +
         * (I-1)日 BX (N-1) *(N+1) （3)计算N天的BX的指数平均TRIX TRIX = (I日) BX *
         * 2 /(N+1) + (I-1)日 TRIX (N-1) *(N+1) (4)计算TRIX的m日移动平均TMA TMA
         * = ((I-M)日TRIX累加) /M日
         */
        double[] output = new double[prices.length];
        double[] tempOutPut = new double[prices.length];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.trix(0, prices.length - 1, prices, period, begin, length, tempOutPut);

        for (int i = 0; i < begin.value; i++) {
            output[i] = 0;
        }
        for (int i = begin.value; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - begin.value];
        }
        return output;
    }

    //商品通道指标
    public double[] getWillR(double[] highPrices, double[] lowPrices, double[] closePrices, int inTimePeriod) {
        /*
         * n日WMS＝（Hn－Ct）/(Hn－Ln)×100。Ct为当天的收盘价；Hn和Ln是最近n日内（包括当天）出现的最高价和最低价
         */
        double[] output = new double[lowPrices.length];
        double[] tempOutPut = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.willR(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, inTimePeriod, begin, length,
                tempOutPut);
        //WillR(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);

        for (int i = 0; i < inTimePeriod - 1; i++) {
            output[i] = 0;
        }
        for (int i = inTimePeriod - 1; 0 < i && i < (lowPrices.length); i++) {
            output[i] = tempOutPut[i - inTimePeriod + 1];
        }

        return output;
    }

    //AD=Chaikin A/D Line：累积派发线指标
    public double[] getAd(double[] highPrices, double[] lowPrices, double[] closePrices, double[] inVolume,
            int optInTimePeriod) {
        /*
         * AD=Chaikin A/D Line：累积派发线指标 CLV计算公式： {[(C-L)-(H-C)]}/(H-L))
         * = CLV C：收盘价。L：当日低点H：。当日高点。
         * CLV的变动范围在-1到1之间，中心点为0。CLV再与相应的时期内的成交量相乘，累积的总和就组成了累积/派发线
         */
        double[] output = new double[lowPrices.length];
        //double[] tempOutPut = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.ad(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, inVolume, begin, length, output);

        //Ad(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, double[] inVolume, out int outBegIdx, out int outNBElement, double[] outReal);

        return output;
    }

    //佳庆指标
    public double[] getAdosc(double[] highPrices, double[] lowPrices, double[] closePrices, double[] inVolume,
            int optInFastPeriod, int optInSlowPeriod) {
        /*
         * (1)ADOSC=Chaikin A/D Oscillator：佳庆指标 (2)
         * CHAIKIN＝A/D的（n）expma - A/D的（m）expma。
         */
        double[] output = new double[lowPrices.length];
        //double[] tempOutPut = new double[lowPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.adOsc(0, lowPrices.length - 1, highPrices, lowPrices, closePrices, inVolume, optInFastPeriod,
                optInSlowPeriod, begin, length, output);
        //AdOsc(int startIdx, int endIdx, double[] inHigh, double[] inLow, double[] inClose, double[] inVolume, int optInFastPeriod, int optInSlowPeriod, out int outBegIdx, out int outNBElement, double[] outReal)

        return output;
    }

    //震荡绝对价
    public double[] getApo(double[] prices, int optInFastPeriod, int optInSlowPeriod) {
        /*
         * APO=Absolute Price Oscillator：震荡绝对价
         */

        double[] output = new double[prices.length];
        double[] tempOutPut = new double[prices.length];

        MAType optInMAType = null;
        //MAType optInSlowD_MAType = new MAType();
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.apo(0, prices.length - 1, prices, optInFastPeriod, optInSlowPeriod, optInMAType, begin, length,
                tempOutPut);
        //Apo(int startIdx, int endIdx, double[] inReal, int optInFastPeriod, int optInSlowPeriod, MAType optInMAType, out int outBegIdx, out int outNBElement, double[] outReal);

        for (int i = 0; i < prices.length - length.value; i++) {
            output[i] = 0;

        }
        for (int i = prices.length - length.value; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - (prices.length - length.value)];
        }

        return output;

    }

    //阿隆指标
    public double[][] getAroon(double[] inHigh, double[] inLow, int optInTimePeriod) {
        /*
         * Aroon：阿隆指标 Aroon(上升)=[(计算期天数-最高价后的天数)/计算期天数]*100
         * Aroon(下降)=[(计算期天数-最低价后的天数)/计算期天数]*100
         */

        double[][] output = { new double[inHigh.length], new double[inHigh.length] };
        double[] tempOutPut1 = new double[inHigh.length];
        double[] tempOutPut2 = new double[inHigh.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        //MAType optInMAType = new MAType();
        //MAType optInSlowD_MAType = new MAType();

        retCode = core.aroon(0, inHigh.length - 1, inHigh, inLow, optInTimePeriod, begin, length, tempOutPut1,
                tempOutPut2);
        //Aroon(int startIdx, int endIdx, double[] inHigh, double[] inLow, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outAroonDown, double[] outAroonUp);

        for (int i = 0; i < inHigh.length - length.value; i++) {
            output[0][i] = 0;
            output[1][i] = 0;
        }
        for (int i = inHigh.length - length.value; 0 < i && i < (inHigh.length); i++) {
            output[0][i] = tempOutPut1[i - (inHigh.length - length.value)];
            output[1][i] = tempOutPut1[i - (inHigh.length - length.value)];
        }

        return output;

    }

    //阿隆震荡
    public double[] getAroonOsc(double[] inHigh, double[] inLow, int optInTimePeriod) {
        /*
         * AROONOSC=Aroon Oscillator：阿隆震荡指标 通过计算阿隆多空线(Aroon up and
         * down)之差值而来
         */

        double[] output = new double[inHigh.length];
        double[] tempOutPut = new double[inHigh.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.aroonOsc(0, inHigh.length - 1, inHigh, inLow, optInTimePeriod, begin, length, tempOutPut);
        //AroonOsc(int startIdx, int endIdx, double[] inHigh, double[] inLow, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);

        for (int i = 0; i < inHigh.length - length.value; i++) {
            output[i] = 0;
        }
        for (int i = inHigh.length - length.value; 0 < i && i < (inHigh.length); i++) {
            output[i] = tempOutPut[i - (inHigh.length - length.value)];
        }

        return output;

    }

    //力量平衡度
    public double[] getBop(double[] openPrices, double[] highPrices, double[] lowPrices, double[] closePrices) {
        /*
         * 力量平衡度
         */
        double[] output = new double[highPrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.bop(0, lowPrices.length - 1, openPrices, highPrices, lowPrices, closePrices, begin, length,
                output);
        //Bop(int startIdx, int endIdx, double[] inOpen, double[] inHigh, double[] inLow, double[] inClose, out int outBegIdx, out int outNBElement, double[] outReal);

        return output;
    }

    //钱德动量摆动指标
    public double[] getCmo(double[] closePrices, int period) {
        /*
         * CMO=Chande Momentum Oscillator：钱德动量摆动指标。 CMO =（Su-Sd) * 100
         * / (Su +Sd)
         * u是今日收盘价与昨日收盘价（上涨日）差值加总。若当日下跌，则增加值为0；Sd是今日收盘价与做日收盘价
         * （下跌日）差值的绝对值加总。 若当日上涨，则增加值为0
         */
        double[] output = new double[closePrices.length];
        double[] tempOutPut = new double[closePrices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.cmo(0, closePrices.length - 1, closePrices, period, begin, length, tempOutPut);
        //Cmo(int startIdx, int endIdx, double[] inReal, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);

        for (int i = 0; i < closePrices.length - length.value; i++) {
            output[i] = 0;
        }
        for (int i = closePrices.length - length.value; 0 < i && i < (closePrices.length); i++) {
            output[i] = tempOutPut[i - (closePrices.length - length.value)];
        }
        return output;
    }

    //考夫曼自适应增均线
    public double[] getKama(double[] prices, int optInTimePeriod) {
        /*
         * KAMA=Kaufman Adaptive Moving Average：考夫曼自适应增均线
         */
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.kama(0, prices.length - 1, prices, optInTimePeriod, begin, length, tempOutPut);
        //Kama(int startIdx, int endIdx, double[] inReal, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);
        for (int i = 0; i < optInTimePeriod; i++) {
            output[i] = 0;
        }
        for (int i = optInTimePeriod; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - optInTimePeriod];
        }
        return output;
    }

    //递归移动平均指标
    public double[] getTrima(double[] prices, int optInTimePeriod) {
        /*
         * TRIMA=Triangular Moving Average：递归移动平均指标 SMA = (P1 + P2 +
         * P3 + P4 + ... + Pn) / n TMA = (SMA1 + SMA2 + SMA3 + SMA4 +
         * ... SMAn) / n Pn为第n天价格
         */
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        RetCode retCode = RetCode.InternalError;
        begin.value = -1;
        length.value = -1;

        retCode = core.trima(0, prices.length - 1, prices, optInTimePeriod, begin, length, tempOutPut);
        //Kama(int startIdx, int endIdx, double[] inReal, int optInTimePeriod, out int outBegIdx, out int outNBElement, double[] outReal);
        for (int i = 0; i < optInTimePeriod - 1; i++) {
            output[i] = 0;
        }
        for (int i = optInTimePeriod - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - optInTimePeriod + 1];
        }
        return output;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
