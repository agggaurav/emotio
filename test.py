import timeit
import cv2
import numpy as np
import os
from sklearn import svm
from sklearn.svm import SVC
from sklearn.multiclass import OneVsRestClassifier
from sklearn.svm import LinearSVC
from sklearn.externals import joblib
start=timeit.default_timer()

#import train
stop=timeit.default_timer()

def facecrop(image):
	face_cascade=cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
	face_cascade2=cv2.CascadeClassifier('haarcascade_frontalface_alt.xml')
	print image.shape
	gray=cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)
	faces=face_cascade.detectMultiScale(gray,1.3,5,minSize=(70,70))
	if len(faces)==0:
            faces=face_cascade2.detectMultiScale(gray,1.3,5)
	print faces
	for (x,y,w,h) in faces:
		face= gray[y:y+h, x:x+w]
		return face[0.1*h :h-0.1*h,0.15*h:w-0.15*h]


print "import time ",stop-start
def extractfeature(path):
	image=cv2.imread(path)
        sift= cv2.xfeatures2d.SIFT_create()
	image=facecrop(image)
        gray=image
        kp,dsc=sift.detectAndCompute(image,None)
        img=cv2.drawKeypoints(image,kp,None)
        cv2.imshow('face',img)
        cv2.waitKey(0)
	return bowdict.compute(image,sift.detect(image))
#filee=open('bow','r')
#filee.read(bowdict)
#filee.close()

sift2 = cv2.xfeatures2d.SIFT_create()
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
test=extractfeature('ab.png')
stop=timeit.default_timer()
print "extract features time",stop-start
start=timeit.default_timer()
print clf.predict(test)
stop=timeit.default_timer()
print "predict time",stop-start	
