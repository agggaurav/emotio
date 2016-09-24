import cv2
import numpy as np
import os
from sklearn import svm
from sklearn import datasets
from sklearn.svm import SVC
from sklearn.multiclass import OneVsRestClassifier
from sklearn.svm import LinearSVC
from sklearn.externals import joblib

image_paths = []
path = "/home/gaurav/ck/cohn-kanade/S042"

#list of our class names
training_names = os.listdir(path)
print training_names
training_paths = []
names_path = []
#get full list of all training images
for p in training_names:
    training_paths1 = os.listdir("/home/gaurav/ck/cohn-kanade/S042/"+p)
    for j in training_paths1:
        training_paths.append("/home/gaurav/ck/cohn-kanade/S042/"+p+"/"+j)
        names_path.append(p)
#orb = cv2.ORB_create()
sift = cv2.xfeatures2d.SIFT_create()
print names_path

descriptors_unclustered = []

dictionarySize = 500

BOW = cv2.BOWKMeansTrainer(dictionarySize)

for p in training_paths:
    print p
    gray = cv2.imread(p,0)
    #gray = cv2.cvtColor(image, cv2.CV_LOAD_IMAGE_GRAYSCALE)
    kp, dsc= sift.detectAndCompute(gray, None)
    BOW.add(dsc)

#dictionary created
dictionary = BOW.cluster()


FLANN_INDEX_KDTREE = 0
index_params = dict(algorithm = FLANN_INDEX_KDTREE, trees = 5)
search_params = dict(checks=50)   # or pass empty dictionary
flann = cv2.FlannBasedMatcher(index_params,search_params)
sift2 = cv2.xfeatures2d.SIFT_create()
bowDiction = cv2.BOWImgDescriptorExtractor(sift2, cv2.BFMatcher(cv2.NORM_L2))
bowDiction.setVocabulary(dictionary)
print "bow dictionary", np.shape(dictionary)
print dictionary

#returns descriptor of image at pth
def feature_extract(pth):
    gray = cv2.imread(pth,0)
    #gray = cv2.cvtColor(im, cv2.CV_LOAD_IMAGE_GRAYSCALE)
    return bowDiction.compute(gray, sift.detect(gray))

train_desc = []
train_labels = []
i = 0
for p in training_paths:
    train_desc.extend(feature_extract(p))
    #print names_path[i]
    if names_path[i]=='001':
        train_labels.append(1)
    if names_path[i]=='002':
        train_labels.append(2)
    if names_path[i]=='003':
        train_labels.append(3)
    if names_path[i]=='004':
        train_labels.append(4)
    if names_path[i]=='005': 
        train_labels.append(5)
    if names_path[i]=='006':
	train_labels.append(6)
    i = i+1

print "svm items", len(train_desc), len(train_desc[0])
count=0
#svm = cv2.SVM()
#svm.train(np.array(train_desc), np.array(train_labels))
#clf = svm.SVC(kernel='linear')
#clf.fit(np.array(train_desc),np.array(train_labels))  
#SVC(C=1.0, cache_size=200, class_weight=None, coef0=0.0,  decision_function_shape='ovr', degree=3, gamma='auto', kernel='linear',  max_iter=-1, probability=False,random_state=None, shrinking=True,  tol=0.001, verbose=False)
#clf = svm.SVC()
#clf.fit(np.array(train_desc),np.array(train_labels)) 
#SVC(C=1.0, class_weight=None, dual=True, fit_intercept=True,intercept_scaling=1, loss='squared_hinge', max_iter=1000,multi_class='ovr', penalty='l2',random_state=None, tol=0.0001,verbose=0)


#clf=OneVsRestClassifier(svm.SVC(kernel='rbf'))
clf = joblib.load('filename2.pkl') 
clf.fit(np.array(train_desc),np.array(train_labels))

i=0
j=0
print training_paths[8]
feature = feature_extract(training_paths[8])
ps = clf.predict(feature)
print ps

#confusion = np.zeros((6,94))
#def classify(pth):
#    feature = feature_extract(pth)
#    p = clf.predict(feature)
#    #print train_labels
#    confusion[train_labels[count]-1,p-1] = confusion[train_labels[count]-1,p-1] +1
    
    

#for p in training_paths:
#    classify(p)
#    count+=1

#def normalizeRows(M):
    #row_sums = M.sum(axis=1)
   # print M
   # print row_sums 
   # for i in range(6):
   #     print i
  #      op=M[i][:]/row_sums[i]
    #return M / row_sums
 #   return op

#confusion = normalizeRows(confusion)

#confusion = confusion.transpose()
    #print confusion
joblib.dump(clf, 'filename2.pkl')
