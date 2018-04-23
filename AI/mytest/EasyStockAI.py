import os
import pandas as pd
import numpy as np

from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import SVC

from sklearn.neighbors import KNeighborsClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LogisticRegression


BASE_PATH = 'C:/Users/[path]/github/EasyStoGu/CommonLib/src/main/resources/AI/'
# Load data
szzsHigh = pd.read_csv(os.path.join(BASE_PATH, "999999_high.csv"), usecols=[0,1])
szzsLow = pd.read_csv(os.path.join(BASE_PATH, "999999_low.csv"), usecols=[0,1])

LuZaoCross = pd.read_csv(os.path.join(BASE_PATH, "LuZaoCross.csv"), usecols=[0,1,2,3,4,5])
LuZaoTrend = pd.read_csv(os.path.join(BASE_PATH, "LuZaoTrend.csv"), usecols=[0,1,2,3,4])
MACD = pd.read_csv(os.path.join(BASE_PATH, "MACD.csv"), usecols=[0,1,2])
QSDD = pd.read_csv(os.path.join(BASE_PATH, "QSDD.csv"), usecols=[0,1,2,3])
ShenXian = pd.read_csv(os.path.join(BASE_PATH, "ShenXian.csv"), usecols=[0,1,2])
WR = pd.read_csv(os.path.join(BASE_PATH, "WR.csv"), usecols=[0,1,2,3])

forecastDay = 0
# For szzs, only need second column, exclude first column (date)
szzsHighData=np.array(szzsHigh)[:,1][0:len(szzsHigh)-forecastDay]
szzsLowData=np.array(szzsLow)[:,1][0:len(szzsLow)-forecastDay]

# For qsdd, only need second column, exclude first column (date)
LuZaoCrossData=np.array(LuZaoCross)[0:len(LuZaoCross)-forecastDay:,1:6] # 0:len(qsddFilter)-2:,1:4
LuZaoTrendData=np.array(LuZaoTrend)[0:len(LuZaoTrend)-forecastDay:,1:6] # 0:len(qsddFilter)-2:,1:4
MACDData=np.array(MACD)[0:len(MACD)-forecastDay:,1:6] # 0:len(qsddFilter)-2:,1:4
QSDDData=np.array(QSDD)[0:len(QSDD)-forecastDay:,1:6] # 0:len(qsddFilter)-2:,1:4
ShenXianData=np.array(ShenXian)[0:len(ShenXian)-forecastDay:,1:6] # 0:len(qsddFilter)-2:,1:4
WRData=np.array(WR)[0:len(WR)-forecastDay:,1:6] # 0:len(qsddFilter)-2:,1:4

# using Classifier to fit the data
clf = RandomForestClassifier().fit(QSDDData.astype('int'), szzsHighData.astype('int'))
qsddP = [[46, 0, 8]] # 2018-01-29
print("QSDD Predict: %d" % clf.predict(qsddP))# 1

clf = RandomForestClassifier().fit(WRData.astype('int'), szzsHighData.astype('int'))
wrP = [[5,13,171]] # 2018-01-29
print("WR Predict: %d" % clf.predict(wrP))# 1