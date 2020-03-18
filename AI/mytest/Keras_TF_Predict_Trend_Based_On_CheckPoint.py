from keras.models import Sequential
from keras.layers import Dense
from numpy import genfromtxt

import numpy as np

# 设定随机数种子
np.random.seed(7)

train_start_index = 1

input_data_start_column = 1
input_data_length = 26

# 导入数据
dataset_train = genfromtxt('./checkPointData/002352_stockCheckPoint_train.csv', delimiter=',')
# 分割输入x和输出Y
train_X = dataset_train[train_start_index:, input_data_start_column : (input_data_start_column + input_data_length)]
train_Y = dataset_train[train_start_index:, (input_data_start_column + input_data_length)]

dataset_predict = genfromtxt('./checkPointData/002352_stockCheckPoint_predict.csv', delimiter=',')
predict_X = dataset_predict[train_start_index:, input_data_start_column : (input_data_start_column + input_data_length)]

# 创建模型
model = Sequential()
model.add(Dense(12, input_dim=input_data_length, activation='relu'))
model.add(Dense(8, activation='relu'))
model.add(Dense(1, activation='sigmoid'))

# 编译模型
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])

# 训练模型
model.fit(x=train_X, y=train_Y, epochs=400, batch_size=5)

# 评估模型
# scores = model.evaluate(x=predict_X, y=predict_Y)
# print('\n%s : %.2f%%' % (model.metrics_names[1], scores[1]*100))

predict_Y = model.predict_classes(predict_X)

for i in range(len(predict_X)):
	print("X=%s, Predicted=%s" % (predict_X[i], predict_Y[i]))
