import os
import pandas as pd
import numpy as np

from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import SVC

from sklearn.neighbors import KNeighborsClassifier # Yes!!
from sklearn.naive_bayes import GaussianNB #Yes!!!
from sklearn.naive_bayes import MultinomialNB  
from sklearn.naive_bayes import BernoulliNB #Yes!!!
from sklearn.linear_model import LogisticRegression

from sklearn.model_selection import train_test_split  
from sklearn.metrics import precision_recall_curve  
from sklearn.metrics import classification_report  


BASE_PATH = 'f:/github/EasyStoGu/CommonLib/src/main/resources/AI/'
# Load data
szzsHigh = pd.read_csv(os.path.join(BASE_PATH, "999999_high.csv"), usecols=[0,1])

LuZaoCross = pd.read_csv(os.path.join(BASE_PATH, "LuZaoCross.csv"), usecols=[0,1,2,3,4,5])
LuZaoTrend = pd.read_csv(os.path.join(BASE_PATH, "LuZaoTrend.csv"), usecols=[0,1,2,3,4])
MACD = pd.read_csv(os.path.join(BASE_PATH, "MACD.csv"), usecols=[0,1,2])
QSDD = pd.read_csv(os.path.join(BASE_PATH, "QSDD.csv"), usecols=[0,1,2,3])
ShenXian = pd.read_csv(os.path.join(BASE_PATH, "ShenXian.csv"), usecols=[0,1,2])
WR = pd.read_csv(os.path.join(BASE_PATH, "WR.csv"), usecols=[0,1,2,3])


forecastDay = 1
skipDay = 0
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
#clf = BernoulliNB().fit(QSDDData.astype('int'), szzsHighData.astype('int'))
#qsddP = [[22,0,3]] # 2018-01-26
#print("QSDD Predict: %d" % clf.predict(qsddP))# 1


#clf = BernoulliNB().fit(WRData.astype('int'), szzsHighData.astype('int'))
#wrP = [[12,9,34]] # 2018-01-26
#print("WR Predict: %d" % clf.predict(wrP))# 1

#merge the checkPoing statistics
#QsddWR = np.hstack([QSDDData,WRData])
#clf = BernoulliNB().fit(QsddWR.astype('int'), szzsHighData.astype('int'))
#qsddWrP = [[22,0,3,12,9,34]] # 2018-01-26
#print("QSDD & WR Predict should return 1: %d" % clf.predict(qsddWrP))# 1

# Split Train and test
qsdd_train, qsdd_test, szzs_train, szzs_test = train_test_split(QSDDData, szzsHighData, test_size = 0.2)
clf = BernoulliNB().fit(qsdd_train.astype('int'), szzs_train.astype('int'))
doc_class_predicted = clf.predict(qsdd_test)
print("QSDD Predict Precision: %.2f" % np.mean(doc_class_predicted == szzs_test))
qsddP = [[22,0,3]] # 2018-01-26
print("QSDD Predict should return 1: %d" % clf.predict(qsddP))# 1

# Split Train and test
wr_train, wr_test, szzs_train, szzs_test = train_test_split(WRData, szzsHighData, test_size = 0.2)
clf = BernoulliNB().fit(wr_train.astype('int'), szzs_train.astype('int'))
doc_class_predicted = clf.predict(wr_test)
print("WR Predict Precision: %.2f" % np.mean(doc_class_predicted == szzs_test))  

wrP = [[12,9,34]] # 2018-01-26
print("WR Predict should return 1: %d" % clf.predict(wrP))# 1

#merge the checkPoing statistics
QsddWR = np.hstack([QSDDData,WRData])
qsddwr_train, qsddwr_test, szzs_train, szzs_test = train_test_split(QsddWR, szzsHighData, test_size = 0.2)
clf = BernoulliNB().fit(qsddwr_train.astype('int'), szzs_train.astype('int'))
doc_class_predicted = clf.predict(qsddwr_test)
print("QSDD & WR Predict Precision: %.2f" % np.mean(doc_class_predicted == szzs_test))

qsddWrP = [[22,0,3,12,9,34]] # 2018-01-26
print("QSDD & WR Predict should return 1: %d" % clf.predict(qsddWrP))# 1

