import os
import pandas as pd
import numpy as np

from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import SVC

from sklearn.neighbors import KNeighborsClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LogisticRegression


BASE_PATH = 'C:/Users/eyaweiw/github/EasyStoGu/CommonLib/src/main/resources/AI/'
# Load data
szzs = pd.read_csv(os.path.join(BASE_PATH, "999999_high.csv"), usecols=[0,1])
qsdd = pd.read_csv(os.path.join(BASE_PATH, "WR.csv"), usecols=[0,1,2,3])

# Make sure the data has same index (same date)
idx_for_qsdd = qsdd['date'].isin(szzs['date']) 
qsddFilter = qsdd[idx_for_qsdd]

idx_for_szzs = szzs['date'].isin(qsddFilter['date'])
szzsFilter = szzs[idx_for_szzs]

forecastDay = 2
# For szzs, only need second column, exclude first column (date)
szzsData=np.array(szzsFilter)[:,1][0:len(szzsFilter)-forecastDay]
# For qsdd, only need second column, exclude first column (date)
qsddData=np.array(qsddFilter)[0:len(qsddFilter)-forecastDay:,1:6] # 0:len(qsddFilter)-2:,1:6

print("qsdd:")
print(qsddData)
print("szzs:")
print(szzsData)

clf = RandomForestClassifier().fit(qsddData.astype('int'), szzsData.astype('int'))

#qsdd = [[46, 0, 8]] # 2018-01-29
#print(clf.predict(qsdd))# 1

#luzao = [[36,160,704,310]] # 2018-01-29
#print(clf.predict(luzao))# 1

wr = [[5,13,171]] # 2018-01-29
print(clf.predict(wr))# 1