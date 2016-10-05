import cv2
import numpy as np
import os
from sklearn import svm
from sklearn import datasets
from sklearn.svm import SVC
from sklearn.multiclass import OneVsRestClassifier
from sklearn.svm import LinearSVC
from sklearn.externals import joblib
from getdata import getdata

images,labels=getdata()
imagespath = "dataset/ck+/cohn-kanade-images/"
labelspath="dataset/ck+/Emotion/"
def createBOW():
    dsize=10
    bof=cv2.BOWKMeansTrainer(dsize)
    sessions=os.listdir(imagespath)
    for subject in images:
        #print subject
        sub=subject[0][0][:4]
        #print sub
        for session in subject:
            #print session
            sess=session[0][5:8]
            #print sess
            for image in session:
                #sub=image[:4]
                #sess=image[5:8]
                #print image
                gray=cv2.imread(imagespath+sub+'/'+sess+'/'+image,0)
                kp,dsc=sift.detectAndCompute(gray,None)
                bof.add(dsc)
                #extractfeature(image)
    #print 'hahah'
    dictionary=bof.cluster()
    FLANN_INDEX_KDTREE = 0
    index_params = dict(algorithm = FLANN_INDEX_KDTREE, trees = 5)
    search_params = dict(checks=50)   # or pass empty dictionary
    flann = cv2.FlannBasedMatcher(index_params,search_params)
    print type(dictionary)
    print 'h'
    bowdict.setVocabulary(dictionary)
    joblib.dump(dictionary,'trained/bow/bow.pkl')
    print 'a'
    #filee=open('bow','w')
    #filee.write(bowdict)
    #filee.close()

def extractfeature(path):
    image=cv2.imread(path,0)
    return bowdict.compute(image,sift.detect(image))

def trainSVM():
    train_desc=[]
    train_labels=[]
    for i,l in zip(images,labels):
        #print i,l
        subject=i[0][0][:4]
        #print subject
        for ii,ll in zip(i,l):
            #print ii,ll
            session=ii[0][5:8]
            cnt=0
            #print session
            for image in ii:
                #print imagespath+subject+'/'+session+'/'+image
                feature=extractfeature(imagespath+subject+'/'+session+'/'+image)
                #print feature
                train_desc.extend(feature)
                cnt+=1
            for _ in xrange(cnt):
                labelfile=ll[0]
                data=open(labelspath+subject+'/'+session+'/'+labelfile)
                emotion=data.read()
                train_labels.append(emotion)
    clf=OneVsRestClassifier(svm.SVC(kernel='rbf'))
    clf.fit(np.array(train_desc),np.array(train_labels))
    joblib.dump(clf,'trained/svm/svm.pkl') 
sift=cv2.xfeatures2d.SIFT_create()
sift2 = cv2.xfeatures2d.SIFT_create()
bowdict = cv2.BOWImgDescriptorExtractor(sift2, cv2.BFMatcher(cv2.NORM_L2))
createBOW()
#print bowdict
trainSVM()
