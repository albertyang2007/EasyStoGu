import os
import pandas as pd
import numpy as np

from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import SVC

from sklearn.neighbors import KNeighborsClassifier
from sklearn.naive_bayes import GaussianNB #Yes!!!
from sklearn.linear_model import LogisticRegression


BASE_PATH = 'C:/Users/eyaweiw/github/EasyStoGu/CommonLib/src/main/resources/AI/'
# Load data
szzsHigh = pd.read_csv(os.path.join(BASE_PATH, "999999_high.csv"), usecols=[0,1])

LuZaoCross = pd.read_csv(os.path.join(BASE_PATH, "LuZaoCross.csv"), usecols=[0,1,2,3,4,5])
LuZaoTrend = pd.read_csv(os.path.join(BASE_PATH, "LuZaoTrend.csv"), usecols=[0,1,2,3,4])
MACD = pd.read_csv(os.path.join(BASE_PATH, "MACD.csv"), usecols=[0,1,2])
QSDD = pd.read_csv(os.path.join(BASE_PATH, "QSDD.csv"), usecols=[0,1,2,3])
ShenXian = pd.read_csv(os.path.join(BASE_PATH, "ShenXian.csv"), usecols=[0,1,2])
WR = pd.read_csv(os.path.join(BASE_PATH, "WR.csv"), usecols=[0,1,2,3])


forecastDay = 1
skipDay = 60
#till 2018-04-23, totally ~5150 records in szzs; 3000: start from 2009-06-04;  4000 start from 2013-07-22
startRowIndex = 3000
endRowIndex = len(szzsHigh) - forecastDay - skipDay

# For szzs, only need second column, exclude first column (date)
szzsHighData=np.array(szzsHigh)[:,1][startRowIndex+forecastDay : len(szzsHigh)-skipDay]

# For qsdd, only need second column, exclude first column (date)
LuZaoCrossData=np.array(LuZaoCross)[startRowIndex:endRowIndex:,1:6] # 0:len(qsddFilter)-2:,1:4
LuZaoTrendData=np.array(LuZaoTrend)[startRowIndex:endRowIndex:,1:6] # 0:len(qsddFilter)-2:,1:4
MACDData=np.array(MACD)[startRowIndex:endRowIndex:,1:6] # 0:len(qsddFilter)-2:,1:4
QSDDData=np.array(QSDD)[startRowIndex:endRowIndex:,1:6] # 0:len(qsddFilter)-2:,1:4
ShenXianData=np.array(ShenXian)[startRowIndex:endRowIndex:,1:6] # 0:len(qsddFilter)-2:,1:4
WRData=np.array(WR)[startRowIndex:endRowIndex:,1:6] # 0:len(qsddFilter)-2:,1:4


# using Classifier to fit the data
clf = GaussianNB().fit(QSDDData.astype('int'), szzsHighData.astype('int'))
qsddP = [[22,0,3]] # 2018-01-26
print("QSDD Predict: %d" % clf.predict(qsddP))# 1

clf = GaussianNB().fit(WRData.astype('int'), szzsHighData.astype('int'))
wrP = [[12,9,34]] # 2018-01-26
print("WR Predict: %d" % clf.predict(wrP))# 1

#merge the checkPoing statistics
QsddWR = np.hstack([QSDDData,WRData])
clf = GaussianNB().fit(QsddWR.astype('int'), szzsHighData.astype('int'))
qsddWrP = [[22,0,3,12,9,34]] # 2018-01-26
print("QSDD & WR Predict should return 1: %d" % clf.predict(qsddWrP))# 1
