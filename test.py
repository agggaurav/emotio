import timeit

import cv2
import numpy as np
import os
from sklearn import svm
from sklearn import datasets
from sklearn.svm import SVC
from sklearn.multiclass import OneVsRestClassifier
from sklearn.svm import LinearSVC
from sklearn.externals import joblib
start=timeit.default_timer()

#import train
stop=timeit.default_timer()


print "import time ",stop-start
def extractfeature(path):
    image=cv2.imread(path,0)
    return bowdict.compute(image,sift.detect(image))
#filee=open('bow','r')
#filee.read(bowdict)
#filee.close()

sift2 = cv2.xfeatures2d.SIFT_create()
sift= cv2.xfeatures2d.SIFT_create()
bowdict = cv2.BOWImgDescriptorExtractor(sift2, cv2.BFMatcher(cv2.NORM_L2))
start=timeit.default_timer()
dictionary=joblib.load('trained/bow/bow.pkl')
stop=timeit.default_timer()
print "dict load time",stop-start

start=timeit.default_timer()
bowdict.setVocabulary(dictionary)
stop=timeit.default_timer()
print "set vocab time",stop-start
start=timeit.default_timer()
clf=joblib.load('trained/svm/svm.pkl')
stop=timeit.default_timer()
print "svm load time",stop-start
#print bowdict
start=timeit.default_timer()
test=extractfeature('test.png')
stop=timeit.default_timer()
print "extract features time",stop-start
start=timeit.default_timer()
print clf.predict(test)
stop=timeit.default_timer()
print "predict time",stop-start	
