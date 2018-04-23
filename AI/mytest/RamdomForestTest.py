from sklearn.ensemble import RandomForestClassifier
import numpy as np

X = [
    [25,179,15,0],
    [33,190,19,0],
    [28,180,18,2],
    [25,178,18,2],
    [46,100,100,2],
    [40,170,170,1],
    [34,174,20,2],
    [36,181,55,1],
    [35,170,25,2],
    [30,180,35,1],
    [28,174,30,1],
    [29,176,36,1]
    ]

# not select the last two rows
subX = X[0:len(X)-2]
print(subX)
print(X)

X2 = [
    [125,179,15,0],
    [133,190,19,0],
    [128,180,18,2],
    [125,178,18,2],
    [146,100,100,2],
    [140,170,170,1],
    [134,174,20,2],
    [136,181,55,1],
    [135,170,25,2],
    [130,180,35,1],
    [128,174,30,1],
    [129,176,36,1]
    ]

X3 = np.hstack([X, X2])
print("X3:")
print(X3)

Y = [0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1]
# not select the last two elements
subY = Y[0:len(Y)-2]
print(subY)
print(Y)

clf = RandomForestClassifier().fit(X, Y)

p = [[28,180,18,2]]
print(clf.predict(p))